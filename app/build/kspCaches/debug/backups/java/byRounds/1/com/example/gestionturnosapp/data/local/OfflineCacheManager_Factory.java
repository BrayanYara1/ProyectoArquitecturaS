package com.example.gestionturnosapp.data.local;

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
public final class OfflineCacheManager_Factory implements Factory<OfflineCacheManager> {
  private final Provider<Context> contextProvider;

  private final Provider<AppDatabase> databaseProvider;

  private final Provider<Gson> gsonProvider;

  public OfflineCacheManager_Factory(Provider<Context> contextProvider,
      Provider<AppDatabase> databaseProvider, Provider<Gson> gsonProvider) {
    this.contextProvider = contextProvider;
    this.databaseProvider = databaseProvider;
    this.gsonProvider = gsonProvider;
  }

  @Override
  public OfflineCacheManager get() {
    return newInstance(contextProvider.get(), databaseProvider.get(), gsonProvider.get());
  }

  public static OfflineCacheManager_Factory create(Provider<Context> contextProvider,
      Provider<AppDatabase> databaseProvider, Provider<Gson> gsonProvider) {
    return new OfflineCacheManager_Factory(contextProvider, databaseProvider, gsonProvider);
  }

  public static OfflineCacheManager newInstance(Context context, AppDatabase database, Gson gson) {
    return new OfflineCacheManager(context, database, gson);
  }
}
