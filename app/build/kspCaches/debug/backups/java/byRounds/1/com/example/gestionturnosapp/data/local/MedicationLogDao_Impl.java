package com.example.gestionturnosapp.data.local;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.gestionturnosapp.data.model.MedicationLog;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class MedicationLogDao_Impl implements MedicationLogDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<MedicationLog> __insertionAdapterOfMedicationLog;

  private final SharedSQLiteStatement __preparedStmtOfDeleteLogsByMed;

  public MedicationLogDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfMedicationLog = new EntityInsertionAdapter<MedicationLog>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `medication_logs` (`id`,`medId`,`medName`,`dose`,`takenAt`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MedicationLog entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getMedId());
        statement.bindString(3, entity.getMedName());
        statement.bindString(4, entity.getDose());
        statement.bindLong(5, entity.getTakenAt());
      }
    };
    this.__preparedStmtOfDeleteLogsByMed = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM medication_logs WHERE medId = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertLog(final MedicationLog log, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfMedicationLog.insert(log);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteLogsByMed(final String medId, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteLogsByMed.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, medId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteLogsByMed.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<MedicationLog>> getAllLogs() {
    final String _sql = "SELECT * FROM medication_logs ORDER BY takenAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"medication_logs"}, new Callable<List<MedicationLog>>() {
      @Override
      @NonNull
      public List<MedicationLog> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfMedId = CursorUtil.getColumnIndexOrThrow(_cursor, "medId");
          final int _cursorIndexOfMedName = CursorUtil.getColumnIndexOrThrow(_cursor, "medName");
          final int _cursorIndexOfDose = CursorUtil.getColumnIndexOrThrow(_cursor, "dose");
          final int _cursorIndexOfTakenAt = CursorUtil.getColumnIndexOrThrow(_cursor, "takenAt");
          final List<MedicationLog> _result = new ArrayList<MedicationLog>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MedicationLog _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpMedId;
            _tmpMedId = _cursor.getString(_cursorIndexOfMedId);
            final String _tmpMedName;
            _tmpMedName = _cursor.getString(_cursorIndexOfMedName);
            final String _tmpDose;
            _tmpDose = _cursor.getString(_cursorIndexOfDose);
            final long _tmpTakenAt;
            _tmpTakenAt = _cursor.getLong(_cursorIndexOfTakenAt);
            _item = new MedicationLog(_tmpId,_tmpMedId,_tmpMedName,_tmpDose,_tmpTakenAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<MedicationLog>> getLogsByMed(final String medId) {
    final String _sql = "SELECT * FROM medication_logs WHERE medId = ? ORDER BY takenAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, medId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"medication_logs"}, new Callable<List<MedicationLog>>() {
      @Override
      @NonNull
      public List<MedicationLog> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfMedId = CursorUtil.getColumnIndexOrThrow(_cursor, "medId");
          final int _cursorIndexOfMedName = CursorUtil.getColumnIndexOrThrow(_cursor, "medName");
          final int _cursorIndexOfDose = CursorUtil.getColumnIndexOrThrow(_cursor, "dose");
          final int _cursorIndexOfTakenAt = CursorUtil.getColumnIndexOrThrow(_cursor, "takenAt");
          final List<MedicationLog> _result = new ArrayList<MedicationLog>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MedicationLog _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpMedId;
            _tmpMedId = _cursor.getString(_cursorIndexOfMedId);
            final String _tmpMedName;
            _tmpMedName = _cursor.getString(_cursorIndexOfMedName);
            final String _tmpDose;
            _tmpDose = _cursor.getString(_cursorIndexOfDose);
            final long _tmpTakenAt;
            _tmpTakenAt = _cursor.getLong(_cursorIndexOfTakenAt);
            _item = new MedicationLog(_tmpId,_tmpMedId,_tmpMedName,_tmpDose,_tmpTakenAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
