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

public class Leftarrow {
private FloatBuffer vertexBuffer;
private FloatBuffer textureBuffer;
private ByteBuffer indexBuffer;	
	
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

	public Leftarrow(){
	
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
		InputStream is = context.getResources().openRawResource(R.drawable.leftarrow);
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
