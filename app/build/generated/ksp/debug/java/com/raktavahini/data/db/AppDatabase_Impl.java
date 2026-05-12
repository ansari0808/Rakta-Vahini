package com.raktavahini.data.db;

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
  private volatile DonorDao _donorDao;

  private volatile DonationLogDao _donationLogDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `donors` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `bloodGroup` TEXT NOT NULL, `phone` TEXT NOT NULL, `location` TEXT NOT NULL, `latitude` REAL NOT NULL, `longitude` REAL NOT NULL, `lastDonationDate` INTEGER NOT NULL, `isAvailable` INTEGER NOT NULL, `firebaseUid` TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `donation_logs` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `donorId` INTEGER NOT NULL, `donationDate` INTEGER NOT NULL, `center` TEXT NOT NULL, `note` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '5371b319890a127e778df51da4c6d501')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `donors`");
        db.execSQL("DROP TABLE IF EXISTS `donation_logs`");
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
        final HashMap<String, TableInfo.Column> _columnsDonors = new HashMap<String, TableInfo.Column>(10);
        _columnsDonors.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDonors.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDonors.put("bloodGroup", new TableInfo.Column("bloodGroup", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDonors.put("phone", new TableInfo.Column("phone", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDonors.put("location", new TableInfo.Column("location", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDonors.put("latitude", new TableInfo.Column("latitude", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDonors.put("longitude", new TableInfo.Column("longitude", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDonors.put("lastDonationDate", new TableInfo.Column("lastDonationDate", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDonors.put("isAvailable", new TableInfo.Column("isAvailable", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDonors.put("firebaseUid", new TableInfo.Column("firebaseUid", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysDonors = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesDonors = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoDonors = new TableInfo("donors", _columnsDonors, _foreignKeysDonors, _indicesDonors);
        final TableInfo _existingDonors = TableInfo.read(db, "donors");
        if (!_infoDonors.equals(_existingDonors)) {
          return new RoomOpenHelper.ValidationResult(false, "donors(com.raktavahini.model.Donor).\n"
                  + " Expected:\n" + _infoDonors + "\n"
                  + " Found:\n" + _existingDonors);
        }
        final HashMap<String, TableInfo.Column> _columnsDonationLogs = new HashMap<String, TableInfo.Column>(5);
        _columnsDonationLogs.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDonationLogs.put("donorId", new TableInfo.Column("donorId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDonationLogs.put("donationDate", new TableInfo.Column("donationDate", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDonationLogs.put("center", new TableInfo.Column("center", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDonationLogs.put("note", new TableInfo.Column("note", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysDonationLogs = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesDonationLogs = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoDonationLogs = new TableInfo("donation_logs", _columnsDonationLogs, _foreignKeysDonationLogs, _indicesDonationLogs);
        final TableInfo _existingDonationLogs = TableInfo.read(db, "donation_logs");
        if (!_infoDonationLogs.equals(_existingDonationLogs)) {
          return new RoomOpenHelper.ValidationResult(false, "donation_logs(com.raktavahini.model.DonationLog).\n"
                  + " Expected:\n" + _infoDonationLogs + "\n"
                  + " Found:\n" + _existingDonationLogs);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "5371b319890a127e778df51da4c6d501", "58ab26292ddb22cf073b6c7e0e1decbf");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "donors","donation_logs");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `donors`");
      _db.execSQL("DELETE FROM `donation_logs`");
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
    _typeConvertersMap.put(DonorDao.class, DonorDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(DonationLogDao.class, DonationLogDao_Impl.getRequiredConverters());
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
  public DonorDao donorDao() {
    if (_donorDao != null) {
      return _donorDao;
    } else {
      synchronized(this) {
        if(_donorDao == null) {
          _donorDao = new DonorDao_Impl(this);
        }
        return _donorDao;
      }
    }
  }

  @Override
  public DonationLogDao donationLogDao() {
    if (_donationLogDao != null) {
      return _donationLogDao;
    } else {
      synchronized(this) {
        if(_donationLogDao == null) {
          _donationLogDao = new DonationLogDao_Impl(this);
        }
        return _donationLogDao;
      }
    }
  }
}
