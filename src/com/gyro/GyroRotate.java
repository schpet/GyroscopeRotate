package com.gyro;

/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.admob.android.ads.AdView;
import com.admob.android.ads.InterstitialAd;
import com.admob.android.ads.InterstitialAd.Event;
import com.admob.android.ads.InterstitialAdListener;

/**
 * Wrapper activity demonstrating the use of {@link GLSurfaceView}, a view that
 * uses OpenGL drawing into a dedicated surface.
 * 
 * Shows: + How to redraw in response to user input.
 */
public class GyroRotate extends Activity implements SensorEventListener,
		InterstitialAdListener {
	private SensorManager sensorManager = null;
	private TouchSurfaceView mGLSurfaceView;
	private InterstitialAd mInterstitialAd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().clearFlags(
				WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		// Create our Preview view and set it as the content of our
		// Activity
		mGLSurfaceView = new TouchSurfaceView(this);
		
		setContentView(mGLSurfaceView);

		// Setup layout parameters
		RelativeLayout.LayoutParams params;
		params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT);

		// Create a linear layout
		RelativeLayout layout = new RelativeLayout(this);

		ImageView iv = new ImageView(this);
		iv.setImageResource(R.drawable.logo);
		
		layout.addView(iv);
		
		AdView mAdView = new AdView(this);
		mAdView.setVerticalGravity(Gravity.BOTTOM);
		
		layout.addView(mAdView, params);

		addContentView(layout, new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));

		mGLSurfaceView.requestFocus();
		mGLSurfaceView.setFocusableInTouchMode(true);

		mInterstitialAd = new InterstitialAd(Event.APP_START, this);
		mInterstitialAd.requestAd(this);
	}

	@Override
	protected void onResume() {
		// Ideally a game should implement onResume() and onPause()
		// to take appropriate action when the activity looses focus
		super.onResume();

		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
				SensorManager.SENSOR_DELAY_GAME);

		mGLSurfaceView.onResume();
	}

	@Override
	protected void onPause() {
		// Ideally a game should implement onResume() and onPause()
		// to take appropriate action when the activity looses focus
		super.onPause();
		mGLSurfaceView.onPause();
		sensorManager.unregisterListener(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		sensorManager.unregisterListener(this);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		y += event.values[0];
		x += event.values[1];
		z += event.values[2];

		mGLSurfaceView.updateGyro(x, y, z);

		if (DEBUG && i++ % 50 == 0)
			Log.i("gyro", "x=" + x + " y=" + y + " z=" + z);
	}

	int i = 0;
	private static boolean DEBUG = false;

	private float x, y, z = 0;

	@Override
	public void onFailedToReceiveInterstitial(InterstitialAd interstitialAd) {
		if (interstitialAd == mInterstitialAd) {
			// Do something?
		}
	}

	@Override
	public void onReceiveInterstitial(InterstitialAd interstitialAd) {
		if (interstitialAd == mInterstitialAd) {
			mInterstitialAd.show(this);
		}
	}
	
	
}

