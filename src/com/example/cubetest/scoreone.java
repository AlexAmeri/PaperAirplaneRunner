package com.example.cubetest;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

public class scoreone {
private FloatBuffer vertexBuffer;
private FloatBuffer textureBuffer;
private ByteBuffer indexBuffer;	
private int texVal;
private InputStream is; 
	
//pointer	
private int[] textures = new int[1];
	
	float triangleVerts[] = {
			-1f,-1f,
			-1f,1f,
			1f,1f,
			1f,-1f
			
	};
	
	float texture[] = {
			0f,0f,
			0f,1f,
			1f,1f,
			1f,0f
	};
	
	
	
	//Set color
	//float color[] = {0.63671875f, 0.76953125f, 0.22265625f, 1.0f };
	private byte pIndex[] = {0,1,3,3,1,2};

	public scoreone(int tex){
	
    //Vertex buffers
	ByteBuffer bb = ByteBuffer.allocateDirect(triangleVerts.length*4);
	bb.order(ByteOrder.nativeOrder());
	vertexBuffer = bb.asFloatBuffer();
	vertexBuffer.put(triangleVerts);
	vertexBuffer.position(0);
	
	//Order buffer
	indexBuffer= ByteBuffer.allocateDirect(texture.length);
	indexBuffer.put(pIndex);
	indexBuffer.position(0);
	
	//Texture buffer
	ByteBuffer texBuff = ByteBuffer.allocateDirect(texture.length*4);
	texBuff.order(ByteOrder.nativeOrder());
	textureBuffer = texBuff.asFloatBuffer();
	textureBuffer.put(texture);
	textureBuffer.position(0);
	
	texVal=tex;
	}
	
	public void draw(GL10 gl, boolean sidewalk){
		//bind the texture
		if(!sidewalk){
		gl.glColor4f(.65f,.65f,.65f,1f);
		}
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
		
		//point to the buffers
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		
		gl.glFrontFace(GL10.GL_CW);
		gl.glVertexPointer(2,GL10.GL_FLOAT,0,vertexBuffer);
		gl.glTexCoordPointer(2,GL10.GL_FLOAT, 0, textureBuffer);
		
		gl.glDrawElements(GL10.GL_TRIANGLES, pIndex.length, GL10.GL_UNSIGNED_BYTE, indexBuffer);
		
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glColor4f(1f,1f,1f,1f);
	
	}
	
	public void loadGLTexture(GL10 gl, Context context) {
		//Get the texture from the Android resource directory
		//switch the values
		switch(texVal){
		
		case -1:
		is = context.getResources().openRawResource(R.drawable.scoretexx);
		break;
		
		case 0:
			is = context.getResources().openRawResource(R.drawable.zero);
			break;
			
		case 1:
			is = context.getResources().openRawResource(R.drawable.one);
			break;
			
		case 2:
			is = context.getResources().openRawResource(R.drawable.two);
			break;
			
		case 3:
			is = context.getResources().openRawResource(R.drawable.three);
			break;
			
		case 4:
			is = context.getResources().openRawResource(R.drawable.four);
			break;
			
		case 5:
			is = context.getResources().openRawResource(R.drawable.five);
			break;
			
		case 6:
			is = context.getResources().openRawResource(R.drawable.six);
			break;
			
		case 7:
			is = context.getResources().openRawResource(R.drawable.seven);
			break;
			
		case 8:
			is = context.getResources().openRawResource(R.drawable.eight);
			break;
			
		case 9:
			is = context.getResources().openRawResource(R.drawable.nine);
			break;
			
		case 10:
			is = context.getResources().openRawResource(R.drawable.coltrione);
			break;
			
		case 11:
			is = context.getResources().openRawResource(R.drawable.begin);
			break;
			
		case 12:
			is = context.getResources().openRawResource(R.drawable.coltrithree);
			break;
			
		case 13:
			is = context.getResources().openRawResource(R.drawable.coltrifour);
			break;
			
		case 14:
			is = context.getResources().openRawResource(R.drawable.coltritwo);
			break;
			
		case 15:
			is = context.getResources().openRawResource(R.drawable.swipe);
			break;
			
		case 16:
			is = context.getResources().openRawResource(R.drawable.dodge);
			break;
			
		case 17:
			is = context.getResources().openRawResource(R.drawable.flythrough);
			break;
			
		case 18:
			is = context.getResources().openRawResource(R.drawable.follow);
			break;
			
		case 19:
			is = context.getResources().openRawResource(R.drawable.bluetri);
			break;
			
		case 20:
			is = context.getResources().openRawResource(R.drawable.heart);
			break;
			
		case 21:
			is = context.getResources().openRawResource(R.drawable.crash);
			break;
			
		case 22:
			is = context.getResources().openRawResource(R.drawable.pigfinal);
			break;
			
		case 23:
			is = context.getResources().openRawResource(R.drawable.pigbackk);
			break;
			
		}
		
		Bitmap bitmap = null;
		try {
			//BitmapFactory is an Android graphics utility for images
			bitmap = BitmapFactory.decodeStream(is);

		} finally {
			//Always clear and close
			try {
				is.close();
				is = null;
			} catch (IOException e) {
			}
		}

		//Generate one texture pointer...
		gl.glGenTextures(1, textures, 0);
		//...and bind it to our array
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
		
		//Create Nearest Filtered Texture
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);

		//Different possible texture parameters, e.g. GL10.GL_CLAMP_TO_EDGE
		//gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT);
		//gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);
		
		//Use the Android GLUtils to specify a two-dimensional texture image from our bitmap
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
		
		//Clean up
		bitmap.recycle();
	}
	
	
	
}
