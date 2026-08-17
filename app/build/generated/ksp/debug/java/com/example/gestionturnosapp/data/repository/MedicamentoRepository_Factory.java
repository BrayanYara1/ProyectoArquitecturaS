package com.example.gestionturnosapp.data.repository;

import com.example.gestionturnosapp.data.local.MedicationLogDao;
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
public final class MedicamentoRepository_Factory implements Factory<MedicamentoRepository> {
  private final Provider<ApiService> apiServiceProvider;

  private final Provider<OfflineCacheManager> offlineCacheManagerProvider;

  private final Provider<MedicationLogDao> medicationLogDaoProvider;

  private final Provider<NetworkUtils> networkUtilsProvider;

  public MedicamentoRepository_Factory(Provider<ApiService> apiServiceProvider,
      Provider<OfflineCacheManager> offlineCacheManagerProvider,
      Provider<MedicationLogDao> medicationLogDaoProvider,
      Provider<NetworkUtils> networkUtilsProvider) {
    this.apiServiceProvider = apiServiceProvider;
    this.offlineCacheManagerProvider = offlineCacheManagerProvider;
    this.medicationLogDaoProvider = medicationLogDaoProvider;
    this.networkUtilsProvider = networkUtilsProvider;
  }

  @Override
  public MedicamentoRepository get() {
    return newInstance(apiServiceProvider.get(), offlineCacheManagerProvider.get(), medicationLogDaoProvider.get(), networkUtilsProvider.get());
  }

  public static MedicamentoRepository_Factory create(Provider<ApiService> apiServiceProvider,
      Provider<OfflineCacheManager> offlineCacheManagerProvider,
      Provider<MedicationLogDao> medicationLogDaoProvider,
      Provider<NetworkUtils> networkUtilsProvider) {
    return new MedicamentoRepository_Factory(apiServiceProvider, offlineCacheManagerProvider, medicationLogDaoProvider, networkUtilsProvider);
  }

  public static MedicamentoRepository newInstance(ApiService apiService,
      OfflineCacheManager offlineCacheManager, MedicationLogDao medicationLogDao,
      NetworkUtils networkUtils) {
    return new MedicamentoRepository(apiService, offlineCacheManager, medicationLogDao, networkUtils);
  }
}
