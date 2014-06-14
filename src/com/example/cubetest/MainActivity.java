package com.example.cubetest;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.view.Menu;

public class MainActivity extends Activity {

	private MyGLSurfaceView view;
	
	@Override 
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		view = new MyGLSurfaceView(this);
	
		setContentView(view);
	}
	
	protected void onPause(){
		super.onPause();
		view.preventLock();
		//view.onPause();
	}
	
	protected void onResume(){
		super.onResume();
		view.onResume();
	}

}
