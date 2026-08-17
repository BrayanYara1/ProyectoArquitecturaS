package com.example.gestionturnosapp.di;

import com.example.gestionturnosapp.data.local.AppDatabase;
import com.example.gestionturnosapp.data.local.EstudioDao;
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
public final class DatabaseModule_ProvideEstudioDaoFactory implements Factory<EstudioDao> {
  private final Provider<AppDatabase> databaseProvider;

  public DatabaseModule_ProvideEstudioDaoFactory(Provider<AppDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public EstudioDao get() {
    return provideEstudioDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideEstudioDaoFactory create(
      Provider<AppDatabase> databaseProvider) {
    return new DatabaseModule_ProvideEstudioDaoFactory(databaseProvider);
  }

  public static EstudioDao provideEstudioDao(AppDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideEstudioDao(database));
  }
}
