package com.example.gestionturnosapp.ui.turnos;

import com.example.gestionturnosapp.data.UserManager;
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
public final class SolicitarTurnoFragment_MembersInjector implements MembersInjector<SolicitarTurnoFragment> {
  private final Provider<UserManager> userManagerProvider;

  public SolicitarTurnoFragment_MembersInjector(Provider<UserManager> userManagerProvider) {
    this.userManagerProvider = userManagerProvider;
  }

  public static MembersInjector<SolicitarTurnoFragment> create(
      Provider<UserManager> userManagerProvider) {
    return new SolicitarTurnoFragment_MembersInjector(userManagerProvider);
  }

  @Override
  public void injectMembers(SolicitarTurnoFragment instance) {
    injectUserManager(instance, userManagerProvider.get());
  }

  @InjectedFieldSignature("com.example.gestionturnosapp.ui.turnos.SolicitarTurnoFragment.userManager")
  public static void injectUserManager(SolicitarTurnoFragment instance, UserManager userManager) {
    instance.userManager = userManager;
  }
}
