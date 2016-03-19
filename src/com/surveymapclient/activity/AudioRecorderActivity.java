package com.surveymapclient.activity;

import com.surveymapclient.common.FileUtils;
import com.surveymapclient.common.TimeUtils;
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
	 * 测试Bypang
	 */
	
	
	
	
	/**
	 * Status：录音初始状态
	 */
	private static final int STATUS_PREPARE = 0;
    
	/**
	 * Status：正在录音中
	 */
    private static final int STATUS_RECORDING = 1;
    
    /**
     * Status：暂停录音
     */
    private static final int STATUS_PAUSE = 2;
    
    /**
     * Status：播放初始状态
     */
    private static final int STATUS_PLAY_PREPARE = 3;
    
    /**
     * Status：播放中
     */
    private static final int STATUS_PLAY_PLAYING = 4;
    /**
     * Status：播放暂停
     */
    private static final int STATUS_PLAY_PAUSE = 5;
    
    private int status = STATUS_PREPARE;
    
    /**
     * 录音时间
     */
    private TextView tvRecordTime; 
    /**
     * 音频播放进度
     */
    private TextView tvPosition;
    
    /**
     * 录音按钮
     */
    private ImageView btnRecord;// 录音按钮
    /**
     * 最大录音长度
     */
    private static final int MAX_LENGTH = 300 * 1000;
	
    private Handler handler = new Handler();
    
    private Runnable runnable;
    /**
     * 试听界面
     */
    private LinearLayout layoutListen;
    
    /**
     * 音频录音的总长度
     */
    private static int voiceLength;
    /**
     * 录音长度
     */
    private TextView tvLength;
    
    
	
    AudioRecordModel audiorecordmodel;
    /**
     * 播放进度条
     */
    private SeekBar seekBar;
    /**
     * 音频播放类
     */
    private PlayerModel player;
    /**
     * 录音文件名
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
		tvRecordTime=(TextView) findViewById(R.id.tv_time);
		tvLength=(TextView) findViewById(R.id.tv_length);
		tvPosition=(TextView) findViewById(R.id.tv_position);
		layoutListen=(LinearLayout) findViewById(R.id.layout_listen);
		seekBar=(SeekBar) findViewById(R.id.seekbar_play);
		audioDelete=(Button) findViewById(R.id.audio_delete);
		audioCancelandaudio=(Button) findViewById(R.id.audio_cancle);
		audioSaveandPlay=(Button) findViewById(R.id.audio_save);
	}
	
	public void handleRecord(){
		switch(status){
		case STATUS_PREPARE:
			audiorecordmodel.startRecord();
			status = STATUS_RECORDING;
			voiceLength = 0;
			timing();
			break;
		case STATUS_RECORDING:
			pauseAudioRecord();		
			status = STATUS_PAUSE;
			break;
		case STATUS_PAUSE:
			audiorecordmodel.startRecord();
			status = STATUS_RECORDING;
			timing();
			break;
		case STATUS_PLAY_PREPARE:
			player.playUrl(FileUtils.getM4aFilePath(audioRecordFileName));
			status = STATUS_PLAY_PLAYING;
			break;
		case STATUS_PLAY_PLAYING:
			player.pause();
			status = STATUS_PLAY_PAUSE;
			break;
		case STATUS_PLAY_PAUSE:
			player.play();
			status = STATUS_PLAY_PLAYING;
			break;
		}
	}
	/**
	 * 暂停录音
	 */
	public void pauseAudioRecord(){
		audiorecordmodel.pauseRecord();
		if (handler != null && runnable != null) {
			handler.removeCallbacks(runnable);
			runnable = null;
		}
	}
	
	/**
	 * 停止录音
	 */
	public void stopAudioRecord(){
		pauseAudioRecord();
		audiorecordmodel.stopRecord();
		status = STATUS_PLAY_PREPARE;
		showListen();
	}
	/**
	 * 计时功能
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
	 * 显示播放界面
	 */
	private void showListen() {

		tvRecordTime.setVisibility(View.GONE);
		seekBar.setProgress(0);
		tvPosition.setText("00:00");

		
	}
	/**
	 * 
	 * SeekBar进度条改变事件监听类
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
		player.stop();
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.audio_delete) {
			
		}else if(id==R.id.audio_cancle){
			finish();
		}else if(id==R.id.audio_save){
			handleRecord();
		}
		
	}
}
