package com.example.gestionturnosapp.ui.home;

import android.app.Application;
import com.example.gestionturnosapp.data.UserManager;
import com.example.gestionturnosapp.data.local.OfflineCacheManager;
import com.example.gestionturnosapp.data.local.PreferenceManager;
import com.example.gestionturnosapp.data.repository.HealthRepository;
import com.example.gestionturnosapp.data.repository.MedicamentoRepository;
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
public final class HomeViewModel_Factory implements Factory<HomeViewModel> {
  private final Provider<Application> applicationProvider;

  private final Provider<TurnoRepository> turnoRepositoryProvider;

  private final Provider<MedicamentoRepository> medRepositoryProvider;

  private final Provider<HealthRepository> healthRepositoryProvider;

  private final Provider<AchievementManager> achievementManagerProvider;

  private final Provider<UserManager> userManagerProvider;

  private final Provider<PreferenceManager> preferenceManagerProvider;

  private final Provider<OfflineCacheManager> offlineCacheManagerProvider;

  private final Provider<ReminderManager> reminderManagerProvider;

  public HomeViewModel_Factory(Provider<Application> applicationProvider,
      Provider<TurnoRepository> turnoRepositoryProvider,
      Provider<MedicamentoRepository> medRepositoryProvider,
      Provider<HealthRepository> healthRepositoryProvider,
      Provider<AchievementManager> achievementManagerProvider,
      Provider<UserManager> userManagerProvider,
      Provider<PreferenceManager> preferenceManagerProvider,
      Provider<OfflineCacheManager> offlineCacheManagerProvider,
      Provider<ReminderManager> reminderManagerProvider) {
    this.applicationProvider = applicationProvider;
    this.turnoRepositoryProvider = turnoRepositoryProvider;
    this.medRepositoryProvider = medRepositoryProvider;
    this.healthRepositoryProvider = healthRepositoryProvider;
    this.achievementManagerProvider = achievementManagerProvider;
    this.userManagerProvider = userManagerProvider;
    this.preferenceManagerProvider = preferenceManagerProvider;
    this.offlineCacheManagerProvider = offlineCacheManagerProvider;
    this.reminderManagerProvider = reminderManagerProvider;
  }

  @Override
  public HomeViewModel get() {
    return newInstance(applicationProvider.get(), turnoRepositoryProvider.get(), medRepositoryProvider.get(), healthRepositoryProvider.get(), achievementManagerProvider.get(), userManagerProvider.get(), preferenceManagerProvider.get(), offlineCacheManagerProvider.get(), reminderManagerProvider.get());
  }

  public static HomeViewModel_Factory create(Provider<Application> applicationProvider,
      Provider<TurnoRepository> turnoRepositoryProvider,
      Provider<MedicamentoRepository> medRepositoryProvider,
      Provider<HealthRepository> healthRepositoryProvider,
      Provider<AchievementManager> achievementManagerProvider,
      Provider<UserManager> userManagerProvider,
      Provider<PreferenceManager> preferenceManagerProvider,
      Provider<OfflineCacheManager> offlineCacheManagerProvider,
      Provider<ReminderManager> reminderManagerProvider) {
    return new HomeViewModel_Factory(applicationProvider, turnoRepositoryProvider, medRepositoryProvider, healthRepositoryProvider, achievementManagerProvider, userManagerProvider, preferenceManagerProvider, offlineCacheManagerProvider, reminderManagerProvider);
  }

  public static HomeViewModel newInstance(Application application, TurnoRepository turnoRepository,
      MedicamentoRepository medRepository, HealthRepository healthRepository,
      AchievementManager achievementManager, UserManager userManager,
      PreferenceManager preferenceManager, OfflineCacheManager offlineCacheManager,
      ReminderManager reminderManager) {
    return new HomeViewModel(application, turnoRepository, medRepository, healthRepository, achievementManager, userManager, preferenceManager, offlineCacheManager, reminderManager);
  }
}
