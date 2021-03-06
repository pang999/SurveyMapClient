package com.surveymapclient.db.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.surveymapclient.db.greendao.Rectangle;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table RECTANGLE.
*/
public class RectangleDao extends AbstractDao<Rectangle, Long> {

    public static final String TABLENAME = "RECTANGLE";

    /**
     * Properties of entity Rectangle.<br/>
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
        public final static Property Area = new Property(7, Double.class, "area", false, "AREA");
        public final static Property Lenght = new Property(8, Float.class, "lenght", false, "LENGHT");
        public final static Property Width = new Property(9, Float.class, "width", false, "WIDTH");
        public final static Property Desc = new Property(10, String.class, "desc", false, "DESC");
        public final static Property Pcolor = new Property(11, Integer.class, "pcolor", false, "PCOLOR");
        public final static Property Pwidth = new Property(12, Float.class, "pwidth", false, "PWIDTH");
        public final static Property Isfull = new Property(13, Boolean.class, "isfull", false, "ISFULL");
    };


    public RectangleDao(DaoConfig config) {
        super(config);
    }
    
    public RectangleDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'RECTANGLE' (" + //
                "'_id' INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "'KEY' INTEGER," + // 1: key
                "'NAME' TEXT," + // 2: name
                "'STARTX' REAL," + // 3: startx
                "'STARTY' REAL," + // 4: starty
                "'ENDX' REAL," + // 5: endx
                "'ENDY' REAL," + // 6: endy
                "'AREA' REAL," + // 7: area
                "'LENGHT' REAL," + // 8: lenght
                "'WIDTH' REAL," + // 9: width
                "'DESC' TEXT," + // 10: desc
                "'PCOLOR' INTEGER," + // 11: pcolor
                "'PWIDTH' REAL," + // 12: pwidth
                "'ISFULL' INTEGER);"); // 13: isfull
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'RECTANGLE'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Rectangle entity) {
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
 
        Double area = entity.getArea();
        if (area != null) {
            stmt.bindDouble(8, area);
        }
 
        Float lenght = entity.getLenght();
        if (lenght != null) {
            stmt.bindDouble(9, lenght);
        }
 
        Float width = entity.getWidth();
        if (width != null) {
            stmt.bindDouble(10, width);
        }
 
        String desc = entity.getDesc();
        if (desc != null) {
            stmt.bindString(11, desc);
        }
 
        Integer pcolor = entity.getPcolor();
        if (pcolor != null) {
            stmt.bindLong(12, pcolor);
        }
 
        Float pwidth = entity.getPwidth();
        if (pwidth != null) {
            stmt.bindDouble(13, pwidth);
        }
 
        Boolean isfull = entity.getIsfull();
        if (isfull != null) {
            stmt.bindLong(14, isfull ? 1l: 0l);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Rectangle readEntity(Cursor cursor, int offset) {
        Rectangle entity = new Rectangle( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1), // key
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // name
            cursor.isNull(offset + 3) ? null : cursor.getFloat(offset + 3), // startx
            cursor.isNull(offset + 4) ? null : cursor.getFloat(offset + 4), // starty
            cursor.isNull(offset + 5) ? null : cursor.getFloat(offset + 5), // endx
            cursor.isNull(offset + 6) ? null : cursor.getFloat(offset + 6), // endy
            cursor.isNull(offset + 7) ? null : cursor.getDouble(offset + 7), // area
            cursor.isNull(offset + 8) ? null : cursor.getFloat(offset + 8), // lenght
            cursor.isNull(offset + 9) ? null : cursor.getFloat(offset + 9), // width
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // desc
            cursor.isNull(offset + 11) ? null : cursor.getInt(offset + 11), // pcolor
            cursor.isNull(offset + 12) ? null : cursor.getFloat(offset + 12), // pwidth
            cursor.isNull(offset + 13) ? null : cursor.getShort(offset + 13) != 0 // isfull
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Rectangle entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setKey(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
        entity.setName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setStartx(cursor.isNull(offset + 3) ? null : cursor.getFloat(offset + 3));
        entity.setStarty(cursor.isNull(offset + 4) ? null : cursor.getFloat(offset + 4));
        entity.setEndx(cursor.isNull(offset + 5) ? null : cursor.getFloat(offset + 5));
        entity.setEndy(cursor.isNull(offset + 6) ? null : cursor.getFloat(offset + 6));
        entity.setArea(cursor.isNull(offset + 7) ? null : cursor.getDouble(offset + 7));
        entity.setLenght(cursor.isNull(offset + 8) ? null : cursor.getFloat(offset + 8));
        entity.setWidth(cursor.isNull(offset + 9) ? null : cursor.getFloat(offset + 9));
        entity.setDesc(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setPcolor(cursor.isNull(offset + 11) ? null : cursor.getInt(offset + 11));
        entity.setPwidth(cursor.isNull(offset + 12) ? null : cursor.getFloat(offset + 12));
        entity.setIsfull(cursor.isNull(offset + 13) ? null : cursor.getShort(offset + 13) != 0);
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Rectangle entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Rectangle entity) {
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
