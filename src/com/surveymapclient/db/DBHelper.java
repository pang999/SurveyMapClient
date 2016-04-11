package com.surveymapclient.db;

<<<<<<< HEAD
import java.util.ArrayList;
=======
>>>>>>> fa4a202c7816980a0c716bb84e647853e5a4f4f4
import java.util.List;

import com.surveymapclient.activity.base.MyApplication;
import com.surveymapclient.common.Logger;
import com.surveymapclient.db.greendao.Angle;
import com.surveymapclient.db.greendao.AngleDao;
import com.surveymapclient.db.greendao.AudioNote;
import com.surveymapclient.db.greendao.AudioNoteDao;
import com.surveymapclient.db.greendao.Coordinate;
import com.surveymapclient.db.greendao.CoordinateDao;
import com.surveymapclient.db.greendao.DaoMaster;
import com.surveymapclient.db.greendao.DaoSession;
import com.surveymapclient.db.greendao.Line;
import com.surveymapclient.db.greendao.LineDao;
import com.surveymapclient.db.greendao.LineDao.Properties;
import com.surveymapclient.db.greendao.Lines;
import com.surveymapclient.db.greendao.LinesDao;
import com.surveymapclient.db.greendao.Module;
import com.surveymapclient.db.greendao.ModuleDao;
import com.surveymapclient.db.greendao.Polygon;
import com.surveymapclient.db.greendao.PolygonDao;
import com.surveymapclient.db.greendao.Rectangle;
import com.surveymapclient.db.greendao.RectangleDao;
import com.surveymapclient.db.greendao.TextNote;
import com.surveymapclient.db.greendao.TextNoteDao;
<<<<<<< HEAD
import com.surveymapclient.entity.LineBean;
import com.tencent.a.a.a.a.c;

import android.content.Context;
import android.util.Log;
=======

import android.content.Context;
>>>>>>> fa4a202c7816980a0c716bb84e647853e5a4f4f4
import de.greenrobot.dao.query.QueryBuilder;

public class DBHelper {

	private static final String TAG=DBHelper.class.getSimpleName();
	private static DBHelper instance;
	private static Context appContext;
	private DaoSession mDaoSession;
	private LineDao lineDao;
	private LinesDao linesDao;
	private PolygonDao polygonDao;
	private RectangleDao rectangleDao;
	private CoordinateDao coordinateDao;
	private AngleDao angleDao;
	private TextNoteDao textNoteDao;
	private AudioNoteDao audioNoteDao;
	private ModuleDao moduleDao;
	
	public DBHelper() {
		// TODO Auto-generated constructor stub
	}
	//单例模式，DBHelper只初始化一次  
	public static DBHelper getInstance(Context context){
		if (instance==null) {
			instance=new DBHelper();
			if (appContext==null) {
				appContext=context.getApplicationContext();
			}
			instance.mDaoSession=MyApplication.getDaoSession(appContext);
			instance.lineDao=instance.mDaoSession.getLineDao();
			instance.linesDao=instance.mDaoSession.getLinesDao();
			instance.polygonDao=instance.mDaoSession.getPolygonDao();
			instance.rectangleDao=instance.mDaoSession.getRectangleDao();
			instance.coordinateDao=instance.mDaoSession.getCoordinateDao();
			instance.angleDao=instance.mDaoSession.getAngleDao();
			instance.textNoteDao=instance.mDaoSession.getTextNoteDao();
			instance.audioNoteDao=instance.mDaoSession.getAudioNoteDao();
			instance.moduleDao=instance.mDaoSession.getModuleDao();
		}
		
		return instance;
	}
<<<<<<< HEAD

=======
	
>>>>>>> fa4a202c7816980a0c716bb84e647853e5a4f4f4
	public void dropAllTable(){
		LineDao.dropTable(mDaoSession.getDatabase(), true);
		RectangleDao.dropTable(mDaoSession.getDatabase(), true);
		CoordinateDao.dropTable(mDaoSession.getDatabase(), true);
		AngleDao.dropTable(mDaoSession.getDatabase(), true);			
	}
	
	public void creatAllTable(){
		LineDao.createTable(mDaoSession.getDatabase(), true);
		RectangleDao.createTable(mDaoSession.getDatabase(), true);
		CoordinateDao.createTable(mDaoSession.getDatabase(), true);
		AngleDao.createTable(mDaoSession.getDatabase(), true);
		TextNoteDao.createTable(mDaoSession.getDatabase(), true);
	}
	 //查询满足params条件的列表  
	/**
	 * 
	 * @param line
	 */
	public void insertDateLine(Line line){
		lineDao.insert(line);
	}
	public List<Line> searchDataLine(long key){
		QueryBuilder<Line> qBuilder=lineDao.queryBuilder();
		qBuilder.where(Properties.Key.eq(key));
		return qBuilder.list();
	}
<<<<<<< HEAD
	public void deleteDataLine(Line line){
		lineDao.delete(line);
	}
=======
>>>>>>> fa4a202c7816980a0c716bb84e647853e5a4f4f4
	/**
	 * 
	 * @param lines
	 */
	public void insertDataLines(Lines lines){
		linesDao.insert(lines);
	}
<<<<<<< HEAD
	public List<Lines> searchDataLines(long key){
		QueryBuilder<Lines> qBuilder=linesDao.queryBuilder();
		qBuilder.where(com.surveymapclient.db.greendao.LinesDao.Properties.Key.eq(key));
		return qBuilder.list();
	}
//	public List<Lines> getLines(long key){
//		QueryBuilder<Polygon> qBuilder=polygonDao.queryBuilder();
//		qBuilder.where(com.surveymapclient.db.greendao.PolygonDao.Properties.Key.eq(key));		
//		List<Lines> lines=linesDao._queryPolygon_Lines(key);
//		return linesDao._queryPolygon_Lines(key);
//	}
=======
>>>>>>> fa4a202c7816980a0c716bb84e647853e5a4f4f4
	/**
	 * 
	 * @param polygon
	 */
	public void insertDataPolygon(Polygon polygon){
		polygonDao.insert(polygon);
	}
	public List<Polygon> searchDataPolygon(long key){
		QueryBuilder<Polygon> qBuilder=polygonDao.queryBuilder();
		qBuilder.where(com.surveymapclient.db.greendao.PolygonDao.Properties.Key.eq(key));
		return qBuilder.list();
	}
	public List<Polygon> searchDataPolygon(){
		return polygonDao.loadAll();
	}
<<<<<<< HEAD
	public void deleteDataPolygon(Polygon polygon){
		polygonDao.delete(polygon);
	}
	
=======
>>>>>>> fa4a202c7816980a0c716bb84e647853e5a4f4f4
	
