package com.jakewharton.u2020.data.api.oauth;

import android.app.IntentService;
import android.content.Intent;
import com.jakewharton.u2020.U2020AppComponent;
import com.jakewharton.u2020.data.Injector;
import javax.inject.Inject;

public final class OauthService extends IntentService {
  @Inject OauthManager oauthManager;

  public OauthService() {
    super(OauthService.class.getSimpleName());
  }

  @Override public void onCreate() {
    super.onCreate();
    U2020AppComponent component = Injector.obtain(getApplication(), U2020AppComponent.class);
    component.inject(this);
  }

  @Override protected void onHandleIntent(Intent intent) {
    oauthManager.handleResult(intent.getData());
  }
}
