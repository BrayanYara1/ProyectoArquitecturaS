package com.example.gestionturnosapp.ui.medicamentos;

import android.app.Application;
import com.example.gestionturnosapp.data.local.OfflineCacheManager;
import com.example.gestionturnosapp.data.repository.MedicamentoRepository;
import com.example.gestionturnosapp.notifications.ReminderManager;
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
public final class MedicamentosViewModel_Factory implements Factory<MedicamentosViewModel> {
  private final Provider<Application> applicationProvider;

  private final Provider<MedicamentoRepository> repositoryProvider;

  private final Provider<OfflineCacheManager> offlineCacheManagerProvider;

  private final Provider<ReminderManager> reminderManagerProvider;

  public MedicamentosViewModel_Factory(Provider<Application> applicationProvider,
      Provider<MedicamentoRepository> repositoryProvider,
      Provider<OfflineCacheManager> offlineCacheManagerProvider,
      Provider<ReminderManager> reminderManagerProvider) {
    this.applicationProvider = applicationProvider;
    this.repositoryProvider = repositoryProvider;
    this.offlineCacheManagerProvider = offlineCacheManagerProvider;
    this.reminderManagerProvider = reminderManagerProvider;
  }

  @Override
  public MedicamentosViewModel get() {
    return newInstance(applicationProvider.get(), repositoryProvider.get(), offlineCacheManagerProvider.get(), reminderManagerProvider.get());
  }

  public static MedicamentosViewModel_Factory create(Provider<Application> applicationProvider,
      Provider<MedicamentoRepository> repositoryProvider,
      Provider<OfflineCacheManager> offlineCacheManagerProvider,
      Provider<ReminderManager> reminderManagerProvider) {
    return new MedicamentosViewModel_Factory(applicationProvider, repositoryProvider, offlineCacheManagerProvider, reminderManagerProvider);
  }

  public static MedicamentosViewModel newInstance(Application application,
      MedicamentoRepository repository, OfflineCacheManager offlineCacheManager,
      ReminderManager reminderManager) {
    return new MedicamentosViewModel(application, repository, offlineCacheManager, reminderManager);
  }
}
