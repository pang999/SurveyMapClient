package com.surveymapclient.activity;

import com.surveymapclient.pdf.PDFSharer;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.handler.EmailHandler;
import com.umeng.socialize.media.UMImage;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

public class ShareActivity extends Activity implements OnClickListener {

	ImageView email,weixin;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		/*setContentView(R.layout.activity_share);*/
	/*	email=(ImageView) findViewById(R.id.share_email);
		weixin=(ImageView) findViewById(R.id.share_weixin);
		email.setOnClickListener(this);
		weixin.setOnClickListener(this);*/
//		new PDFSharer().share(this, "/sdcard/surveymap/pdf/1460690565111.pdf");
	}
	@Override
	public void onClick(View v) {
	/*	EmailHandler emailHandler = new EmailHandler();
		emailHandler.addToSocialSDK();
        // 邮件  
        MailShareContent mail = new MailShareContent(urlImage);  
        mail.setTitle(title);  
        mail.setShareContent(content);  
        mail.set
        // 设置tencent分享内容  
        mController.setShareMedia(mail);  
		*/
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.share_email:
//			  new ShareAction(this).setPlatform(SHARE_MEDIA.WEIXIN).setCallback(umShareListener)
//              .withText("hello umeng")
//              .withTargetUrl("http://dev.umeng.com")
//             .share();
			break;

		case R.id.share_weixin:

//			 new ShareAction(this).setPlatform(SHARE_MEDIA.EMAIL).setCallback(umShareListener)
//             .withText("hello umeng")
//             .withTitle("dddddd")
//  
//             .share();
			break;
		}
		
	}
    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {

            Toast.makeText(ShareActivity.this, platform + "分享成功！", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(ShareActivity.this,platform + "分享失败！", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(ShareActivity.this,platform + "分享取消！", Toast.LENGTH_SHORT).show();
        }
    };
}
