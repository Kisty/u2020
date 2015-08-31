package com.jakewharton.u2020;

import com.jakewharton.u2020.dagger.PerApp;
import com.jakewharton.u2020.data.AnimationSpeed;
import com.jakewharton.u2020.data.ApiEndpoint;
import com.jakewharton.u2020.data.CaptureIntents;
import com.jakewharton.u2020.data.IsMockMode;
import com.jakewharton.u2020.data.NetworkDelay;
import com.jakewharton.u2020.data.NetworkFailurePercent;
import com.jakewharton.u2020.data.NetworkVariancePercent;
import com.jakewharton.u2020.data.PicassoDebugging;
import com.jakewharton.u2020.data.PixelGridEnabled;
import com.jakewharton.u2020.data.PixelRatioEnabled;
import com.jakewharton.u2020.data.ScalpelEnabled;
import com.jakewharton.u2020.data.ScalpelWireframeEnabled;
import com.jakewharton.u2020.data.api.MockGithubService;
import com.jakewharton.u2020.data.prefs.BooleanPreference;
import com.jakewharton.u2020.data.prefs.IntPreference;
import com.jakewharton.u2020.data.prefs.LongPreference;
import com.jakewharton.u2020.data.prefs.NetworkProxyPreference;
import com.jakewharton.u2020.data.prefs.StringPreference;
import com.jakewharton.u2020.ui.debug.ContextualDebugActions;
import com.jakewharton.u2020.ui.debug.DebugView;
import com.squareup.okhttp.OkHttpClient;
import dagger.Component;
import java.util.Set;
import javax.inject.Named;
import retrofit.mock.MockRetrofit;
import retrofit.mock.NetworkBehavior;

@PerApp
@Component(modules = { U2020Module.class, DebugU2020Module.class })
public interface U2020AppComponent extends U2020Graph {

  void inject(DebugView debugView);

  @Named("Api") OkHttpClient okHttpClient();

  @IsMockMode boolean isMockMode();

  @ApiEndpoint StringPreference networkEndpoint();

  @CaptureIntents BooleanPreference captureIntents();

  @AnimationSpeed IntPreference animationSpeed();

  @PicassoDebugging BooleanPreference picassoDebugging();

  @PixelGridEnabled BooleanPreference pixelGridEnabled();

  @PixelRatioEnabled BooleanPreference pixelRatioEnabled();

  @ScalpelEnabled BooleanPreference scalpelEnabled();

  @ScalpelWireframeEnabled BooleanPreference scalpelWireframeEnabled();

  @NetworkDelay LongPreference networkDelay();

  @NetworkFailurePercent IntPreference networkFailurePercent();

  @NetworkVariancePercent IntPreference networkVariancePercent();

  MockRetrofit mockRetrofit();

  MockGithubService mockGithubService();

  NetworkProxyPreference networkProxyPreference();

  NetworkBehavior behavior();

  Set<ContextualDebugActions.DebugAction> debugActions();

  final class Initializer {
    public static U2020AppComponent init(U2020App app) {
      return DaggerU2020AppComponent.builder()
          .u2020Module(new U2020Module(app))
          .debugU2020Module(new DebugU2020Module())
          .build();
    }
  }
}
