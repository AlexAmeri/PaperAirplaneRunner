package com.example.cubetest;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;

public class PlaneTriangle {

	private FloatBuffer vertexBuffer;
	private FloatBuffer foldVertexBuffer;

	
	//Coordinates per vertex
	static final int COORDS_PER_VERTEX =3;
	
	float triangleVerts[] = {
			     0.0f, 1f,   // top
		         0f, -1f, //bottom left
		         1f, -1f // bottom right	
	};
	
	float foldVerts[] = {
				

				-.5f, 0f,		//center
				1f,-1f		//right
	};
	
	//Set color
	//float color[] = {0.63671875f, 0.76953125f, 0.22265625f, 1.0f };
	private short pIndex[] = {0,1,2};
	private short pfIndex[] = {0,1};
	private ShortBuffer pBuff;
	private ShortBuffer pfBuff;
	
	public PlaneTriangle(){
	
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
	
	//fold order buffer
	ByteBuffer pbfBuff = ByteBuffer.allocateDirect(pfIndex.length*2);
	pbfBuff.order(ByteOrder.nativeOrder());
	pfBuff = pbfBuff.asShortBuffer();
	pfBuff.put(pfIndex);
	pfBuff.position(0);
	
	//Vertex of fold buffers
	ByteBuffer foldBuff = ByteBuffer.allocateDirect(foldVerts.length*4);
	foldBuff.order(ByteOrder.nativeOrder());
	foldVertexBuffer = foldBuff.asFloatBuffer();
	foldVertexBuffer.put(foldVerts);
	foldVertexBuffer.position(0);
	
	}
	
	public void draw(GL10 gl, boolean lines){

		if(!lines)
		gl.glColor4f(1,1,1,1);
		
		else
		gl.glColor4f(0,0,0,1);
	
		
		gl.glFrontFace(GL10.GL_CCW);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glVertexPointer(2,GL10.GL_FLOAT,0,vertexBuffer);
		
		if(!lines)
		gl.glDrawElements(GL10.GL_TRIANGLES, pIndex.length, GL10.GL_UNSIGNED_SHORT, pBuff);
		
		else
			gl.glDrawElements(GL10.GL_LINE_LOOP, pIndex.length, GL10.GL_UNSIGNED_SHORT, pBuff);
	
	
		
		
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glColor4f(1f,1f,1f,1f);
	}
	
	
	
	
	}
	
	

	
	

