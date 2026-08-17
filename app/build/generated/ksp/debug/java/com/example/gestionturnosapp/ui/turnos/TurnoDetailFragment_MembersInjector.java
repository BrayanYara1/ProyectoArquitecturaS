package com.example.gestionturnosapp.ui.turnos;

import com.example.gestionturnosapp.data.UserManager;
import com.example.gestionturnosapp.data.local.OfflineCacheManager;
import com.example.gestionturnosapp.notifications.ReminderManager;
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
public final class TurnoDetailFragment_MembersInjector implements MembersInjector<TurnoDetailFragment> {
  private final Provider<UserManager> userManagerProvider;

  private final Provider<ReminderManager> reminderManagerProvider;

  private final Provider<OfflineCacheManager> offlineCacheManagerProvider;

  public TurnoDetailFragment_MembersInjector(Provider<UserManager> userManagerProvider,
      Provider<ReminderManager> reminderManagerProvider,
      Provider<OfflineCacheManager> offlineCacheManagerProvider) {
    this.userManagerProvider = userManagerProvider;
    this.reminderManagerProvider = reminderManagerProvider;
    this.offlineCacheManagerProvider = offlineCacheManagerProvider;
  }

  public static MembersInjector<TurnoDetailFragment> create(
      Provider<UserManager> userManagerProvider, Provider<ReminderManager> reminderManagerProvider,
      Provider<OfflineCacheManager> offlineCacheManagerProvider) {
    return new TurnoDetailFragment_MembersInjector(userManagerProvider, reminderManagerProvider, offlineCacheManagerProvider);
  }

  @Override
  public void injectMembers(TurnoDetailFragment instance) {
    injectUserManager(instance, userManagerProvider.get());
    injectReminderManager(instance, reminderManagerProvider.get());
    injectOfflineCacheManager(instance, offlineCacheManagerProvider.get());
  }

  @InjectedFieldSignature("com.example.gestionturnosapp.ui.turnos.TurnoDetailFragment.userManager")
  public static void injectUserManager(TurnoDetailFragment instance, UserManager userManager) {
    instance.userManager = userManager;
  }

  @InjectedFieldSignature("com.example.gestionturnosapp.ui.turnos.TurnoDetailFragment.reminderManager")
  public static void injectReminderManager(TurnoDetailFragment instance,
      ReminderManager reminderManager) {
    instance.reminderManager = reminderManager;
  }

  @InjectedFieldSignature("com.example.gestionturnosapp.ui.turnos.TurnoDetailFragment.offlineCacheManager")
  public static void injectOfflineCacheManager(TurnoDetailFragment instance,
      OfflineCacheManager offlineCacheManager) {
    instance.offlineCacheManager = offlineCacheManager;
  }
}
