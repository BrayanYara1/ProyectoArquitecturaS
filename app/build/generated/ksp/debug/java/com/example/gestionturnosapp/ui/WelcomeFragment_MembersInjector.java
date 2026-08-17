package com.example.gestionturnosapp.ui;

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
public final class WelcomeFragment_MembersInjector implements MembersInjector<WelcomeFragment> {
  private final Provider<UserManager> userManagerProvider;

  public WelcomeFragment_MembersInjector(Provider<UserManager> userManagerProvider) {
    this.userManagerProvider = userManagerProvider;
  }

  public static MembersInjector<WelcomeFragment> create(Provider<UserManager> userManagerProvider) {
    return new WelcomeFragment_MembersInjector(userManagerProvider);
  }

  @Override
  public void injectMembers(WelcomeFragment instance) {
    injectUserManager(instance, userManagerProvider.get());
  }

  @InjectedFieldSignature("com.example.gestionturnosapp.ui.WelcomeFragment.userManager")
  public static void injectUserManager(WelcomeFragment instance, UserManager userManager) {
    instance.userManager = userManager;
  }
}
