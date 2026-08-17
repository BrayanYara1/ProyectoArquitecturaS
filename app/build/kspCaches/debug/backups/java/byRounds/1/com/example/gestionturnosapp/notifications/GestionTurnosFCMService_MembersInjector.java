package com.example.gestionturnosapp.notifications;

import com.example.gestionturnosapp.data.UserManager;
import com.example.gestionturnosapp.data.remote.ApiService;
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
public final class GestionTurnosFCMService_MembersInjector implements MembersInjector<GestionTurnosFCMService> {
  private final Provider<UserManager> userManagerProvider;

  private final Provider<ApiService> apiServiceProvider;

  public GestionTurnosFCMService_MembersInjector(Provider<UserManager> userManagerProvider,
      Provider<ApiService> apiServiceProvider) {
    this.userManagerProvider = userManagerProvider;
    this.apiServiceProvider = apiServiceProvider;
  }

  public static MembersInjector<GestionTurnosFCMService> create(
      Provider<UserManager> userManagerProvider, Provider<ApiService> apiServiceProvider) {
    return new GestionTurnosFCMService_MembersInjector(userManagerProvider, apiServiceProvider);
  }

  @Override
  public void injectMembers(GestionTurnosFCMService instance) {
    injectUserManager(instance, userManagerProvider.get());
    injectApiService(instance, apiServiceProvider.get());
  }

  @InjectedFieldSignature("com.example.gestionturnosapp.notifications.GestionTurnosFCMService.userManager")
  public static void injectUserManager(GestionTurnosFCMService instance, UserManager userManager) {
    instance.userManager = userManager;
  }

  @InjectedFieldSignature("com.example.gestionturnosapp.notifications.GestionTurnosFCMService.apiService")
  public static void injectApiService(GestionTurnosFCMService instance, ApiService apiService) {
    instance.apiService = apiService;
  }
}
