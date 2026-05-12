package com.raktavahini.data.db;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.raktavahini.model.DonationLog;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
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
public final class DonationLogDao_Impl implements DonationLogDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<DonationLog> __insertionAdapterOfDonationLog;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAllLogs;

  public DonationLogDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfDonationLog = new EntityInsertionAdapter<DonationLog>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `donation_logs` (`id`,`donorId`,`donationDate`,`center`,`note`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final DonationLog entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getDonorId());
        statement.bindLong(3, entity.getDonationDate());
        statement.bindString(4, entity.getCenter());
        statement.bindString(5, entity.getNote());
      }
    };
    this.__preparedStmtOfDeleteAllLogs = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM donation_logs";
        return _query;
      }
    };
  }

  @Override
  public Object insertLog(final DonationLog log, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfDonationLog.insertAndReturnId(log);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAllLogs(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAllLogs.acquire();
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
          __preparedStmtOfDeleteAllLogs.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public LiveData<List<DonationLog>> getLogsForDonor(final int donorId) {
    final String _sql = "SELECT * FROM donation_logs WHERE donorId = ? ORDER BY donationDate DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, donorId);
    return __db.getInvalidationTracker().createLiveData(new String[] {"donation_logs"}, false, new Callable<List<DonationLog>>() {
      @Override
      @Nullable
      public List<DonationLog> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDonorId = CursorUtil.getColumnIndexOrThrow(_cursor, "donorId");
          final int _cursorIndexOfDonationDate = CursorUtil.getColumnIndexOrThrow(_cursor, "donationDate");
          final int _cursorIndexOfCenter = CursorUtil.getColumnIndexOrThrow(_cursor, "center");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final List<DonationLog> _result = new ArrayList<DonationLog>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final DonationLog _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpDonorId;
            _tmpDonorId = _cursor.getInt(_cursorIndexOfDonorId);
            final long _tmpDonationDate;
            _tmpDonationDate = _cursor.getLong(_cursorIndexOfDonationDate);
            final String _tmpCenter;
            _tmpCenter = _cursor.getString(_cursorIndexOfCenter);
            final String _tmpNote;
            _tmpNote = _cursor.getString(_cursorIndexOfNote);
            _item = new DonationLog(_tmpId,_tmpDonorId,_tmpDonationDate,_tmpCenter,_tmpNote);
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
  public LiveData<DonationLog> getLatestLogForDonor(final int donorId) {
    final String _sql = "SELECT * FROM donation_logs WHERE donorId = ? ORDER BY donationDate DESC LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, donorId);
    return __db.getInvalidationTracker().createLiveData(new String[] {"donation_logs"}, false, new Callable<DonationLog>() {
      @Override
      @Nullable
      public DonationLog call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDonorId = CursorUtil.getColumnIndexOrThrow(_cursor, "donorId");
          final int _cursorIndexOfDonationDate = CursorUtil.getColumnIndexOrThrow(_cursor, "donationDate");
          final int _cursorIndexOfCenter = CursorUtil.getColumnIndexOrThrow(_cursor, "center");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final DonationLog _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpDonorId;
            _tmpDonorId = _cursor.getInt(_cursorIndexOfDonorId);
            final long _tmpDonationDate;
            _tmpDonationDate = _cursor.getLong(_cursorIndexOfDonationDate);
            final String _tmpCenter;
            _tmpCenter = _cursor.getString(_cursorIndexOfCenter);
            final String _tmpNote;
            _tmpNote = _cursor.getString(_cursorIndexOfNote);
            _result = new DonationLog(_tmpId,_tmpDonorId,_tmpDonationDate,_tmpCenter,_tmpNote);
          } else {
            _result = null;
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
  public LiveData<Integer> getDonationCount(final int donorId) {
    final String _sql = "SELECT COUNT(*) FROM donation_logs WHERE donorId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, donorId);
    return __db.getInvalidationTracker().createLiveData(new String[] {"donation_logs"}, false, new Callable<Integer>() {
      @Override
      @Nullable
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final Integer _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(0);
            }
            _result = _tmp;
          } else {
            _result = null;
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
  public Object getCountForDonorSync(final int donorId,
      final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM donation_logs WHERE donorId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, donorId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
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
