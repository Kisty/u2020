package com.jakewharton.u2020.data;

import android.app.Application;
import android.content.SharedPreferences;
import android.net.Uri;
import com.jakewharton.u2020.IsInstrumentationTest;
import com.jakewharton.u2020.dagger.PerApp;
import com.jakewharton.u2020.data.api.DebugApiModule;
import com.jakewharton.u2020.data.api.oauth.AccessToken;
import com.jakewharton.u2020.data.prefs.BooleanPreference;
import com.jakewharton.u2020.data.prefs.IntPreference;
import com.jakewharton.u2020.data.prefs.LongPreference;
import com.jakewharton.u2020.data.prefs.NetworkProxyPreference;
import com.jakewharton.u2020.data.prefs.RxSharedPreferences;
import com.jakewharton.u2020.data.prefs.StringPreference;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;
import dagger.Module;
import dagger.Provides;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import retrofit.mock.NetworkBehavior;
import rx.Observable;
import timber.log.Timber;

@Module(includes = DebugApiModule.class)
public final class DebugDataModule {
  private static final int DEFAULT_ANIMATION_SPEED = 1; // 1x (normal) speed.
  private static final boolean DEFAULT_PICASSO_DEBUGGING = false; // Debug indicators displayed
  private static final boolean DEFAULT_PIXEL_GRID_ENABLED = false; // No pixel grid overlay.
  private static final boolean DEFAULT_PIXEL_RATIO_ENABLED = false; // No pixel ratio overlay.
  private static final boolean DEFAULT_SCALPEL_ENABLED = false; // No crazy 3D view tree.
  private static final boolean DEFAULT_SCALPEL_WIREFRAME_ENABLED = false; // Draw views by default.
  private static final boolean DEFAULT_SEEN_DEBUG_DRAWER = false; // Show debug drawer first time.
  private static final boolean DEFAULT_CAPTURE_INTENTS = true; // Capture external intents.

  @Provides @PerApp
  RxSharedPreferences provideRxSharedPreferences(SharedPreferences preferences) {
    return RxSharedPreferences.create(preferences);
  }

  @Provides @PerApp IntentFactory provideIntentFactory(@IsMockMode boolean isMockMode,
      @CaptureIntents BooleanPreference captureIntents) {
    return new DebugIntentFactory(IntentFactory.REAL, isMockMode, captureIntents);
  }

  @Provides @PerApp OkHttpClient provideOkHttpClient(Application app,
      NetworkProxyPreference networkProxy) {
    OkHttpClient client = DataModule.createOkHttpClient(app);
    client.setSslSocketFactory(createBadSslSocketFactory());
    client.setProxy(networkProxy.getProxy());
    return client;
  }

  @Provides @PerApp @AccessToken StringPreference provideAccessToken(SharedPreferences prefs,
      @ApiEndpoint StringPreference endpoint) {
    // Return an endpoint-specific preference.
    return new StringPreference(prefs, "access-token-" + endpoint.get());
  }

  @Provides @PerApp @ApiEndpoint
  StringPreference provideEndpointPreference(SharedPreferences preferences) {
    return new StringPreference(preferences, "debug_endpoint", ApiEndpoints.MOCK_MODE.url);
  }

  @Provides @PerApp @IsMockMode boolean provideIsMockMode(@ApiEndpoint StringPreference endpoint,
      @IsInstrumentationTest boolean isInstrumentationTest) {
    // Running in an instrumentation forces mock mode.
    return isInstrumentationTest || ApiEndpoints.isMockMode(endpoint.get());
  }

  @Provides @PerApp @NetworkDelay LongPreference provideNetworkDelay(
      SharedPreferences preferences) {
    return new LongPreference(preferences, "debug_network_delay", 2000);
  }

  @Provides @PerApp @NetworkFailurePercent IntPreference provideNetworkFailurePercent(
      SharedPreferences preferences) {
    return new IntPreference(preferences, "debug_network_failure_percent", 3);
  }

  @Provides @PerApp @NetworkVariancePercent IntPreference provideNetworkVariancePercent(
      SharedPreferences preferences) {
    return new IntPreference(preferences, "debug_network_variance_percent", 40);
  }

  @Provides @PerApp NetworkProxyPreference provideNetworkProxy(SharedPreferences preferences) {
    return new NetworkProxyPreference(preferences, "debug_network_proxy");
  }

