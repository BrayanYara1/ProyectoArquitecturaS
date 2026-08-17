package com.example.gestionturnosapp.ui.auth;

import android.app.Application;
import com.example.gestionturnosapp.data.UserManager;
import com.example.gestionturnosapp.data.remote.ApiService;
import com.example.gestionturnosapp.data.repository.AuthRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class AuthViewModel_Factory implements Factory<AuthViewModel> {
  private final Provider<Application> applicationProvider;

  private final Provider<AuthRepository> repositoryProvider;

  private final Provider<UserManager> userManagerProvider;

  private final Provider<ApiService> apiServiceProvider;

  public AuthViewModel_Factory(Provider<Application> applicationProvider,
      Provider<AuthRepository> repositoryProvider, Provider<UserManager> userManagerProvider,
      Provider<ApiService> apiServiceProvider) {
    this.applicationProvider = applicationProvider;
    this.repositoryProvider = repositoryProvider;
    this.userManagerProvider = userManagerProvider;
    this.apiServiceProvider = apiServiceProvider;
  }

  @Override
  public AuthViewModel get() {
    return newInstance(applicationProvider.get(), repositoryProvider.get(), userManagerProvider.get(), apiServiceProvider.get());
  }

  public static AuthViewModel_Factory create(Provider<Application> applicationProvider,
      Provider<AuthRepository> repositoryProvider, Provider<UserManager> userManagerProvider,
      Provider<ApiService> apiServiceProvider) {
    return new AuthViewModel_Factory(applicationProvider, repositoryProvider, userManagerProvider, apiServiceProvider);
  }

  public static AuthViewModel newInstance(Application application, AuthRepository repository,
      UserManager userManager, ApiService apiService) {
    return new AuthViewModel(application, repository, userManager, apiService);
  }
}
