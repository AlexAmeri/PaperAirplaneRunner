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

public class MessageScreen {
private FloatBuffer vertexBuffer;
private FloatBuffer textureBuffer;
private int texVal;
InputStream is;
	
private int[] textures = new int[1];
	
	float triangleVerts[] = {
			-1.5f,-1,
			-1.5f,1,
			1.5f,1,
			1.5f,-1,
	};
	
	float texture[] = {
			0f,0f,
			0f,1f,
			1f,1f,
			1f,0f
	};
	
	
	//Set color
	//float color[] = {0.63671875f, 0.76953125f, 0.22265625f, 1.0f };
	private byte pIndex[] = {0,1,3,2,3,1};
	private ShortBuffer pBuff;
	private ByteBuffer indexBuffer;

	
	public MessageScreen(int tex){
		
	    //Vertex buffers
		ByteBuffer bb = ByteBuffer.allocateDirect(triangleVerts.length*4);
		bb.order(ByteOrder.nativeOrder());
		vertexBuffer = bb.asFloatBuffer();
		vertexBuffer.put(triangleVerts);
		vertexBuffer.position(0);
		
		//Order buffer
		indexBuffer= ByteBuffer.allocateDirect(pIndex.length);
		indexBuffer.put(pIndex);
		indexBuffer.position(0);
		
		//Texture buffer
		ByteBuffer texBuff = ByteBuffer.allocateDirect(texture.length*4);
		texBuff.order(ByteOrder.nativeOrder());
		textureBuffer = texBuff.asFloatBuffer();
		textureBuffer.put(texture);
		textureBuffer.position(0);
		
		texVal = tex;
		}
		
		public void draw(GL10 gl){

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
			if(texVal==0)
			is = context.getResources().openRawResource(R.drawable.mainmenuoo);
			
			if(texVal==1)
			is = context.getResources().openRawResource(R.drawable.deadtex);
			
			if(texVal==2)
			is = context.getResources().openRawResource(R.drawable.howto);
			
			if(texVal==3)
			is = context.getResources().openRawResource(R.drawable.about);
			
			if(texVal==4)
			is = context.getResources().openRawResource(R.drawable.chooselevel);
			
			if(texVal==5)
			is = context.getResources().openRawResource(R.drawable.delr);
			
			if(texVal==6)
			is = context.getResources().openRawResource(R.drawable.deltasl);
			
			if(texVal==7)
			is = context.getResources().openRawResource(R.drawable.deltah);
			
			if(texVal==8)
			is = context.getResources().openRawResource(R.drawable.deltav);
			


			
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
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);
			
			//Use the Android GLUtils to specify a two-dimensional texture image from our bitmap
			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
			
			//Clean up
			bitmap.recycle();
		}
}
		
		
