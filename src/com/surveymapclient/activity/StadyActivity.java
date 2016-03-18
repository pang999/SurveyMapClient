package com.surveymapclient.activity;

import com.surveymapclient.view.StardyView;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipDescription;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnDragListener;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ZoomControls;

@SuppressLint("NewApi")
public class StadyActivity extends Activity {
	
	/** 
     * ΪImageView����һ���ַ���ǩ 
     */  
    private static final String IMAGEVIEW_TAG = "icon bitmap"; 

	private StardyView stadyview;
    private ZoomControls zoom;
    /** 
     * ���������϶���ImageView 
     */  
    private ImageView imageView;  
    /** 
     * �϶��¼����� 
     */  
    private myDragEventListener mDragListen;  
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stady);
		stadyview=(StardyView) findViewById(R.id.canvasview);
		init();
		zoom=(ZoomControls) findViewById(R.id.zoomControls1);
		zoom.setIsZoomInEnabled(true);
		zoom.setIsZoomOutEnabled(true);
		zoom.setOnZoomInClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				float scale=(float) 1.2;
			}
		});
		zoom.setOnZoomOutClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				float scale=(float) 0.8;
			}
		});
	}
	
	public void DanLianXian(View v){
		stadyview.drawlines();
		Toast.makeText(this, "������", Toast.LENGTH_LONG).show();
	}
	public void DuoLianXian(View v){
		stadyview.saveBitmap();

	}
	public void CheHui(View v){
		stadyview.undo();
	}
	public void ZhuSi(View v){
//		stadyview.redo();
		
	}
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}
	
	private void init(){
		imageView=new ImageView(this);
		imageView=(ImageView) findViewById(R.id.move);
		// ���ñ�ǩ
		imageView.setTag(IMAGEVIEW_TAG);
		imageView.setOnLongClickListener(mLongClick);
		stadyview.setOnDragListener(mDragListen);
	}
	
	/** 
     * �϶�ImageView�ĳ����¼����� 
     */  
	private OnLongClickListener mLongClick=new OnLongClickListener() {
		
		@Override
		public boolean onLongClick(View v) {
			// TODO Auto-generated method stub
			// ����һ��ClipData����  
            // �����Ϊ��������һ���з���ClipData.newPlainText()���Դ���һ�����ı�ClipData  
  
            // ����ImageView�ı�ǩ����һ��ClipData.Item����
			ClipData.Item item=new ClipData.Item((CharSequence)v.getTag());
			// ʹ�ñ�ǩ�����ı����Ѿ�������item������һ��ClipData����  
            // ���ｫ��ClipData�д���һ���µ�ClipDescription������������MIME����Ϊ"text/plain" 
			ClipData dragdata=new ClipData((CharSequence)v.getTag(),
					new String[] { ClipDescription.MIMETYPE_TEXT_PLAIN }, item);
			 // ʵ������קӰ��.  
            View.DragShadowBuilder myShadow = new MyDragShadowBuilder(imageView);  
  
            // ��ʼ��ק 
			return  v.startDrag(dragdata, // ����ק������  
                    myShadow, // ��ק��Ӱ��  
                    null, // ����Ҫʹ�ñ�������  
                    0); // ��ǣ�Ŀǰ�ò���������Ϊ0��
		}
	};
	
	private static class MyDragShadowBuilder extends View.DragShadowBuilder{
		// �϶���Ӱ��ͼ�� ��Ϊһ��drawable������  
        private static Drawable shadow; 
        // ���캯��  
        public MyDragShadowBuilder(View v) {  
            // ͨ��myDragShadowBuilder�洢View����  
            super(v);  
            // ����һ������ק��ͼ�񣬴�ͼ�����ͨ��ϵͳ��Canvas�����  
            shadow = new ColorDrawable(Color.LTGRAY);  
        } 
        // ����һ���ص�����������Ӱ��ά�Ⱥʹ����㷵�ظ�ϵͳ 
        @Override
        public void onProvideShadowMetrics(Point shadowSize, Point shadowTouchPoint) {
        	// TODO Auto-generated method stub
        	// ���嵱�صı���  
            int width;  
            int height;  
            // ������Ӱ�Ŀ��Ϊ��ͼһ��  
            width = getView().getWidth() *2;  
            // ������Ӱ�ĸ߶�Ϊ��ͼһ��  
            height = getView().getHeight() *2;  
            // ��ק��Ӱ��һ��ColorDrawable. ������ϵ�ά�Ⱥ�ϵͳ���ṩ��Canvas��һ����  
            // ��ˣ���ק��Ӱ���ᱻCanvas����  
            shadow.setBounds(0, 0, width, height);             
            // ���ò�����Ⱥ͸߶ȵĴ�С.ͨ����С�������ظ�ϵͳ  
            shadowSize.set(width, height);  
            // ���ô������λ��Ϊ��ק��Ӱ������  
            shadowTouchPoint.set(width / 2, height / 2);
        }
        // �ڻ���Canvas�ж���һ���ص�������������ק����Ӱ���û�����ͨ������onProvideShadowMetrics()�ṩ��ά��  
        // ��ϵͳ���� 
        @Override
        public void onDrawShadow(Canvas canvas) {
        	// TODO Auto-generated method stub
        	super.onDrawShadow(canvas);
        	shadow.draw(canvas);
        }
	}
	protected class myDragEventListener implements OnDragListener{

		// �÷�����ϵͳ���ã�������ק�¼�����ʱ  
		@Override
		public boolean onDrag(View v, DragEvent event) {
			// TODO Auto-generated method stub
			// ����һ���������洢ͨ���¼����ݵ�action����  
            final int action = event.getAction();  
            // ÿ���¼��Ĵ���
            switch (action) {
			case DragEvent.ACTION_DRAG_STARTED:
				 // ȷ���Ƿ������ͼ��View�����Խ�����ק����������  
//				if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
					((StardyView)v).drawlines();
//					v.invalidate();
					return true;
//				}else {
//					return false;
//				}
			case DragEvent.ACTION_DRAG_ENTERED:
				((StardyView)v).drawtext();
				v.invalidate();
				return true;
			case DragEvent.ACTION_DRAG_LOCATION:  
                // ���Ը��¼�  
                return (true);
			case DragEvent.ACTION_DRAG_EXITED:  
                System.out.println("ACTION_DRAG_EXITED----------------");  
                ((StardyView)v).drawlines(); 
                // ǿ���ػ���ͼ����ʾ�µ�����  
                v.invalidate();  
                return (true);
			case DragEvent.ACTION_DROP:  
                System.out.println("ACTION_DROP----------------");  
                // ���item������ק����  
                ClipData.Item item = event.getClipData().getItemAt(0);  
                // ��item����ı�����  
                CharSequence dragData = item.getText();  
                // ��ʾ��ק�����а�������Ϣ.  
                Toast.makeText(StadyActivity.this, "Dragged data is: " + dragData, Toast.LENGTH_SHORT).show();  
                // ����������ɫ������  
                ((StardyView)v).drawlines(); 
                // ǿ���ػ���ͼ����ʾ�µ�����  
                v.invalidate();  
                // ����true. DragEvent.getResult()���᷵��true.  
                return (true); 
            case DragEvent.ACTION_DRAG_ENDED:
            	((StardyView)v).drawpath();  
                // ǿ���ػ���ͼ����ʾ�µ�����  
                v.invalidate();  
                //ͨ��getResult()�����ķ���ֵ�жϷ�����ʲô  
                if (event.getResult()) {  
                    Toast.makeText(StadyActivity.this, "The drop was handled.", Toast.LENGTH_LONG).show();  
                } else {  
                    Toast.makeText(StadyActivity.this, "The drop didn't work.", Toast.LENGTH_LONG).show();  

                }; 
            	return true;
			default:
				break;
			}
			return false;
		}
		
	}
}
