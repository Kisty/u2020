package com.jakewharton.u2020.data;

import android.app.Application;
import android.content.SharedPreferences;
import android.net.Uri;
import com.jakewharton.u2020.dagger.PerApp;
import com.jakewharton.u2020.data.api.ReleaseApiModule;
import com.jakewharton.u2020.data.api.oauth.AccessToken;
import com.jakewharton.u2020.data.prefs.StringPreference;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;
import dagger.Module;
import dagger.Provides;
import timber.log.Timber;

@Module(includes = ReleaseApiModule.class)
public class ReleaseDataModule {
  @Provides @PerApp OkHttpClient provideOkHttpClient(Application app) {
    return DataModule.createOkHttpClient(app);
  }

  @Provides @PerApp Picasso providePicasso(Application app, OkHttpClient client) {
    return new Picasso.Builder(app)
        .downloader(new OkHttpDownloader(client))
        .listener(new Picasso.Listener() {
          @Override public void onImageLoadFailed(Picasso picasso, Uri uri, Exception e) {
            Timber.e(e, "Failed to load image: %s", uri);
          }
        })
        .build();
  }

  @Provides @PerApp IntentFactory provideIntentFactory() {
    return IntentFactory.REAL;
  }

  @Provides @PerApp @AccessToken StringPreference provideAccessToken(SharedPreferences prefs) {
    return new StringPreference(prefs, "access-token");
  }

}
