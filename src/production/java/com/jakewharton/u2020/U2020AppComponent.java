package com.jakewharton.u2020;

import com.jakewharton.u2020.dagger.PerApp;
import dagger.Component;

@PerApp // Constraints this component to one-per-application or unscoped bindings.
@Component(modules = {U2020Module.class, ProductionU2020Module.class})
public interface U2020AppComponent extends U2020Graph {

  final class Initializer {
    public static U2020AppComponent init(U2020App app) {
      return DaggerU2020AppComponent.builder()
          .u2020Module(new U2020Module(app))
          .productionU2020Module(new ProductionU2020Module())
          .build();
    }
  }
}
