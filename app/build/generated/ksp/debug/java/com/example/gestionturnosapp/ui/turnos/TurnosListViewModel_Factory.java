package com.example.gestionturnosapp.ui.turnos;

import android.app.Application;
import com.example.gestionturnosapp.data.local.OfflineCacheManager;
import com.example.gestionturnosapp.data.repository.TurnoRepository;
import com.example.gestionturnosapp.notifications.ReminderManager;
import com.example.gestionturnosapp.util.AchievementManager;
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
public final class TurnosListViewModel_Factory implements Factory<TurnosListViewModel> {
  private final Provider<Application> applicationProvider;

  private final Provider<TurnoRepository> repositoryProvider;

  private final Provider<AchievementManager> achievementManagerProvider;

  private final Provider<ReminderManager> reminderManagerProvider;

  private final Provider<OfflineCacheManager> offlineCacheManagerProvider;

  public TurnosListViewModel_Factory(Provider<Application> applicationProvider,
      Provider<TurnoRepository> repositoryProvider,
      Provider<AchievementManager> achievementManagerProvider,
      Provider<ReminderManager> reminderManagerProvider,
      Provider<OfflineCacheManager> offlineCacheManagerProvider) {
    this.applicationProvider = applicationProvider;
    this.repositoryProvider = repositoryProvider;
    this.achievementManagerProvider = achievementManagerProvider;
    this.reminderManagerProvider = reminderManagerProvider;
    this.offlineCacheManagerProvider = offlineCacheManagerProvider;
  }

  @Override
  public TurnosListViewModel get() {
    return newInstance(applicationProvider.get(), repositoryProvider.get(), achievementManagerProvider.get(), reminderManagerProvider.get(), offlineCacheManagerProvider.get());
  }

  public static TurnosListViewModel_Factory create(Provider<Application> applicationProvider,
      Provider<TurnoRepository> repositoryProvider,
      Provider<AchievementManager> achievementManagerProvider,
      Provider<ReminderManager> reminderManagerProvider,
      Provider<OfflineCacheManager> offlineCacheManagerProvider) {
    return new TurnosListViewModel_Factory(applicationProvider, repositoryProvider, achievementManagerProvider, reminderManagerProvider, offlineCacheManagerProvider);
  }

  public static TurnosListViewModel newInstance(Application application, TurnoRepository repository,
      AchievementManager achievementManager, ReminderManager reminderManager,
      OfflineCacheManager offlineCacheManager) {
    return new TurnosListViewModel(application, repository, achievementManager, reminderManager, offlineCacheManager);
  }
}
