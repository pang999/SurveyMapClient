package com.surveymapclient.pdf;

import java.io.File;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

/**
 * @Description(描述): PDF分享者
 * @Package(包名): com.surveymapclient.pdf
 * @ClassName(类名): PDFSharer
 * @author(作者): Pang
 * @date(时间): 2016-4-13 下午1:55:31
 * @version(版本): V1.0
 */
public class PDFSharer {

	/**
	 * @Description（描述）: 调用此函数分析pdf文件application/pdf
	 * 
	 *      Example: Actually it is very easy to use,when you want
	 *               to share a pdf file,call like this:
	 *               new PDFSharer().share(this, "/sdcard/aa.pdf");
	 * 
	 *                   void
	 * @author Pang
	 * @date 2016-4-13 下午1:56:13
	 */
	private ProgressDialog mProgressDialog = null;
	public PDFSharer(Context context) {
		// TODO Auto-generated constructor stub
		mProgressDialog = new ProgressDialog(context);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.setCanceledOnTouchOutside(false);
		mProgressDialog.setCancelable(false);
		mProgressDialog.setTitle("提示");
		mProgressDialog.setMessage("正在保存数据，请耐心等候......");	
	}
	public void share(Context context, String path) {
		if (path==null||"".equals(path)||!new File(path).exists()) {
			Toast.makeText(context, "PDF文件不存在!", Toast.LENGTH_SHORT).show();
			return;
		}
		context.startActivity(Intent.createChooser(
				new Intent()
						.setAction(Intent.ACTION_SEND)
						.putExtra(Intent.EXTRA_STREAM,
								Uri.fromFile(new File(path)))
						.setType("application/pdf"), "分享到:"));

	}
	
	public void showProgressDialog(){
		mProgressDialog.show();
	}
	
	public void dismissProgressDialog(){
		if(mProgressDialog.isShowing()){
			mProgressDialog.cancel();
			mProgressDialog.dismiss();
		}
	}

}