  @Provides @PerApp @CaptureIntents
  BooleanPreference provideCaptureIntentsPreference(SharedPreferences preferences) {
    return new BooleanPreference(preferences, "debug_capture_intents", DEFAULT_CAPTURE_INTENTS);
  }

  @Provides @PerApp @AnimationSpeed
  IntPreference provideAnimationSpeed(SharedPreferences preferences) {
    return new IntPreference(preferences, "debug_animation_speed", DEFAULT_ANIMATION_SPEED);
  }

  @Provides @PerApp @PicassoDebugging
  BooleanPreference providePicassoDebugging(SharedPreferences preferences) {
    return new BooleanPreference(preferences, "debug_picasso_debugging", DEFAULT_PICASSO_DEBUGGING);
  }

  @Provides @PerApp @PixelGridEnabled
  BooleanPreference providePixelGridEnabled(SharedPreferences preferences) {
    return new BooleanPreference(preferences, "debug_pixel_grid_enabled",
        DEFAULT_PIXEL_GRID_ENABLED);
  }

  @Provides @PerApp @PixelGridEnabled
  Observable<Boolean> provideObservablePixelGridEnabled(RxSharedPreferences preferences) {
    return preferences.getBoolean("debug_pixel_grid_enabled", DEFAULT_PIXEL_GRID_ENABLED);
  }

  @Provides @PerApp @PixelRatioEnabled
  BooleanPreference providePixelRatioEnabled(SharedPreferences preferences) {
    return new BooleanPreference(preferences, "debug_pixel_ratio_enabled",
        DEFAULT_PIXEL_RATIO_ENABLED);
  }

  @Provides @PerApp @PixelRatioEnabled
  Observable<Boolean> provideObservablePixelRatioEnabled(RxSharedPreferences preferences) {
    return preferences.getBoolean("debug_pixel_ratio_enabled", DEFAULT_PIXEL_RATIO_ENABLED);
  }

  @Provides @PerApp @SeenDebugDrawer
  BooleanPreference provideSeenDebugDrawer(SharedPreferences preferences) {
    return new BooleanPreference(preferences, "debug_seen_debug_drawer", DEFAULT_SEEN_DEBUG_DRAWER);
  }

  @Provides @PerApp @ScalpelEnabled
  BooleanPreference provideScalpelEnabled(SharedPreferences preferences) {
    return new BooleanPreference(preferences, "debug_scalpel_enabled", DEFAULT_SCALPEL_ENABLED);
  }

  @Provides @PerApp @ScalpelEnabled
  Observable<Boolean> provideObservableScalpelEnabled(RxSharedPreferences preferences) {
    return preferences.getBoolean("debug_scalpel_enabled", DEFAULT_SCALPEL_ENABLED);
  }

  @Provides @PerApp @ScalpelWireframeEnabled
  BooleanPreference provideScalpelWireframeEnabled(SharedPreferences preferences) {
    return new BooleanPreference(preferences, "debug_scalpel_wireframe_drawer",
        DEFAULT_SCALPEL_WIREFRAME_ENABLED);
  }

  @Provides @PerApp @ScalpelWireframeEnabled
  Observable<Boolean> provideObservableScalpelWireframeEnabled(RxSharedPreferences preferences) {
    return preferences.getBoolean("debug_scalpel_wireframe_drawer",
        DEFAULT_SCALPEL_WIREFRAME_ENABLED);
  }

  @Provides @PerApp Picasso providePicasso(OkHttpClient client, NetworkBehavior behavior,
      @IsMockMode boolean isMockMode, Application app) {
    Picasso.Builder builder = new Picasso.Builder(app).downloader(new OkHttpDownloader(client));
    if (isMockMode) {
      builder.addRequestHandler(new MockRequestHandler(behavior, app.getAssets()));
    }
    builder.listener(new Picasso.Listener() {
      @Override public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
        Timber.e(exception, "Error while loading image " + uri);
      }
    });
    return builder.build();
  }

  private static SSLSocketFactory createBadSslSocketFactory() {
    try {
      // Construct SSLSocketFactory that accepts any cert.
      SSLContext context = SSLContext.getInstance("TLS");
      TrustManager permissive = new X509TrustManager() {
        @Override public void checkClientTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {
        }

        @Override public void checkServerTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {
        }

        @Override public X509Certificate[] getAcceptedIssuers() {
          return null;
        }
      };
      context.init(null, new TrustManager[] { permissive }, null);
      return context.getSocketFactory();
    } catch (Exception e) {
      throw new AssertionError(e);
    }
  }
}
