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
		//音频录音的文件名称
		audioRecordFileName = TimeUtils.getTimestamp();
		audiorecordmodel=new AudioRecordModel(this, audioRecordFileName);
		
		tvRecordTime=(TextView) findViewById(R.id.tv_time);//录音时间
		tvLength=(TextView) findViewById(R.id.tv_length);//录音(播音)总长度
		tvPosition=(TextView) findViewById(R.id.tv_position);//播放进度
		layoutListen=(LinearLayout) findViewById(R.id.layout_listen);//播放界面
		seekBar=(SeekBar) findViewById(R.id.seekbar_play);//播放进度条
		
		audioDelete=(Button) findViewById(R.id.audio_delete);
		audioCancelandaudio=(Button) findViewById(R.id.audio_cancle);
		audioSaveandPlay=(Button) findViewById(R.id.audio_save);
		
		audioCancelandaudio.setOnClickListener(this);
		audioDelete.setOnClickListener(this);
		audioSaveandPlay.setOnClickListener(this);
		
		audioCancelandaudio.setText("录音");		
		
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
		case STATUS_PREPARE://录音前初始化
			audiorecordmodel.startRecord();
			status = STATUS_RECORDING;			
			voiceLength = 0;
			audioCancelandaudio.setText("暂停");
			timing();
			break;
		case STATUS_RECORDING://正在录音....
			pauseAudioRecord();		
			status = STATUS_PAUSE;
			audioCancelandaudio.setText("暂停");
			break;
		case STATUS_PAUSE://录音暂停....
			audiorecordmodel.startRecord();
			status = STATUS_RECORDING;
			audioCancelandaudio.setText("录音");
			timing();
			break;
		}
	}
	public void RecordPlayHandle(){
		switch(status){
		case STATUS_PLAY_PREPARE://播放初始化.....
			player.playUrl(FileUtils.getM4aFilePath(audioRecordFileName));
			status = STATUS_PLAY_PLAYING;
			break;
		case STATUS_PLAY_PLAYING://正在播放.....
			player.pause();
			status = STATUS_PLAY_PAUSE;
			break;
		case STATUS_PLAY_PAUSE://播放暂停
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
			if (audioCancelandaudio.getText().toString()=="取消") {
				IToast.show(this, "取消");
				this.finish();
			}else {
				AudioRecordHandle();
			}		
		}else if(id==R.id.audio_save){
			if (audioSaveandPlay.getText().toString()=="保存") {
				 IToast.show(this, "停止并保存");
				 stopAudioRecord();	
			}
			if (audioSaveandPlay.getText().toString()=="播放") {
				IToast.show(this, "播放");
			}
		}
		
	}
}
