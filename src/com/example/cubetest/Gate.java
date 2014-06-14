package com.example.cubetest;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;


public class Gate {

	private FloatBuffer vertexBuffer;
	public static int gateCount;
	private Heart heart;
	
	//Coordinates per vertex
	static final int COORDS_PER_VERTEX =3;
	
	float triangleVerts[] = {
			     
			    //Outside
			     0.0f, 1f,   // top
		         1f, -1f, //bottom right
		         -1f, -1f, // bottom left
		         
	         
	};
	//.7402
	//.85
	//Set color
	//float color[] = {0.63671875f, 0.76953125f, 0.22265625f, 1.0f };
	private short pIndex[] = {0,1,2};

	private ShortBuffer pBuff;
	
	public Gate(){
	
	heart = new Heart();
	
    //Vertex buffers
	ByteBuffer bb = ByteBuffer.allocateDirect(triangleVerts.length*4);
	bb.order(ByteOrder.nativeOrder());
	vertexBuffer = bb.asFloatBuffer();
	vertexBuffer.put(triangleVerts);
	vertexBuffer.position(0);
	
	//Order buffer
	ByteBuffer pbBuff = ByteBuffer.allocateDirect(pIndex.length*2);
	pbBuff.order(ByteOrder.nativeOrder());
	pBuff = pbBuff.asShortBuffer();
	pBuff.put(pIndex);
	pBuff.position(0);
	}
	
	public void draw(GL10 gl, int color){
	 
	  
		switch(color){
		case 0:
		gl.glColor4f(1f,0f,0f,1f);
		break;
		case 1:
		gl.glColor4f(0f,1f,0f,1f);
		break;
		case 2:
		gl.glColor4f(0f,0f,1f,1f);
		break;
		case 3:
		gl.glColor4f(1f,1f,0f,1f);
		break;
		case 6:
		gl.glColor4f(0,0,0,1f);
		break;
		case 7:
		gl.glColor4f((247f/255f),0,1f,1f);
		break;
		}
		if(color!=4){
		gl.glFrontFace(GL10.GL_CW);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glVertexPointer(2,GL10.GL_FLOAT,0,vertexBuffer);
		if(color!=6)
		gl.glLineWidth(15.0f);
		else
			gl.glLineWidth(3f);
		gl.glDrawElements(GL10.GL_LINE_LOOP, pIndex.length, GL10.GL_UNSIGNED_SHORT, pBuff);
		
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glColor4f(1f,1f,1f,1f);
		}
	  
	  if(color==4){
		  gl.glScalef(10,10,10);
		  heart.draw(gl);
		  gl.glScalef(.1f,.1f,.1f);
	  }
	}
	
	
	
	
	}
	
	

	
	

