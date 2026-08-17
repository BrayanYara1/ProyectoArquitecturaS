package com.example.gestionturnosapp.data.repository;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

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
public final class EspecialidadRepository_Factory implements Factory<EspecialidadRepository> {
  @Override
  public EspecialidadRepository get() {
    return newInstance();
  }

  public static EspecialidadRepository_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static EspecialidadRepository newInstance() {
    return new EspecialidadRepository();
  }

  private static final class InstanceHolder {
    private static final EspecialidadRepository_Factory INSTANCE = new EspecialidadRepository_Factory();
  }
}
