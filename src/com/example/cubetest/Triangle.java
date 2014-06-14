package com.example.cubetest;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;

public class Triangle {

	private FloatBuffer vertexBuffer;
	
	
	
	//Coordinates per vertex
	static final int COORDS_PER_VERTEX =3;
	
	float triangleVerts[] = {
			     0.0f, 1f,   // top
		         1f, -1f, //bottom left
		         -1f, -1f // bottom right	
	};
	
	//Set color
	//float color[] = {0.63671875f, 0.76953125f, 0.22265625f, 1.0f };
	private short pIndex[] = {0,1,2};
	private ShortBuffer pBuff;
	
	public Triangle(){
	
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
	
	public void draw(GL10 gl, boolean isWhite, boolean heart){
		if(!isWhite&&!heart)
		gl.glColor4f(0,0,0,1);
		
		else if(!heart&&isWhite)
			gl.glColor4f(1,1,1,1);
		else
			gl.glColor4f(1,0,0,1);
		
		gl.glFrontFace(GL10.GL_CW);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glVertexPointer(2,GL10.GL_FLOAT,0,vertexBuffer);
		
		gl.glDrawElements(GL10.GL_TRIANGLES, pIndex.length, GL10.GL_UNSIGNED_SHORT, pBuff);
		
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glColor4f(1f,1f,1f,1f);
	}
	
	
	
	
	}
	
	

	
	

