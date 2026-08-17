package com.example.gestionturnosapp.util;

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
public final class SmartAssistant_Factory implements Factory<SmartAssistant> {
  private final Provider<Context> contextProvider;

  public SmartAssistant_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public SmartAssistant get() {
    return newInstance(contextProvider.get());
  }

  public static SmartAssistant_Factory create(Provider<Context> contextProvider) {
    return new SmartAssistant_Factory(contextProvider);
  }

  public static SmartAssistant newInstance(Context context) {
    return new SmartAssistant(context);
  }
}
