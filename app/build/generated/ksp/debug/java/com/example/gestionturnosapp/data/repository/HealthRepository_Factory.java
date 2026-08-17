package com.example.gestionturnosapp.data.repository;

import com.example.gestionturnosapp.data.local.HealthRecordDao;
import com.example.gestionturnosapp.data.local.SymptomDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class HealthRepository_Factory implements Factory<HealthRepository> {
  private final Provider<HealthRecordDao> healthRecordDaoProvider;

  private final Provider<SymptomDao> symptomDaoProvider;

  public HealthRepository_Factory(Provider<HealthRecordDao> healthRecordDaoProvider,
      Provider<SymptomDao> symptomDaoProvider) {
    this.healthRecordDaoProvider = healthRecordDaoProvider;
    this.symptomDaoProvider = symptomDaoProvider;
  }

  @Override
  public HealthRepository get() {
    return newInstance(healthRecordDaoProvider.get(), symptomDaoProvider.get());
  }

  public static HealthRepository_Factory create(Provider<HealthRecordDao> healthRecordDaoProvider,
      Provider<SymptomDao> symptomDaoProvider) {
    return new HealthRepository_Factory(healthRecordDaoProvider, symptomDaoProvider);
  }

  public static HealthRepository newInstance(HealthRecordDao healthRecordDao,
      SymptomDao symptomDao) {
    return new HealthRepository(healthRecordDao, symptomDao);
  }
}
