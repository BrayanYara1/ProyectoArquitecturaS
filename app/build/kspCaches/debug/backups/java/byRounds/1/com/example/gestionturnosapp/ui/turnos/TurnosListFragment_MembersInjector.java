package com.example.gestionturnosapp.ui.turnos;

import com.example.gestionturnosapp.data.UserManager;
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
public final class TurnosListFragment_MembersInjector implements MembersInjector<TurnosListFragment> {
  private final Provider<UserManager> userManagerProvider;

  private final Provider<OfflineCacheManager> offlineCacheManagerProvider;

  public TurnosListFragment_MembersInjector(Provider<UserManager> userManagerProvider,
      Provider<OfflineCacheManager> offlineCacheManagerProvider) {
    this.userManagerProvider = userManagerProvider;
    this.offlineCacheManagerProvider = offlineCacheManagerProvider;
  }

  public static MembersInjector<TurnosListFragment> create(
      Provider<UserManager> userManagerProvider,
      Provider<OfflineCacheManager> offlineCacheManagerProvider) {
    return new TurnosListFragment_MembersInjector(userManagerProvider, offlineCacheManagerProvider);
  }

  @Override
  public void injectMembers(TurnosListFragment instance) {
    injectUserManager(instance, userManagerProvider.get());
    injectOfflineCacheManager(instance, offlineCacheManagerProvider.get());
  }

  @InjectedFieldSignature("com.example.gestionturnosapp.ui.turnos.TurnosListFragment.userManager")
  public static void injectUserManager(TurnosListFragment instance, UserManager userManager) {
    instance.userManager = userManager;
  }

  @InjectedFieldSignature("com.example.gestionturnosapp.ui.turnos.TurnosListFragment.offlineCacheManager")
  public static void injectOfflineCacheManager(TurnosListFragment instance,
      OfflineCacheManager offlineCacheManager) {
    instance.offlineCacheManager = offlineCacheManager;
  }
}
