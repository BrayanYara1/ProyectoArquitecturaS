package com.example.gestionturnosapp.ui.auth;

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
public final class LoginFragment_MembersInjector implements MembersInjector<LoginFragment> {
  private final Provider<UserManager> userManagerProvider;

  public LoginFragment_MembersInjector(Provider<UserManager> userManagerProvider) {
    this.userManagerProvider = userManagerProvider;
  }

  public static MembersInjector<LoginFragment> create(Provider<UserManager> userManagerProvider) {
    return new LoginFragment_MembersInjector(userManagerProvider);
  }

  @Override
  public void injectMembers(LoginFragment instance) {
    injectUserManager(instance, userManagerProvider.get());
  }

  @InjectedFieldSignature("com.example.gestionturnosapp.ui.auth.LoginFragment.userManager")
  public static void injectUserManager(LoginFragment instance, UserManager userManager) {
    instance.userManager = userManager;
  }
}
