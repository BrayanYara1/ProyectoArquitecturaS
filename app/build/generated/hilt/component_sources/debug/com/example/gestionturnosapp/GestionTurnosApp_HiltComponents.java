package com.example.gestionturnosapp;

import androidx.hilt.work.HiltWrapper_WorkerFactoryModule;
import com.example.gestionturnosapp.di.AppModule;
import com.example.gestionturnosapp.di.DatabaseModule;
import com.example.gestionturnosapp.di.NetworkModule;
import com.example.gestionturnosapp.notifications.GestionTurnosFCMService_GeneratedInjector;
import com.example.gestionturnosapp.notifications.ReminderReceiver_GeneratedInjector;
import com.example.gestionturnosapp.ui.WelcomeFragment_GeneratedInjector;
import com.example.gestionturnosapp.ui.auth.AuthViewModel_HiltModules;
import com.example.gestionturnosapp.ui.auth.LoginFragment_GeneratedInjector;
import com.example.gestionturnosapp.ui.auth.RegisterFragment_GeneratedInjector;
import com.example.gestionturnosapp.ui.auth.VerifyAccountFragment_GeneratedInjector;
import com.example.gestionturnosapp.ui.chat.ChatFragment_GeneratedInjector;
import com.example.gestionturnosapp.ui.chat.ChatViewModel_HiltModules;
import com.example.gestionturnosapp.ui.especialidades.EspecialidadesFragment_GeneratedInjector;
import com.example.gestionturnosapp.ui.especialidades.EspecialidadesViewModel_HiltModules;
import com.example.gestionturnosapp.ui.estudios.EstudiosFragment_GeneratedInjector;
import com.example.gestionturnosapp.ui.estudios.EstudiosViewModel_HiltModules;
import com.example.gestionturnosapp.ui.home.HomeFragment_GeneratedInjector;
import com.example.gestionturnosapp.ui.home.HomeViewModel_HiltModules;
import com.example.gestionturnosapp.ui.medicamentos.MedicamentosFragment_GeneratedInjector;
import com.example.gestionturnosapp.ui.medicamentos.MedicamentosViewModel_HiltModules;
import com.example.gestionturnosapp.ui.profile.AchievementsFragment_GeneratedInjector;
import com.example.gestionturnosapp.ui.profile.HealthStatsFragment_GeneratedInjector;
import com.example.gestionturnosapp.ui.profile.HealthStatsViewModel_HiltModules;
import com.example.gestionturnosapp.ui.profile.ProfileViewModel_HiltModules;
import com.example.gestionturnosapp.ui.profile.UserProfileFragment_GeneratedInjector;
import com.example.gestionturnosapp.ui.settings.SettingsFragment_GeneratedInjector;
import com.example.gestionturnosapp.ui.settings.SettingsViewModel_HiltModules;
import com.example.gestionturnosapp.ui.turnos.SolicitarTurnoFragment_GeneratedInjector;
import com.example.gestionturnosapp.ui.turnos.TurnoDetailFragment_GeneratedInjector;
import com.example.gestionturnosapp.ui.turnos.TurnosListFragment_GeneratedInjector;
import com.example.gestionturnosapp.ui.turnos.TurnosListViewModel_HiltModules;
import com.example.gestionturnosapp.util.SyncWorker_HiltModule;
import dagger.Binds;
import dagger.Component;
import dagger.Module;
import dagger.Subcomponent;
import dagger.hilt.android.components.ActivityComponent;
import dagger.hilt.android.components.ActivityRetainedComponent;
import dagger.hilt.android.components.FragmentComponent;
import dagger.hilt.android.components.ServiceComponent;
import dagger.hilt.android.components.ViewComponent;
import dagger.hilt.android.components.ViewModelComponent;
import dagger.hilt.android.components.ViewWithFragmentComponent;
import dagger.hilt.android.flags.FragmentGetContextFix;
import dagger.hilt.android.flags.HiltWrapper_FragmentGetContextFix_FragmentGetContextFixModule;
import dagger.hilt.android.internal.builders.ActivityComponentBuilder;
import dagger.hilt.android.internal.builders.ActivityRetainedComponentBuilder;
import dagger.hilt.android.internal.builders.FragmentComponentBuilder;
import dagger.hilt.android.internal.builders.ServiceComponentBuilder;
import dagger.hilt.android.internal.builders.ViewComponentBuilder;
import dagger.hilt.android.internal.builders.ViewModelComponentBuilder;
import dagger.hilt.android.internal.builders.ViewWithFragmentComponentBuilder;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories;
import dagger.hilt.android.internal.lifecycle.HiltViewModelFactory;
import dagger.hilt.android.internal.lifecycle.HiltWrapper_DefaultViewModelFactories_ActivityModule;
import dagger.hilt.android.internal.lifecycle.HiltWrapper_HiltViewModelFactory_ActivityCreatorEntryPoint;
import dagger.hilt.android.internal.lifecycle.HiltWrapper_HiltViewModelFactory_ViewModelModule;
import dagger.hilt.android.internal.managers.ActivityComponentManager;
import dagger.hilt.android.internal.managers.FragmentComponentManager;
import dagger.hilt.android.internal.managers.HiltWrapper_ActivityRetainedComponentManager_ActivityRetainedComponentBuilderEntryPoint;
import dagger.hilt.android.internal.managers.HiltWrapper_ActivityRetainedComponentManager_ActivityRetainedLifecycleEntryPoint;
import dagger.hilt.android.internal.managers.HiltWrapper_ActivityRetainedComponentManager_LifecycleModule;
import dagger.hilt.android.internal.managers.HiltWrapper_SavedStateHandleModule;
import dagger.hilt.android.internal.managers.ServiceComponentManager;
import dagger.hilt.android.internal.managers.ViewComponentManager;
import dagger.hilt.android.internal.modules.ApplicationContextModule;
import dagger.hilt.android.internal.modules.HiltWrapper_ActivityModule;
import dagger.hilt.android.scopes.ActivityRetainedScoped;
import dagger.hilt.android.scopes.ActivityScoped;
import dagger.hilt.android.scopes.FragmentScoped;
import dagger.hilt.android.scopes.ServiceScoped;
import dagger.hilt.android.scopes.ViewModelScoped;
import dagger.hilt.android.scopes.ViewScoped;
import dagger.hilt.components.SingletonComponent;
import dagger.hilt.internal.GeneratedComponent;
import dagger.hilt.migration.DisableInstallInCheck;
import javax.annotation.processing.Generated;
import javax.inject.Singleton;

