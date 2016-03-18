package com.surveymapclient.model;

import java.io.IOException;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.surveymapclient.common.TimeUtils;
import com.surveymapclient.impl.MyPlayerCallback;

import android.R.menu;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Handler;
import android.widget.MediaController.MediaPlayerControl;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * 播放音频。
 * 
 */

public class PlayerModel implements OnBufferingUpdateListener,
	OnCompletionListener,MediaPlayer.OnPreparedListener{
	
	public MediaPlayer mediaplayer;
	SeekBar sbkProgress;
	TextView tvPosition;
	MyPlayerCallback callback;
	private static final int PERIOD = 500;
	public int currentPosition = 0;
	
	/**
	 * 线程池。
	 */
	private ExecutorService service=Executors.newFixedThreadPool(10);
	
	public PlayerModel(SeekBar seekBar,TextView textView) {
		// TODO Auto-generated constructor stub
		this.sbkProgress=seekBar;
		this.tvPosition=textView;
		createMedioplayer();
	}
	
	/**
	 * 创建MediaPlayer
	 */
	private void createMedioplayer(){
		try {
			mediaplayer = new MediaPlayer();
			mediaplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaplayer.setOnBufferingUpdateListener(this);
			mediaplayer.setOnPreparedListener(this);
			mediaplayer.setOnCompletionListener(this);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	/**
	 * 通过定时器和Handler来更新进度条.
	 */
	TimerTask mTimeTask=new TimerTask() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			if (mediaplayer == null)
				return;
			if (mediaplayer != null && mediaplayer.isPlaying()
					&& sbkProgress.isPressed() == false) {
			}
		}
	};
	
	Runnable runnable;
	final Handler handler=new Handler();
	/**
	 * 计时 TODO
	 */
	private void timing(){
		runnable=new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (mediaplayer == null)
					return;
				if (mediaplayer != null && mediaplayer.isPlaying()
						&& sbkProgress.isPressed() == false) {
					setSeekBarProgress();
				}
				handler.postDelayed(this, PERIOD);			
			}
		};
		handler.postDelayed(runnable, 0);
	}

	public void setSeekBarProgress(){
		int position = mediaplayer.getCurrentPosition();
		int duration = mediaplayer.getDuration();
		currentPosition = position;
		tvPosition.setText(TimeUtils.convertMilliSecondToMinute2(position));
		if (duration >= 0) {
			long pos = sbkProgress.getMax() * position / duration;
			sbkProgress.setProgress((int) pos);
		}
	}
	
	public void setMyPlayerCallback(MyPlayerCallback callback ){
		this.callback=callback;
	}
	public void play() {
		timing();
		mediaplayer.start();
	}
	/**
	 * 新开一个线程，播放音频。
	 * 
	 * @param videoUrl
	 */
	public void playUrl(final String videoUrl) {
		if (mediaplayer == null) {
			createMedioplayer();
		}
		timing();
		service.submit(new Runnable() {
			@Override
			public void run() {
				try {
					mediaplayer.reset();
					mediaplayer.setDataSource(videoUrl);
					mediaplayer.prepare();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}
	public void pause() {
		handler.removeCallbacks(runnable);
		mediaplayer.pause();
	}

	public void stop() {
		handler.removeCallbacks(runnable);
		if (mediaplayer != null) {
			mediaplayer.stop();
			mediaplayer.release();
			mediaplayer = null;
		}
	}
	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		// TODO Auto-generated method stub
		if (callback != null) {
			callback.onCompletion();
		}
		mediaplayer.stop();
	}

	/**  
	 * 通过onPrepared播放  
	 */
	@Override
	public void onPrepared(MediaPlayer mp) {
		// TODO Auto-generated method stub
		mp.start();
		if (callback != null) {
			callback.onPrepared();
		}
	}

}
