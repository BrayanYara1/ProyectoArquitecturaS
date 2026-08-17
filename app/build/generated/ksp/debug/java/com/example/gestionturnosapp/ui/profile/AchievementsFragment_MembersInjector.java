package com.example.gestionturnosapp.ui.profile;

import com.example.gestionturnosapp.util.AchievementManager;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class AchievementsFragment_MembersInjector implements MembersInjector<AchievementsFragment> {
  private final Provider<AchievementManager> achievementManagerProvider;

  public AchievementsFragment_MembersInjector(
      Provider<AchievementManager> achievementManagerProvider) {
    this.achievementManagerProvider = achievementManagerProvider;
  }

  public static MembersInjector<AchievementsFragment> create(
      Provider<AchievementManager> achievementManagerProvider) {
    return new AchievementsFragment_MembersInjector(achievementManagerProvider);
  }

  @Override
  public void injectMembers(AchievementsFragment instance) {
    injectAchievementManager(instance, achievementManagerProvider.get());
  }

  @InjectedFieldSignature("com.example.gestionturnosapp.ui.profile.AchievementsFragment.achievementManager")
  public static void injectAchievementManager(AchievementsFragment instance,
      AchievementManager achievementManager) {
    instance.achievementManager = achievementManager;
  }
}
