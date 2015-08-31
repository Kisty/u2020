package com.jakewharton.u2020.data.api;

import com.jakewharton.u2020.dagger.PerApp;
import com.jakewharton.u2020.data.api.oauth.OauthInterceptor;
import com.squareup.moshi.Moshi;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;
import retrofit.MoshiConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

@Module
public class ReleaseApiModule {

  @Provides @PerApp HttpUrl provideBaseUrl() {
    return ApiModule.PRODUCTION_API_URL;
  }


  @Provides @PerApp @Named("Api") OkHttpClient provideApiClient(OkHttpClient client,
      OauthInterceptor oauthInterceptor) {
    return ApiModule.createApiClient(client, oauthInterceptor);
  }

  @Provides @PerApp
  Retrofit provideRetrofit(HttpUrl baseUrl, @Named("Api") OkHttpClient client, Moshi moshi) {
    return new Retrofit.Builder() //
        .client(client) //
        .baseUrl(baseUrl) //
        .addConverterFactory(MoshiConverterFactory.create(moshi)) //
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create()) //
        .build();
  }

  @Provides @PerApp GithubService provideGithubService(Retrofit retrofit) {
    return retrofit.create(GithubService.class);
  }
}
