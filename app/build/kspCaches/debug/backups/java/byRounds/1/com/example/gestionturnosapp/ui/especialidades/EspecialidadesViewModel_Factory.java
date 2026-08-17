package com.example.gestionturnosapp.ui.especialidades;

import android.app.Application;
import com.example.gestionturnosapp.data.repository.EspecialidadRepository;
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
public final class EspecialidadesViewModel_Factory implements Factory<EspecialidadesViewModel> {
  private final Provider<Application> applicationProvider;

  private final Provider<EspecialidadRepository> repositoryProvider;

  public EspecialidadesViewModel_Factory(Provider<Application> applicationProvider,
      Provider<EspecialidadRepository> repositoryProvider) {
    this.applicationProvider = applicationProvider;
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public EspecialidadesViewModel get() {
    return newInstance(applicationProvider.get(), repositoryProvider.get());
  }

  public static EspecialidadesViewModel_Factory create(Provider<Application> applicationProvider,
      Provider<EspecialidadRepository> repositoryProvider) {
    return new EspecialidadesViewModel_Factory(applicationProvider, repositoryProvider);
  }

  public static EspecialidadesViewModel newInstance(Application application,
      EspecialidadRepository repository) {
    return new EspecialidadesViewModel(application, repository);
  }
}