@Generated("dagger.hilt.processor.internal.root.RootProcessor")
public final class GestionTurnosApp_HiltComponents {
  private GestionTurnosApp_HiltComponents() {
  }

  @Module(
      subcomponents = ServiceC.class
  )
  @DisableInstallInCheck
  @Generated("dagger.hilt.processor.internal.root.RootProcessor")
  abstract interface ServiceCBuilderModule {
    @Binds
    ServiceComponentBuilder bind(ServiceC.Builder builder);
  }

  @Module(
      subcomponents = ActivityRetainedC.class
  )
  @DisableInstallInCheck
  @Generated("dagger.hilt.processor.internal.root.RootProcessor")
  abstract interface ActivityRetainedCBuilderModule {
    @Binds
    ActivityRetainedComponentBuilder bind(ActivityRetainedC.Builder builder);
  }

  @Module(
      subcomponents = ActivityC.class
  )
  @DisableInstallInCheck
  @Generated("dagger.hilt.processor.internal.root.RootProcessor")
  abstract interface ActivityCBuilderModule {
    @Binds
    ActivityComponentBuilder bind(ActivityC.Builder builder);
  }

  @Module(
      subcomponents = ViewModelC.class
  )
  @DisableInstallInCheck
  @Generated("dagger.hilt.processor.internal.root.RootProcessor")
  abstract interface ViewModelCBuilderModule {
    @Binds
    ViewModelComponentBuilder bind(ViewModelC.Builder builder);
  }

  @Module(
      subcomponents = ViewC.class
  )
  @DisableInstallInCheck
  @Generated("dagger.hilt.processor.internal.root.RootProcessor")
  abstract interface ViewCBuilderModule {
    @Binds
    ViewComponentBuilder bind(ViewC.Builder builder);
  }

  @Module(
      subcomponents = FragmentC.class
  )
  @DisableInstallInCheck
  @Generated("dagger.hilt.processor.internal.root.RootProcessor")
  abstract interface FragmentCBuilderModule {
    @Binds
    FragmentComponentBuilder bind(FragmentC.Builder builder);
  }

  @Module(
      subcomponents = ViewWithFragmentC.class
  )
  @DisableInstallInCheck
  @Generated("dagger.hilt.processor.internal.root.RootProcessor")
  abstract interface ViewWithFragmentCBuilderModule {
    @Binds
    ViewWithFragmentComponentBuilder bind(ViewWithFragmentC.Builder builder);
  }

  @Component(
      modules = {
          AppModule.class,
          ApplicationContextModule.class,
          DatabaseModule.class,
          ActivityRetainedCBuilderModule.class,
          ServiceCBuilderModule.class,
          HiltWrapper_FragmentGetContextFix_FragmentGetContextFixModule.class,
          HiltWrapper_WorkerFactoryModule.class,
          NetworkModule.class,
          SyncWorker_HiltModule.class
      }
  )
  @Singleton
  public abstract static class SingletonC implements GestionTurnosApp_GeneratedInjector,
      ReminderReceiver_GeneratedInjector,
      FragmentGetContextFix.FragmentGetContextFixEntryPoint,
      HiltWrapper_ActivityRetainedComponentManager_ActivityRetainedComponentBuilderEntryPoint,
      ServiceComponentManager.ServiceComponentBuilderEntryPoint,
      SingletonComponent,
      GeneratedComponent {
  }

  @Subcomponent
  @ServiceScoped
  public abstract static class ServiceC implements GestionTurnosFCMService_GeneratedInjector,
      ServiceComponent,
      GeneratedComponent {
    @Subcomponent.Builder
    abstract interface Builder extends ServiceComponentBuilder {
    }
  }

