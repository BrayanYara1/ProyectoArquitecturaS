package com.example.gestionturnosapp.util;

import android.content.Context;
import com.example.gestionturnosapp.data.local.PreferenceManager;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class EmergencyManager_Factory implements Factory<EmergencyManager> {
  private final Provider<Context> contextProvider;

  private final Provider<PreferenceManager> preferenceManagerProvider;

  public EmergencyManager_Factory(Provider<Context> contextProvider,
      Provider<PreferenceManager> preferenceManagerProvider) {
    this.contextProvider = contextProvider;
    this.preferenceManagerProvider = preferenceManagerProvider;
  }

  @Override
  public EmergencyManager get() {
    return newInstance(contextProvider.get(), preferenceManagerProvider.get());
  }

  public static EmergencyManager_Factory create(Provider<Context> contextProvider,
      Provider<PreferenceManager> preferenceManagerProvider) {
    return new EmergencyManager_Factory(contextProvider, preferenceManagerProvider);
  }

  public static EmergencyManager newInstance(Context context, PreferenceManager preferenceManager) {
    return new EmergencyManager(context, preferenceManager);
  }
}
