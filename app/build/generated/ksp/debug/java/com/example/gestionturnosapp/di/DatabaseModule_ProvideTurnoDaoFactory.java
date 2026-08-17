package com.example.gestionturnosapp.di;

import com.example.gestionturnosapp.data.local.AppDatabase;
import com.example.gestionturnosapp.data.local.TurnoDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class DatabaseModule_ProvideTurnoDaoFactory implements Factory<TurnoDao> {
  private final Provider<AppDatabase> databaseProvider;

  public DatabaseModule_ProvideTurnoDaoFactory(Provider<AppDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public TurnoDao get() {
    return provideTurnoDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideTurnoDaoFactory create(
      Provider<AppDatabase> databaseProvider) {
    return new DatabaseModule_ProvideTurnoDaoFactory(databaseProvider);
  }

  public static TurnoDao provideTurnoDao(AppDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideTurnoDao(database));
  }
}
