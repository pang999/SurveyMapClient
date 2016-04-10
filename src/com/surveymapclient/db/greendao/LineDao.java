package com.surveymapclient.db.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.surveymapclient.db.greendao.Line;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table LINE.
*/
public class LineDao extends AbstractDao<Line, Long> {

    public static final String TABLENAME = "LINE";

    /**
     * Properties of entity Line.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Key = new Property(1, Long.class, "key", false, "KEY");
        public final static Property Name = new Property(2, String.class, "name", false, "NAME");
        public final static Property Startx = new Property(3, Float.class, "startx", false, "STARTX");
        public final static Property Starty = new Property(4, Float.class, "starty", false, "STARTY");
        public final static Property Endx = new Property(5, Float.class, "endx", false, "ENDX");
        public final static Property Endy = new Property(6, Float.class, "endy", false, "ENDY");
        public final static Property Lenght = new Property(7, Double.class, "lenght", false, "LENGHT");
        public final static Property Angle = new Property(8, Double.class, "angle", false, "ANGLE");
        public final static Property Desc = new Property(9, String.class, "desc", false, "DESC");
        public final static Property Pcolor = new Property(10, Integer.class, "pcolor", false, "PCOLOR");
        public final static Property Pwidth = new Property(11, Float.class, "pwidth", false, "PWIDTH");
        public final static Property Isfull = new Property(12, Boolean.class, "isfull", false, "ISFULL");
    };


    public LineDao(DaoConfig config) {
        super(config);
    }
    
    public LineDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'LINE' (" + //
                "'_id' INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "'KEY' INTEGER," + // 1: key
                "'NAME' TEXT," + // 2: name
                "'STARTX' REAL," + // 3: startx
                "'STARTY' REAL," + // 4: starty
                "'ENDX' REAL," + // 5: endx
                "'ENDY' REAL," + // 6: endy
                "'LENGHT' REAL," + // 7: lenght
                "'ANGLE' REAL," + // 8: angle
                "'DESC' TEXT," + // 9: desc
                "'PCOLOR' INTEGER," + // 10: pcolor
                "'PWIDTH' REAL," + // 11: pwidth
                "'ISFULL' INTEGER);"); // 12: isfull
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'LINE'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Line entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Long key = entity.getKey();
        if (key != null) {
            stmt.bindLong(2, key);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(3, name);
        }
 
        Float startx = entity.getStartx();
        if (startx != null) {
            stmt.bindDouble(4, startx);
        }
 
        Float starty = entity.getStarty();
        if (starty != null) {
            stmt.bindDouble(5, starty);
        }
 
        Float endx = entity.getEndx();
        if (endx != null) {
            stmt.bindDouble(6, endx);
        }
 
        Float endy = entity.getEndy();
        if (endy != null) {
            stmt.bindDouble(7, endy);
        }
 
        Double lenght = entity.getLenght();
        if (lenght != null) {
            stmt.bindDouble(8, lenght);
        }
 
        Double angle = entity.getAngle();
        if (angle != null) {
            stmt.bindDouble(9, angle);
        }
 
        String desc = entity.getDesc();
        if (desc != null) {
            stmt.bindString(10, desc);
        }
 
        Integer pcolor = entity.getPcolor();
        if (pcolor != null) {
            stmt.bindLong(11, pcolor);
        }
 
        Float pwidth = entity.getPwidth();
        if (pwidth != null) {
            stmt.bindDouble(12, pwidth);
        }
 
        Boolean isfull = entity.getIsfull();
        if (isfull != null) {
            stmt.bindLong(13, isfull ? 1l: 0l);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Line readEntity(Cursor cursor, int offset) {
        Line entity = new Line( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1), // key
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // name
            cursor.isNull(offset + 3) ? null : cursor.getFloat(offset + 3), // startx
            cursor.isNull(offset + 4) ? null : cursor.getFloat(offset + 4), // starty
            cursor.isNull(offset + 5) ? null : cursor.getFloat(offset + 5), // endx
            cursor.isNull(offset + 6) ? null : cursor.getFloat(offset + 6), // endy
            cursor.isNull(offset + 7) ? null : cursor.getDouble(offset + 7), // lenght
            cursor.isNull(offset + 8) ? null : cursor.getDouble(offset + 8), // angle
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // desc
            cursor.isNull(offset + 10) ? null : cursor.getInt(offset + 10), // pcolor
            cursor.isNull(offset + 11) ? null : cursor.getFloat(offset + 11), // pwidth
            cursor.isNull(offset + 12) ? null : cursor.getShort(offset + 12) != 0 // isfull
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Line entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setKey(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
        entity.setName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setStartx(cursor.isNull(offset + 3) ? null : cursor.getFloat(offset + 3));
        entity.setStarty(cursor.isNull(offset + 4) ? null : cursor.getFloat(offset + 4));
        entity.setEndx(cursor.isNull(offset + 5) ? null : cursor.getFloat(offset + 5));
        entity.setEndy(cursor.isNull(offset + 6) ? null : cursor.getFloat(offset + 6));
        entity.setLenght(cursor.isNull(offset + 7) ? null : cursor.getDouble(offset + 7));
        entity.setAngle(cursor.isNull(offset + 8) ? null : cursor.getDouble(offset + 8));
        entity.setDesc(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setPcolor(cursor.isNull(offset + 10) ? null : cursor.getInt(offset + 10));
        entity.setPwidth(cursor.isNull(offset + 11) ? null : cursor.getFloat(offset + 11));
        entity.setIsfull(cursor.isNull(offset + 12) ? null : cursor.getShort(offset + 12) != 0);
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Line entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Line entity) {
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