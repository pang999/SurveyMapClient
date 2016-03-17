package com.surveymapclient.activity;

import org.achartengine.GraphicalView;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

public class GraphicalActivity extends Activity {

	private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
	/** The chart view that displays the data. */
	GraphicalView mGraphcalView;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_graphical);
		
		// set some properties on the main renderer
		mRenderer.setApplyBackgroundColor(true);
		mRenderer.setBackgroundColor(Color.WHITE);
		mRenderer.setShowGrid(true);
		mRenderer.setAxesColor(Color.GRAY);
		
	}
}
