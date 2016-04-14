package com.surveymapclient.view;

import com.surveymapclient.common.Logger;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.View;

public class MagnifyView extends View {

	Bitmap bitmap;
	private  Matrix matrix = new Matrix(); 
	private byte[] mb;
	// 读取本地图片尺寸
	BitmapFactory.Options options;
			
	
	public MagnifyView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		matrix.setScale(2, 2);
		// 设置为true，options依然对应此图片，但解码器不会为此图片分配内存
	    options=new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		bitmap=Bitmap.createBitmap(80,80,Bitmap.Config.RGB_565);

		
	}
	public void setDrawableCanvas(byte[] bytes){
		mb=bytes;
		invalidate();
	}
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		bitmap=getPicFromBytes(mb, options);
		Logger.i("放大图片", ""+mb);
		if (bitmap!=null) {			
			canvas.drawBitmap(bitmap, matrix, null);
			bitmap.recycle();
		}
	}
	 /**  
     * @param 将字节数组转换为ImageView可调用的Bitmap对象  
     * @param bytes  
     * @param opts  
     * @return Bitmap  
     */    
    public static Bitmap getPicFromBytes(byte[] bytes,    
            BitmapFactory.Options opts) {    
        if (bytes != null)    
            if (opts != null)    
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length,    
                        opts);    
            else    
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);    
        return null;    
    }  

}
