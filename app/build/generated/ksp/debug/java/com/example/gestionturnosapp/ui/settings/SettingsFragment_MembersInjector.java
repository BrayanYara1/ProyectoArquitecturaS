package com.example.gestionturnosapp.ui.settings;

import com.example.gestionturnosapp.data.UserManager;
import com.example.gestionturnosapp.data.local.OfflineCacheManager;
import com.example.gestionturnosapp.data.local.PreferenceManager;
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
public final class SettingsFragment_MembersInjector implements MembersInjector<SettingsFragment> {
  private final Provider<PreferenceManager> preferenceManagerProvider;

  private final Provider<UserManager> userManagerProvider;

  private final Provider<OfflineCacheManager> offlineCacheManagerProvider;

  private final Provider<ReminderManager> reminderManagerProvider;

  public SettingsFragment_MembersInjector(Provider<PreferenceManager> preferenceManagerProvider,
      Provider<UserManager> userManagerProvider,
      Provider<OfflineCacheManager> offlineCacheManagerProvider,
      Provider<ReminderManager> reminderManagerProvider) {
    this.preferenceManagerProvider = preferenceManagerProvider;
    this.userManagerProvider = userManagerProvider;
    this.offlineCacheManagerProvider = offlineCacheManagerProvider;
    this.reminderManagerProvider = reminderManagerProvider;
  }

  public static MembersInjector<SettingsFragment> create(
      Provider<PreferenceManager> preferenceManagerProvider,
      Provider<UserManager> userManagerProvider,
      Provider<OfflineCacheManager> offlineCacheManagerProvider,
      Provider<ReminderManager> reminderManagerProvider) {
    return new SettingsFragment_MembersInjector(preferenceManagerProvider, userManagerProvider, offlineCacheManagerProvider, reminderManagerProvider);
  }

  @Override
  public void injectMembers(SettingsFragment instance) {
    injectPreferenceManager(instance, preferenceManagerProvider.get());
    injectUserManager(instance, userManagerProvider.get());
    injectOfflineCacheManager(instance, offlineCacheManagerProvider.get());
    injectReminderManager(instance, reminderManagerProvider.get());
  }

  @InjectedFieldSignature("com.example.gestionturnosapp.ui.settings.SettingsFragment.preferenceManager")
  public static void injectPreferenceManager(SettingsFragment instance,
      PreferenceManager preferenceManager) {
    instance.preferenceManager = preferenceManager;
  }

  @InjectedFieldSignature("com.example.gestionturnosapp.ui.settings.SettingsFragment.userManager")
  public static void injectUserManager(SettingsFragment instance, UserManager userManager) {
    instance.userManager = userManager;
  }

  @InjectedFieldSignature("com.example.gestionturnosapp.ui.settings.SettingsFragment.offlineCacheManager")
  public static void injectOfflineCacheManager(SettingsFragment instance,
      OfflineCacheManager offlineCacheManager) {
    instance.offlineCacheManager = offlineCacheManager;
  }

  @InjectedFieldSignature("com.example.gestionturnosapp.ui.settings.SettingsFragment.reminderManager")
  public static void injectReminderManager(SettingsFragment instance,
      ReminderManager reminderManager) {
    instance.reminderManager = reminderManager;
  }
}
