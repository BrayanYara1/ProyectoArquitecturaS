package com.example.gestionturnosapp.ui.estudios;

import com.example.gestionturnosapp.data.UserManager;
import com.example.gestionturnosapp.data.local.ImageStorageManager;
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
public final class EstudiosFragment_MembersInjector implements MembersInjector<EstudiosFragment> {
  private final Provider<UserManager> userManagerProvider;

  private final Provider<ImageStorageManager> imageStorageManagerProvider;

  private final Provider<PreferenceManager> preferenceManagerProvider;

  private final Provider<OfflineCacheManager> offlineCacheManagerProvider;

  public EstudiosFragment_MembersInjector(Provider<UserManager> userManagerProvider,
      Provider<ImageStorageManager> imageStorageManagerProvider,
      Provider<PreferenceManager> preferenceManagerProvider,
      Provider<OfflineCacheManager> offlineCacheManagerProvider) {
    this.userManagerProvider = userManagerProvider;
    this.imageStorageManagerProvider = imageStorageManagerProvider;
    this.preferenceManagerProvider = preferenceManagerProvider;
    this.offlineCacheManagerProvider = offlineCacheManagerProvider;
  }

  public static MembersInjector<EstudiosFragment> create(Provider<UserManager> userManagerProvider,
      Provider<ImageStorageManager> imageStorageManagerProvider,
      Provider<PreferenceManager> preferenceManagerProvider,
      Provider<OfflineCacheManager> offlineCacheManagerProvider) {
    return new EstudiosFragment_MembersInjector(userManagerProvider, imageStorageManagerProvider, preferenceManagerProvider, offlineCacheManagerProvider);
  }

  @Override
  public void injectMembers(EstudiosFragment instance) {
    injectUserManager(instance, userManagerProvider.get());
    injectImageStorageManager(instance, imageStorageManagerProvider.get());
    injectPreferenceManager(instance, preferenceManagerProvider.get());
    injectOfflineCacheManager(instance, offlineCacheManagerProvider.get());
  }

  @InjectedFieldSignature("com.example.gestionturnosapp.ui.estudios.EstudiosFragment.userManager")
  public static void injectUserManager(EstudiosFragment instance, UserManager userManager) {
    instance.userManager = userManager;
  }

  @InjectedFieldSignature("com.example.gestionturnosapp.ui.estudios.EstudiosFragment.imageStorageManager")
  public static void injectImageStorageManager(EstudiosFragment instance,
      ImageStorageManager imageStorageManager) {
    instance.imageStorageManager = imageStorageManager;
  }

  @InjectedFieldSignature("com.example.gestionturnosapp.ui.estudios.EstudiosFragment.preferenceManager")
  public static void injectPreferenceManager(EstudiosFragment instance,
      PreferenceManager preferenceManager) {
    instance.preferenceManager = preferenceManager;
  }

  @InjectedFieldSignature("com.example.gestionturnosapp.ui.estudios.EstudiosFragment.offlineCacheManager")
  public static void injectOfflineCacheManager(EstudiosFragment instance,
      OfflineCacheManager offlineCacheManager) {
    instance.offlineCacheManager = offlineCacheManager;
  }
}
