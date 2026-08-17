package com.example.gestionturnosapp.data.repository;

import com.example.gestionturnosapp.data.remote.ApiService;
import com.example.gestionturnosapp.util.NetworkUtils;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation"
})
public final class ChatRepository_Factory implements Factory<ChatRepository> {
  private final Provider<ApiService> apiServiceProvider;

  private final Provider<NetworkUtils> networkUtilsProvider;

  public ChatRepository_Factory(Provider<ApiService> apiServiceProvider,
      Provider<NetworkUtils> networkUtilsProvider) {
    this.apiServiceProvider = apiServiceProvider;
    this.networkUtilsProvider = networkUtilsProvider;
  }

  @Override
  public ChatRepository get() {
    return newInstance(apiServiceProvider.get(), networkUtilsProvider.get());
  }

  public static ChatRepository_Factory create(Provider<ApiService> apiServiceProvider,
      Provider<NetworkUtils> networkUtilsProvider) {
    return new ChatRepository_Factory(apiServiceProvider, networkUtilsProvider);
  }

  public static ChatRepository newInstance(ApiService apiService, NetworkUtils networkUtils) {
    return new ChatRepository(apiService, networkUtils);
  }
}
