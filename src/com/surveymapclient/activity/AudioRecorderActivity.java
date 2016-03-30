package com.surveymapclient.activity;

import com.surveymapclient.common.FileUtils;
import com.surveymapclient.common.IToast;
import com.surveymapclient.common.TimeUtils;
import com.surveymapclient.impl.MyPlayerCallback;
import com.surveymapclient.model.AudioRecordModel;
import com.surveymapclient.model.PlayerModel;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
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
    
    Button audioDelete,audioCancelandaudio,audioSaveandPlay;   
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_audiorecorder);	
		initView();
	}
	
	private void initView(){
		//��Ƶ¼�����ļ�����
		audioRecordFileName = TimeUtils.getTimestamp();
		audiorecordmodel=new AudioRecordModel(this, audioRecordFileName);
		
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
		
		audioCancelandaudio.setText("¼��");		
		
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
	public void AudioRecordHandle(){
		switch(status){
		case STATUS_PREPARE://¼��ǰ��ʼ��
			audiorecordmodel.startRecord();
			status = STATUS_RECORDING;			
			voiceLength = 0;
			audioCancelandaudio.setText("��ͣ");
			timing();
			break;
		case STATUS_RECORDING://����¼��....
			pauseAudioRecord();		
			status = STATUS_PAUSE;
			audioCancelandaudio.setText("��ͣ");
			break;
		case STATUS_PAUSE://¼����ͣ....
			audiorecordmodel.startRecord();
			status = STATUS_RECORDING;
			audioCancelandaudio.setText("¼��");
			timing();
			break;
		}
	}
	public void RecordPlayHandle(){
		switch(status){
		case STATUS_PLAY_PREPARE://���ų�ʼ��.....
			player.playUrl(FileUtils.getM4aFilePath(audioRecordFileName));
			status = STATUS_PLAY_PLAYING;
			break;
		case STATUS_PLAY_PLAYING://���ڲ���.....
			player.pause();
			status = STATUS_PLAY_PAUSE;
			break;
		case STATUS_PLAY_PAUSE://������ͣ
			player.play();
			status = STATUS_PLAY_PLAYING;
			break;
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
		status = STATUS_PLAY_PREPARE;
		showListen();
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
	 * ��ʾ���Ž���
	 */
	private void showListen() {
		tvRecordTime.setVisibility(View.GONE);
		seekBar.setProgress(0);
		tvPosition.setText("00:00");		
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
			this.finish();
		}else if(id==R.id.audio_cancle){
			if (audioCancelandaudio.getText().toString()=="ȡ��") {
				IToast.show(this, "ȡ��");
				this.finish();
			}else {
				AudioRecordHandle();
			}		
		}else if(id==R.id.audio_save){
			if (audioSaveandPlay.getText().toString()=="����") {
				 IToast.show(this, "ֹͣ������");
				 stopAudioRecord();	
			}
			if (audioSaveandPlay.getText().toString()=="����") {
				IToast.show(this, "����");
			}
		}
		
	}
}
