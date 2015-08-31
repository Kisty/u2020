package com.jakewharton.u2020.data.api;

import com.jakewharton.u2020.data.api.oauth.OauthInterceptor;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import dagger.Module;

@Module
public final class ApiModule {
  public static final HttpUrl PRODUCTION_API_URL = HttpUrl.parse("https://api.github.com");

  static OkHttpClient createApiClient(OkHttpClient client, OauthInterceptor oauthInterceptor) {
    client = client.clone();
    client.interceptors().add(oauthInterceptor);
    return client;
  }
}
