package com.surveymapclient.activity;
import java.io.IOException;

import com.surveymapclient.common.Contants;
import com.surveymapclient.common.FileUtils;
import com.surveymapclient.common.IToast;
import com.surveymapclient.common.Logger;
import com.surveymapclient.common.TimeUtils;
import com.surveymapclient.impl.MyPlayerCallback;
import com.surveymapclient.model.AudioRecordModel;
import com.surveymapclient.model.PlayerModel;

import android.app.Activity;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.SyncStateContract.Constants;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class AudioRecorderActivity extends Activity implements OnClickListener {

	
	/**
	 * ����Bypang
	 */
	
	
	
	
	/**
	 * Status��¼����ʼ״̬
	 */
	private static final int STATUS_PREPARE = 0;
    
	/**
	 * Status������¼����
	 */
    private static final int STATUS_RECORDING = 1;
    
    /**
     * Status����ͣ¼��
     */
    private static final int STATUS_PAUSE = 2;
    
    /**
     * Status�����ų�ʼ״̬
     */
    private static final int STATUS_PLAY_PREPARE = 3;
    
    /**
     * Status��������
     */
    private static final int STATUS_PLAY_PLAYING = 4;
    /**
     * Status��������ͣ
     */
    private static final int STATUS_PLAY_PAUSE = 5;
    
    private int status = STATUS_PREPARE;
    /**
     * ¼��ʱ��
     */
    private TextView tvRecordTime; 
    /**
     * ��Ƶ���Ž���
     */
    private TextView tvPosition;
    
    /**
     * ¼����ť
     */
    private ImageView btnRecord;// ¼����ť
    /**
     * ���¼������
     */
    private static final int MAX_LENGTH = 300 * 1000;
	
    private Handler handler = new Handler();
    
    private Runnable runnable;
    /**
     * ��������
     */
    private LinearLayout layoutListen;
    
    /**
     * ��Ƶ¼�����ܳ���
     */
    private static int voiceLength;
    /**
     * ¼������
     */
    private TextView tvLength;
    
    
	
    AudioRecordModel audiorecordmodel;
    /**
     * ���Ž�����
     */
    private SeekBar seekBar;
    /**
     * ��Ƶ������
     */
    private PlayerModel player;
    /**
     * ¼���ļ���
     */
    private String audioRecordFileName;
    
    private int type=-1;
    boolean isfirst=true;
    private String url="";
    private int audiolenght=0;
    Button audioDelete,audioCancelandaudio,audioSaveandPlay;   
  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_audiorecorder);
		Bundle bundle=getIntent().getExtras();		
	    type=bundle.getInt("TYPE");
	    if (type==1) {
	    	url=bundle.getString("URL");
	    	audiolenght=bundle.getInt("Len");
	    	Logger.i("¼��", "url="+url);
			
		}
	    Logger.i("¼��", ""+type);
		initView();
		initPlay();
		initRecord();
	}
	
	private void initView(){
				
		tvRecordTime=(TextView) findViewById(R.id.tv_time);//¼��ʱ��
		tvLength=(TextView) findViewById(R.id.tv_length);//¼��(����)�ܳ���
		tvPosition=(TextView) findViewById(R.id.tv_position);//���Ž���
		layoutListen=(LinearLayout) findViewById(R.id.layout_listen);//���Ž���
		seekBar=(SeekBar) findViewById(R.id.seekbar_play);//���Ž�����
		
		audioDelete=(Button) findViewById(R.id.audio_delete);
		audioCancelandaudio=(Button) findViewById(R.id.audio_cancle);
		audioSaveandPlay=(Button) findViewById(R.id.audio_save);
		
		audioCancelandaudio.setOnClickListener(this);
		audioDelete.setOnClickListener(this);
		audioSaveandPlay.setOnClickListener(this);			
		seekBar.setOnSeekBarChangeListener(new SeekBarChangeEvent());
		seekBar.setEnabled(false);
		player=new PlayerModel(seekBar, tvPosition);
		player.setMyPlayerCallback(new MyPlayerCallback() {
			
			@Override
			public void onPrepared() {
				// TODO Auto-generated method stub
				seekBar.setEnabled(true);
			}
			
			@Override
			public void onCompletion() {
				// TODO Auto-generated method stub
				status = STATUS_PLAY_PREPARE;
				seekBar.setEnabled(false);
				seekBar.setProgress(0);
				tvPosition.setText("00:00");
			}
		});
	}
	/**
	 * ¼����ʼ��
	 */
	private void initRecord(){
		if (type==0) {
			//��Ƶ¼�����ļ�����
			audioRecordFileName = TimeUtils.getTimestamp();
			audiorecordmodel=new AudioRecordModel(this, audioRecordFileName);
			audioDelete.setText("����");	
			audioCancelandaudio.setText("¼��");
			audioSaveandPlay.setText("����");
		}				
	}
	/**
	 * ���ų�ʼ��
	 */
	private void initPlay(){
		if (type==1) {
			audioDelete.setText("����");	
			audioCancelandaudio.setText("����");
			audioSaveandPlay.setText("ȡ��");
			status=STATUS_PLAY_PREPARE;			
			tvRecordTime.setVisibility(View.GONE);
			layoutListen.setVisibility(View.VISIBLE);
			tvLength.setText(TimeUtils.convertMilliSecondToMinute2(audiolenght));
			seekBar.setProgress(0);
			tvPosition.setText("00:00");
		}		
	}
	/**
	 * ��ͣ¼��
	 */
	public void pauseAudioRecord(){
		audiorecordmodel.pauseRecord();
		if (handler != null && runnable != null) {
			handler.removeCallbacks(runnable);
			runnable = null;
		}
	}
	
	/**
	 * ֹͣ¼��
	 */
	public void stopAudioRecord(){
		pauseAudioRecord();
		audiorecordmodel.stopRecord();
	}
	/**
	 * ��ʱ����
	 */
	private void timing() {
		runnable = new Runnable() {
			@Override
			public void run() {
				voiceLength += 100;
				if (voiceLength >= (MAX_LENGTH - 10 * 1000)) {
					tvRecordTime.setTextColor(getResources().getColor(
							R.color.red_n));
				} else {
					tvRecordTime.setTextColor(Color.WHITE);
				}
				if (voiceLength > MAX_LENGTH) {
					stopAudioRecord();				
				} else {
					tvRecordTime.setText(TimeUtils.convertMilliSecondToMinute2(voiceLength));
					handler.postDelayed(this, 100);
				}
			}
		};
		handler.postDelayed(runnable, 100);
	}

	/**
	 * 
	 * SeekBar�������ı��¼�������
	 */
	class SeekBarChangeEvent implements SeekBar.OnSeekBarChangeListener{

		int progress;
		
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			// TODO Auto-generated method stub
			if (null != player && player.mediaplayer != null) {
				this.progress = progress * player.mediaplayer.getDuration()
						/ seekBar.getMax();
				tvPosition.setText(TimeUtils
						.convertMilliSecondToMinute2(player.currentPosition));
			}
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			if (player.mediaplayer != null) {
				player.mediaplayer.seekTo(progress);
			}
		}
		
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (player!=null) {
			player.stop();
		}		
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.audio_delete) {
			if (type==0) {
				this.finish();
			}else if (type==1) {
				Bundle bundle=new Bundle();
				bundle.putInt("BackAudio", 1);
				AudioRecorderActivity.this.setResult(RESULT_OK, getIntent().putExtras(bundle));			
				this.finish();
			}
		}else if(id==R.id.audio_cancle){
			if (type==0) {
				AudioRecordHandle();
			}else if (type==1) {
				RecordPlayHandle();
			}	
		}else if(id==R.id.audio_save){
			if (type==0) {
				stopAudioRecord();
				audioSaveandPlay.setText("ȷ��");
				type=100;
				
			}else if (type==1) {
				this.finish();
			}else if (audiorecordmodel.complite==1) {	
//				IToast.show(this, "ȷ��");
				Bundle bundle=new Bundle();
				bundle.putInt("BackAudio", 0);
				bundle.putInt("AudioLen", voiceLength);
				bundle.putString("AudioUrl", audioRecordFileName);
				AudioRecorderActivity.this.setResult(RESULT_OK, getIntent().putExtras(bundle));			
				this.finish();
			}
		}
		
	}
	
	public void AudioRecordHandle(){
		switch(status){
		case STATUS_PREPARE:
			audiorecordmodel.startRecord();
			status = STATUS_RECORDING;
			audioCancelandaudio.setText("��ͣ");
			voiceLength = 0;
			timing();
			break;
		case STATUS_RECORDING:
			pauseAudioRecord();
			audioCancelandaudio.setText("¼��");
			status = STATUS_PAUSE;
			break;
		case STATUS_PAUSE:
			audiorecordmodel.startRecord();
			audioCancelandaudio.setText("��ͣ");
			status = STATUS_RECORDING;
			timing();
			break;
		}
	}
	public void RecordPlayHandle(){
		switch(status){
		case STATUS_PLAY_PREPARE:
			player.playUrl(FileUtils.getM4aFilePath(url));
			Log.i("¼��", "Audio="+FileUtils.getM4aFilePath(url));
			audioCancelandaudio.setText("��ͣ");
			status = STATUS_PLAY_PLAYING;
			break;
		case STATUS_PLAY_PLAYING:
			player.pause();
			audioCancelandaudio.setText("����");
			status = STATUS_PLAY_PAUSE;
			break;
		case STATUS_PLAY_PAUSE:
			player.play();
			audioCancelandaudio.setText("��ͣ");
			status = STATUS_PLAY_PLAYING;
			break;
		}
	}
}
