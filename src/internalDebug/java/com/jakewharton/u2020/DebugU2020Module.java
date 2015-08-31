package com.jakewharton.u2020;

import com.jakewharton.u2020.dagger.PerApp;
import com.jakewharton.u2020.data.DebugDataModule;
import com.jakewharton.u2020.ui.DebugUiModule;
import dagger.Module;
import dagger.Provides;

@Module(
    includes = {
        DebugUiModule.class,
        DebugDataModule.class,
        DebugActionsModule.class,
    }
)
public final class DebugU2020Module {
  // Low-tech flag to force certain debug build behaviors when running in an instrumentation test.
  // This value is used in the creation of singletons so it must be set before the graph is created.
  static boolean instrumentationTest = false;

  @Provides @PerApp @IsInstrumentationTest boolean provideIsInstrumentationTest() {
    return instrumentationTest;
  }
}
