package com.example.gestionturnosapp.util;

import android.content.Context;
import androidx.work.WorkerParameters;
import com.example.gestionturnosapp.data.local.OfflineCacheManager;
import com.example.gestionturnosapp.data.repository.HealthRepository;
import com.example.gestionturnosapp.data.repository.MedicamentoRepository;
import com.example.gestionturnosapp.data.repository.TurnoRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class SyncWorker_Factory {
  private final Provider<MedicamentoRepository> medRepositoryProvider;

  private final Provider<TurnoRepository> turnoRepositoryProvider;

  private final Provider<HealthRepository> healthRepositoryProvider;

  private final Provider<OfflineCacheManager> offlineCacheManagerProvider;

  public SyncWorker_Factory(Provider<MedicamentoRepository> medRepositoryProvider,
      Provider<TurnoRepository> turnoRepositoryProvider,
      Provider<HealthRepository> healthRepositoryProvider,
      Provider<OfflineCacheManager> offlineCacheManagerProvider) {
    this.medRepositoryProvider = medRepositoryProvider;
    this.turnoRepositoryProvider = turnoRepositoryProvider;
    this.healthRepositoryProvider = healthRepositoryProvider;
    this.offlineCacheManagerProvider = offlineCacheManagerProvider;
  }

  public SyncWorker get(Context appContext, WorkerParameters workerParams) {
    return newInstance(appContext, workerParams, medRepositoryProvider.get(), turnoRepositoryProvider.get(), healthRepositoryProvider.get(), offlineCacheManagerProvider.get());
  }

  public static SyncWorker_Factory create(Provider<MedicamentoRepository> medRepositoryProvider,
      Provider<TurnoRepository> turnoRepositoryProvider,
      Provider<HealthRepository> healthRepositoryProvider,
      Provider<OfflineCacheManager> offlineCacheManagerProvider) {
    return new SyncWorker_Factory(medRepositoryProvider, turnoRepositoryProvider, healthRepositoryProvider, offlineCacheManagerProvider);
  }

  public static SyncWorker newInstance(Context appContext, WorkerParameters workerParams,
      MedicamentoRepository medRepository, TurnoRepository turnoRepository,
      HealthRepository healthRepository, OfflineCacheManager offlineCacheManager) {
    return new SyncWorker(appContext, workerParams, medRepository, turnoRepository, healthRepository, offlineCacheManager);
  }
}
