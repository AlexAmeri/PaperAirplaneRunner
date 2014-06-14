package com.example.cubetest;

import android.content.Context;
import android.hardware.SensorEvent;
import android.opengl.GLSurfaceView;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

public class MyGLSurfaceView extends GLSurfaceView {
	private int width;
	private int height;
	private float mPreviousX;
	private float mPreviousY;
	public float dx;
	public float movX;

	
	public MyGLSurfaceView(Context context){
        super(context);

        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(new OGLRenderer(context));
        
        //Get Screen data
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
    	width = metrics.widthPixels;
    	height = metrics.heightPixels;	
    }
	
	public static void preventLock(){
		OGLRenderer.menPress=-1000;
	}
	
	
	
	@Override
	public boolean onTouchEvent(MotionEvent event){
		//User input location
		float x = event.getX();
		float y = event.getY();
		
		//If the user is playing the game
		if(OGLRenderer.gameState==1||OGLRenderer.gameState==9||OGLRenderer.gameState==10||OGLRenderer.gameState==11){
		switch (event.getAction()) {
        case MotionEvent.ACTION_MOVE:

        	dx=(x-mPreviousX);
            
            
            float dy = y - mPreviousY;

            if(OGLRenderer.posx>-200f&&OGLRenderer.posx<200){
           // OGLRenderer.oldPosx=OGLRenderer.posx;
            //OGLRenderer.posx-=dx*.3f;
            OGLRenderer.dx=-dx;
            }
            else if(OGLRenderer.posx<=-200f&&dx<0){
           // OGLRenderer.oldPosx=OGLRenderer.posx;
           //OGLRenderer.posx-=dx*.3f;
            OGLRenderer.dx=-dx;
            }
            else if(OGLRenderer.posx>=200f&&dx>0){
            	//OGLRenderer.oldPosx=OGLRenderer.posx;
                //OGLRenderer.posx-=dx*.3f;
            	OGLRenderer.dx=-dx;
            }
    }

    mPreviousX = x;
    mPreviousY = y;
		

	  
		
	}
	
	
	//Initial main menu
	if(OGLRenderer.frameCount-OGLRenderer.menPress>=30){
	 OGLRenderer.menPress=OGLRenderer.frameCount;	
	
	 if(OGLRenderer.gameState==0){
		//Check if in bounds
		if(x<(width*2f/3f)&&x>(width/3)){
			//Check what was pressed
			if((y<height/2f)&&(y>height*.3f)){
				//Go to "how to play" screen
				OGLRenderer.gameState=2;
			}
			
			if((y<height*.75f)&&(y>height/2f)){
				//Go to "about" screen
				OGLRenderer.gameState=3;
			}
			if((y<height)&&(y>.75f*height)){
				//Go to "choose level" screen
				OGLRenderer.gameState=4;
			}
			
		}
		
	}
	
	//Choose level menu
	else if(OGLRenderer.gameState==4){
		//Check if in bounds
		if(x<(width*5f/6f)&&x>(width/6f)){
			//Check what was pressed
			if((y<height*(180f/400f))&&(y>height*.25f)){
				//Go to "Delta Run" screen
				OGLRenderer.gameState=5;
			}
			
			if((y<height*(230f/400f))&&(y>height*(180f/400f))){
				//Go to "Delta Slalom" screen
				OGLRenderer.gameState=6;
			}
			if((y<height*(280f/400f))&&(y>height*(230f/400f))){
				//Go to "Delta Hero" screen
				OGLRenderer.gameState=7;
			}
			if((y<height*(320f/400f))&&(y>height*(280f/400f))){
				//Go to "Delta Venom" screen
				OGLRenderer.gameState=8;
			}
			
		}
		
		if(x<.4f*width&&y>.6f*height){
			//Return to main menu
			OGLRenderer.gameState=0;
			}
		
	}
	
		
	//About and howto menus
	else if((OGLRenderer.gameState==7)||(OGLRenderer.gameState==8)||(OGLRenderer.gameState==5)||(OGLRenderer.gameState==6)){
		if(x<.4f*width&&y>.6f*height){
		//Return to choose game menu
		OGLRenderer.gameState=4;
		}
		
		if((OGLRenderer.gameState==5)&&x>.6f*width&&y>.6f*height){
			OGLRenderer.playerScore=0;
			OGLRenderer.lives=4;
			OGLRenderer.initiate=false;
			OGLRenderer.frameCount=0;
			OGLRenderer.checkIndex=1;
			OGLRenderer.gameState=10;
			
		}
		
		if((OGLRenderer.gameState==6)&&x>.6f*width&&y>.6f*height){
			OGLRenderer.playerScore=0;
			OGLRenderer.lives=4;
			OGLRenderer.frameCount=0;
			OGLRenderer.initiate=false;
			OGLRenderer.checkIndex=1;
			OGLRenderer.gameState=9;
			
		}
		
		if((OGLRenderer.gameState==7)&&x>.6f*width&&y>.6f*height){
			OGLRenderer.playerScore=0;
			OGLRenderer.lives=4;
			OGLRenderer.initiate=false;
			OGLRenderer.frameCount=0;
			OGLRenderer.checkIndex=1;
			OGLRenderer.gameState=11;
			
		}
	}
		
		
	//Game menus return
	else if((OGLRenderer.gameState==3)||(OGLRenderer.gameState==2)){
		if(x<.4f*width&&y>.6f*height){
		//Return to main menu
		OGLRenderer.gameState=0;
		}
	}
		
	else if(OGLRenderer.gameState==5){
		if(x>.6f*width&&y>.6f*height){
		//Return to main menu
		//OGLRenderer.gameState=0;
		}
	}
		
		
	else if(OGLRenderer.gameState==6){
		if(x>.6f*width&&y>.6f*height){
		//Return to main menu
		OGLRenderer.gameState=9;
		}
		
		
	}
		
	else if(OGLRenderer.gameState==7){
		if(x>.6f*width&&y>.6f*height){
		//Return to main menu
		//OGLRenderer.gameState=0;
		}
	}
		
	if(OGLRenderer.gameState==8){
		if(x>.6f*width&&y>.6f*height){
		//Return to main menu
			OGLRenderer.playerScore=0;
		OGLRenderer.lives=4;
		OGLRenderer.frameCount=0;
		OGLRenderer.checkIndex=1;
		OGLRenderer.initiate=false;
		OGLRenderer.gameState=1;
		}
	}
	}
	
		return true;

	
	}
	
	
	
	
	
	
}
