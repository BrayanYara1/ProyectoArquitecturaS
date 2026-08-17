package com.example.gestionturnosapp.ui.estudios;

import android.app.Application;
import com.example.gestionturnosapp.data.local.OfflineCacheManager;
import com.example.gestionturnosapp.data.repository.EstudioRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class EstudiosViewModel_Factory implements Factory<EstudiosViewModel> {
  private final Provider<Application> applicationProvider;

  private final Provider<EstudioRepository> repositoryProvider;

  private final Provider<OfflineCacheManager> offlineCacheManagerProvider;

  public EstudiosViewModel_Factory(Provider<Application> applicationProvider,
      Provider<EstudioRepository> repositoryProvider,
      Provider<OfflineCacheManager> offlineCacheManagerProvider) {
    this.applicationProvider = applicationProvider;
    this.repositoryProvider = repositoryProvider;
    this.offlineCacheManagerProvider = offlineCacheManagerProvider;
  }

  @Override
  public EstudiosViewModel get() {
    return newInstance(applicationProvider.get(), repositoryProvider.get(), offlineCacheManagerProvider.get());
  }

  public static EstudiosViewModel_Factory create(Provider<Application> applicationProvider,
      Provider<EstudioRepository> repositoryProvider,
      Provider<OfflineCacheManager> offlineCacheManagerProvider) {
    return new EstudiosViewModel_Factory(applicationProvider, repositoryProvider, offlineCacheManagerProvider);
  }

  public static EstudiosViewModel newInstance(Application application, EstudioRepository repository,
      OfflineCacheManager offlineCacheManager) {
    return new EstudiosViewModel(application, repository, offlineCacheManager);
  }
}
