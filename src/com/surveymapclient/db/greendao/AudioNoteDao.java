package com.surveymapclient.db.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.surveymapclient.db.greendao.AudioNote;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table AUDIO_NOTE.
*/
public class AudioNoteDao extends AbstractDao<AudioNote, Long> {

    public static final String TABLENAME = "AUDIO_NOTE";

    /**
     * Properties of entity AudioNote.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Key = new Property(1, Long.class, "key", false, "KEY");
        public final static Property Lenght = new Property(2, Integer.class, "lenght", false, "LENGHT");
        public final static Property Url = new Property(3, String.class, "url", false, "URL");
        public final static Property Ax = new Property(4, Float.class, "ax", false, "AX");
        public final static Property Ay = new Property(5, Float.class, "ay", false, "AY");
    };


    public AudioNoteDao(DaoConfig config) {
        super(config);
    }
    
    public AudioNoteDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'AUDIO_NOTE' (" + //
                "'_id' INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "'KEY' INTEGER," + // 1: key
                "'LENGHT' INTEGER," + // 2: lenght
                "'URL' TEXT," + // 3: url
                "'AX' REAL," + // 4: ax
                "'AY' REAL);"); // 5: ay
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'AUDIO_NOTE'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, AudioNote entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Long key = entity.getKey();
        if (key != null) {
            stmt.bindLong(2, key);
        }
 
        Integer lenght = entity.getLenght();
        if (lenght != null) {
            stmt.bindLong(3, lenght);
        }
 
        String url = entity.getUrl();
        if (url != null) {
            stmt.bindString(4, url);
        }
 
        Float ax = entity.getAx();
        if (ax != null) {
            stmt.bindDouble(5, ax);
        }
 
        Float ay = entity.getAy();
        if (ay != null) {
            stmt.bindDouble(6, ay);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public AudioNote readEntity(Cursor cursor, int offset) {
        AudioNote entity = new AudioNote( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1), // key
            cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2), // lenght
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // url
            cursor.isNull(offset + 4) ? null : cursor.getFloat(offset + 4), // ax
            cursor.isNull(offset + 5) ? null : cursor.getFloat(offset + 5) // ay
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, AudioNote entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setKey(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
        entity.setLenght(cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2));
        entity.setUrl(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setAx(cursor.isNull(offset + 4) ? null : cursor.getFloat(offset + 4));
        entity.setAy(cursor.isNull(offset + 5) ? null : cursor.getFloat(offset + 5));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(AudioNote entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(AudioNote entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
