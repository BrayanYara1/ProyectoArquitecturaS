package com.example.gestionturnosapp.notifications;

import com.example.gestionturnosapp.data.local.PreferenceManager;
import com.example.gestionturnosapp.data.repository.MedicamentoRepository;
import com.example.gestionturnosapp.data.repository.TurnoRepository;
import com.example.gestionturnosapp.util.AchievementManager;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class ReminderReceiver_MembersInjector implements MembersInjector<ReminderReceiver> {
  private final Provider<ReminderManager> reminderManagerProvider;

  private final Provider<MedicamentoRepository> medRepositoryProvider;

  private final Provider<TurnoRepository> turnoRepositoryProvider;

  private final Provider<AchievementManager> achievementManagerProvider;

  private final Provider<PreferenceManager> preferenceManagerProvider;

  public ReminderReceiver_MembersInjector(Provider<ReminderManager> reminderManagerProvider,
      Provider<MedicamentoRepository> medRepositoryProvider,
      Provider<TurnoRepository> turnoRepositoryProvider,
      Provider<AchievementManager> achievementManagerProvider,
      Provider<PreferenceManager> preferenceManagerProvider) {
    this.reminderManagerProvider = reminderManagerProvider;
    this.medRepositoryProvider = medRepositoryProvider;
    this.turnoRepositoryProvider = turnoRepositoryProvider;
    this.achievementManagerProvider = achievementManagerProvider;
    this.preferenceManagerProvider = preferenceManagerProvider;
  }

  public static MembersInjector<ReminderReceiver> create(
      Provider<ReminderManager> reminderManagerProvider,
      Provider<MedicamentoRepository> medRepositoryProvider,
      Provider<TurnoRepository> turnoRepositoryProvider,
      Provider<AchievementManager> achievementManagerProvider,
      Provider<PreferenceManager> preferenceManagerProvider) {
    return new ReminderReceiver_MembersInjector(reminderManagerProvider, medRepositoryProvider, turnoRepositoryProvider, achievementManagerProvider, preferenceManagerProvider);
  }

  @Override
  public void injectMembers(ReminderReceiver instance) {
    injectReminderManager(instance, reminderManagerProvider.get());
    injectMedRepository(instance, medRepositoryProvider.get());
    injectTurnoRepository(instance, turnoRepositoryProvider.get());
    injectAchievementManager(instance, achievementManagerProvider.get());
    injectPreferenceManager(instance, preferenceManagerProvider.get());
  }

  @InjectedFieldSignature("com.example.gestionturnosapp.notifications.ReminderReceiver.reminderManager")
  public static void injectReminderManager(ReminderReceiver instance,
      ReminderManager reminderManager) {
    instance.reminderManager = reminderManager;
  }

  @InjectedFieldSignature("com.example.gestionturnosapp.notifications.ReminderReceiver.medRepository")
  public static void injectMedRepository(ReminderReceiver instance,
      MedicamentoRepository medRepository) {
    instance.medRepository = medRepository;
  }

  @InjectedFieldSignature("com.example.gestionturnosapp.notifications.ReminderReceiver.turnoRepository")
  public static void injectTurnoRepository(ReminderReceiver instance,
      TurnoRepository turnoRepository) {
    instance.turnoRepository = turnoRepository;
  }

  @InjectedFieldSignature("com.example.gestionturnosapp.notifications.ReminderReceiver.achievementManager")
  public static void injectAchievementManager(ReminderReceiver instance,
      AchievementManager achievementManager) {
    instance.achievementManager = achievementManager;
  }

  @InjectedFieldSignature("com.example.gestionturnosapp.notifications.ReminderReceiver.preferenceManager")
  public static void injectPreferenceManager(ReminderReceiver instance,
      PreferenceManager preferenceManager) {
    instance.preferenceManager = preferenceManager;
  }
}
