package com.example.cubetest;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

public class Skyscraper {

	private FloatBuffer vertexBuffer;
	private ByteBuffer indexBuffer;
	private FloatBuffer textureBuffer;
	private int[] textures = new int[3];
	/*
	float CubeVerts[] ={		
	1,1,-1, //p0 - topFrontRight
	1,-1,-1, //p1 - bottomFrontRight
	-1,-1,-1, //p2 - bottomFrontLeft
	-1,1,-1, //p3 - front top left
	
	1,1,1, //p0 - topBackRight
	1,-1,1, //p1 - bottomBackRight
	-1,-1,1, //p2 - bottomBackLeft
	-1,1,1, //p3 - front back left
	
	};
	*/
	
	
	
	/*
	private short[] pIndex = {
		3,4,0,  0,4,1,  3,0,1,
		3,7,4,  7,6,4,  7,3,6,
		3,1,2,  1,6,2,  6,3,2,
		1,4,5,  5,6,1,  6,5,4,
	};
	*/
	private float vertices[] = {
			//Vertices according to faces
    		-1.0f, -2.0f, 1.0f, //Vertex 0
    		1.0f, -2.0f, 1.0f,  //v1
    		-1.0f, 4.0f, 1.0f,  //v2
    		1.0f, 4.0f, 1.0f,   //v3
    		
    		1.0f, -2.0f, 1.0f,	//...
    		1.0f, -2.0f, -1.0f,    		
    		1.0f, 4.0f, 1.0f,
    		1.0f, 4.0f, -1.0f,
    		
    		1.0f, -2.0f, -1.0f,
    		-1.0f, -2.0f, -1.0f,    		
    		1.0f, 4.0f, -1.0f,
    		-1.0f, 4.0f, -1.0f,
    		
    		-1.0f, -2.0f, -1.0f,
    		-1.0f, -2.0f, 1.0f,    		
    		-1.0f, 4.0f, -1.0f,
    		-1.0f, 4.0f, 1.0f,
    		
    		-1.0f, -2.0f, -1.0f,
    		1.0f, -2.0f, -1.0f,    		
    		-1.0f, -2.0f, 1.0f,
    		1.0f, -2.0f, 1.0f,
    		
    		-1.0f, 4.0f, 1.0f,
    		1.0f, 4.0f, 1.0f,    		
    		-1.0f, 4.0f, -1.0f,
    		1.0f, 4.0f, -1.0f,
								};

/** The initial texture coordinates (u, v) */	
private float texture[] = {    		
    		//Mapping coordinates for the vertices
    		0.0f, 0.0f,
    		0.0f, 1.0f,
    		1.0f, 0.0f,
    		1.0f, 1.0f, 
    		
    		0.0f, 0.0f,
    		0.0f, 1.0f,
    		1.0f, 0.0f,
    		1.0f, 1.0f,
    		
    		0.0f, 0.0f,
    		0.0f, 1.0f,
    		1.0f, 0.0f,
    		1.0f, 1.0f,
    		
    		0.0f, 0.0f,
    		0.0f, 1.0f,
    		1.0f, 0.0f,
    		1.0f, 1.0f,
    		
    		0.0f, 0.0f,
    		0.0f, 1.0f,
    		1.0f, 0.0f,
    		1.0f, 1.0f,
    		
    		0.0f, 0.0f,
    		0.0f, 1.0f,
    		1.0f, 0.0f,
    		1.0f, 1.0f,

    							};

/** The initial indices definition */	
private byte indices[] = {
			//Faces definition
    		0,1,3, 0,3,2, 			//Face front
    		4,5,7, 4,7,6, 			//Face right
    		8,9,11, 8,11,10, 		//... 
    		12,13,15, 12,15,14, 	
    		16,17,19, 16,19,18, 	
    		20,21,23, 20,23,22, 	
								};
	
	
	public Skyscraper(){
		//Vertex buffer
		ByteBuffer cubePoints = ByteBuffer.allocateDirect(vertices.length*4);
		cubePoints.order(ByteOrder.nativeOrder());
		vertexBuffer = cubePoints.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);
		
		//Order buffer
        indexBuffer = ByteBuffer.allocateDirect(indices.length);
        indexBuffer.put(indices);
        indexBuffer.position(0);
		
		//Texture buffer
		ByteBuffer texBuf = ByteBuffer.allocateDirect(texture.length * 4);
	    texBuf.order(ByteOrder.nativeOrder());
	    textureBuffer = texBuf.asFloatBuffer();
	    textureBuffer.put(texture);
	    textureBuffer.position(0);
		
		}
	
	public void draw(GL10 gl, int filter){
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[filter]);
		
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		
		gl.glFrontFace(GL10.GL_CCW);
		gl.glEnable(GL10.GL_CULL_FACE);
		gl.glCullFace(GL10.GL_BACK);
	
		gl.glVertexPointer(3,GL10.GL_FLOAT,0,vertexBuffer);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);
		
		gl.glDrawElements(GL10.GL_TRIANGLES, indices.length, GL10.GL_UNSIGNED_BYTE, indexBuffer);
		
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glDisable(GL10.GL_CULL_FACE);
	
	}
	
	public void loadGLTexture(GL10 gl, Context context){
		InputStream is = context.getResources().openRawResource(R.drawable.skyscraper);
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeStream(is);
		} finally {
			try{
				is.close();
				is = null;
			} catch (IOException e){
				
			}
		}
		
		//Generate three texture pointers
		gl.glGenTextures(3,textures,0);
		//gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
		
		//Create Nearest Filtered Texture and bind it to texture 0
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
		
		//Create Linear Filtered Texture and bind it to texture 1
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[1]);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
		
		//Create mipmapped textures and bind it to texture 2
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[2]);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR_MIPMAP_NEAREST);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
		
		/*
		//Different possible texture parameters, e.g. GL10.GL_CLAMP_TO_EDGE
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);
				
		//Use the Android GLUtils to specify a two-dimensional texture image from our bitmap
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
		*/
		
		//Check if Gl version 1.1
		if(gl instanceof GL11){
			gl.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_GENERATE_MIPMAP, GL11.GL_TRUE);
			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
		} else {
			buildMipmap(gl, bitmap);
		}
				
		//Clean up
		bitmap.recycle();
		
		
	}
	
	private void buildMipmap(GL10 gl, Bitmap bitmap){
		//
		int level = 0;
		//
		int height = bitmap.getHeight();
		int width = bitmap.getWidth();
		
		//
		while(height >=1 || width >=1){
			//First off, generate the texture from the bitmap and set it to the proper level
			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, level, bitmap, 0);
			
			//
			if(height == 1 || width ==1){
				break;
			}
			
			//Increase mipmap level
			level++;
			
			//
			height /=2;
			width /=2;
			Bitmap bitmap2 = Bitmap.createScaledBitmap(bitmap, width, height, true);
			
			//Clean up
			bitmap.recycle();
			bitmap = bitmap2;
		}
		}
	}

		
		
		

	

