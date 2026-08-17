package com.example.gestionturnosapp.di;

import com.example.gestionturnosapp.data.local.AchievementDao;
import com.example.gestionturnosapp.data.local.AppDatabase;
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
public final class DatabaseModule_ProvideAchievementDaoFactory implements Factory<AchievementDao> {
  private final Provider<AppDatabase> databaseProvider;

  public DatabaseModule_ProvideAchievementDaoFactory(Provider<AppDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public AchievementDao get() {
    return provideAchievementDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideAchievementDaoFactory create(
      Provider<AppDatabase> databaseProvider) {
    return new DatabaseModule_ProvideAchievementDaoFactory(databaseProvider);
  }

  public static AchievementDao provideAchievementDao(AppDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideAchievementDao(database));
  }
}
