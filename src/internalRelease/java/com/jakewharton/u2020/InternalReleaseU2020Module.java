package com.jakewharton.u2020;

import com.jakewharton.u2020.data.ReleaseDataModule;
import com.jakewharton.u2020.ui.InternalReleaseUiModule;
import dagger.Module;

@Module(
    includes = {
        InternalReleaseUiModule.class,
        ReleaseDataModule.class
    })
public final class InternalReleaseU2020Module {
}
