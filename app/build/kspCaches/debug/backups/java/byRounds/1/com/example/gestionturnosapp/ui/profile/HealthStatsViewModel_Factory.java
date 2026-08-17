package com.example.gestionturnosapp.ui.profile;

import com.example.gestionturnosapp.data.UserManager;
import com.example.gestionturnosapp.data.repository.HealthRepository;
import com.example.gestionturnosapp.data.repository.MedicamentoRepository;
import com.example.gestionturnosapp.data.repository.TurnoRepository;
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
public final class HealthStatsViewModel_Factory implements Factory<HealthStatsViewModel> {
  private final Provider<HealthRepository> repositoryProvider;

  private final Provider<MedicamentoRepository> medRepositoryProvider;

  private final Provider<TurnoRepository> turnoRepositoryProvider;

  private final Provider<AchievementManager> achievementManagerProvider;

  private final Provider<UserManager> userManagerProvider;

  public HealthStatsViewModel_Factory(Provider<HealthRepository> repositoryProvider,
      Provider<MedicamentoRepository> medRepositoryProvider,
      Provider<TurnoRepository> turnoRepositoryProvider,
      Provider<AchievementManager> achievementManagerProvider,
      Provider<UserManager> userManagerProvider) {
    this.repositoryProvider = repositoryProvider;
    this.medRepositoryProvider = medRepositoryProvider;
    this.turnoRepositoryProvider = turnoRepositoryProvider;
    this.achievementManagerProvider = achievementManagerProvider;
    this.userManagerProvider = userManagerProvider;
  }

  @Override
  public HealthStatsViewModel get() {
    return newInstance(repositoryProvider.get(), medRepositoryProvider.get(), turnoRepositoryProvider.get(), achievementManagerProvider.get(), userManagerProvider.get());
  }

  public static HealthStatsViewModel_Factory create(Provider<HealthRepository> repositoryProvider,
      Provider<MedicamentoRepository> medRepositoryProvider,
      Provider<TurnoRepository> turnoRepositoryProvider,
      Provider<AchievementManager> achievementManagerProvider,
      Provider<UserManager> userManagerProvider) {
    return new HealthStatsViewModel_Factory(repositoryProvider, medRepositoryProvider, turnoRepositoryProvider, achievementManagerProvider, userManagerProvider);
  }

  public static HealthStatsViewModel newInstance(HealthRepository repository,
      MedicamentoRepository medRepository, TurnoRepository turnoRepository,
      AchievementManager achievementManager, UserManager userManager) {
    return new HealthStatsViewModel(repository, medRepository, turnoRepository, achievementManager, userManager);
  }
}
