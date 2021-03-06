package com.surveymapclient.db.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.surveymapclient.db.greendao.Coordinate;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table COORDINATE.
*/
public class CoordinateDao extends AbstractDao<Coordinate, Long> {

    public static final String TABLENAME = "COORDINATE";

    /**
     * Properties of entity Coordinate.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Key = new Property(1, Long.class, "key", false, "KEY");
        public final static Property Name = new Property(2, String.class, "name", false, "NAME");
        public final static Property Xaxisx = new Property(3, Float.class, "xaxisx", false, "XAXISX");
        public final static Property Xaxisy = new Property(4, Float.class, "xaxisy", false, "XAXISY");
        public final static Property Yaxisx = new Property(5, Float.class, "yaxisx", false, "YAXISX");
        public final static Property Yaxisy = new Property(6, Float.class, "yaxisy", false, "YAXISY");
        public final static Property Zaxisx = new Property(7, Float.class, "zaxisx", false, "ZAXISX");
        public final static Property Zaxisy = new Property(8, Float.class, "zaxisy", false, "ZAXISY");
        public final static Property Centerx = new Property(9, Float.class, "centerx", false, "CENTERX");
        public final static Property Centery = new Property(10, Float.class, "centery", false, "CENTERY");
        public final static Property Volum = new Property(11, Double.class, "volum", false, "VOLUM");
        public final static Property Lenght = new Property(12, Float.class, "lenght", false, "LENGHT");
        public final static Property Width = new Property(13, Float.class, "width", false, "WIDTH");
        public final static Property Height = new Property(14, Float.class, "height", false, "HEIGHT");
        public final static Property Desc = new Property(15, String.class, "desc", false, "DESC");
        public final static Property Pcolor = new Property(16, Integer.class, "pcolor", false, "PCOLOR");
        public final static Property Pwidth = new Property(17, Float.class, "pwidth", false, "PWIDTH");
        public final static Property Isfull = new Property(18, Boolean.class, "isfull", false, "ISFULL");
    };


    public CoordinateDao(DaoConfig config) {
        super(config);
    }
    
    public CoordinateDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'COORDINATE' (" + //
                "'_id' INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "'KEY' INTEGER," + // 1: key
                "'NAME' TEXT," + // 2: name
                "'XAXISX' REAL," + // 3: xaxisx
                "'XAXISY' REAL," + // 4: xaxisy
                "'YAXISX' REAL," + // 5: yaxisx
                "'YAXISY' REAL," + // 6: yaxisy
                "'ZAXISX' REAL," + // 7: zaxisx
                "'ZAXISY' REAL," + // 8: zaxisy
                "'CENTERX' REAL," + // 9: centerx
                "'CENTERY' REAL," + // 10: centery
                "'VOLUM' REAL," + // 11: volum
                "'LENGHT' REAL," + // 12: lenght
                "'WIDTH' REAL," + // 13: width
                "'HEIGHT' REAL," + // 14: height
                "'DESC' TEXT," + // 15: desc
                "'PCOLOR' INTEGER," + // 16: pcolor
                "'PWIDTH' REAL," + // 17: pwidth
                "'ISFULL' INTEGER);"); // 18: isfull
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'COORDINATE'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Coordinate entity) {
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
 
        Float xaxisx = entity.getXaxisx();
        if (xaxisx != null) {
            stmt.bindDouble(4, xaxisx);
        }
 
        Float xaxisy = entity.getXaxisy();
        if (xaxisy != null) {
            stmt.bindDouble(5, xaxisy);
        }
 
        Float yaxisx = entity.getYaxisx();
        if (yaxisx != null) {
            stmt.bindDouble(6, yaxisx);
        }
 
        Float yaxisy = entity.getYaxisy();
        if (yaxisy != null) {
            stmt.bindDouble(7, yaxisy);
        }
 
        Float zaxisx = entity.getZaxisx();
        if (zaxisx != null) {
            stmt.bindDouble(8, zaxisx);
        }
 
        Float zaxisy = entity.getZaxisy();
        if (zaxisy != null) {
            stmt.bindDouble(9, zaxisy);
        }
 
        Float centerx = entity.getCenterx();
        if (centerx != null) {
            stmt.bindDouble(10, centerx);
        }
 
        Float centery = entity.getCentery();
        if (centery != null) {
            stmt.bindDouble(11, centery);
        }
 
        Double volum = entity.getVolum();
        if (volum != null) {
            stmt.bindDouble(12, volum);
        }
 
        Float lenght = entity.getLenght();
        if (lenght != null) {
            stmt.bindDouble(13, lenght);
        }
 
        Float width = entity.getWidth();
        if (width != null) {
            stmt.bindDouble(14, width);
        }
 
        Float height = entity.getHeight();
        if (height != null) {
            stmt.bindDouble(15, height);
        }
 
        String desc = entity.getDesc();
        if (desc != null) {
            stmt.bindString(16, desc);
        }
 
        Integer pcolor = entity.getPcolor();
        if (pcolor != null) {
            stmt.bindLong(17, pcolor);
        }
 
        Float pwidth = entity.getPwidth();
        if (pwidth != null) {
            stmt.bindDouble(18, pwidth);
        }
 
        Boolean isfull = entity.getIsfull();
        if (isfull != null) {
            stmt.bindLong(19, isfull ? 1l: 0l);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Coordinate readEntity(Cursor cursor, int offset) {
        Coordinate entity = new Coordinate( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1), // key
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // name
            cursor.isNull(offset + 3) ? null : cursor.getFloat(offset + 3), // xaxisx
            cursor.isNull(offset + 4) ? null : cursor.getFloat(offset + 4), // xaxisy
            cursor.isNull(offset + 5) ? null : cursor.getFloat(offset + 5), // yaxisx
            cursor.isNull(offset + 6) ? null : cursor.getFloat(offset + 6), // yaxisy
            cursor.isNull(offset + 7) ? null : cursor.getFloat(offset + 7), // zaxisx
            cursor.isNull(offset + 8) ? null : cursor.getFloat(offset + 8), // zaxisy
            cursor.isNull(offset + 9) ? null : cursor.getFloat(offset + 9), // centerx
            cursor.isNull(offset + 10) ? null : cursor.getFloat(offset + 10), // centery
            cursor.isNull(offset + 11) ? null : cursor.getDouble(offset + 11), // volum
            cursor.isNull(offset + 12) ? null : cursor.getFloat(offset + 12), // lenght
            cursor.isNull(offset + 13) ? null : cursor.getFloat(offset + 13), // width
            cursor.isNull(offset + 14) ? null : cursor.getFloat(offset + 14), // height
            cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15), // desc
            cursor.isNull(offset + 16) ? null : cursor.getInt(offset + 16), // pcolor
            cursor.isNull(offset + 17) ? null : cursor.getFloat(offset + 17), // pwidth
            cursor.isNull(offset + 18) ? null : cursor.getShort(offset + 18) != 0 // isfull
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Coordinate entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setKey(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
        entity.setName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setXaxisx(cursor.isNull(offset + 3) ? null : cursor.getFloat(offset + 3));
        entity.setXaxisy(cursor.isNull(offset + 4) ? null : cursor.getFloat(offset + 4));
        entity.setYaxisx(cursor.isNull(offset + 5) ? null : cursor.getFloat(offset + 5));
        entity.setYaxisy(cursor.isNull(offset + 6) ? null : cursor.getFloat(offset + 6));
        entity.setZaxisx(cursor.isNull(offset + 7) ? null : cursor.getFloat(offset + 7));
        entity.setZaxisy(cursor.isNull(offset + 8) ? null : cursor.getFloat(offset + 8));
        entity.setCenterx(cursor.isNull(offset + 9) ? null : cursor.getFloat(offset + 9));
        entity.setCentery(cursor.isNull(offset + 10) ? null : cursor.getFloat(offset + 10));
        entity.setVolum(cursor.isNull(offset + 11) ? null : cursor.getDouble(offset + 11));
        entity.setLenght(cursor.isNull(offset + 12) ? null : cursor.getFloat(offset + 12));
        entity.setWidth(cursor.isNull(offset + 13) ? null : cursor.getFloat(offset + 13));
        entity.setHeight(cursor.isNull(offset + 14) ? null : cursor.getFloat(offset + 14));
        entity.setDesc(cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15));
        entity.setPcolor(cursor.isNull(offset + 16) ? null : cursor.getInt(offset + 16));
        entity.setPwidth(cursor.isNull(offset + 17) ? null : cursor.getFloat(offset + 17));
        entity.setIsfull(cursor.isNull(offset + 18) ? null : cursor.getShort(offset + 18) != 0);
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Coordinate entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Coordinate entity) {
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
