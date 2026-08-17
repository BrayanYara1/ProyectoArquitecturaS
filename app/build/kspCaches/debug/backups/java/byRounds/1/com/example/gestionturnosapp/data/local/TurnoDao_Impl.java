package com.example.gestionturnosapp.data.local;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomDatabaseKt;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.gestionturnosapp.data.model.Turno;
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

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class TurnoDao_Impl implements TurnoDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Turno> __insertionAdapterOfTurno;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAllTurnos;

  private final SharedSQLiteStatement __preparedStmtOfDeleteById;

  public TurnoDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfTurno = new EntityInsertionAdapter<Turno>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `turnos` (`id`,`pacienteNombre`,`fecha`,`hora`,`motivo`,`estado`,`especialidad`,`doctor`) VALUES (?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Turno entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getPacienteNombre());
        statement.bindString(3, entity.getFecha());
        statement.bindString(4, entity.getHora());
        statement.bindString(5, entity.getMotivo());
        statement.bindString(6, entity.getEstado());
        if (entity.getEspecialidad() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getEspecialidad());
        }
        if (entity.getDoctor() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getDoctor());
        }
      }
    };
    this.__preparedStmtOfDeleteAllTurnos = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM turnos";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM turnos WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertTurnos(final List<Turno> turnos,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfTurno.insert(turnos);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object clearAndInsert(final List<Turno> turnos,
      final Continuation<? super Unit> $completion) {
    return RoomDatabaseKt.withTransaction(__db, (__cont) -> TurnoDao.DefaultImpls.clearAndInsert(TurnoDao_Impl.this, turnos, __cont), $completion);
  }

  @Override
  public Object deleteAllTurnos(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAllTurnos.acquire();
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
          __preparedStmtOfDeleteAllTurnos.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteById(final String turnoId, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteById.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, turnoId);
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
          __preparedStmtOfDeleteById.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object getAllTurnos(final Continuation<? super List<Turno>> $completion) {
    final String _sql = "SELECT * FROM turnos ORDER BY fecha ASC, hora ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Turno>>() {
      @Override
      @NonNull
      public List<Turno> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPacienteNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "pacienteNombre");
          final int _cursorIndexOfFecha = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha");
          final int _cursorIndexOfHora = CursorUtil.getColumnIndexOrThrow(_cursor, "hora");
          final int _cursorIndexOfMotivo = CursorUtil.getColumnIndexOrThrow(_cursor, "motivo");
          final int _cursorIndexOfEstado = CursorUtil.getColumnIndexOrThrow(_cursor, "estado");
          final int _cursorIndexOfEspecialidad = CursorUtil.getColumnIndexOrThrow(_cursor, "especialidad");
          final int _cursorIndexOfDoctor = CursorUtil.getColumnIndexOrThrow(_cursor, "doctor");
          final List<Turno> _result = new ArrayList<Turno>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Turno _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpPacienteNombre;
            _tmpPacienteNombre = _cursor.getString(_cursorIndexOfPacienteNombre);
            final String _tmpFecha;
            _tmpFecha = _cursor.getString(_cursorIndexOfFecha);
            final String _tmpHora;
            _tmpHora = _cursor.getString(_cursorIndexOfHora);
            final String _tmpMotivo;
            _tmpMotivo = _cursor.getString(_cursorIndexOfMotivo);
            final String _tmpEstado;
            _tmpEstado = _cursor.getString(_cursorIndexOfEstado);
            final String _tmpEspecialidad;
            if (_cursor.isNull(_cursorIndexOfEspecialidad)) {
              _tmpEspecialidad = null;
            } else {
              _tmpEspecialidad = _cursor.getString(_cursorIndexOfEspecialidad);
            }
            final String _tmpDoctor;
            if (_cursor.isNull(_cursorIndexOfDoctor)) {
              _tmpDoctor = null;
            } else {
              _tmpDoctor = _cursor.getString(_cursorIndexOfDoctor);
            }
            _item = new Turno(_tmpId,_tmpPacienteNombre,_tmpFecha,_tmpHora,_tmpMotivo,_tmpEstado,_tmpEspecialidad,_tmpDoctor);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
