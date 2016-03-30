package com.surveymapclient.activity.fragment;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.surveymapclient.activity.CameraActivity;
import com.surveymapclient.activity.DefineActivity;
import com.surveymapclient.activity.HomeActivity;
import com.surveymapclient.activity.R;
import com.surveymapclient.activity.TestCameraActivity;
import com.surveymapclient.common.Logger;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

public class HomeFragment extends Fragment implements OnClickListener{

	ImageView define,takephoto,ablum;
	private static final int PHOTO_WITH_DATA = 18;  //从SD卡中得到图片
	private static final int PHOTO_WITH_CAMERA = 37;// 拍摄照片
	private String imgName = "";
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view=inflater.inflate(R.layout.fragment_home, container, false);
		define=(ImageView) view.findViewById(R.id.define);
		takephoto=(ImageView) view.findViewById(R.id.takephoto);
		ablum=(ImageView) view.findViewById(R.id.ablum);
		define.setOnClickListener(this);
		takephoto.setOnClickListener(this);
		ablum.setOnClickListener(this);
		return view;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(resultCode == getActivity().RESULT_OK) {  //返回成功
			if (requestCode==PHOTO_WITH_DATA) {
				ContentResolver resolver = getActivity().getContentResolver();
				//照片的原始资源地址
				Uri originalUri = data.getData();  
				//System.out.println(originalUri.toString());  //" content://media/external/images/media/15838 "

//                //将原始路径转换成图片的路径 
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
						Intent intent=new Intent(getActivity(),CameraActivity.class);
			            Bundle bundle=new Bundle();
			            bundle.putString(TestCameraActivity.KEY_FILENAME, selectedImagePath);
			            bundle.putInt("Where", 1);
			            intent.putExtras(bundle);
			            startActivity(intent);
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.define:
			Intent defineintent=new Intent(getActivity(),DefineActivity.class);
			startActivity(defineintent);	
			break;
		case R.id.takephoto:
			Intent takeintent=new Intent(getActivity(),TestCameraActivity.class);
			startActivity(takeintent);
			break;
		case R.id.ablum:
			doPickPhotoFromGallery();
			break;
		}
	}
	/**从相册获取图片**/
	private void doPickPhotoFromGallery() {
		Intent intent = new Intent();
		intent.setType("image/*");  // 开启Pictures画面Type设定为image
		intent.setAction(Intent.ACTION_GET_CONTENT); //使用Intent.ACTION_GET_CONTENT这个Action 
		startActivityForResult(intent, PHOTO_WITH_DATA); //取得相片后返回到本画面
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
	        Cursor cursor = getActivity().managedQuery(uri, projection, null, null, null);  
	        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);  
	        cursor.moveToFirst();  
	        String path =  cursor.getString(column_index);
	        return path;  
	    }  
	 
	 /**保存图片到本应用下**/
	 private void savePicture(String fileName,Bitmap bitmap) {
		   
		FileOutputStream fos =null;
		try {//直接写入名称即可，没有会被自动创建；私有：只有本应用才能访问，重新内容写入会被覆盖
			fos = getActivity().openFileOutput(fileName, Context.MODE_PRIVATE); 
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
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}
}
