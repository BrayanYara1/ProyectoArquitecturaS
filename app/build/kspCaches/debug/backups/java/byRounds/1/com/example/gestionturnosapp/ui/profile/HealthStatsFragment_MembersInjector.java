package com.example.gestionturnosapp.ui.profile;

import com.example.gestionturnosapp.data.UserManager;
import com.example.gestionturnosapp.data.local.OfflineCacheManager;
import com.example.gestionturnosapp.data.local.PreferenceManager;
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
public final class HealthStatsFragment_MembersInjector implements MembersInjector<HealthStatsFragment> {
  private final Provider<PreferenceManager> preferenceManagerProvider;

  private final Provider<UserManager> userManagerProvider;

  private final Provider<OfflineCacheManager> offlineCacheManagerProvider;

  public HealthStatsFragment_MembersInjector(Provider<PreferenceManager> preferenceManagerProvider,
      Provider<UserManager> userManagerProvider,
      Provider<OfflineCacheManager> offlineCacheManagerProvider) {
    this.preferenceManagerProvider = preferenceManagerProvider;
    this.userManagerProvider = userManagerProvider;
    this.offlineCacheManagerProvider = offlineCacheManagerProvider;
  }

  public static MembersInjector<HealthStatsFragment> create(
      Provider<PreferenceManager> preferenceManagerProvider,
      Provider<UserManager> userManagerProvider,
      Provider<OfflineCacheManager> offlineCacheManagerProvider) {
    return new HealthStatsFragment_MembersInjector(preferenceManagerProvider, userManagerProvider, offlineCacheManagerProvider);
  }

  @Override
  public void injectMembers(HealthStatsFragment instance) {
    injectPreferenceManager(instance, preferenceManagerProvider.get());
    injectUserManager(instance, userManagerProvider.get());
    injectOfflineCacheManager(instance, offlineCacheManagerProvider.get());
  }

  @InjectedFieldSignature("com.example.gestionturnosapp.ui.profile.HealthStatsFragment.preferenceManager")
  public static void injectPreferenceManager(HealthStatsFragment instance,
      PreferenceManager preferenceManager) {
    instance.preferenceManager = preferenceManager;
  }

  @InjectedFieldSignature("com.example.gestionturnosapp.ui.profile.HealthStatsFragment.userManager")
  public static void injectUserManager(HealthStatsFragment instance, UserManager userManager) {
    instance.userManager = userManager;
  }

  @InjectedFieldSignature("com.example.gestionturnosapp.ui.profile.HealthStatsFragment.offlineCacheManager")
  public static void injectOfflineCacheManager(HealthStatsFragment instance,
      OfflineCacheManager offlineCacheManager) {
    instance.offlineCacheManager = offlineCacheManager;
  }
}
