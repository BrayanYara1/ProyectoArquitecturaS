package com.example.gestionturnosapp.util;

import android.content.Context;
import com.example.gestionturnosapp.data.local.AchievementDao;
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
public final class AchievementManager_Factory implements Factory<AchievementManager> {
  private final Provider<Context> contextProvider;

  private final Provider<AchievementDao> achievementDaoProvider;

  public AchievementManager_Factory(Provider<Context> contextProvider,
      Provider<AchievementDao> achievementDaoProvider) {
    this.contextProvider = contextProvider;
    this.achievementDaoProvider = achievementDaoProvider;
  }

  @Override
  public AchievementManager get() {
    return newInstance(contextProvider.get(), achievementDaoProvider.get());
  }

  public static AchievementManager_Factory create(Provider<Context> contextProvider,
      Provider<AchievementDao> achievementDaoProvider) {
    return new AchievementManager_Factory(contextProvider, achievementDaoProvider);
  }

  public static AchievementManager newInstance(Context context, AchievementDao achievementDao) {
    return new AchievementManager(context, achievementDao);
  }
}
