package com.surveymapclient.db.greendao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import de.greenrobot.dao.AbstractDaoMaster;
import de.greenrobot.dao.identityscope.IdentityScopeType;

import com.surveymapclient.db.greendao.LineDao;
import com.surveymapclient.db.greendao.PolygonDao;
import com.surveymapclient.db.greendao.LinesDao;
import com.surveymapclient.db.greendao.RectangleDao;
import com.surveymapclient.db.greendao.CoordinateDao;
import com.surveymapclient.db.greendao.AngleDao;
import com.surveymapclient.db.greendao.TextNoteDao;
import com.surveymapclient.db.greendao.AudioNoteDao;
import com.surveymapclient.db.greendao.ModuleDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * Master of DAO (schema version 3): knows all DAOs.
*/
public class DaoMaster extends AbstractDaoMaster {
    public static final int SCHEMA_VERSION = 3;

    /** Creates underlying database table using DAOs. */
    public static void createAllTables(SQLiteDatabase db, boolean ifNotExists) {
        LineDao.createTable(db, ifNotExists);
        PolygonDao.createTable(db, ifNotExists);
        LinesDao.createTable(db, ifNotExists);
        RectangleDao.createTable(db, ifNotExists);
        CoordinateDao.createTable(db, ifNotExists);
        AngleDao.createTable(db, ifNotExists);
        TextNoteDao.createTable(db, ifNotExists);
        AudioNoteDao.createTable(db, ifNotExists);
        ModuleDao.createTable(db, ifNotExists);
    }
    
    /** Drops underlying database table using DAOs. */
    public static void dropAllTables(SQLiteDatabase db, boolean ifExists) {
        LineDao.dropTable(db, ifExists);
        PolygonDao.dropTable(db, ifExists);
        LinesDao.dropTable(db, ifExists);
        RectangleDao.dropTable(db, ifExists);
        CoordinateDao.dropTable(db, ifExists);
        AngleDao.dropTable(db, ifExists);
        TextNoteDao.dropTable(db, ifExists);
        AudioNoteDao.dropTable(db, ifExists);
        ModuleDao.dropTable(db, ifExists);
    }
    
    public static abstract class OpenHelper extends SQLiteOpenHelper {

        public OpenHelper(Context context, String name, CursorFactory factory) {
            super(context, name, factory, SCHEMA_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.i("greenDAO", "Creating tables for schema version " + SCHEMA_VERSION);
            createAllTables(db, false);
        }
    }
    
    /** WARNING: Drops all table on Upgrade! Use only during development. */
    public static class DevOpenHelper extends OpenHelper {
        public DevOpenHelper(Context context, String name, CursorFactory factory) {
            super(context, name, factory);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.i("greenDAO", "Upgrading schema from version " + oldVersion + " to " + newVersion + " by dropping all tables");
            dropAllTables(db, true);
            onCreate(db);
        }
    }

    public DaoMaster(SQLiteDatabase db) {
        super(db, SCHEMA_VERSION);
        registerDaoClass(LineDao.class);
        registerDaoClass(PolygonDao.class);
        registerDaoClass(LinesDao.class);
        registerDaoClass(RectangleDao.class);
        registerDaoClass(CoordinateDao.class);
        registerDaoClass(AngleDao.class);
        registerDaoClass(TextNoteDao.class);
        registerDaoClass(AudioNoteDao.class);
        registerDaoClass(ModuleDao.class);
    }
    
    public DaoSession newSession() {
        return new DaoSession(db, IdentityScopeType.Session, daoConfigMap);
    }
    
    public DaoSession newSession(IdentityScopeType type) {
        return new DaoSession(db, type, daoConfigMap);
    }
    
}
