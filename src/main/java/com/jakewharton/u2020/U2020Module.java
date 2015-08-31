package com.jakewharton.u2020;

import android.app.Application;
import com.jakewharton.u2020.dagger.PerApp;
import com.jakewharton.u2020.data.DataModule;
import dagger.Module;
import dagger.Provides;

@Module(includes = DataModule.class)
public final class U2020Module {
  private final U2020App app;

  public U2020Module(U2020App app) {
    this.app = app;
  }

  @Provides @PerApp Application provideApplication() {
    return app;
  }
}
