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
import com.example.gestionturnosapp.data.model.EstudioMedico;
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
public final class EstudioDao_Impl implements EstudioDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<EstudioMedico> __insertionAdapterOfEstudioMedico;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAllEstudios;

  private final SharedSQLiteStatement __preparedStmtOfDeleteById;

  public EstudioDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfEstudioMedico = new EntityInsertionAdapter<EstudioMedico>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `estudios` (`id`,`titulo`,`fecha`,`tipo`,`resultadoBreve`,`urlDocumento`,`notas`) VALUES (?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final EstudioMedico entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getTitulo());
        statement.bindString(3, entity.getFecha());
        statement.bindString(4, entity.getTipo());
        statement.bindString(5, entity.getResultadoBreve());
        if (entity.getUrlDocumento() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getUrlDocumento());
        }
        if (entity.getNotas() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getNotas());
        }
      }
    };
    this.__preparedStmtOfDeleteAllEstudios = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM estudios";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM estudios WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertEstudios(final List<EstudioMedico> estudios,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfEstudioMedico.insert(estudios);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object clearAndInsert(final List<EstudioMedico> estudios,
      final Continuation<? super Unit> $completion) {
    return RoomDatabaseKt.withTransaction(__db, (__cont) -> EstudioDao.DefaultImpls.clearAndInsert(EstudioDao_Impl.this, estudios, __cont), $completion);
  }

  @Override
  public Object deleteAllEstudios(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAllEstudios.acquire();
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
          __preparedStmtOfDeleteAllEstudios.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteById(final String estudioId, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteById.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, estudioId);
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
  public Object getAllEstudios(final Continuation<? super List<EstudioMedico>> $completion) {
    final String _sql = "SELECT * FROM estudios ORDER BY fecha DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<EstudioMedico>>() {
      @Override
      @NonNull
      public List<EstudioMedico> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitulo = CursorUtil.getColumnIndexOrThrow(_cursor, "titulo");
          final int _cursorIndexOfFecha = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha");
          final int _cursorIndexOfTipo = CursorUtil.getColumnIndexOrThrow(_cursor, "tipo");
          final int _cursorIndexOfResultadoBreve = CursorUtil.getColumnIndexOrThrow(_cursor, "resultadoBreve");
          final int _cursorIndexOfUrlDocumento = CursorUtil.getColumnIndexOrThrow(_cursor, "urlDocumento");
          final int _cursorIndexOfNotas = CursorUtil.getColumnIndexOrThrow(_cursor, "notas");
          final List<EstudioMedico> _result = new ArrayList<EstudioMedico>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final EstudioMedico _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpTitulo;
            _tmpTitulo = _cursor.getString(_cursorIndexOfTitulo);
            final String _tmpFecha;
            _tmpFecha = _cursor.getString(_cursorIndexOfFecha);
            final String _tmpTipo;
            _tmpTipo = _cursor.getString(_cursorIndexOfTipo);
            final String _tmpResultadoBreve;
            _tmpResultadoBreve = _cursor.getString(_cursorIndexOfResultadoBreve);
            final String _tmpUrlDocumento;
            if (_cursor.isNull(_cursorIndexOfUrlDocumento)) {
              _tmpUrlDocumento = null;
            } else {
              _tmpUrlDocumento = _cursor.getString(_cursorIndexOfUrlDocumento);
            }
            final String _tmpNotas;
            if (_cursor.isNull(_cursorIndexOfNotas)) {
              _tmpNotas = null;
            } else {
              _tmpNotas = _cursor.getString(_cursorIndexOfNotas);
            }
            _item = new EstudioMedico(_tmpId,_tmpTitulo,_tmpFecha,_tmpTipo,_tmpResultadoBreve,_tmpUrlDocumento,_tmpNotas);
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
