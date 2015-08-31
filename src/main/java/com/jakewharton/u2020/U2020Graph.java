package com.jakewharton.u2020;

import android.app.Application;
import com.jakewharton.u2020.data.IntentFactory;
import com.jakewharton.u2020.data.api.GithubService;
import com.jakewharton.u2020.data.api.oauth.OauthService;
import com.jakewharton.u2020.ui.ActivityHierarchyServer;
import com.jakewharton.u2020.ui.AppContainer;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.Picasso;

public interface U2020Graph {
  void inject(U2020App app);

  void inject(OauthService oauthService);

  Application application();

  OkHttpClient client();

  Picasso picasso();

  IntentFactory intentFactory();

  GithubService githubService();

  AppContainer appContainer();

  ActivityHierarchyServer activityHierarchyServer();
}
