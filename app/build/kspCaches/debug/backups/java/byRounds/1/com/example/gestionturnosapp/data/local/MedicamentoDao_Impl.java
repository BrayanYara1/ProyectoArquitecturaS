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
import com.example.gestionturnosapp.data.model.Medicamento;
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
public final class MedicamentoDao_Impl implements MedicamentoDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Medicamento> __insertionAdapterOfMedicamento;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAllMedicamentos;

  private final SharedSQLiteStatement __preparedStmtOfDeleteById;

  public MedicamentoDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfMedicamento = new EntityInsertionAdapter<Medicamento>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `medicamentos` (`id`,`nombre`,`dosis`,`frecuencia`,`proximaToma`,`stockActual`,`stockMinimo`,`notas`) VALUES (?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Medicamento entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getNombre());
        statement.bindString(3, entity.getDosis());
        statement.bindString(4, entity.getFrecuencia());
        statement.bindString(5, entity.getProximaToma());
        statement.bindLong(6, entity.getStockActual());
        statement.bindLong(7, entity.getStockMinimo());
        if (entity.getNotas() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getNotas());
        }
      }
    };
    this.__preparedStmtOfDeleteAllMedicamentos = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM medicamentos";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM medicamentos WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertMedicamentos(final List<Medicamento> meds,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfMedicamento.insert(meds);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object clearAndInsert(final List<Medicamento> meds,
      final Continuation<? super Unit> $completion) {
    return RoomDatabaseKt.withTransaction(__db, (__cont) -> MedicamentoDao.DefaultImpls.clearAndInsert(MedicamentoDao_Impl.this, meds, __cont), $completion);
  }

  @Override
  public Object deleteAllMedicamentos(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAllMedicamentos.acquire();
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
          __preparedStmtOfDeleteAllMedicamentos.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteById(final String medId, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteById.acquire();
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
          __preparedStmtOfDeleteById.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object getAllMedicamentos(final Continuation<? super List<Medicamento>> $completion) {
    final String _sql = "SELECT * FROM medicamentos";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Medicamento>>() {
      @Override
      @NonNull
      public List<Medicamento> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "nombre");
          final int _cursorIndexOfDosis = CursorUtil.getColumnIndexOrThrow(_cursor, "dosis");
          final int _cursorIndexOfFrecuencia = CursorUtil.getColumnIndexOrThrow(_cursor, "frecuencia");
          final int _cursorIndexOfProximaToma = CursorUtil.getColumnIndexOrThrow(_cursor, "proximaToma");
          final int _cursorIndexOfStockActual = CursorUtil.getColumnIndexOrThrow(_cursor, "stockActual");
          final int _cursorIndexOfStockMinimo = CursorUtil.getColumnIndexOrThrow(_cursor, "stockMinimo");
          final int _cursorIndexOfNotas = CursorUtil.getColumnIndexOrThrow(_cursor, "notas");
          final List<Medicamento> _result = new ArrayList<Medicamento>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Medicamento _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpNombre;
            _tmpNombre = _cursor.getString(_cursorIndexOfNombre);
            final String _tmpDosis;
            _tmpDosis = _cursor.getString(_cursorIndexOfDosis);
            final String _tmpFrecuencia;
            _tmpFrecuencia = _cursor.getString(_cursorIndexOfFrecuencia);
            final String _tmpProximaToma;
            _tmpProximaToma = _cursor.getString(_cursorIndexOfProximaToma);
            final int _tmpStockActual;
            _tmpStockActual = _cursor.getInt(_cursorIndexOfStockActual);
            final int _tmpStockMinimo;
            _tmpStockMinimo = _cursor.getInt(_cursorIndexOfStockMinimo);
            final String _tmpNotas;
            if (_cursor.isNull(_cursorIndexOfNotas)) {
              _tmpNotas = null;
            } else {
              _tmpNotas = _cursor.getString(_cursorIndexOfNotas);
            }
            _item = new Medicamento(_tmpId,_tmpNombre,_tmpDosis,_tmpFrecuencia,_tmpProximaToma,_tmpStockActual,_tmpStockMinimo,_tmpNotas);
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