	/**
	 * 
	 * @param rectangle
	 */
	public void insertDataRect(Rectangle rectangle){
		rectangleDao.insert(rectangle);
	}
	public List<Rectangle> searchDataRectangle(long key){
		QueryBuilder<Rectangle> qBuilder=rectangleDao.queryBuilder();
		qBuilder.where(com.surveymapclient.db.greendao.RectangleDao.Properties.Key.eq(key));
		return qBuilder.list();
	}
<<<<<<< HEAD
	public void deleteDataRectangle(Rectangle rectangle){
		rectangleDao.delete(rectangle);
	}
=======
	
>>>>>>> fa4a202c7816980a0c716bb84e647853e5a4f4f4
	/**
	 * 
	 * @param coordinate
	 */
	public void insertDataCoor(Coordinate coordinate){
		coordinateDao.insert(coordinate);
	}
	public List<Coordinate> searchDataCoordinate(long key){
		QueryBuilder<Coordinate> qBuilder=coordinateDao.queryBuilder();
		qBuilder.where(com.surveymapclient.db.greendao.CoordinateDao.Properties.Key.eq(key));
		return qBuilder.list();
	}
<<<<<<< HEAD
	public void deleteDataCoor(Coordinate coordinate){
		coordinateDao.delete(coordinate);
	}
=======
>>>>>>> fa4a202c7816980a0c716bb84e647853e5a4f4f4
	/**
	 * 
	 * @param angle
	 */
	public void insertDataAngle(Angle angle){
						
		angleDao.insert(angle);
		Logger.i("数据库数据", "角度="+angleDao.loadAll().size());
	}
<<<<<<< HEAD
=======
	public List<Angle> searchDataAngle(){
//		QueryBuilder<Angle> qBuilder=angleDao.queryBuilder();
//		qBuilder.where(com.surveymapclient.db.greendao.AngleDao.Properties.Key.eq(key));
		return angleDao.loadAll();
	}
>>>>>>> fa4a202c7816980a0c716bb84e647853e5a4f4f4
	public List<Angle> searchDataAngle(long key){
		QueryBuilder<Angle> queryBuilder=angleDao.queryBuilder();
		queryBuilder.where(com.surveymapclient.db.greendao.AngleDao.Properties.Key.eq(key));
		return queryBuilder.list();
	}
<<<<<<< HEAD
	public void deleteDataAngle(Angle angle){
		angleDao.delete(angle);
	}
=======
>>>>>>> fa4a202c7816980a0c716bb84e647853e5a4f4f4
	/**
	 * 
	 * @param textNote
	 */
	public void insertDataText(TextNote textNote){
		textNoteDao.insert(textNote);
	}
<<<<<<< HEAD
	public void deleteDataText(TextNote textNote){
		textNoteDao.delete(textNote);
	}
=======
>>>>>>> fa4a202c7816980a0c716bb84e647853e5a4f4f4
	public List<TextNote> searchDataText(long key){
		QueryBuilder<TextNote> qBuilder=textNoteDao.queryBuilder();
		qBuilder.where(com.surveymapclient.db.greendao.TextNoteDao.Properties.Key.eq(key));
		return qBuilder.list();
	}
	/**
	 * 
	 * @param audioNote
	 */
	public void insertDataAudio(AudioNote audioNote){
		audioNoteDao.insert(audioNote);
	}
	public List<AudioNote> searchDataAudio(long key){
		QueryBuilder<AudioNote> qBuilder=audioNoteDao.queryBuilder();
		qBuilder.where(com.surveymapclient.db.greendao.AudioNoteDao.Properties.Key.eq(key));
		return qBuilder.list();
	}
<<<<<<< HEAD
	public void deleteDataAudio(AudioNote audio){
		audioNoteDao.delete(audio);
	}
=======
>>>>>>> fa4a202c7816980a0c716bb84e647853e5a4f4f4
	/**
	 * 
	 * @param module
	 */
	public void insertDataModule(Module module){
		moduleDao.insert(module);
<<<<<<< HEAD
		
=======
>>>>>>> fa4a202c7816980a0c716bb84e647853e5a4f4f4
	}
	public List<Module> searchDataModule(){	
		return moduleDao.loadAll();
	}
<<<<<<< HEAD
	public void deleteDataModule(Module module){
		moduleDao.delete(module);
	}
	
=======
>>>>>>> fa4a202c7816980a0c716bb84e647853e5a4f4f4
}
