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
	// ��ȡ����ͼƬ�ߴ�
	BitmapFactory.Options options;
			
	
	public MagnifyView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		matrix.setScale(2, 2);
		// ����Ϊtrue��options��Ȼ��Ӧ��ͼƬ��������������Ϊ��ͼƬ�����ڴ�
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
		Logger.i("�Ŵ�ͼƬ", ""+mb);
		if (bitmap!=null) {			
			canvas.drawBitmap(bitmap, matrix, null);
			bitmap.recycle();
		}
	}
	 /**  
     * @param ���ֽ�����ת��ΪImageView�ɵ��õ�Bitmap����  
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
