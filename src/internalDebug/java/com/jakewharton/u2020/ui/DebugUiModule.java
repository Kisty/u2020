package com.jakewharton.u2020.ui;

import com.jakewharton.u2020.IsInstrumentationTest;
import com.jakewharton.u2020.dagger.PerApp;
import com.jakewharton.u2020.ui.debug.DebugAppContainer;
import com.jakewharton.u2020.ui.debug.SocketActivityHierarchyServer;
import dagger.Module;
import dagger.Provides;

@Module
public class DebugUiModule {
  @Provides @PerApp AppContainer provideAppContainer(DebugAppContainer debugAppContainer,
      @IsInstrumentationTest boolean isInstrumentationTest) {
    // Do not add the debug controls for when we are running inside of an instrumentation test.
    return isInstrumentationTest ? AppContainer.DEFAULT : debugAppContainer;
  }

  @Provides @PerApp ActivityHierarchyServer provideActivityHierarchyServer() {
    return new SocketActivityHierarchyServer();
  }
}
