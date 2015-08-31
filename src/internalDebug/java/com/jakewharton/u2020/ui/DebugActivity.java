package com.jakewharton.u2020.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import com.jakewharton.u2020.R;
import com.jakewharton.u2020.U2020AppComponent;
import com.jakewharton.u2020.data.Injector;

public final class DebugActivity extends Activity {
  private U2020AppComponent component;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    component = Injector.obtain(getApplication(), U2020AppComponent.class);
    setContentView(R.layout.debug_activity);
  }

  @Override public Object getSystemService(@NonNull String name) {
    if (Injector.matchesService(name, U2020AppComponent.class)) {
      return component;
    }
    return super.getSystemService(name);
  }
}
