package com.example.gestionturnosapp.ui.profile;

import com.example.gestionturnosapp.data.UserManager;
import com.example.gestionturnosapp.data.local.ImageStorageManager;
import com.example.gestionturnosapp.data.local.OfflineCacheManager;
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
public final class UserProfileFragment_MembersInjector implements MembersInjector<UserProfileFragment> {
  private final Provider<UserManager> userManagerProvider;

  private final Provider<ImageStorageManager> imageStorageManagerProvider;

  private final Provider<OfflineCacheManager> offlineCacheManagerProvider;

  public UserProfileFragment_MembersInjector(Provider<UserManager> userManagerProvider,
      Provider<ImageStorageManager> imageStorageManagerProvider,
      Provider<OfflineCacheManager> offlineCacheManagerProvider) {
    this.userManagerProvider = userManagerProvider;
    this.imageStorageManagerProvider = imageStorageManagerProvider;
    this.offlineCacheManagerProvider = offlineCacheManagerProvider;
  }

  public static MembersInjector<UserProfileFragment> create(
      Provider<UserManager> userManagerProvider,
      Provider<ImageStorageManager> imageStorageManagerProvider,
      Provider<OfflineCacheManager> offlineCacheManagerProvider) {
    return new UserProfileFragment_MembersInjector(userManagerProvider, imageStorageManagerProvider, offlineCacheManagerProvider);
  }

  @Override
  public void injectMembers(UserProfileFragment instance) {
    injectUserManager(instance, userManagerProvider.get());
    injectImageStorageManager(instance, imageStorageManagerProvider.get());
    injectOfflineCacheManager(instance, offlineCacheManagerProvider.get());
  }

  @InjectedFieldSignature("com.example.gestionturnosapp.ui.profile.UserProfileFragment.userManager")
  public static void injectUserManager(UserProfileFragment instance, UserManager userManager) {
    instance.userManager = userManager;
  }

  @InjectedFieldSignature("com.example.gestionturnosapp.ui.profile.UserProfileFragment.imageStorageManager")
  public static void injectImageStorageManager(UserProfileFragment instance,
      ImageStorageManager imageStorageManager) {
    instance.imageStorageManager = imageStorageManager;
  }

  @InjectedFieldSignature("com.example.gestionturnosapp.ui.profile.UserProfileFragment.offlineCacheManager")
  public static void injectOfflineCacheManager(UserProfileFragment instance,
      OfflineCacheManager offlineCacheManager) {
    instance.offlineCacheManager = offlineCacheManager;
  }
}
