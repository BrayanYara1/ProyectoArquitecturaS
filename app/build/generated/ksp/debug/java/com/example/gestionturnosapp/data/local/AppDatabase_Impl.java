package com.example.gestionturnosapp.data.local;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile TurnoDao _turnoDao;

  private volatile MedicamentoDao _medicamentoDao;

  private volatile EstudioDao _estudioDao;

  private volatile MensajeDao _mensajeDao;

  private volatile HealthRecordDao _healthRecordDao;

  private volatile AchievementDao _achievementDao;

  private volatile MedicationLogDao _medicationLogDao;

  private volatile SymptomDao _symptomDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(8) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `turnos` (`id` TEXT NOT NULL, `pacienteNombre` TEXT NOT NULL, `fecha` TEXT NOT NULL, `hora` TEXT NOT NULL, `motivo` TEXT NOT NULL, `estado` TEXT NOT NULL, `especialidad` TEXT, `doctor` TEXT, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `medicamentos` (`id` TEXT NOT NULL, `nombre` TEXT NOT NULL, `dosis` TEXT NOT NULL, `frecuencia` TEXT NOT NULL, `proximaToma` TEXT NOT NULL, `stockActual` INTEGER NOT NULL, `stockMinimo` INTEGER NOT NULL, `notas` TEXT, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `estudios` (`id` TEXT NOT NULL, `titulo` TEXT NOT NULL, `fecha` TEXT NOT NULL, `tipo` TEXT NOT NULL, `resultadoBreve` TEXT NOT NULL, `urlDocumento` TEXT, `notas` TEXT, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `mensajes` (`id` TEXT NOT NULL, `remitente` TEXT NOT NULL, `texto` TEXT NOT NULL, `fecha` INTEGER NOT NULL, `leido` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `health_records` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `type` TEXT NOT NULL, `value` REAL NOT NULL, `valueSecondary` REAL, `date` INTEGER NOT NULL, `note` TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `achievements` (`id` TEXT NOT NULL, `title` TEXT NOT NULL, `description` TEXT NOT NULL, `iconResId` INTEGER NOT NULL, `isUnlocked` INTEGER NOT NULL, `progress` INTEGER NOT NULL, `target` INTEGER NOT NULL, `unlockedDate` INTEGER, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `medication_logs` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `medId` TEXT NOT NULL, `medName` TEXT NOT NULL, `dose` TEXT NOT NULL, `takenAt` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `symptom_records` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `description` TEXT NOT NULL, `intensity` INTEGER NOT NULL, `date` INTEGER NOT NULL, `notes` TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '829a63b0a09ad22e1377368bb42cd93e')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `turnos`");
        db.execSQL("DROP TABLE IF EXISTS `medicamentos`");
        db.execSQL("DROP TABLE IF EXISTS `estudios`");
        db.execSQL("DROP TABLE IF EXISTS `mensajes`");
        db.execSQL("DROP TABLE IF EXISTS `health_records`");
        db.execSQL("DROP TABLE IF EXISTS `achievements`");
        db.execSQL("DROP TABLE IF EXISTS `medication_logs`");
        db.execSQL("DROP TABLE IF EXISTS `symptom_records`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsTurnos = new HashMap<String, TableInfo.Column>(8);
        _columnsTurnos.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTurnos.put("pacienteNombre", new TableInfo.Column("pacienteNombre", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTurnos.put("fecha", new TableInfo.Column("fecha", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTurnos.put("hora", new TableInfo.Column("hora", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTurnos.put("motivo", new TableInfo.Column("motivo", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTurnos.put("estado", new TableInfo.Column("estado", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTurnos.put("especialidad", new TableInfo.Column("especialidad", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTurnos.put("doctor", new TableInfo.Column("doctor", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTurnos = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesTurnos = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoTurnos = new TableInfo("turnos", _columnsTurnos, _foreignKeysTurnos, _indicesTurnos);
        final TableInfo _existingTurnos = TableInfo.read(db, "turnos");
        if (!_infoTurnos.equals(_existingTurnos)) {
          return new RoomOpenHelper.ValidationResult(false, "turnos(com.example.gestionturnosapp.data.model.Turno).\n"
                  + " Expected:\n" + _infoTurnos + "\n"
                  + " Found:\n" + _existingTurnos);
        }
        final HashMap<String, TableInfo.Column> _columnsMedicamentos = new HashMap<String, TableInfo.Column>(8);
        _columnsMedicamentos.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedicamentos.put("nombre", new TableInfo.Column("nombre", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedicamentos.put("dosis", new TableInfo.Column("dosis", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedicamentos.put("frecuencia", new TableInfo.Column("frecuencia", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedicamentos.put("proximaToma", new TableInfo.Column("proximaToma", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedicamentos.put("stockActual", new TableInfo.Column("stockActual", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedicamentos.put("stockMinimo", new TableInfo.Column("stockMinimo", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedicamentos.put("notas", new TableInfo.Column("notas", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMedicamentos = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesMedicamentos = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoMedicamentos = new TableInfo("medicamentos", _columnsMedicamentos, _foreignKeysMedicamentos, _indicesMedicamentos);
        final TableInfo _existingMedicamentos = TableInfo.read(db, "medicamentos");
        if (!_infoMedicamentos.equals(_existingMedicamentos)) {
          return new RoomOpenHelper.ValidationResult(false, "medicamentos(com.example.gestionturnosapp.data.model.Medicamento).\n"
                  + " Expected:\n" + _infoMedicamentos + "\n"
                  + " Found:\n" + _existingMedicamentos);
        }
        final HashMap<String, TableInfo.Column> _columnsEstudios = new HashMap<String, TableInfo.Column>(7);
        _columnsEstudios.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEstudios.put("titulo", new TableInfo.Column("titulo", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEstudios.put("fecha", new TableInfo.Column("fecha", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEstudios.put("tipo", new TableInfo.Column("tipo", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEstudios.put("resultadoBreve", new TableInfo.Column("resultadoBreve", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEstudios.put("urlDocumento", new TableInfo.Column("urlDocumento", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEstudios.put("notas", new TableInfo.Column("notas", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysEstudios = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesEstudios = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoEstudios = new TableInfo("estudios", _columnsEstudios, _foreignKeysEstudios, _indicesEstudios);
        final TableInfo _existingEstudios = TableInfo.read(db, "estudios");
        if (!_infoEstudios.equals(_existingEstudios)) {
          return new RoomOpenHelper.ValidationResult(false, "estudios(com.example.gestionturnosapp.data.model.EstudioMedico).\n"
                  + " Expected:\n" + _infoEstudios + "\n"
                  + " Found:\n" + _existingEstudios);
        }
        final HashMap<String, TableInfo.Column> _columnsMensajes = new HashMap<String, TableInfo.Column>(5);
        _columnsMensajes.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMensajes.put("remitente", new TableInfo.Column("remitente", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMensajes.put("texto", new TableInfo.Column("texto", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMensajes.put("fecha", new TableInfo.Column("fecha", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMensajes.put("leido", new TableInfo.Column("leido", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMensajes = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesMensajes = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoMensajes = new TableInfo("mensajes", _columnsMensajes, _foreignKeysMensajes, _indicesMensajes);
        final TableInfo _existingMensajes = TableInfo.read(db, "mensajes");
        if (!_infoMensajes.equals(_existingMensajes)) {
          return new RoomOpenHelper.ValidationResult(false, "mensajes(com.example.gestionturnosapp.data.model.Mensaje).\n"
                  + " Expected:\n" + _infoMensajes + "\n"
                  + " Found:\n" + _existingMensajes);
        }
        final HashMap<String, TableInfo.Column> _columnsHealthRecords = new HashMap<String, TableInfo.Column>(6);
        _columnsHealthRecords.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHealthRecords.put("type", new TableInfo.Column("type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHealthRecords.put("value", new TableInfo.Column("value", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHealthRecords.put("valueSecondary", new TableInfo.Column("valueSecondary", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHealthRecords.put("date", new TableInfo.Column("date", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHealthRecords.put("note", new TableInfo.Column("note", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysHealthRecords = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesHealthRecords = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoHealthRecords = new TableInfo("health_records", _columnsHealthRecords, _foreignKeysHealthRecords, _indicesHealthRecords);
        final TableInfo _existingHealthRecords = TableInfo.read(db, "health_records");
        if (!_infoHealthRecords.equals(_existingHealthRecords)) {
          return new RoomOpenHelper.ValidationResult(false, "health_records(com.example.gestionturnosapp.data.model.HealthRecord).\n"
                  + " Expected:\n" + _infoHealthRecords + "\n"
                  + " Found:\n" + _existingHealthRecords);
        }
        final HashMap<String, TableInfo.Column> _columnsAchievements = new HashMap<String, TableInfo.Column>(8);
        _columnsAchievements.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAchievements.put("title", new TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAchievements.put("description", new TableInfo.Column("description", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAchievements.put("iconResId", new TableInfo.Column("iconResId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAchievements.put("isUnlocked", new TableInfo.Column("isUnlocked", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAchievements.put("progress", new TableInfo.Column("progress", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAchievements.put("target", new TableInfo.Column("target", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAchievements.put("unlockedDate", new TableInfo.Column("unlockedDate", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysAchievements = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesAchievements = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoAchievements = new TableInfo("achievements", _columnsAchievements, _foreignKeysAchievements, _indicesAchievements);
        final TableInfo _existingAchievements = TableInfo.read(db, "achievements");
        if (!_infoAchievements.equals(_existingAchievements)) {
          return new RoomOpenHelper.ValidationResult(false, "achievements(com.example.gestionturnosapp.data.model.Achievement).\n"
                  + " Expected:\n" + _infoAchievements + "\n"
                  + " Found:\n" + _existingAchievements);
        }
        final HashMap<String, TableInfo.Column> _columnsMedicationLogs = new HashMap<String, TableInfo.Column>(5);
        _columnsMedicationLogs.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedicationLogs.put("medId", new TableInfo.Column("medId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedicationLogs.put("medName", new TableInfo.Column("medName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedicationLogs.put("dose", new TableInfo.Column("dose", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedicationLogs.put("takenAt", new TableInfo.Column("takenAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMedicationLogs = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesMedicationLogs = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoMedicationLogs = new TableInfo("medication_logs", _columnsMedicationLogs, _foreignKeysMedicationLogs, _indicesMedicationLogs);
        final TableInfo _existingMedicationLogs = TableInfo.read(db, "medication_logs");
        if (!_infoMedicationLogs.equals(_existingMedicationLogs)) {
          return new RoomOpenHelper.ValidationResult(false, "medication_logs(com.example.gestionturnosapp.data.model.MedicationLog).\n"
                  + " Expected:\n" + _infoMedicationLogs + "\n"
                  + " Found:\n" + _existingMedicationLogs);
        }
        final HashMap<String, TableInfo.Column> _columnsSymptomRecords = new HashMap<String, TableInfo.Column>(5);
        _columnsSymptomRecords.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSymptomRecords.put("description", new TableInfo.Column("description", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSymptomRecords.put("intensity", new TableInfo.Column("intensity", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSymptomRecords.put("date", new TableInfo.Column("date", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSymptomRecords.put("notes", new TableInfo.Column("notes", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSymptomRecords = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesSymptomRecords = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoSymptomRecords = new TableInfo("symptom_records", _columnsSymptomRecords, _foreignKeysSymptomRecords, _indicesSymptomRecords);
        final TableInfo _existingSymptomRecords = TableInfo.read(db, "symptom_records");
        if (!_infoSymptomRecords.equals(_existingSymptomRecords)) {
          return new RoomOpenHelper.ValidationResult(false, "symptom_records(com.example.gestionturnosapp.data.model.SymptomRecord).\n"
                  + " Expected:\n" + _infoSymptomRecords + "\n"
                  + " Found:\n" + _existingSymptomRecords);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "829a63b0a09ad22e1377368bb42cd93e", "e15f67195c2a04b0853bdf287eced594");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "turnos","medicamentos","estudios","mensajes","health_records","achievements","medication_logs","symptom_records");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `turnos`");
      _db.execSQL("DELETE FROM `medicamentos`");
      _db.execSQL("DELETE FROM `estudios`");
      _db.execSQL("DELETE FROM `mensajes`");
      _db.execSQL("DELETE FROM `health_records`");
      _db.execSQL("DELETE FROM `achievements`");
      _db.execSQL("DELETE FROM `medication_logs`");
      _db.execSQL("DELETE FROM `symptom_records`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(TurnoDao.class, TurnoDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(MedicamentoDao.class, MedicamentoDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(EstudioDao.class, EstudioDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(MensajeDao.class, MensajeDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(HealthRecordDao.class, HealthRecordDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(AchievementDao.class, AchievementDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(MedicationLogDao.class, MedicationLogDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(SymptomDao.class, SymptomDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public TurnoDao turnoDao() {
    if (_turnoDao != null) {
      return _turnoDao;
    } else {
      synchronized(this) {
        if(_turnoDao == null) {
          _turnoDao = new TurnoDao_Impl(this);
        }
        return _turnoDao;
      }
    }
  }

  @Override
  public MedicamentoDao medicamentoDao() {
    if (_medicamentoDao != null) {
      return _medicamentoDao;
    } else {
      synchronized(this) {
        if(_medicamentoDao == null) {
          _medicamentoDao = new MedicamentoDao_Impl(this);
        }
        return _medicamentoDao;
      }
    }
  }

  @Override
  public EstudioDao estudioDao() {
    if (_estudioDao != null) {
      return _estudioDao;
    } else {
      synchronized(this) {
        if(_estudioDao == null) {
          _estudioDao = new EstudioDao_Impl(this);
        }
        return _estudioDao;
      }
    }
  }

  @Override
  public MensajeDao mensajeDao() {
    if (_mensajeDao != null) {
      return _mensajeDao;
    } else {
      synchronized(this) {
        if(_mensajeDao == null) {
          _mensajeDao = new MensajeDao_Impl(this);
        }
        return _mensajeDao;
      }
    }
  }

  @Override
  public HealthRecordDao healthRecordDao() {
    if (_healthRecordDao != null) {
      return _healthRecordDao;
    } else {
      synchronized(this) {
        if(_healthRecordDao == null) {
          _healthRecordDao = new HealthRecordDao_Impl(this);
        }
        return _healthRecordDao;
      }
    }
  }

  @Override
  public AchievementDao achievementDao() {
    if (_achievementDao != null) {
      return _achievementDao;
    } else {
      synchronized(this) {
        if(_achievementDao == null) {
          _achievementDao = new AchievementDao_Impl(this);
        }
        return _achievementDao;
      }
    }
  }

  @Override
  public MedicationLogDao medicationLogDao() {
    if (_medicationLogDao != null) {
      return _medicationLogDao;
    } else {
      synchronized(this) {
        if(_medicationLogDao == null) {
          _medicationLogDao = new MedicationLogDao_Impl(this);
        }
        return _medicationLogDao;
      }
    }
  }

  @Override
  public SymptomDao symptomDao() {
    if (_symptomDao != null) {
      return _symptomDao;
    } else {
      synchronized(this) {
        if(_symptomDao == null) {
          _symptomDao = new SymptomDao_Impl(this);
        }
        return _symptomDao;
      }
    }
  }
}
