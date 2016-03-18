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

	private static final int PHOTO_WITH_DATA = 18;  //��SD���еõ�ͼƬ
	private static final int PHOTO_WITH_CAMERA = 37;// ������Ƭ
	
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
		if(resultCode == RESULT_OK) {  //���سɹ�
			switch (requestCode) {
			case PHOTO_WITH_CAMERA:  {//���ջ�ȡͼƬ
				String status = Environment.getExternalStorageState();
				if(status.equals(Environment.MEDIA_MOUNTED)) { //�Ƿ���SD��
					
					Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+"/image.jpg");
					
					imgName = createPhotoFileName();
					//дһ�����������ļ����浽��Ӧ��������
//	            	savePicture(imgName,bitmap);

	            	if (bitmap != null) {
						//Ϊ��ֹԭʼͼƬ�������ڴ��������������Сԭͼ��ʾ��Ȼ���ͷ�ԭʼBitmapռ�õ��ڴ�
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
//	            	Toast.makeText(HomeActivity.this, "�ѱ��汾Ӧ�õ�files�ļ�����", Toast.LENGTH_LONG).show();
				}else {
					Toast.makeText(HomeActivity.this, "û��SD��", Toast.LENGTH_LONG).show();
				}
				break;
			}
				case PHOTO_WITH_DATA:  {//��ͼ����ѡ��ͼƬ
					ContentResolver resolver = getContentResolver();
					//��Ƭ��ԭʼ��Դ��ַ
					Uri originalUri = data.getData();  
					//System.out.println(originalUri.toString());  //" content://media/external/images/media/15838 "

//	                //��ԭʼ·��ת����ͼƬ��·�� 
	                String selectedImagePath = uri2filePath(originalUri);  
	            	Logger.i("ͼƬ��·��","selectedImagePath="+ selectedImagePath);
	                System.out.println(selectedImagePath);  //" /mnt/sdcard/DCIM/Camera/IMG_20130603_185143.jpg "
					try {
						 //ʹ��ContentProviderͨ��URI��ȡԭʼͼƬ
						Bitmap photo = MediaStore.Images.Media.getBitmap(resolver, originalUri);
						
						imgName = createPhotoFileName();
		       			//дһ�����������ļ����浽��Ӧ��������
		            	savePicture(imgName,photo);
		            	
		            	if (photo != null) {
							//Ϊ��ֹԭʼͼƬ�������ڴ��������������Сԭͼ��ʾ��Ȼ���ͷ�ԭʼBitmapռ�õ��ڴ�
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

	/**������ȡͼƬ**/
	private void doPickPhotoFromGallery() {
		Intent intent = new Intent();
		intent.setType("image/*");  // ����Pictures����Type�趨Ϊimage
		intent.setAction(Intent.ACTION_GET_CONTENT); //ʹ��Intent.ACTION_GET_CONTENT���Action 
		startActivityForResult(intent, PHOTO_WITH_DATA); //ȡ����Ƭ�󷵻ص�������
	}
	
	/**���ջ�ȡ��Ƭ**/
	private void doTakePhoto() {
	    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //����ϵͳ���
	   
	    Uri imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"image.jpg"));
		//ָ����Ƭ����·����SD������image.jpgΪһ����ʱ�ļ���ÿ�����պ����ͼƬ���ᱻ�滻
	    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
       
	    //ֱ��ʹ�ã�û����С  
        startActivityForResult(intent, PHOTO_WITH_CAMERA);  //�û�����˴������ȡ
	}
	/**����ͼƬ��ͬ���ļ���**/
	private String createPhotoFileName() {
		String fileName = "";
		Date date = new Date(System.currentTimeMillis());  //ϵͳ��ǰʱ��
		SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
		fileName = dateFormat.format(date) + ".jpg";
		return fileName;
	}
	
	 /**��ȡ�ļ�·��**/
	 public String uri2filePath(Uri uri)  
	    {  
	        String[] projection = {MediaStore.Images.Media.DATA };  
	        Cursor cursor = managedQuery(uri, projection, null, null, null);  
	        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);  
	        cursor.moveToFirst();  
	        String path =  cursor.getString(column_index);
	        return path;  
	    }  
	 
	 /**����ͼƬ����Ӧ����**/
	 private void savePicture(String fileName,Bitmap bitmap) {
		   
		FileOutputStream fos =null;
		try {//ֱ��д�����Ƽ��ɣ�û�лᱻ�Զ�������˽�У�ֻ�б�Ӧ�ò��ܷ��ʣ���������д��ᱻ����
			fos = HomeActivity.this.openFileOutput(fileName, Context.MODE_PRIVATE); 
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);// ��ͼƬд��ָ���ļ�����
			
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
