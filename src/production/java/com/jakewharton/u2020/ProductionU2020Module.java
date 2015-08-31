package com.jakewharton.u2020;

import com.jakewharton.u2020.data.ReleaseDataModule;
import com.jakewharton.u2020.ui.ProductionUiModule;
import dagger.Module;

@Module(
    includes = {
        ProductionUiModule.class,
        ReleaseDataModule.class,
    })
public final class ProductionU2020Module {
}
