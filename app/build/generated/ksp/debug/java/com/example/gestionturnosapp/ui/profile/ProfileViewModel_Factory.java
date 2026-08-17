package com.example.gestionturnosapp.ui.profile;

import android.app.Application;
import com.example.gestionturnosapp.data.UserManager;
import com.example.gestionturnosapp.data.remote.ApiService;
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
public final class ProfileViewModel_Factory implements Factory<ProfileViewModel> {
  private final Provider<Application> applicationProvider;

  private final Provider<UserManager> userManagerProvider;

  private final Provider<ApiService> apiServiceProvider;

  public ProfileViewModel_Factory(Provider<Application> applicationProvider,
      Provider<UserManager> userManagerProvider, Provider<ApiService> apiServiceProvider) {
    this.applicationProvider = applicationProvider;
    this.userManagerProvider = userManagerProvider;
    this.apiServiceProvider = apiServiceProvider;
  }

  @Override
  public ProfileViewModel get() {
    return newInstance(applicationProvider.get(), userManagerProvider.get(), apiServiceProvider.get());
  }

  public static ProfileViewModel_Factory create(Provider<Application> applicationProvider,
      Provider<UserManager> userManagerProvider, Provider<ApiService> apiServiceProvider) {
    return new ProfileViewModel_Factory(applicationProvider, userManagerProvider, apiServiceProvider);
  }

  public static ProfileViewModel newInstance(Application application, UserManager userManager,
      ApiService apiService) {
    return new ProfileViewModel(application, userManager, apiService);
  }
}
