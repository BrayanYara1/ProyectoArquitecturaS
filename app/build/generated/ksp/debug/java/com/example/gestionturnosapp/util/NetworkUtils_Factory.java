package com.example.gestionturnosapp.util;

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
public final class NetworkUtils_Factory implements Factory<NetworkUtils> {
  private final Provider<Gson> gsonProvider;

  private final Provider<Context> contextProvider;

  public NetworkUtils_Factory(Provider<Gson> gsonProvider, Provider<Context> contextProvider) {
    this.gsonProvider = gsonProvider;
    this.contextProvider = contextProvider;
  }

  @Override
  public NetworkUtils get() {
    return newInstance(gsonProvider.get(), contextProvider.get());
  }

  public static NetworkUtils_Factory create(Provider<Gson> gsonProvider,
      Provider<Context> contextProvider) {
    return new NetworkUtils_Factory(gsonProvider, contextProvider);
  }

  public static NetworkUtils newInstance(Gson gson, Context context) {
    return new NetworkUtils(gson, context);
  }
}
