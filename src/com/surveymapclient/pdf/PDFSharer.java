package com.surveymapclient.pdf;

import java.io.File;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

/**
 * @Description(����): PDF������
 * @Package(����): com.surveymapclient.pdf
 * @ClassName(����): PDFSharer
 * @author(����): Pang
 * @date(ʱ��): 2016-4-13 ����1:55:31
 * @version(�汾): V1.0
 */
public class PDFSharer {

	/**
	 * @Description��������: ���ô˺�������pdf�ļ�application/pdf
	 * 
	 *      Example: Actually it is very easy to use,when you want
	 *               to share a pdf file,call like this:
	 *               new PDFSharer().share(this, "/sdcard/aa.pdf");
	 * 
	 *                   void
	 * @author Pang
	 * @date 2016-4-13 ����1:56:13
	 */
	private ProgressDialog mProgressDialog = null;
	public PDFSharer(Context context) {
		// TODO Auto-generated constructor stub
		mProgressDialog = new ProgressDialog(context);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.setCanceledOnTouchOutside(false);
		mProgressDialog.setCancelable(false);
		mProgressDialog.setTitle("��ʾ");
		mProgressDialog.setMessage("���ڱ������ݣ������ĵȺ�......");	
	}
	public void share(Context context, String path) {
		if (path==null||"".equals(path)||!new File(path).exists()) {
			Toast.makeText(context, "PDF�ļ�������!", Toast.LENGTH_SHORT).show();
			return;
		}
		context.startActivity(Intent.createChooser(
				new Intent()
						.setAction(Intent.ACTION_SEND)
						.putExtra(Intent.EXTRA_STREAM,
								Uri.fromFile(new File(path)))
						.setType("application/pdf"), "����:"));

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