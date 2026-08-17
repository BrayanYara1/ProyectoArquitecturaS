package com.example.gestionturnosapp.ui.home;

import com.example.gestionturnosapp.data.UserManager;
import com.example.gestionturnosapp.data.local.ImageStorageManager;
import com.example.gestionturnosapp.data.local.OfflineCacheManager;
import com.example.gestionturnosapp.util.EmergencyManager;
import com.example.gestionturnosapp.util.SmartAssistant;
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
public final class HomeFragment_MembersInjector implements MembersInjector<HomeFragment> {
  private final Provider<UserManager> userManagerProvider;

  private final Provider<ImageStorageManager> imageStorageManagerProvider;

  private final Provider<OfflineCacheManager> offlineCacheManagerProvider;

  private final Provider<SmartAssistant> smartAssistantProvider;

  private final Provider<EmergencyManager> emergencyManagerProvider;

  public HomeFragment_MembersInjector(Provider<UserManager> userManagerProvider,
      Provider<ImageStorageManager> imageStorageManagerProvider,
      Provider<OfflineCacheManager> offlineCacheManagerProvider,
      Provider<SmartAssistant> smartAssistantProvider,
      Provider<EmergencyManager> emergencyManagerProvider) {
    this.userManagerProvider = userManagerProvider;
    this.imageStorageManagerProvider = imageStorageManagerProvider;
    this.offlineCacheManagerProvider = offlineCacheManagerProvider;
    this.smartAssistantProvider = smartAssistantProvider;
    this.emergencyManagerProvider = emergencyManagerProvider;
  }

  public static MembersInjector<HomeFragment> create(Provider<UserManager> userManagerProvider,
      Provider<ImageStorageManager> imageStorageManagerProvider,
      Provider<OfflineCacheManager> offlineCacheManagerProvider,
      Provider<SmartAssistant> smartAssistantProvider,
      Provider<EmergencyManager> emergencyManagerProvider) {
    return new HomeFragment_MembersInjector(userManagerProvider, imageStorageManagerProvider, offlineCacheManagerProvider, smartAssistantProvider, emergencyManagerProvider);
  }

  @Override
  public void injectMembers(HomeFragment instance) {
    injectUserManager(instance, userManagerProvider.get());
    injectImageStorageManager(instance, imageStorageManagerProvider.get());
    injectOfflineCacheManager(instance, offlineCacheManagerProvider.get());
    injectSmartAssistant(instance, smartAssistantProvider.get());
    injectEmergencyManager(instance, emergencyManagerProvider.get());
  }

  @InjectedFieldSignature("com.example.gestionturnosapp.ui.home.HomeFragment.userManager")
  public static void injectUserManager(HomeFragment instance, UserManager userManager) {
    instance.userManager = userManager;
  }

  @InjectedFieldSignature("com.example.gestionturnosapp.ui.home.HomeFragment.imageStorageManager")
  public static void injectImageStorageManager(HomeFragment instance,
      ImageStorageManager imageStorageManager) {
    instance.imageStorageManager = imageStorageManager;
  }

  @InjectedFieldSignature("com.example.gestionturnosapp.ui.home.HomeFragment.offlineCacheManager")
  public static void injectOfflineCacheManager(HomeFragment instance,
      OfflineCacheManager offlineCacheManager) {
    instance.offlineCacheManager = offlineCacheManager;
  }

  @InjectedFieldSignature("com.example.gestionturnosapp.ui.home.HomeFragment.smartAssistant")
  public static void injectSmartAssistant(HomeFragment instance, SmartAssistant smartAssistant) {
    instance.smartAssistant = smartAssistant;
  }

  @InjectedFieldSignature("com.example.gestionturnosapp.ui.home.HomeFragment.emergencyManager")
  public static void injectEmergencyManager(HomeFragment instance,
      EmergencyManager emergencyManager) {
    instance.emergencyManager = emergencyManager;
  }
}
