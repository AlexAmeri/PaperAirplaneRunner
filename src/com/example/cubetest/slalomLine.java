package com.example.cubetest;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;


import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;

public class slalomLine {

	private FloatBuffer vertexBuffer;
	public static int gateCount;
	
	
	//Coordinates per vertex
	static final int COORDS_PER_VERTEX =3;
	
	float triangleVerts[] = new float[808];
	//.7402
	//.85
	//Set color
	//float color[] = {0.63671875f, 0.76953125f, 0.22265625f, 1.0f };
	private short pIndex[] = new short[403];

	private ShortBuffer pBuff;
	
	public slalomLine(int[] coords){
	
	//Fill the vertex float buffer
	for(int i=0;i<coords.length;i++){
		triangleVerts[(i*4)+6]=(float)coords[i]-180f;
		triangleVerts[(i*4)+7]=400f+(300f*i);
		triangleVerts[(i*4)+8]=(float)coords[i]-180f;
		triangleVerts[(i*4)+9]=400f+(300f*i);
	}
	
	//Set the first two vertices to default value
	triangleVerts[0]=0;
	triangleVerts[1]=0f;
	triangleVerts[2]=0f;
	triangleVerts[3]=100f;
	triangleVerts[4]=0f;
	triangleVerts[5]=100f;

	
	//Fill the vertex point buffer
	for(int i=0;i<pIndex.length;i++){
		pIndex[i]=(short)i;
	}
	
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
	
	public void draw(GL10 gl){
		
		gl.glColor4f(0,1,0,1);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glVertexPointer(2,GL10.GL_FLOAT,0,vertexBuffer);
	
		gl.glLineWidth(8f);
		gl.glDrawElements(GL10.GL_LINES, pIndex.length, GL10.GL_UNSIGNED_SHORT, pBuff);
		
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glColor4f(1f,1f,1f,1f);
	  }
	}
	
	
	

	
	

	
	

