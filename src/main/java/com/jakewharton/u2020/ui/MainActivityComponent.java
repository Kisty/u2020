package com.jakewharton.u2020.ui;

import android.support.v4.widget.DrawerLayout;
import com.jakewharton.u2020.U2020AppComponent;
import com.jakewharton.u2020.dagger.PerActivity;
import com.jakewharton.u2020.data.Injector;
import com.jakewharton.u2020.ui.trending.TrendingView;
import dagger.Component;

@PerActivity
@Component(dependencies = U2020AppComponent.class,
           modules = { MainActivityModule.class })
public interface MainActivityComponent {
  void inject(MainActivity mainActivity);

  void inject(TrendingView trendingView);

  DrawerLayout drawerLayout();

  final class Initializer {
    public static MainActivityComponent init(MainActivity mainActivity) {
      return DaggerMainActivityComponent.builder()
          .u2020AppComponent(
              Injector.obtain(mainActivity.getApplicationContext(), U2020AppComponent.class))
          .mainActivityModule(new MainActivityModule(mainActivity))
          .build();
    }
  }
}
