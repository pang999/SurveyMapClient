package com.surveymapclient.activity;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.surveymapclient.common.ImageTools;
import com.surveymapclient.common.Logger;
import com.surveymapclient.view.fragment.EditeAndDelDialog;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class HomeActivity extends Activity {

	private static final int PHOTO_WITH_DATA = 18;  //从SD卡中得到图片
	private static final int PHOTO_WITH_CAMERA = 37;// 拍摄照片
	
	private String imgPath  = "";
	private String imgName = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
	}

	public void SelfDefine(View v){
		Intent intent=new Intent(HomeActivity.this,DefineActivity.class);
		startActivity(intent);		
	}
	public void CameraBack(View v){
		Intent intent=new Intent(HomeActivity.this,TestCameraActivity.class);
		startActivity(intent);
//		doTakePhoto();
	}
	public void FromAlbum(View v){
		doPickPhotoFromGallery();
	}
	public void StadyView(View v){
		Intent intent=new Intent(HomeActivity.this,TestCameraActivity.class);
		startActivity(intent);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	public static final String KEY_FILENAME = "filename";
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(resultCode == RESULT_OK) {  //返回成功
			switch (requestCode) {
			case PHOTO_WITH_CAMERA:  {//拍照获取图片
				String status = Environment.getExternalStorageState();
				if(status.equals(Environment.MEDIA_MOUNTED)) { //是否有SD卡
					
					Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+"/image.jpg");
					
					imgName = createPhotoFileName();
					//写一个方法将此文件保存到本应用下面啦
//	            	savePicture(imgName,bitmap);

	            	if (bitmap != null) {
						//为防止原始图片过大导致内存溢出，这里先缩小原图显示，然后释放原始Bitmap占用的内存
//						Bitmap smallBitmap = ImageTools.zoomBitmap(bitmap, bitmap.getWidth()/5 , bitmap.getHeight()/5 );
//						Intent intent=new Intent(HomeActivity.this,CameraActivity.class);
//			            intent.putExtra("bitmap", smallBitmap);
////						intent.putExtra("bitmap", Environment.getExternalStorageDirectory()+"/image.jpg");
//			            startActivity(intent);	
	            		Intent i = new Intent(HomeActivity.this, ImageShowActivity.class);
	        			i.putExtra(KEY_FILENAME, Environment.getExternalStorageDirectory()+"/image.jpg");
	        			startActivity(i);
	        			finish();
//						iv_temp.setImageBitmap(smallBitmap);
					}
//	            	Toast.makeText(HomeActivity.this, "已保存本应用的files文件夹下", Toast.LENGTH_LONG).show();
				}else {
					Toast.makeText(HomeActivity.this, "没有SD卡", Toast.LENGTH_LONG).show();
				}
				break;
			}
				case PHOTO_WITH_DATA:  {//从图库中选择图片
					ContentResolver resolver = getContentResolver();
					//照片的原始资源地址
					Uri originalUri = data.getData();  
					//System.out.println(originalUri.toString());  //" content://media/external/images/media/15838 "

//	                //将原始路径转换成图片的路径 
	                String selectedImagePath = uri2filePath(originalUri);  
	            	Logger.i("图片的路径","selectedImagePath="+ selectedImagePath);
	                System.out.println(selectedImagePath);  //" /mnt/sdcard/DCIM/Camera/IMG_20130603_185143.jpg "
					try {
						 //使用ContentProvider通过URI获取原始图片
						Bitmap photo = MediaStore.Images.Media.getBitmap(resolver, originalUri);
						
						imgName = createPhotoFileName();
		       			//写一个方法将此文件保存到本应用下面啦
		            	savePicture(imgName,photo);
		            	
		            	if (photo != null) {
							//为防止原始图片过大导致内存溢出，这里先缩小原图显示，然后释放原始Bitmap占用的内存
							Intent intent=new Intent(HomeActivity.this,CameraActivity.class);
				            Bundle bundle=new Bundle();
				            bundle.putString(TestCameraActivity.KEY_FILENAME, selectedImagePath);
//							intent.putExtra(TestCameraActivity.KEY_FILENAME, selectedImagePath);
				            bundle.putInt("Where", 1);
				            intent.putExtras(bundle);
				            startActivity(intent);
						}
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				break;
				}
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**从相册获取图片**/
	private void doPickPhotoFromGallery() {
		Intent intent = new Intent();
		intent.setType("image/*");  // 开启Pictures画面Type设定为image
		intent.setAction(Intent.ACTION_GET_CONTENT); //使用Intent.ACTION_GET_CONTENT这个Action 
		startActivityForResult(intent, PHOTO_WITH_DATA); //取得相片后返回到本画面
	}
	
	/**拍照获取相片**/
	private void doTakePhoto() {
	    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //调用系统相机
	   
	    Uri imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"image.jpg"));
		//指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
	    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
       
	    //直接使用，没有缩小  
        startActivityForResult(intent, PHOTO_WITH_CAMERA);  //用户点击了从相机获取
	}
	/**创建图片不同的文件名**/
	private String createPhotoFileName() {
		String fileName = "";
		Date date = new Date(System.currentTimeMillis());  //系统当前时间
		SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
		fileName = dateFormat.format(date) + ".jpg";
		return fileName;
	}
	
	 /**获取文件路径**/
	 public String uri2filePath(Uri uri)  
	    {  
	        String[] projection = {MediaStore.Images.Media.DATA };  
	        Cursor cursor = managedQuery(uri, projection, null, null, null);  
	        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);  
	        cursor.moveToFirst();  
	        String path =  cursor.getString(column_index);
	        return path;  
	    }  
	 
	 /**保存图片到本应用下**/
	 private void savePicture(String fileName,Bitmap bitmap) {
		   
		FileOutputStream fos =null;
		try {//直接写入名称即可，没有会被自动创建；私有：只有本应用才能访问，重新内容写入会被覆盖
			fos = HomeActivity.this.openFileOutput(fileName, Context.MODE_PRIVATE); 
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);// 把图片写入指定文件夹中
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(null != fos) {
					fos.close();
					fos = null;
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	 
	 @Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}
}
