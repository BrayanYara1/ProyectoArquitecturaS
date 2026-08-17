package com.example.gestionturnosapp.data.repository;

import com.example.gestionturnosapp.data.local.OfflineCacheManager;
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
public final class TurnoRepository_Factory implements Factory<TurnoRepository> {
  private final Provider<ApiService> apiServiceProvider;

  private final Provider<OfflineCacheManager> offlineCacheManagerProvider;

  private final Provider<NetworkUtils> networkUtilsProvider;

  public TurnoRepository_Factory(Provider<ApiService> apiServiceProvider,
      Provider<OfflineCacheManager> offlineCacheManagerProvider,
      Provider<NetworkUtils> networkUtilsProvider) {
    this.apiServiceProvider = apiServiceProvider;
    this.offlineCacheManagerProvider = offlineCacheManagerProvider;
    this.networkUtilsProvider = networkUtilsProvider;
  }

  @Override
  public TurnoRepository get() {
    return newInstance(apiServiceProvider.get(), offlineCacheManagerProvider.get(), networkUtilsProvider.get());
  }

  public static TurnoRepository_Factory create(Provider<ApiService> apiServiceProvider,
      Provider<OfflineCacheManager> offlineCacheManagerProvider,
      Provider<NetworkUtils> networkUtilsProvider) {
    return new TurnoRepository_Factory(apiServiceProvider, offlineCacheManagerProvider, networkUtilsProvider);
  }

  public static TurnoRepository newInstance(ApiService apiService,
      OfflineCacheManager offlineCacheManager, NetworkUtils networkUtils) {
    return new TurnoRepository(apiService, offlineCacheManager, networkUtils);
  }
}
