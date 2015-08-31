package com.jakewharton.u2020.ui;

import com.jakewharton.u2020.dagger.PerApp;
import dagger.Module;
import dagger.Provides;

@Module
public final class UiModule {
  @Provides @PerApp AppContainer provideAppContainer() {
    return AppContainer.DEFAULT;
  }

  @Provides @PerApp ActivityHierarchyServer provideActivityHierarchyServer() {
    return ActivityHierarchyServer.NONE;
  }
}
