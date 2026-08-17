package com.example.gestionturnosapp.data.local;

import android.content.Context;
import com.example.gestionturnosapp.data.UserManager;
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
public final class ImageStorageManager_Factory implements Factory<ImageStorageManager> {
  private final Provider<Context> contextProvider;

  private final Provider<UserManager> userManagerProvider;

  public ImageStorageManager_Factory(Provider<Context> contextProvider,
      Provider<UserManager> userManagerProvider) {
    this.contextProvider = contextProvider;
    this.userManagerProvider = userManagerProvider;
  }

  @Override
  public ImageStorageManager get() {
    return newInstance(contextProvider.get(), userManagerProvider.get());
  }

  public static ImageStorageManager_Factory create(Provider<Context> contextProvider,
      Provider<UserManager> userManagerProvider) {
    return new ImageStorageManager_Factory(contextProvider, userManagerProvider);
  }

  public static ImageStorageManager newInstance(Context context, UserManager userManager) {
    return new ImageStorageManager(context, userManager);
  }
}