  @Subcomponent(
      modules = {
          AuthViewModel_HiltModules.KeyModule.class,
          ChatViewModel_HiltModules.KeyModule.class,
          EspecialidadesViewModel_HiltModules.KeyModule.class,
          EstudiosViewModel_HiltModules.KeyModule.class,
          ActivityCBuilderModule.class,
          ViewModelCBuilderModule.class,
          HealthStatsViewModel_HiltModules.KeyModule.class,
          HiltWrapper_ActivityRetainedComponentManager_LifecycleModule.class,
          HiltWrapper_SavedStateHandleModule.class,
          HomeViewModel_HiltModules.KeyModule.class,
          MedicamentosViewModel_HiltModules.KeyModule.class,
          ProfileViewModel_HiltModules.KeyModule.class,
          SettingsViewModel_HiltModules.KeyModule.class,
          TurnosListViewModel_HiltModules.KeyModule.class
      }
  )
  @ActivityRetainedScoped
  public abstract static class ActivityRetainedC implements ActivityRetainedComponent,
      ActivityComponentManager.ActivityComponentBuilderEntryPoint,
      HiltWrapper_ActivityRetainedComponentManager_ActivityRetainedLifecycleEntryPoint,
      GeneratedComponent {
    @Subcomponent.Builder
    abstract interface Builder extends ActivityRetainedComponentBuilder {
    }
  }

  @Subcomponent(
      modules = {
          FragmentCBuilderModule.class,
          ViewCBuilderModule.class,
          HiltWrapper_ActivityModule.class,
          HiltWrapper_DefaultViewModelFactories_ActivityModule.class
      }
  )
  @ActivityScoped
  public abstract static class ActivityC implements MainActivity_GeneratedInjector,
      ActivityComponent,
      DefaultViewModelFactories.ActivityEntryPoint,
      HiltWrapper_HiltViewModelFactory_ActivityCreatorEntryPoint,
      FragmentComponentManager.FragmentComponentBuilderEntryPoint,
      ViewComponentManager.ViewComponentBuilderEntryPoint,
      GeneratedComponent {
    @Subcomponent.Builder
    abstract interface Builder extends ActivityComponentBuilder {
    }
  }

  @Subcomponent(
      modules = {
          AuthViewModel_HiltModules.BindsModule.class,
          ChatViewModel_HiltModules.BindsModule.class,
          EspecialidadesViewModel_HiltModules.BindsModule.class,
          EstudiosViewModel_HiltModules.BindsModule.class,
          HealthStatsViewModel_HiltModules.BindsModule.class,
          HiltWrapper_HiltViewModelFactory_ViewModelModule.class,
          HomeViewModel_HiltModules.BindsModule.class,
          MedicamentosViewModel_HiltModules.BindsModule.class,
          ProfileViewModel_HiltModules.BindsModule.class,
          SettingsViewModel_HiltModules.BindsModule.class,
          TurnosListViewModel_HiltModules.BindsModule.class
      }
  )
  @ViewModelScoped
  public abstract static class ViewModelC implements ViewModelComponent,
      HiltViewModelFactory.ViewModelFactoriesEntryPoint,
      GeneratedComponent {
    @Subcomponent.Builder
    abstract interface Builder extends ViewModelComponentBuilder {
    }
  }

  @Subcomponent
  @ViewScoped
  public abstract static class ViewC implements ViewComponent,
      GeneratedComponent {
    @Subcomponent.Builder
    abstract interface Builder extends ViewComponentBuilder {
    }
  }

  @Subcomponent(
      modules = ViewWithFragmentCBuilderModule.class
  )
  @FragmentScoped
  public abstract static class FragmentC implements WelcomeFragment_GeneratedInjector,
      LoginFragment_GeneratedInjector,
      RegisterFragment_GeneratedInjector,
      VerifyAccountFragment_GeneratedInjector,
      ChatFragment_GeneratedInjector,
      EspecialidadesFragment_GeneratedInjector,
      EstudiosFragment_GeneratedInjector,
      HomeFragment_GeneratedInjector,
      MedicamentosFragment_GeneratedInjector,
      AchievementsFragment_GeneratedInjector,
      HealthStatsFragment_GeneratedInjector,
      UserProfileFragment_GeneratedInjector,
      SettingsFragment_GeneratedInjector,
      SolicitarTurnoFragment_GeneratedInjector,
      TurnoDetailFragment_GeneratedInjector,
      TurnosListFragment_GeneratedInjector,
      FragmentComponent,
      DefaultViewModelFactories.FragmentEntryPoint,
      ViewComponentManager.ViewWithFragmentComponentBuilderEntryPoint,
      GeneratedComponent {
    @Subcomponent.Builder
    abstract interface Builder extends FragmentComponentBuilder {
    }
  }

  @Subcomponent
  @ViewScoped
  public abstract static class ViewWithFragmentC implements ViewWithFragmentComponent,
      GeneratedComponent {
    @Subcomponent.Builder
    abstract interface Builder extends ViewWithFragmentComponentBuilder {
    }
  }
}
