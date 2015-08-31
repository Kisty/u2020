package com.jakewharton.u2020.data.api;

import com.jakewharton.u2020.dagger.PerApp;
import com.jakewharton.u2020.data.ApiEndpoint;
import com.jakewharton.u2020.data.IsMockMode;
import com.jakewharton.u2020.data.NetworkDelay;
import com.jakewharton.u2020.data.NetworkFailurePercent;
import com.jakewharton.u2020.data.NetworkVariancePercent;
import com.jakewharton.u2020.data.api.oauth.OauthInterceptor;
import com.jakewharton.u2020.data.prefs.IntPreference;
import com.jakewharton.u2020.data.prefs.LongPreference;
import com.jakewharton.u2020.data.prefs.StringPreference;
import com.squareup.moshi.Moshi;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;
import retrofit.MoshiConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import retrofit.mock.MockRetrofit;
import retrofit.mock.NetworkBehavior;
import retrofit.mock.RxJavaBehaviorAdapter;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

@Module
public final class DebugApiModule {
  @Provides @PerApp HttpUrl provideHttpUrl(@ApiEndpoint StringPreference apiEndpoint) {
    return HttpUrl.parse(apiEndpoint.get());
  }

  @Provides @PerApp @Named("Api") OkHttpClient provideApiClient(OkHttpClient client,
      OauthInterceptor oauthInterceptor, LoggingInterceptor loggingInterceptor) {
    client = ApiModule.createApiClient(client, oauthInterceptor);
    client.interceptors().add(loggingInterceptor);
    return client;
  }

  @Provides @PerApp NetworkBehavior provideBehavior(@NetworkDelay LongPreference networkDelay,
      @NetworkFailurePercent IntPreference networkFailurePercent,
      @NetworkVariancePercent IntPreference networkVariancePercent) {
    NetworkBehavior behavior = NetworkBehavior.create();
    behavior.setDelay(networkDelay.get(), MILLISECONDS);
    behavior.setFailurePercent(networkFailurePercent.get());
    behavior.setVariancePercent(networkVariancePercent.get());
    return behavior;
  }

  @Provides @PerApp MockRetrofit provideMockRetrofit(NetworkBehavior behavior) {
    return new MockRetrofit(behavior, RxJavaBehaviorAdapter.create());
  }

  @Provides @PerApp Retrofit provideRetrofit(HttpUrl baseUrl, @Named("Api") OkHttpClient client, Moshi moshi) {
    return new Retrofit.Builder()
        .client(client)
        .baseUrl(baseUrl)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .build();
  }

  @Provides @PerApp GithubService provideGithubService(Retrofit retrofit,
      MockRetrofit mockRetrofit, @IsMockMode boolean isMockMode, MockGithubService mockService) {
    if (isMockMode) {
      return mockRetrofit.create(GithubService.class, mockService);
    }
    return retrofit.create(GithubService.class);
  }
}
