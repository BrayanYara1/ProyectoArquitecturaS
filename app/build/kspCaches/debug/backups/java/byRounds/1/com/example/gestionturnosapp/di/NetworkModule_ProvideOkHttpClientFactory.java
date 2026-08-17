package com.example.gestionturnosapp.di;

import com.example.gestionturnosapp.data.UserManager;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import okhttp3.OkHttpClient;

@ScopeMetadata("javax.inject.Singleton")
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
public final class NetworkModule_ProvideOkHttpClientFactory implements Factory<OkHttpClient> {
  private final Provider<UserManager> userManagerProvider;

  public NetworkModule_ProvideOkHttpClientFactory(Provider<UserManager> userManagerProvider) {
    this.userManagerProvider = userManagerProvider;
  }

  @Override
  public OkHttpClient get() {
    return provideOkHttpClient(userManagerProvider.get());
  }

  public static NetworkModule_ProvideOkHttpClientFactory create(
      Provider<UserManager> userManagerProvider) {
    return new NetworkModule_ProvideOkHttpClientFactory(userManagerProvider);
  }

  public static OkHttpClient provideOkHttpClient(UserManager userManager) {
    return Preconditions.checkNotNullFromProvides(NetworkModule.INSTANCE.provideOkHttpClient(userManager));
  }
}
