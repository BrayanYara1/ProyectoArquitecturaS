package com.example.gestionturnosapp.ui.chat;

import android.app.Application;
import com.example.gestionturnosapp.data.local.OfflineCacheManager;
import com.example.gestionturnosapp.data.repository.ChatRepository;
import com.example.gestionturnosapp.data.repository.HealthRepository;
import com.example.gestionturnosapp.data.repository.MedicamentoRepository;
import com.example.gestionturnosapp.data.repository.TurnoRepository;
import com.example.gestionturnosapp.util.SmartAssistant;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class ChatViewModel_Factory implements Factory<ChatViewModel> {
  private final Provider<Application> applicationProvider;

  private final Provider<ChatRepository> repositoryProvider;

  private final Provider<TurnoRepository> turnoRepositoryProvider;

  private final Provider<MedicamentoRepository> medRepositoryProvider;

  private final Provider<HealthRepository> healthRepositoryProvider;

  private final Provider<SmartAssistant> smartAssistantProvider;

  private final Provider<OfflineCacheManager> offlineCacheManagerProvider;

  public ChatViewModel_Factory(Provider<Application> applicationProvider,
      Provider<ChatRepository> repositoryProvider,
      Provider<TurnoRepository> turnoRepositoryProvider,
      Provider<MedicamentoRepository> medRepositoryProvider,
      Provider<HealthRepository> healthRepositoryProvider,
      Provider<SmartAssistant> smartAssistantProvider,
      Provider<OfflineCacheManager> offlineCacheManagerProvider) {
    this.applicationProvider = applicationProvider;
    this.repositoryProvider = repositoryProvider;
    this.turnoRepositoryProvider = turnoRepositoryProvider;
    this.medRepositoryProvider = medRepositoryProvider;
    this.healthRepositoryProvider = healthRepositoryProvider;
    this.smartAssistantProvider = smartAssistantProvider;
    this.offlineCacheManagerProvider = offlineCacheManagerProvider;
  }

  @Override
  public ChatViewModel get() {
    return newInstance(applicationProvider.get(), repositoryProvider.get(), turnoRepositoryProvider.get(), medRepositoryProvider.get(), healthRepositoryProvider.get(), smartAssistantProvider.get(), offlineCacheManagerProvider.get());
  }

  public static ChatViewModel_Factory create(Provider<Application> applicationProvider,
      Provider<ChatRepository> repositoryProvider,
      Provider<TurnoRepository> turnoRepositoryProvider,
      Provider<MedicamentoRepository> medRepositoryProvider,
      Provider<HealthRepository> healthRepositoryProvider,
      Provider<SmartAssistant> smartAssistantProvider,
      Provider<OfflineCacheManager> offlineCacheManagerProvider) {
    return new ChatViewModel_Factory(applicationProvider, repositoryProvider, turnoRepositoryProvider, medRepositoryProvider, healthRepositoryProvider, smartAssistantProvider, offlineCacheManagerProvider);
  }

  public static ChatViewModel newInstance(Application application, ChatRepository repository,
      TurnoRepository turnoRepository, MedicamentoRepository medRepository,
      HealthRepository healthRepository, SmartAssistant smartAssistant,
      OfflineCacheManager offlineCacheManager) {
    return new ChatViewModel(application, repository, turnoRepository, medRepository, healthRepository, smartAssistant, offlineCacheManager);
  }
}
