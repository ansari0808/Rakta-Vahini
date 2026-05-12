package com.raktavahini.data.db;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.raktavahini.model.Donor;
import java.lang.Class;
import java.lang.Exception;
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
public final class DonorDao_Impl implements DonorDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Donor> __insertionAdapterOfDonor;

  private final EntityDeletionOrUpdateAdapter<Donor> __updateAdapterOfDonor;

  private final SharedSQLiteStatement __preparedStmtOfUpdateLastDonationDate;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAllDonors;

  public DonorDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfDonor = new EntityInsertionAdapter<Donor>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `donors` (`id`,`name`,`bloodGroup`,`phone`,`location`,`latitude`,`longitude`,`lastDonationDate`,`isAvailable`,`firebaseUid`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Donor entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindString(3, entity.getBloodGroup());
        statement.bindString(4, entity.getPhone());
        statement.bindString(5, entity.getLocation());
        statement.bindDouble(6, entity.getLatitude());
        statement.bindDouble(7, entity.getLongitude());
        statement.bindLong(8, entity.getLastDonationDate());
        final int _tmp = entity.isAvailable() ? 1 : 0;
        statement.bindLong(9, _tmp);
        if (entity.getFirebaseUid() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getFirebaseUid());
        }
      }
    };
    this.__updateAdapterOfDonor = new EntityDeletionOrUpdateAdapter<Donor>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `donors` SET `id` = ?,`name` = ?,`bloodGroup` = ?,`phone` = ?,`location` = ?,`latitude` = ?,`longitude` = ?,`lastDonationDate` = ?,`isAvailable` = ?,`firebaseUid` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Donor entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindString(3, entity.getBloodGroup());
        statement.bindString(4, entity.getPhone());
        statement.bindString(5, entity.getLocation());
        statement.bindDouble(6, entity.getLatitude());
        statement.bindDouble(7, entity.getLongitude());
        statement.bindLong(8, entity.getLastDonationDate());
        final int _tmp = entity.isAvailable() ? 1 : 0;
        statement.bindLong(9, _tmp);
        if (entity.getFirebaseUid() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getFirebaseUid());
        }
        statement.bindLong(11, entity.getId());
      }
    };
    this.__preparedStmtOfUpdateLastDonationDate = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE donors SET lastDonationDate = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteAllDonors = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM donors";
        return _query;
      }
    };
  }

  @Override
  public Object insertDonor(final Donor donor, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfDonor.insertAndReturnId(donor);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateDonor(final Donor donor, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfDonor.handle(donor);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateLastDonationDate(final int donorId, final long date,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateLastDonationDate.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, date);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, donorId);
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
          __preparedStmtOfUpdateLastDonationDate.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAllDonors(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAllDonors.acquire();
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
          __preparedStmtOfDeleteAllDonors.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public LiveData<Donor> getDonorByUidLiveData(final String uid) {
    final String _sql = "SELECT * FROM donors WHERE firebaseUid = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, uid);
    return __db.getInvalidationTracker().createLiveData(new String[] {"donors"}, false, new Callable<Donor>() {
      @Override
      @Nullable
      public Donor call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfBloodGroup = CursorUtil.getColumnIndexOrThrow(_cursor, "bloodGroup");
          final int _cursorIndexOfPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "phone");
          final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
          final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
          final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
          final int _cursorIndexOfLastDonationDate = CursorUtil.getColumnIndexOrThrow(_cursor, "lastDonationDate");
          final int _cursorIndexOfIsAvailable = CursorUtil.getColumnIndexOrThrow(_cursor, "isAvailable");
          final int _cursorIndexOfFirebaseUid = CursorUtil.getColumnIndexOrThrow(_cursor, "firebaseUid");
          final Donor _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpBloodGroup;
            _tmpBloodGroup = _cursor.getString(_cursorIndexOfBloodGroup);
            final String _tmpPhone;
            _tmpPhone = _cursor.getString(_cursorIndexOfPhone);
            final String _tmpLocation;
            _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
            final double _tmpLatitude;
            _tmpLatitude = _cursor.getDouble(_cursorIndexOfLatitude);
            final double _tmpLongitude;
            _tmpLongitude = _cursor.getDouble(_cursorIndexOfLongitude);
            final long _tmpLastDonationDate;
            _tmpLastDonationDate = _cursor.getLong(_cursorIndexOfLastDonationDate);
            final boolean _tmpIsAvailable;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsAvailable);
            _tmpIsAvailable = _tmp != 0;
            final String _tmpFirebaseUid;
            if (_cursor.isNull(_cursorIndexOfFirebaseUid)) {
              _tmpFirebaseUid = null;
            } else {
              _tmpFirebaseUid = _cursor.getString(_cursorIndexOfFirebaseUid);
            }
            _result = new Donor(_tmpId,_tmpName,_tmpBloodGroup,_tmpPhone,_tmpLocation,_tmpLatitude,_tmpLongitude,_tmpLastDonationDate,_tmpIsAvailable,_tmpFirebaseUid);
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
  public Object getDonorByUid(final String uid, final Continuation<? super Donor> $completion) {
    final String _sql = "SELECT * FROM donors WHERE firebaseUid = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, uid);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Donor>() {
      @Override
      @Nullable
      public Donor call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfBloodGroup = CursorUtil.getColumnIndexOrThrow(_cursor, "bloodGroup");
          final int _cursorIndexOfPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "phone");
          final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
          final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
          final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
          final int _cursorIndexOfLastDonationDate = CursorUtil.getColumnIndexOrThrow(_cursor, "lastDonationDate");
          final int _cursorIndexOfIsAvailable = CursorUtil.getColumnIndexOrThrow(_cursor, "isAvailable");
          final int _cursorIndexOfFirebaseUid = CursorUtil.getColumnIndexOrThrow(_cursor, "firebaseUid");
          final Donor _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpBloodGroup;
            _tmpBloodGroup = _cursor.getString(_cursorIndexOfBloodGroup);
            final String _tmpPhone;
            _tmpPhone = _cursor.getString(_cursorIndexOfPhone);
            final String _tmpLocation;
            _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
            final double _tmpLatitude;
            _tmpLatitude = _cursor.getDouble(_cursorIndexOfLatitude);
            final double _tmpLongitude;
            _tmpLongitude = _cursor.getDouble(_cursorIndexOfLongitude);
            final long _tmpLastDonationDate;
            _tmpLastDonationDate = _cursor.getLong(_cursorIndexOfLastDonationDate);
            final boolean _tmpIsAvailable;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsAvailable);
            _tmpIsAvailable = _tmp != 0;
            final String _tmpFirebaseUid;
            if (_cursor.isNull(_cursorIndexOfFirebaseUid)) {
              _tmpFirebaseUid = null;
            } else {
              _tmpFirebaseUid = _cursor.getString(_cursorIndexOfFirebaseUid);
            }
            _result = new Donor(_tmpId,_tmpName,_tmpBloodGroup,_tmpPhone,_tmpLocation,_tmpLatitude,_tmpLongitude,_tmpLastDonationDate,_tmpIsAvailable,_tmpFirebaseUid);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public LiveData<List<Donor>> getAllDonors() {
    final String _sql = "SELECT * FROM donors ORDER BY name ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[] {"donors"}, false, new Callable<List<Donor>>() {
      @Override
      @Nullable
      public List<Donor> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfBloodGroup = CursorUtil.getColumnIndexOrThrow(_cursor, "bloodGroup");
          final int _cursorIndexOfPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "phone");
          final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
          final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
          final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
          final int _cursorIndexOfLastDonationDate = CursorUtil.getColumnIndexOrThrow(_cursor, "lastDonationDate");
          final int _cursorIndexOfIsAvailable = CursorUtil.getColumnIndexOrThrow(_cursor, "isAvailable");
          final int _cursorIndexOfFirebaseUid = CursorUtil.getColumnIndexOrThrow(_cursor, "firebaseUid");
          final List<Donor> _result = new ArrayList<Donor>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Donor _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpBloodGroup;
            _tmpBloodGroup = _cursor.getString(_cursorIndexOfBloodGroup);
            final String _tmpPhone;
            _tmpPhone = _cursor.getString(_cursorIndexOfPhone);
            final String _tmpLocation;
            _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
            final double _tmpLatitude;
            _tmpLatitude = _cursor.getDouble(_cursorIndexOfLatitude);
            final double _tmpLongitude;
            _tmpLongitude = _cursor.getDouble(_cursorIndexOfLongitude);
            final long _tmpLastDonationDate;
            _tmpLastDonationDate = _cursor.getLong(_cursorIndexOfLastDonationDate);
            final boolean _tmpIsAvailable;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsAvailable);
            _tmpIsAvailable = _tmp != 0;
            final String _tmpFirebaseUid;
            if (_cursor.isNull(_cursorIndexOfFirebaseUid)) {
              _tmpFirebaseUid = null;
            } else {
              _tmpFirebaseUid = _cursor.getString(_cursorIndexOfFirebaseUid);
            }
            _item = new Donor(_tmpId,_tmpName,_tmpBloodGroup,_tmpPhone,_tmpLocation,_tmpLatitude,_tmpLongitude,_tmpLastDonationDate,_tmpIsAvailable,_tmpFirebaseUid);
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
  public LiveData<List<Donor>> getEligibleDonors(final String bloodGroup) {
    final String _sql = "SELECT * FROM donors WHERE bloodGroup = ? AND isAvailable = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, bloodGroup);
    return __db.getInvalidationTracker().createLiveData(new String[] {"donors"}, false, new Callable<List<Donor>>() {
      @Override
      @Nullable
      public List<Donor> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfBloodGroup = CursorUtil.getColumnIndexOrThrow(_cursor, "bloodGroup");
          final int _cursorIndexOfPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "phone");
          final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
          final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
          final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
          final int _cursorIndexOfLastDonationDate = CursorUtil.getColumnIndexOrThrow(_cursor, "lastDonationDate");
          final int _cursorIndexOfIsAvailable = CursorUtil.getColumnIndexOrThrow(_cursor, "isAvailable");
          final int _cursorIndexOfFirebaseUid = CursorUtil.getColumnIndexOrThrow(_cursor, "firebaseUid");
          final List<Donor> _result = new ArrayList<Donor>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Donor _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpBloodGroup;
            _tmpBloodGroup = _cursor.getString(_cursorIndexOfBloodGroup);
            final String _tmpPhone;
            _tmpPhone = _cursor.getString(_cursorIndexOfPhone);
            final String _tmpLocation;
            _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
            final double _tmpLatitude;
            _tmpLatitude = _cursor.getDouble(_cursorIndexOfLatitude);
            final double _tmpLongitude;
            _tmpLongitude = _cursor.getDouble(_cursorIndexOfLongitude);
            final long _tmpLastDonationDate;
            _tmpLastDonationDate = _cursor.getLong(_cursorIndexOfLastDonationDate);
            final boolean _tmpIsAvailable;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsAvailable);
            _tmpIsAvailable = _tmp != 0;
            final String _tmpFirebaseUid;
            if (_cursor.isNull(_cursorIndexOfFirebaseUid)) {
              _tmpFirebaseUid = null;
            } else {
              _tmpFirebaseUid = _cursor.getString(_cursorIndexOfFirebaseUid);
            }
            _item = new Donor(_tmpId,_tmpName,_tmpBloodGroup,_tmpPhone,_tmpLocation,_tmpLatitude,_tmpLongitude,_tmpLastDonationDate,_tmpIsAvailable,_tmpFirebaseUid);
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
