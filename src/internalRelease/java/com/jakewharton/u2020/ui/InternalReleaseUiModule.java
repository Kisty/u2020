package com.jakewharton.u2020.ui;

import com.jakewharton.u2020.dagger.PerApp;
import dagger.Module;
import dagger.Provides;

@Module
public final class InternalReleaseUiModule {
  @Provides @PerApp AppContainer provideAppContainer(
      TelescopeAppContainer telescopeAppContainer) {
    return telescopeAppContainer;
  }

  @Provides @PerApp ActivityHierarchyServer provideActivityHierarchyServer() {
    return ActivityHierarchyServer.NONE;
  }
}
