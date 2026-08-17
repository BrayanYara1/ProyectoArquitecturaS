package com.example.gestionturnosapp.ui.medicamentos;

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
public final class MedicamentosFragment_MembersInjector implements MembersInjector<MedicamentosFragment> {
  private final Provider<ReminderManager> reminderManagerProvider;

  private final Provider<UserManager> userManagerProvider;

  private final Provider<OfflineCacheManager> offlineCacheManagerProvider;

  public MedicamentosFragment_MembersInjector(Provider<ReminderManager> reminderManagerProvider,
      Provider<UserManager> userManagerProvider,
      Provider<OfflineCacheManager> offlineCacheManagerProvider) {
    this.reminderManagerProvider = reminderManagerProvider;
    this.userManagerProvider = userManagerProvider;
    this.offlineCacheManagerProvider = offlineCacheManagerProvider;
  }

  public static MembersInjector<MedicamentosFragment> create(
      Provider<ReminderManager> reminderManagerProvider, Provider<UserManager> userManagerProvider,
      Provider<OfflineCacheManager> offlineCacheManagerProvider) {
    return new MedicamentosFragment_MembersInjector(reminderManagerProvider, userManagerProvider, offlineCacheManagerProvider);
  }

  @Override
  public void injectMembers(MedicamentosFragment instance) {
    injectReminderManager(instance, reminderManagerProvider.get());
    injectUserManager(instance, userManagerProvider.get());
    injectOfflineCacheManager(instance, offlineCacheManagerProvider.get());
  }

  @InjectedFieldSignature("com.example.gestionturnosapp.ui.medicamentos.MedicamentosFragment.reminderManager")
  public static void injectReminderManager(MedicamentosFragment instance,
      ReminderManager reminderManager) {
    instance.reminderManager = reminderManager;
  }

  @InjectedFieldSignature("com.example.gestionturnosapp.ui.medicamentos.MedicamentosFragment.userManager")
  public static void injectUserManager(MedicamentosFragment instance, UserManager userManager) {
    instance.userManager = userManager;
  }

  @InjectedFieldSignature("com.example.gestionturnosapp.ui.medicamentos.MedicamentosFragment.offlineCacheManager")
  public static void injectOfflineCacheManager(MedicamentosFragment instance,
      OfflineCacheManager offlineCacheManager) {
    instance.offlineCacheManager = offlineCacheManager;
  }
}
