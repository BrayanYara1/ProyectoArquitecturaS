package com.example.gestionturnosapp.di;

import com.example.gestionturnosapp.data.local.AppDatabase;
import com.example.gestionturnosapp.data.local.MedicationLogDao;
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
public final class DatabaseModule_ProvideMedicationLogDaoFactory implements Factory<MedicationLogDao> {
  private final Provider<AppDatabase> databaseProvider;

  public DatabaseModule_ProvideMedicationLogDaoFactory(Provider<AppDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public MedicationLogDao get() {
    return provideMedicationLogDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideMedicationLogDaoFactory create(
      Provider<AppDatabase> databaseProvider) {
    return new DatabaseModule_ProvideMedicationLogDaoFactory(databaseProvider);
  }

  public static MedicationLogDao provideMedicationLogDao(AppDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideMedicationLogDao(database));
  }
}
