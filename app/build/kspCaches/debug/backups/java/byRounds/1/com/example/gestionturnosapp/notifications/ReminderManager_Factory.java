package com.example.gestionturnosapp.notifications;

import android.content.Context;
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
public final class ReminderManager_Factory implements Factory<ReminderManager> {
  private final Provider<Context> contextProvider;

  public ReminderManager_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public ReminderManager get() {
    return newInstance(contextProvider.get());
  }

  public static ReminderManager_Factory create(Provider<Context> contextProvider) {
    return new ReminderManager_Factory(contextProvider);
  }

  public static ReminderManager newInstance(Context context) {
    return new ReminderManager(context);
  }
}
