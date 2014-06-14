package com.example.cubetest;

import javax.microedition.khronos.opengles.GL10;



public class Heart {
	
	private Triangle HeartPart;
	
	public Heart(){
		
		HeartPart = new Triangle();
		
	}
	
	public void draw(GL10 gl){

		gl.glScalef(.15f,.15f,.15f);
		gl.glRotatef(180,0,0,1);
		HeartPart.draw(gl,false,true);
		gl.glRotatef(180,0,0,1);
		gl.glTranslatef(0,1.5f,0);
		gl.glScalef(.6f,.5f,1f);
		gl.glTranslatef(.75f,0f,0f);
		HeartPart.draw(gl,false,true);
		gl.glTranslatef(-1.5f,0f,0f);
		HeartPart.draw(gl,false,true);
		
		//clean up
		gl.glTranslatef(.75f,0f,0f);
		gl.glTranslatef(0,-1.5f,0);
		gl.glScalef(1f/.6f,2f,1f);
		gl.glRotatef(180,0,0,1);
		
		
		
		
	}

}
