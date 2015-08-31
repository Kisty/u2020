package com.jakewharton.u2020.data;

import android.app.Application;
import android.content.SharedPreferences;
import com.jakewharton.u2020.dagger.PerApp;
import com.jakewharton.u2020.data.api.ApiModule;
import com.squareup.moshi.Moshi;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;
import dagger.Module;
import dagger.Provides;
import java.io.File;
import org.threeten.bp.Clock;

import static android.content.Context.MODE_PRIVATE;
import static com.jakewharton.byteunits.DecimalByteUnit.MEGABYTES;
import static java.util.concurrent.TimeUnit.SECONDS;

@Module(includes = ApiModule.class)
public final class DataModule {
  static final int DISK_CACHE_SIZE = (int) MEGABYTES.toBytes(50);

  @Provides @PerApp SharedPreferences provideSharedPreferences(Application app) {
    return app.getSharedPreferences("u2020", MODE_PRIVATE);
  }

  @Provides @PerApp Moshi provideMoshi() {
    return new Moshi.Builder()
        .add(new InstantAdapter())
        .build();
  }

  @Provides @PerApp Clock provideClock() {
    return Clock.systemDefaultZone();
  }

  static OkHttpClient createOkHttpClient(Application app) {
    OkHttpClient client = new OkHttpClient();
    client.setConnectTimeout(10, SECONDS);
    client.setReadTimeout(10, SECONDS);
    client.setWriteTimeout(10, SECONDS);

    // Install an HTTP cache in the application cache directory.
    File cacheDir = new File(app.getCacheDir(), "http");
    Cache cache = new Cache(cacheDir, DISK_CACHE_SIZE);
    client.setCache(cache);

    return client;
  }
}
