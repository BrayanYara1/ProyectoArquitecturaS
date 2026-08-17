package com.example.gestionturnosapp.ui.chat;

import com.example.gestionturnosapp.data.UserManager;
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
public final class ChatFragment_MembersInjector implements MembersInjector<ChatFragment> {
  private final Provider<UserManager> userManagerProvider;

  public ChatFragment_MembersInjector(Provider<UserManager> userManagerProvider) {
    this.userManagerProvider = userManagerProvider;
  }

  public static MembersInjector<ChatFragment> create(Provider<UserManager> userManagerProvider) {
    return new ChatFragment_MembersInjector(userManagerProvider);
  }

  @Override
  public void injectMembers(ChatFragment instance) {
    injectUserManager(instance, userManagerProvider.get());
  }

  @InjectedFieldSignature("com.example.gestionturnosapp.ui.chat.ChatFragment.userManager")
  public static void injectUserManager(ChatFragment instance, UserManager userManager) {
    instance.userManager = userManager;
  }
}
