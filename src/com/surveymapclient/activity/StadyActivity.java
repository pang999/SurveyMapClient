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
     * 为ImageView创建一个字符标签 
     */  
    private static final String IMAGEVIEW_TAG = "icon bitmap"; 

	private StardyView stadyview;
    private ZoomControls zoom;
    /** 
     * 创建用于拖动的ImageView 
     */  
    private ImageView imageView;  
    /** 
     * 拖动事件监听 
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
		Toast.makeText(this, "点中了", Toast.LENGTH_LONG).show();
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
		// 设置标签
		imageView.setTag(IMAGEVIEW_TAG);
		imageView.setOnLongClickListener(mLongClick);
		stadyview.setOnDragListener(mDragListen);
	}
	
	/** 
     * 拖动ImageView的长按事件监听 
     */  
	private OnLongClickListener mLongClick=new OnLongClickListener() {
		
		@Override
		public boolean onLongClick(View v) {
			// TODO Auto-generated method stub
			// 创建一个ClipData对象  
            // 这里分为两步，第一步中方法ClipData.newPlainText()可以创建一个纯文本ClipData  
  
            // 根据ImageView的标签创建一个ClipData.Item对象
			ClipData.Item item=new ClipData.Item((CharSequence)v.getTag());
			// 使用标签，纯文本和已经创建的item来创建一个ClipData对象  
            // 这里将在ClipData中创建一个新的ClipDescription对象并设置它的MIME类型为"text/plain" 
			ClipData dragdata=new ClipData((CharSequence)v.getTag(),
					new String[] { ClipDescription.MIMETYPE_TEXT_PLAIN }, item);
			 // 实例化拖拽影子.  
            View.DragShadowBuilder myShadow = new MyDragShadowBuilder(imageView);  
  
            // 开始拖拽 
			return  v.startDrag(dragdata, // 被拖拽的数据  
                    myShadow, // 拖拽的影子  
                    null, // 不需要使用本地数据  
                    0); // 标记（目前用不到，设置为0）
		}
	};
	
	private static class MyDragShadowBuilder extends View.DragShadowBuilder{
		// 拖动阴影的图像， 作为一个drawable来定义  
        private static Drawable shadow; 
        // 构造函数  
        public MyDragShadowBuilder(View v) {  
            // 通过myDragShadowBuilder存储View参数  
            super(v);  
            // 创建一个可拖拽的图像，此图像可以通过系统的Canvas来填充  
            shadow = new ColorDrawable(Color.LTGRAY);  
        } 
        // 定义一个回调方法，将阴影的维度和触摸点返回给系统 
        @Override
        public void onProvideShadowMetrics(Point shadowSize, Point shadowTouchPoint) {
        	// TODO Auto-generated method stub
        	// 定义当地的变量  
            int width;  
            int height;  
            // 设置阴影的宽度为视图一半  
            width = getView().getWidth() *2;  
            // 设置阴影的高度为视图一半  
            height = getView().getHeight() *2;  
            // 拖拽阴影是一个ColorDrawable. 这个集合的维度和系统所提供的Canvas是一样的  
            // 因此，拖拽阴影将会被Canvas覆盖  
            shadow.setBounds(0, 0, width, height);             
            // 设置参数宽度和高度的大小.通过大小参数返回给系统  
            shadowSize.set(width, height);  
            // 设置触摸点的位置为拖拽阴影的中心  
            shadowTouchPoint.set(width / 2, height / 2);
        }
        // 在画布Canvas中定义一个回调函数来绘制拖拽的阴影，该画布是通过方法onProvideShadowMetrics()提供的维度  
        // 由系统构造 
        @Override
        public void onDrawShadow(Canvas canvas) {
        	// TODO Auto-generated method stub
        	super.onDrawShadow(canvas);
        	shadow.draw(canvas);
        }
	}
	protected class myDragEventListener implements OnDragListener{

		// 该方法由系统调用，当有拖拽事件发生时  
		@Override
		public boolean onDrag(View v, DragEvent event) {
			// TODO Auto-generated method stub
			// 定义一个变量来存储通过事件传递的action类型  
            final int action = event.getAction();  
            // 每个事件的处理
            switch (action) {
			case DragEvent.ACTION_DRAG_STARTED:
				 // 确定是否这个视图（View）可以接收拖拽的数据类型  
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
                // 忽略该事件  
                return (true);
			case DragEvent.ACTION_DRAG_EXITED:  
                System.out.println("ACTION_DRAG_EXITED----------------");  
                ((StardyView)v).drawlines(); 
                // 强制重绘视图以显示新的特性  
                v.invalidate();  
                return (true);
			case DragEvent.ACTION_DROP:  
                System.out.println("ACTION_DROP----------------");  
                // 获得item包括拖拽数据  
                ClipData.Item item = event.getClipData().getItemAt(0);  
                // 从item获得文本数据  
                CharSequence dragData = item.getText();  
                // 显示拖拽数据中包含的信息.  
                Toast.makeText(StadyActivity.this, "Dragged data is: " + dragData, Toast.LENGTH_SHORT).show();  
                // 重新设置颜色和文字  
                ((StardyView)v).drawlines(); 
                // 强制重绘视图以显示新的特性  
                v.invalidate();  
                // 返回true. DragEvent.getResult()将会返回true.  
                return (true); 
            case DragEvent.ACTION_DRAG_ENDED:
            	((StardyView)v).drawpath();  
                // 强制重绘视图以显示新的特性  
                v.invalidate();  
                //通过getResult()方法的返回值判断发生了什么  
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
