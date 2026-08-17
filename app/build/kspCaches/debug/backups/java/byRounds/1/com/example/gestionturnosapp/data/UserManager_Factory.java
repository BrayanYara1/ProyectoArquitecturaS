package com.example.gestionturnosapp.data;

import android.content.Context;
import com.google.gson.Gson;
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
public final class UserManager_Factory implements Factory<UserManager> {
  private final Provider<Context> contextProvider;

  private final Provider<Gson> gsonProvider;

  public UserManager_Factory(Provider<Context> contextProvider, Provider<Gson> gsonProvider) {
    this.contextProvider = contextProvider;
    this.gsonProvider = gsonProvider;
  }

  @Override
  public UserManager get() {
    return newInstance(contextProvider.get(), gsonProvider.get());
  }

  public static UserManager_Factory create(Provider<Context> contextProvider,
      Provider<Gson> gsonProvider) {
    return new UserManager_Factory(contextProvider, gsonProvider);
  }

  public static UserManager newInstance(Context context, Gson gson) {
    return new UserManager(context, gson);
  }
}
