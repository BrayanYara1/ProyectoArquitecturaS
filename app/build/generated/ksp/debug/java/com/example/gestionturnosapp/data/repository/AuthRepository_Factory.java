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
public final class AuthRepository_Factory implements Factory<AuthRepository> {
  private final Provider<ApiService> apiServiceProvider;

  private final Provider<NetworkUtils> networkUtilsProvider;

  public AuthRepository_Factory(Provider<ApiService> apiServiceProvider,
      Provider<NetworkUtils> networkUtilsProvider) {
    this.apiServiceProvider = apiServiceProvider;
    this.networkUtilsProvider = networkUtilsProvider;
  }

  @Override
  public AuthRepository get() {
    return newInstance(apiServiceProvider.get(), networkUtilsProvider.get());
  }

  public static AuthRepository_Factory create(Provider<ApiService> apiServiceProvider,
      Provider<NetworkUtils> networkUtilsProvider) {
    return new AuthRepository_Factory(apiServiceProvider, networkUtilsProvider);
  }

  public static AuthRepository newInstance(ApiService apiService, NetworkUtils networkUtils) {
    return new AuthRepository(apiService, networkUtils);
  }
}
