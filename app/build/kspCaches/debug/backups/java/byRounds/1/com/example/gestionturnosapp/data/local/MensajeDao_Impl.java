package com.example.gestionturnosapp.data.local;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.gestionturnosapp.data.model.Mensaje;
import java.lang.Class;
import java.lang.Exception;
import java.lang.IllegalStateException;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class MensajeDao_Impl implements MensajeDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Mensaje> __insertionAdapterOfMensaje;

  private final Converters __converters = new Converters();

  private final SharedSQLiteStatement __preparedStmtOfDeleteAllMensajes;

  public MensajeDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfMensaje = new EntityInsertionAdapter<Mensaje>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `mensajes` (`id`,`remitente`,`texto`,`fecha`,`leido`) VALUES (?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Mensaje entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getRemitente());
        statement.bindString(3, entity.getTexto());
        final Long _tmp = __converters.dateToTimestamp(entity.getFecha());
        if (_tmp == null) {
          statement.bindNull(4);
        } else {
          statement.bindLong(4, _tmp);
        }
        final int _tmp_1 = entity.getLeido() ? 1 : 0;
        statement.bindLong(5, _tmp_1);
      }
    };
    this.__preparedStmtOfDeleteAllMensajes = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM mensajes";
        return _query;
      }
    };
  }

  @Override
  public Object insertMensajes(final List<Mensaje> mensajes,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfMensaje.insert(mensajes);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAllMensajes(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAllMensajes.acquire();
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
          __preparedStmtOfDeleteAllMensajes.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object getAllMensajes(final Continuation<? super List<Mensaje>> $completion) {
    final String _sql = "SELECT * FROM mensajes ORDER BY fecha ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Mensaje>>() {
      @Override
      @NonNull
      public List<Mensaje> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfRemitente = CursorUtil.getColumnIndexOrThrow(_cursor, "remitente");
          final int _cursorIndexOfTexto = CursorUtil.getColumnIndexOrThrow(_cursor, "texto");
          final int _cursorIndexOfFecha = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha");
          final int _cursorIndexOfLeido = CursorUtil.getColumnIndexOrThrow(_cursor, "leido");
          final List<Mensaje> _result = new ArrayList<Mensaje>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Mensaje _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpRemitente;
            _tmpRemitente = _cursor.getString(_cursorIndexOfRemitente);
            final String _tmpTexto;
            _tmpTexto = _cursor.getString(_cursorIndexOfTexto);
            final Date _tmpFecha;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfFecha)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfFecha);
            }
            final Date _tmp_1 = __converters.fromTimestamp(_tmp);
            if (_tmp_1 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.util.Date', but it was NULL.");
            } else {
              _tmpFecha = _tmp_1;
            }
            final boolean _tmpLeido;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfLeido);
            _tmpLeido = _tmp_2 != 0;
            _item = new Mensaje(_tmpId,_tmpRemitente,_tmpTexto,_tmpFecha,_tmpLeido);
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
