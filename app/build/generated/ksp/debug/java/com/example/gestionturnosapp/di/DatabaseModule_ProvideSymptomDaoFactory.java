package com.example.gestionturnosapp.di;

import com.example.gestionturnosapp.data.local.AppDatabase;
import com.example.gestionturnosapp.data.local.SymptomDao;
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
public final class DatabaseModule_ProvideSymptomDaoFactory implements Factory<SymptomDao> {
  private final Provider<AppDatabase> databaseProvider;

  public DatabaseModule_ProvideSymptomDaoFactory(Provider<AppDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public SymptomDao get() {
    return provideSymptomDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideSymptomDaoFactory create(
      Provider<AppDatabase> databaseProvider) {
    return new DatabaseModule_ProvideSymptomDaoFactory(databaseProvider);
  }

  public static SymptomDao provideSymptomDao(AppDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideSymptomDao(database));
  }
}
