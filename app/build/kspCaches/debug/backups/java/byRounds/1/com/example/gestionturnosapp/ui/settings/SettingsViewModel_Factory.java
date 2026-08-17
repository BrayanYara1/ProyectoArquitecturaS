package com.example.gestionturnosapp.ui.settings;

import android.app.Application;
import com.example.gestionturnosapp.data.local.PreferenceManager;
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
public final class SettingsViewModel_Factory implements Factory<SettingsViewModel> {
  private final Provider<Application> applicationProvider;

  private final Provider<PreferenceManager> preferenceManagerProvider;

  public SettingsViewModel_Factory(Provider<Application> applicationProvider,
      Provider<PreferenceManager> preferenceManagerProvider) {
    this.applicationProvider = applicationProvider;
    this.preferenceManagerProvider = preferenceManagerProvider;
  }

  @Override
  public SettingsViewModel get() {
    return newInstance(applicationProvider.get(), preferenceManagerProvider.get());
  }

  public static SettingsViewModel_Factory create(Provider<Application> applicationProvider,
      Provider<PreferenceManager> preferenceManagerProvider) {
    return new SettingsViewModel_Factory(applicationProvider, preferenceManagerProvider);
  }

  public static SettingsViewModel newInstance(Application application,
      PreferenceManager preferenceManager) {
    return new SettingsViewModel(application, preferenceManager);
  }
}
