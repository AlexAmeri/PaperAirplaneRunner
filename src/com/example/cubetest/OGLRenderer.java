package com.example.cubetest;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.R.bool;
import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLU;
import android.opengl.GLSurfaceView.Renderer;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

public class OGLRenderer implements Renderer {
	
	public static volatile boolean initiate=false;
	int goodCount=0;
	public volatile static int menPress=-100;
	public volatile static int gameState=0;
	public volatile static int oldState=0;
	public volatile int newState=0;
	int t;
	int lineStartVal;
	int ans;
	float avg;
	private MessageScreen mainMenu;
	public int digits;
	public float anss;
	public int crashStartFrame=-100;

	private scoreone score;
	private scoreone zero;
	private scoreone one;
	private scoreone two;
	private scoreone three;
	private scoreone four;
	private scoreone five;
	private scoreone six;
	private scoreone seven;
	private scoreone eight;
	private scoreone nine;
	private scoreone beginsMessage;
	private scoreone dodgeBuildingsMessage;
	private scoreone swipeScreenMessage;
	private scoreone followLineMessage;
	private scoreone flyGatesMessage;
	private scoreone heartMessage;
	private scoreone coltrioneMessage;
	private scoreone coltritwoMessage;
	private scoreone blueMessage;
	private scoreone coltrithreeMessage;
	private scoreone coltrifourMessage;
	private scoreone crashMessage;
	private scoreone pigFace;
	private scoreone pigBack;
	
	
	
	private MessageScreen aboutMenu;
	private MessageScreen howtoMenu;
	private MessageScreen playMenu;
	private MessageScreen deltaRun;
	private MessageScreen deltaSlalom;
	private MessageScreen deltaHero;
	private MessageScreen deltaVenom;
	private MessageScreen deadMessage;
	private Leftarrow scoreBack; 
	
	
	private PlaneTriangle playerTri;
	
	
	private Triangle tr;
	private Triangle l;
	private Triangle r;
	private Rightarrow scoreGood;
	private Rightarrow scoreBad;
	private Heart h;
	private Cube cube;
	private Cube2 cube2;
	private Skyscraper s;
	private Gate g;
	private slalomLine slalom;
	private Leftarrow ground;
	private int filter = 0; //Which texture filter?
	private int fogFilter = 0;  //Which fog filter to use?
	private Context context;
	public static volatile int lives;
	public static volatile int checkIndex;
	private int prevGate=0;
	private Triangle player;
	
	
	public static volatile float[] slideHistory;
	
	public int screenHeight;
	public int screenWidth;
	
	boolean correct = false;
	boolean wrong = false;
	int ansFrame=0;
	
	public volatile static float xpos;
	
	private int oldLtouch=0;
	private int oldRtouch=0;
	private int startFrame=0;
	
	public volatile static boolean lTouch = false;
	public volatile static boolean rTouch = false;
	public static volatile int playerScore=0;
	
	private int fogMode[] = {
							  GL10.GL_EXP,
							  GL10.GL_EXP2,
							  GL10.GL_LINEAR
	};
	
	private int[] lineLocation = new int[200];
	
	private float[] fogColor = {215f/255f, 241f/255f, 242f/255f, 1f};
	private FloatBuffer fogColorBuffer;
	
	int rotX=1;
	int rotZ=1;
	public volatile static float posx;
	public volatile static float oldPosx=0f;
	public volatile static float dx=0f;
	volatile static float posz;
	boolean goingAway = true;
	
	float chunkOneMid;
	float chunkTwoMid;
	
	public int startX;
	public int endX;
	
	
	float zpos=0f;
	int startVal;

	public boolean[][] location = new boolean[5000][600];
	public boolean[][] gate = new boolean[5000][600];
	
	public int cubeIncrement=5;
	public static int frameCount=0;
	Random positionCalc = new Random();
	boolean begin;
	public static volatile boolean startgame=false;
	int row=0;
	int tick = 0;
	int prevCol=0;
	
	int skip=0;
	
	boolean second = false;
	
	long deltaTime;
	long oldTime;

	int height;
	int width;
	int gateColor;
	boolean sequenceStart;
	public int[] sequence = new int[1000];
	public static volatile int sequenceLength=0;
	
	private int temp;
	private int rr;
	private int gg;
	private int f;
	
	
	public OGLRenderer(Context context){
	
	posz=0f;	
	
	//Get the screen dimensions
	DisplayMetrics metrics = context.getResources().getDisplayMetrics();
	width = metrics.widthPixels;
	height = metrics.heightPixels;	
		
	//Call each object	
	cube = new Cube();
	cube2 = new Cube2();
    l = new Triangle();
    r = new Triangle();
    s = new Skyscraper();
    ground = new Leftarrow();
    g= new Gate();
    tr= new Triangle();
    h = new Heart();
    scoreGood = new Rightarrow(true);
    scoreBad = new Rightarrow(false);
    player = new Triangle();
    coltrioneMessage = new scoreone(10);
    playerTri = new PlaneTriangle();
    pigFace = new scoreone(22);
    pigBack = new scoreone(23);
    
    //Load the number textures
    score = new scoreone(-1);
    zero = new scoreone(0);
    one = new scoreone(1);
    two = new scoreone(2);
    three = new scoreone(3);
    four = new scoreone(4);
    five = new scoreone(5);
    six = new scoreone(6);
    seven = new scoreone(7);
    eight = new scoreone(8);
    nine = new scoreone(9);
    
    //Load the menus
    mainMenu = new MessageScreen(0);
    deadMessage = new MessageScreen(1);
    aboutMenu = new MessageScreen(3);
	howtoMenu = new MessageScreen(2);
	playMenu = new MessageScreen(4);
    deltaRun = new MessageScreen(5);
    deltaSlalom = new MessageScreen(6);
	deltaHero = new MessageScreen(7);
	deltaVenom = new MessageScreen(8);
	
	//Load the messages
	beginsMessage = new scoreone(11);
	dodgeBuildingsMessage= new scoreone(16);
	swipeScreenMessage= new scoreone(15);
	followLineMessage= new scoreone(18);
	flyGatesMessage= new scoreone(17);
	coltritwoMessage= new scoreone(14);
	coltrithreeMessage= new scoreone(12);
	coltrifourMessage= new scoreone(13);
	blueMessage = new scoreone(19);
	heartMessage = new scoreone(20);
	crashMessage = new scoreone(21);
	
	//vals
	lineStartVal=0;
	slideHistory = new float[12];
    
    //Set gamestate
    gameState=0;
    
    //Set the context
	this.context = context;
	
	//Initialize position
	posx=0f;
	
	//Initialize the timestamp
	oldTime = SystemClock.elapsedRealtime();
    int count=1;
    
	
	//Initialize the position matrix
		for(int i=0; i<location.length;i++){
			if(i%50!=0){
			//small buildings
			if(i%3!=0){
			for(int j=0;j<cubeIncrement*5;j++){
				int x = positionCalc.nextInt(300)*2;
				if(count%2==0)
				location[i][x]=true;
				else
				location[i][x+1]=true;
				count++;
			}
			location[i][100]=true;
			location[i][500]=true;
		}
			//skyscrapers
			else if(i%3==0){
				for(int j=0;j<cubeIncrement;j++){
					int x = positionCalc.nextInt(150)*4;
					location[i][x]=true;
				}
				location[i][100]=true;
				location[i][500]=true;
			}

		
		}
			else{
				location[i][100]=true;
				location[i][500]=true;
			}
		
			if(i%120==0&&cubeIncrement<=8)
				cubeIncrement++;

		}
		
		//line slalom matrix
		for(int i=2;i<lineLocation.length;i++){
			int seed = positionCalc.nextInt(180)*2;
			lineLocation[i]=seed;
		}
		
		
		slalom = new slalomLine(lineLocation);

		//Initialize the gate matrix
		cubeIncrement=5;
				for(int i=0; i<gate.length;i++){
					for(int j=0;j<cubeIncrement/1.5;j++){
						int x = positionCalc.nextInt(300)*2;
						gate[i][x]=true;
						
					
				}
					if(i%120==0&&cubeIncrement<=9)
						cubeIncrement++;
				}

		
		//Build the fog buffer
		ByteBuffer fog = ByteBuffer.allocateDirect(fogColor.length*4);
		fog.order(ByteOrder.nativeOrder());
		fogColorBuffer = fog.asFloatBuffer();
		fogColorBuffer.put(fogColor);
		fogColorBuffer.position(0);

	}
	
	public void setSequence(){
		
		if(gameState!=11)
		sequenceLength+=2;
		
		if(gameState==11&&sequenceLength<2)
		sequenceLength+=2;
		
		for(int i=0; i<sequenceLength; i+=2){
			sequence[i]=5;
			sequence[i+1]=positionCalc.nextInt(4);
		}
		startFrame=frameCount;
		checkIndex=1;
		sequenceStart=true;
		
	}
	
	
	public void check(int checkVal){
		if(checkVal!=4){
		if(sequence[checkIndex]==checkVal){
			ansFrame=frameCount;
			correct=true;
			playerScore+=1000;
			checkIndex+=2;
		}
		
		else if(sequence[checkIndex]!=checkVal){
			if(sequenceLength>=4){
			sequenceLength-=4;
			}
			else if(sequenceLength<4){
				sequenceLength-=2;
			}
			
			ansFrame = frameCount;
			wrong = true;
			setSequence();
		}
		
		if(checkIndex>sequenceLength){
			setSequence();
		}
		}
		
		if(checkVal==4&&lives<4){
			lives+=1;
		}
	}
	

	
	public void die(){
		menPress=0;
		frameCount=20;
		gameState=2;
	}
	
	public void detectCollision(int val){
		
		//This will detect collisions in front of the camera
		//#1 Search directly ahead for buildings (check if building arrays contain true)
		//#2 If there are buildings ahead, determine if they are in player's path by
		//	 doing if(posx-buildingx)<building width
		//#3 If they are in player's path, determine their distance
		//#4 If distance is sufficiently close (<.5 meters) register a collision
		
		//Search array
	
		for(int x=startX;x<endX;x++){
			
			if(location[startVal][x]==true){
				if(Math.abs(posx-(x-300))<=1f&&Math.abs((startVal)*6f+100f-posz)<=1.3f){
					//Register a collision with a building
					if(frameCount-prevCol>10){
						prevCol=frameCount;
						if(lives>0)
						lives-=1;
						crashStartFrame=frameCount;
						//Check if all the player's lives are gone
						if(lives==0&&gameState!=9){
							die();
						}
					}
				}
			}
			
			if(gameState==1||gameState==11){
			if(gate[startVal][x]==true){
				if(Math.abs(posx-(x-300))<=1f&&Math.abs((startVal)*6f+100f-posz)<=1.3f){
					//Register a collision with a gate
					if(frameCount-prevGate>10){
					prevGate=frameCount;
					check(startVal%5);
					}
				}
			}
		
			
		}
			if(gameState==9){
				if(lineStartVal!=0){
				if(lineLocation[lineStartVal+1]==(x-120)){
					if(Math.abs(posx-(x-300))<=1f&&Math.abs((lineStartVal+1)*300f+400f-posz)<=1.3f){
						//Register a collision with a gate
						if(frameCount-prevGate>10){
						playerScore+=1000;
						prevGate=frameCount;
						ansFrame=frameCount;
						correct=true;
						}
					}
				}
				}
				
				if(lineStartVal==0){
					if(lineLocation[lineStartVal]==(x-120)){
						if(Math.abs(posx-(x-300))<=1f&&Math.abs((lineStartVal)*300f+400f-posz)<=1.3f){
							//Register a collision with a gate
							if(frameCount-prevGate>10){
							playerScore+=1000;
							prevGate=frameCount;
							ansFrame=frameCount;
							correct=true;
							}
						}
					}
					}
				
			}
			
			if(gameState==10||gameState==9){
				if(gate[startVal][x]==true&&startVal%5==4){
					if(Math.abs(posx-(x-300))<=1f&&Math.abs((startVal)*6f+100f-posz)<=1.3f){
						//Register a collision with a gate
						if(frameCount-prevGate>10){
						prevGate=frameCount;
						check(startVal%5);
						}
					}
				}
			
				
			}
			
			if(gameState==10){
				if(gate[startVal][x]==true){
					if(Math.abs(posx-(x-300))<=1f&&Math.abs((startVal)*6f+100f-posz)<=1.3f){
						//Register a collision with a gate
						if(frameCount-prevGate>10){
						prevGate=frameCount;
						ansFrame = frameCount;
						correct = true;
						playerScore+=1000;
						}
					}
				}
			
				
			}
			
		}
		
		
	}
	


	public void onDrawFrame(GL10 gl){
		
		if(oldState!=newState){
			if(newState==oldState+1){
				posz=0;
				lives=4;
				gameState=1;
				oldState=newState;
			}
		}
		
		/*
		//BOOKMARK
		gl.glPushMatrix();
		gl.glPopMatrix();
		//if(gameState==0){
		//if((gameState!=1)&&(gameState!=9)&&(gameState!=10)&&(gameState!=11)){
			//Menus
			
			deltaTime = SystemClock.elapsedRealtime()-oldTime;
			oldTime = SystemClock.elapsedRealtime();
			
			//Set up
			gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		    
			gl.glMatrixMode(GL10.GL_MODELVIEW);
			//gl.glFogf(GL10.GL_FOG_MODE, fogMode[1]);
			gl.glLoadIdentity();
			GLU.gluLookAt(gl, 0,0,0, 0,0,5,0,2f,0);
			
			//Background pig 
			gl.glPushMatrix();
			 gl.glTranslatef(posx,-.75f,posz+.9f);
			 gl.glRotatef(90,1,0,0);
			 gl.glScalef(.3f,.3f,.3f);
			 playerTri.draw(gl,false);
			 playerTri.draw(gl,true);
			 gl.glRotatef(180,0,1,0);
			 playerTri.draw(gl,false);
			 playerTri.draw(gl,true);
			 gl.glRotatef(90,0,1,0);
			 playerTri.draw(gl,false);
			 playerTri.draw(gl,true);
			 gl.glPopMatrix();
			 
			//pig
			 gl.glPushMatrix();
			 gl.glTranslatef(posx-1f,-.3f,posz+.9f);
			 gl.glRotatef(frameCount,0,1,0);
			 gl.glScalef(.3f,.3f,.3f);
			 gl.glRotatef(180,0,0,1);
		    // gl.glAlphaFunc(GL10.GL_GREATER, 0.1f );
		    // gl.glEnable( GL10.GL_ALPHA_TEST );
			// pigBack.draw(gl,true);
			 
			 gl.glPopMatrix();
			
		//}
		
		*/
		
		//The main menu
		if(gameState==0){
			
			deltaTime = SystemClock.elapsedRealtime()-oldTime;
			oldTime = SystemClock.elapsedRealtime();
			
			//Set up
			gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		    
			gl.glMatrixMode(GL10.GL_MODELVIEW);
			gl.glFogf(GL10.GL_FOG_MODE, fogMode[1]);
			gl.glLoadIdentity();
			GLU.gluLookAt(gl, 0,0,0, 0,0,5,0,2f,0);
			
			//Draw menu
			gl.glPushMatrix();
			gl.glTranslatef(0,-.16f,.6f);
			gl.glRotatef(180,0,0,1);
			gl.glScalef(1.2f,1.1f,1.1f);
			mainMenu.draw(gl);
			gl.glPopMatrix();
		}
		
		
		//The about menu
		if(gameState==3){
			
			deltaTime = SystemClock.elapsedRealtime()-oldTime;
			oldTime = SystemClock.elapsedRealtime();
			
			//Set up
			gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		    
			gl.glMatrixMode(GL10.GL_MODELVIEW);
			gl.glFogf(GL10.GL_FOG_MODE, fogMode[1]);
			gl.glLoadIdentity();
			GLU.gluLookAt(gl, 0,0,0, 0,0,5,0,2f,0);
			
			//Draw menu
			gl.glPushMatrix();
			gl.glTranslatef(0,-.16f,.6f);
			gl.glRotatef(180,0,0,1);
			gl.glScalef(1.2f,1.1f,1.1f);
			aboutMenu.draw(gl);
			gl.glPopMatrix();
			
			posz=0;
		}
		
		//The howto menu
			if(gameState==2){
					
					deltaTime = SystemClock.elapsedRealtime()-oldTime;
					oldTime = SystemClock.elapsedRealtime();
					
					//Set up
					gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
				    
					gl.glMatrixMode(GL10.GL_MODELVIEW);
					gl.glFogf(GL10.GL_FOG_MODE, fogMode[1]);
					gl.glLoadIdentity();
					GLU.gluLookAt(gl, 0,0,0, 0,0,5,0,2f,0);
					
					//Draw menu
					gl.glPushMatrix();
					gl.glTranslatef(0,-.16f,.6f);
					gl.glRotatef(180,0,0,1);
					gl.glScalef(1.2f,1.1f,1.1f);
					howtoMenu.draw(gl);
					gl.glPopMatrix();
					
					posz=0;
				}
			
			//The choose level menu
			if(gameState==4){
					
					deltaTime = SystemClock.elapsedRealtime()-oldTime;
					oldTime = SystemClock.elapsedRealtime();
					
					//Set up
					gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
				    
					gl.glMatrixMode(GL10.GL_MODELVIEW);
					gl.glFogf(GL10.GL_FOG_MODE, fogMode[1]);
					gl.glLoadIdentity();
					GLU.gluLookAt(gl, 0,0,0, 0,0,5,0,2f,0);
					
					//Draw menu
					gl.glPushMatrix();
					gl.glTranslatef(0,-.16f,.6f);
					gl.glRotatef(180,0,0,1);
					gl.glScalef(1.2f,1.1f,1.1f);
					playMenu.draw(gl);
					gl.glPopMatrix();
					
					posz=0;
				}
		
			//The delta run menu
			if(gameState==5){
					
					deltaTime = SystemClock.elapsedRealtime()-oldTime;
					oldTime = SystemClock.elapsedRealtime();
					
					//Set up
					gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
				    
					gl.glMatrixMode(GL10.GL_MODELVIEW);
					gl.glFogf(GL10.GL_FOG_MODE, fogMode[1]);
					gl.glLoadIdentity();
					GLU.gluLookAt(gl, 0,0,0, 0,0,5,0,2f,0);
					
					//Draw menu
					gl.glPushMatrix();
					gl.glTranslatef(0,-.16f,.6f);
					gl.glRotatef(180,0,0,1);
					gl.glScalef(1.2f,1.1f,1.1f);
					deltaRun.draw(gl);
					gl.glPopMatrix();
					
					posz=0;
				}
			
			//The delta slalom menu
			if(gameState==6){
					
					deltaTime = SystemClock.elapsedRealtime()-oldTime;
					oldTime = SystemClock.elapsedRealtime();
					
					//Set up
					gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
				    
					gl.glMatrixMode(GL10.GL_MODELVIEW);
					gl.glFogf(GL10.GL_FOG_MODE, fogMode[1]);
					gl.glLoadIdentity();
					GLU.gluLookAt(gl, 0,0,0, 0,0,5,0,2f,0);
					
					//Draw menu
					gl.glPushMatrix();
					gl.glTranslatef(0,-.16f,.6f);
					gl.glRotatef(180,0,0,1);
					gl.glScalef(1.2f,1.1f,1.1f);
					deltaSlalom.draw(gl);
					gl.glPopMatrix();
					
					posz=0;
				}
			
			//The delta hero menu
			if(gameState==7){
					
					deltaTime = SystemClock.elapsedRealtime()-oldTime;
					oldTime = SystemClock.elapsedRealtime();
					
					//Set up
					gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
				    
					gl.glMatrixMode(GL10.GL_MODELVIEW);
					gl.glFogf(GL10.GL_FOG_MODE, fogMode[1]);
					gl.glLoadIdentity();
					GLU.gluLookAt(gl, 0,0,0, 0,0,5,0,2f,0);
					
					//Draw menu
					gl.glPushMatrix();
					gl.glTranslatef(0,-.16f,.6f);
					gl.glRotatef(180,0,0,1);
					gl.glScalef(1.2f,1.1f,1.1f);
					deltaHero.draw(gl);
					gl.glPopMatrix();
					
					posz=0;
				}
		
			//The delta venom menu
			if(gameState==8){
					
					deltaTime = SystemClock.elapsedRealtime()-oldTime;
					oldTime = SystemClock.elapsedRealtime();
					
					//Set up
					gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
				    
					gl.glMatrixMode(GL10.GL_MODELVIEW);
					gl.glFogf(GL10.GL_FOG_MODE, fogMode[1]);
					gl.glLoadIdentity();
					GLU.gluLookAt(gl, 0,0,0, 0,0,5,0,2f,0);
					
					//Draw menu
					gl.glPushMatrix();
					gl.glTranslatef(0,-.16f,.6f);
					gl.glRotatef(180,0,0,1);
					gl.glScalef(1.2f,1.1f,1.1f);
					deltaVenom.draw(gl);
					gl.glPopMatrix();
					
					posz=0;
				}
			
///////////////////DELTA HERO/////////////////////////////////////////////////////////////////////////////////////////////////////// HERO			
			
			if(gameState==11){
			if(frameCount%3==0){
				dx=0;
			}
			if(frameCount==0){
				posz=50f;
				lives=4;
			}
			if(frameCount/4==760)
				setSequence();
			
			gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

		    
			gl.glMatrixMode(GL10.GL_MODELVIEW);
			gl.glFogf(GL10.GL_FOG_MODE, fogMode[1]);
			gl.glLoadIdentity();
			GLU.gluLookAt(gl, posx,0,posz, posx,0,posz+5f,0,2f,0);

			//Draw the bullseye
			 gl.glPushMatrix();
			 gl.glTranslatef(posx,-.1f,posz+1f);
			 gl.glScalef(.05f,.05f,.05f);
			 gl.glTranslatef(0,.25f,0);
			 gl.glScalef(4f,4f,4f);
			 g.draw(gl,6);
			 gl.glPopMatrix();
			
			
			
			
			deltaTime = SystemClock.elapsedRealtime()-oldTime;
			oldTime = SystemClock.elapsedRealtime();
			
			//float angle = .090f * ((int)time);
			
			//get slide history and average values
			for(int i=slideHistory.length-1;i>0;i--){
					slideHistory[i]=slideHistory[i-1];
					avg+=slideHistory[i];
			}
			slideHistory[0]=.2f*dx;	
			avg*=.6f;
			posx+=(avg/slideHistory.length);
			
			
			if(posz<100f)
				startVal=0;
			else
			startVal=((int)(posz-100f)/6);
			
			
			 startX = 300+(int)posx-60;
			 endX = startX+120;
			 
			 detectCollision(startVal);
			
			 
			 
			 
			 
			 //Draw the player
			 gl.glPushMatrix();
			 gl.glTranslatef(posx,-.75f,posz+.9f);
			 gl.glRotatef(90,1,0,0);
			 gl.glRotatef(-6f*(avg/slideHistory.length),0,0,1);
			 gl.glScalef(.3f,.3f,.3f);
			 playerTri.draw(gl,false);
			 playerTri.draw(gl,true);
			 gl.glRotatef(180,0,1,0);
			 playerTri.draw(gl,false);
			 playerTri.draw(gl,true);
			 gl.glRotatef(90,0,1,0);
			 playerTri.draw(gl,false);
			 playerTri.draw(gl,true);
			 gl.glPopMatrix();
			 
			 //Draw the bullseye
			 gl.glPushMatrix();
			 gl.glTranslatef(posx,-.1f,posz+1f);
			 gl.glScalef(.05f,.05f,.05f);
			 tr.draw(gl, false, true);
			 gl.glPopMatrix();
			 
			 //Draw the hearts
			 for(int i=0; i<lives; i++){
			 gl.glPushMatrix();
			 gl.glTranslatef(posx-1f,.7f,posz+.8f);
			 gl.glTranslatef(-.4f*i,0f,0f);
			 h.draw(gl);
			 gl.glPopMatrix();
			 }
			

			slalom.draw(gl);
			
			//draw the sequence
			if(sequenceStart){
				t = frameCount-startFrame;

				
				//Get the current element to indicate to the player and draw it
				if(t<60){
					ans = sequence[t/30];
					gl.glPushMatrix();
					gl.glTranslatef(posx,0,posz+3f);
					if(t<sequence.length*30)
					if(ans!=5){
					g.draw(gl,ans);
					}
					gl.glPopMatrix();
				}
				
				else
					sequenceStart=false;
				
			}
			

			//Draw the "ground" in strips
			for(int i=0;i<10;i++){
				gl.glPushMatrix();
				gl.glTranslatef(posx,-2f,posz+(6f*i));
				gl.glRotatef(90f,1,0,0);
				gl.glScalef(120f,10f,1f);
				ground.draw(gl,false);
				gl.glPopMatrix();
			}
			
			
			//draw the answer indicator
			if(correct){
				if(frameCount-ansFrame<15){
					 gl.glPushMatrix();
					 gl.glTranslatef(posx,1.5f,posz+2.5f);
					 gl.glRotatef(180,0,0,1);
					 scoreGood.draw(gl);
					 gl.glPopMatrix();
				}
				
				else
					correct=false;
			}
			
			if(wrong){
				if(frameCount-ansFrame<15){
					 gl.glPushMatrix();
					 gl.glTranslatef(posx,1.5f,posz+2.5f);
					 gl.glRotatef(180,0,0,1);
					 scoreBad.draw(gl);
					 gl.glPopMatrix();
				}
				
				else
					wrong=false;
			}
			
			
			//THE SCORE
			//Draw the scoreboard
			 gl.glPushMatrix();
			 gl.glTranslatef(posx+(6f*(.8f/2.5f)),2.5f*(.8f/2.5f),posz+.8f);
			 gl.glRotatef(180,0,0,1);
			 gl.glScalef(1.5f,1,1);
			 gl.glScalef((.8f/2.5f),(.8f/2.5f),(.8f/2.5f));
			 score.draw(gl,false);
			 gl.glScalef(.5f,0f,0f);
			 gl.glPopMatrix();
			 
			
			 
			 //Find out how many digits in the score
			digits=0;
			anss=playerScore;
			while(anss>=1f){
			digits+=1;
			anss=anss/10f;
			}

			
			//draw the score
			gl.glPushMatrix();
			gl.glTranslatef(posx+(5f*(.8f/2.5f)),2.5f*(.8f/2.5f),posz+.8f);
			gl.glScalef((.4f/2.5f),(.8f/2.5f),(.8f/2.5f));
			for(int i=digits;i>0;i--){
				gl.glTranslatef(-1.6f*i,0,0);
				
			    temp=playerScore/(int)(Math.pow(10,digits-i));
				rr = temp/10;
			    gg = temp-(rr*10);
			    f=5;
				gl.glRotatef(180,0,0,1);
				switch(gg){
				
				case 0:
					zero.draw(gl,false);
					break;
					
				case 1:
					one.draw(gl,false);
					break;
					
				case 2:
					two.draw(gl,false);
					break;
					
				case 3:
					three.draw(gl,false);
					break;
					
				case 4:
					four.draw(gl,false);
					break;
					
				case 5:
					five.draw(gl,false);
					break;
					
				case 6:
					six.draw(gl,false);
					break;
					
				case 7:
				    seven.draw(gl,false);
					break;
					
				case 8:
					eight.draw(gl,false);
					break;
					
				case 9:
					nine.draw(gl,false);
					break;
				}
				gl.glRotatef(-180,0,0,1);
				gl.glTranslatef(1.6f*i,0,0);
				
			}
			 gl.glPopMatrix();
			 
			 
			//GAME INSTRUCTIONS
				if(frameCount/4<=750&&frameCount/4>50){
					if(frameCount/4<150){
						//Display steering message
						gl.glPushMatrix();
						 gl.glTranslatef(posx,.75f,posz+2.5f);
						 gl.glRotatef(180,0,0,1);
						 gl.glScalef(4,1,1);
						 coltrioneMessage.draw(gl,false);			
						 gl.glPopMatrix();
					}
					if(frameCount/4>=150&&frameCount/4<250){
						//Display building crash message
						gl.glPushMatrix();
						 gl.glTranslatef(posx,.75f,posz+2.5f);
						 gl.glRotatef(180,0,0,1);
						 gl.glScalef(4,1,1);
						 coltritwoMessage.draw(gl,false);			
						 gl.glPopMatrix();
					}
					if(frameCount/4>=250&&frameCount/4<350){
						//Display gate message
						gl.glPushMatrix();
						 gl.glTranslatef(posx,.75f,posz+2.5f);
						 gl.glRotatef(180,0,0,1);
						 gl.glScalef(4,1,1);
						 swipeScreenMessage.draw(gl,false);			
						 gl.glPopMatrix();
					}
					if(frameCount/4>=350&&frameCount/4<450){
						//Display gate message
						gl.glPushMatrix();
						 gl.glTranslatef(posx,.75f,posz+2.5f);
						 gl.glRotatef(180,0,0,1);
						 gl.glScalef(4,1,1);
						 dodgeBuildingsMessage.draw(gl,false);			
						 gl.glPopMatrix();
					}
					if(frameCount/4>=450&&frameCount/4<550){
						//Display gate message
						gl.glPushMatrix();
						 gl.glTranslatef(posx,.75f,posz+2.5f);
						 gl.glRotatef(180,0,0,1);
						 gl.glScalef(4,1,1);
						 heartMessage.draw(gl,false);			
						 gl.glPopMatrix();
					}
					if(frameCount/4>=550&&frameCount/4<600){
						//Display 3 message
						gl.glPushMatrix();
						 gl.glTranslatef(posx,.75f,posz+2.5f);
						 gl.glRotatef(180,0,0,1);

						 three.draw(gl,false);			
						 gl.glPopMatrix();
					}
					if(frameCount/4>=600&&frameCount/4<650){
						//Display 2 message
						gl.glPushMatrix();
						 gl.glTranslatef(posx,.75f,posz+2.5f);
						 gl.glRotatef(180,0,0,1);
						 two.draw(gl,false);			
						 gl.glPopMatrix();
					}
					if(frameCount/4>=650&&frameCount/4<700){
						//Display 1 message
						gl.glPushMatrix();
						 gl.glTranslatef(posx,.75f,posz+2.5f);
						 gl.glRotatef(180,0,0,1);
						 one.draw(gl,false);			
						 gl.glPopMatrix();
					}
					if(frameCount/4>=700&&frameCount/4<750){
						//Display begin message
						gl.glPushMatrix();
						 gl.glTranslatef(posx,.75f,posz+2.5f);
						 gl.glRotatef(180,0,0,1);
						 beginsMessage.draw(gl,false);			
						 gl.glPopMatrix();
					}
				}
				
				if(frameCount/4>700)
					initiate=true;
				
				//Crash message
				if(frameCount-crashStartFrame<=20){
					gl.glPushMatrix();
					 gl.glTranslatef(posx,-1f,posz+2.5f);
					 gl.glRotatef(180,0,0,1);
					 gl.glScalef(4,1,1);
					 crashMessage.draw(gl,false);			
					 gl.glPopMatrix();
					
				}
			 
			 
			//Draw
			for(int i=startVal;i<startVal+7;i++){
				int count=0;
				//Draw normal buildings
				if(i%3!=0){
					for(int j=startX; j<endX; j++){
						//Check if there's something there
						if(location[i][j]==true){
							//Draw a cube and its "city block"
							gl.glPushMatrix();
							float x = (float)j-300f;
							float z= (float)i*6f+100f;
							gl.glTranslatef(x,-1f,z);
							gl.glRotatef(90,0,0,1);					
							if(j%2==0){
								if(j!=100||j!=500)
								cube.draw(gl, 2);
								else{									
									cube.draw(gl, 2);
									gl.glPopMatrix();
									gl.glPushMatrix();
									gl.glTranslatef(x,-1f,z+4f);
									cube.draw(gl,2);
								}
								gl.glPopMatrix();
								gl.glPushMatrix();
								gl.glTranslatef(x-.5f,-1.99f,z);
								gl.glRotatef(90,1,0,0);
								gl.glColor4f(222f/255f,223f/255f,184f/255f,1f);
								gl.glScalef(3f,2f,1);
								ground.draw(gl,true);
								gl.glColor4f(1f,1f,1f,1f);
								gl.glPopMatrix();
							
							}
							else {
								gl.glTranslatef(0,-1f,0);
								if(j!=100||j!=500)
									cube2.draw(gl, 2);
									else{										
										cube2.draw(gl, 2);
										gl.glPopMatrix();
										gl.glPushMatrix();
										gl.glTranslatef(x,-1f,z+4f);
										cube2.draw(gl,2);
				
									}
								gl.glPopMatrix();
								gl.glPushMatrix();
								gl.glTranslatef(x+1f,-1.99f,z);
								gl.glRotatef(90,1,0,0);
								gl.glColor4f(222f/255f,223f/255f,184f/255f,1f);
								gl.glScalef(3f,2f,1);
								ground.draw(gl,true);
								gl.glColor4f(1f,1f,1f,1f);
								gl.glPopMatrix();
								
							}


						}
					}
				}
				//Draw skyscrapers
				if(i%3==0){
					for(int j=startX; j<endX; j++){
						//Check if there's something there
						if(location[i][j]==true){
							//Draw a cube
							gl.glPushMatrix();
							float x = (float)j-300f;
							float z= (float)i*6f+100f;
							gl.glTranslatef(x,0f,z);
							if(j!=100||j!=500)
								s.draw(gl, 2);
								else{
									
									s.draw(gl, 2);
									gl.glPopMatrix();
									gl.glPushMatrix();
									gl.glTranslatef(x,0f,z+4f);
									s.draw(gl,2);
								}
							gl.glPopMatrix();
							gl.glPushMatrix();
							gl.glTranslatef(x-.5f,-1.99f,z);
							gl.glRotatef(90,1,0,0);
							gl.glColor4f(222f/255f,223f/255f,184f/255f,1f);
							gl.glScalef(3f,2f,1);
							ground.draw(gl,true);
							gl.glColor4f(1f,1f,1f,1f);
							gl.glPopMatrix();

						}
					}
				}
				

				//Draw gates
				for(int j=startX; j<endX; j++){
					//Check if there's something there
					if(gate[i][j]==true){
						//Draw a gate
						gl.glPushMatrix();
						float x = (float)j-300f;
						float z= (float)i*6f+100f;
						gl.glTranslatef(x,0f,z);
						g.draw(gl, i%5);
						gl.glPopMatrix();
					}

			}
			}

			if(gameState==11){
			frameCount+=1;
			
			}
			
			if(initiate)
				posz+=.015f*deltaTime;
				
			}	
			
			
			
///////////////////DELTA RUN ////////////////////////////////////////////////////////////////////////////////////////////////////// RUN
			if(gameState==10){
				
				
				
				
				
				if(frameCount%4==0){
					dx=0;
				}
				if(frameCount==0){
					posz=50f;
					lives=4;
				}
				gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
			  
				gl.glMatrixMode(GL10.GL_MODELVIEW);
				gl.glFogf(GL10.GL_FOG_MODE, fogMode[1]);
				gl.glLoadIdentity();
				gl.glEnable(GL10.GL_BLEND);
				//gl.glDisable(GL10.GL_DEPTH_TEST);
				GLU.gluLookAt(gl, posx,0,posz, posx,0,posz+5f,0,2f,0);
				frameCount++;
				
				//Draw the bullseye
				 gl.glPushMatrix();
				 gl.glTranslatef(posx,-.1f,posz+1f);
				 gl.glScalef(.05f,.05f,.05f);
				 gl.glTranslatef(0,.25f,0);
				 gl.glScalef(4f,4f,4f);
				 g.draw(gl,6);
				 gl.glPopMatrix();
				

				deltaTime = SystemClock.elapsedRealtime()-oldTime;
				oldTime = SystemClock.elapsedRealtime();
				
				//float angle = .090f * ((int)time);
				
				//get slide history and average values
				for(int i=slideHistory.length-1;i>0;i--){
						slideHistory[i]=slideHistory[i-1];
						avg+=slideHistory[i];
				}
				slideHistory[0]=.2f*dx;	
				avg*=.6f;
				posx+=(avg/slideHistory.length);
				
				
				
				
				
				if(posz<100f){
					startVal=0;
					lineStartVal=0;
				}
				else{
				startVal=((int)(posz-100f)/6);
				lineStartVal=((int)(posz-100f)/300);
				}
				
				
				
				
				 startX = 300+(int)posx-60;
				 endX = startX+120;
				 
				 detectCollision(startVal);
				
				 /*
				 //Draw the scoreboard
				 gl.glPushMatrix();
				 gl.glTranslatef(posx+5f,2.5f,posz+2.5f);
				 gl.glRotatef(180,0,0,1);
				 score.draw(gl);
				 gl.glTranslatef(-3f,0,0);
				 score.draw(gl);
				 gl.glTranslatef(4.5f,0,0);
				 gl.glColor4f(215f/255f, 241f/255f, 242f/255f, 1f);
				 ground.draw(gl,true);
				 gl.glPopMatrix();
				 */
				
				//Draw the player
			 
				 gl.glPushMatrix();				
				 gl.glTranslatef(posx,-.75f,posz+.9f);
				 gl.glRotatef(90,1,0,0);
				 gl.glRotatef(-6f*(avg/slideHistory.length),0,0,1);
				 gl.glScalef(.3f,.3f,.3f);
				 playerTri.draw(gl,false);
				 playerTri.draw(gl,true);
				 gl.glRotatef(180,0,1,0);
				 playerTri.draw(gl,false);
				 playerTri.draw(gl,true);
				 gl.glRotatef(90,0,1,0);
				 playerTri.draw(gl,false);
				 playerTri.draw(gl,true);
				 gl.glPopMatrix();
				 

				
				
				 
				 //Draw the hearts
					 for(int i=0; i<lives; i++){
					 gl.glPushMatrix();
					 gl.glTranslatef(posx-1f,.7f,posz+.8f);
					 gl.glTranslatef(-.4f*i,0f,0f);
					 h.draw(gl);
					 gl.glPopMatrix();
					 }
					 
				 //Draw the bullseye
				 gl.glPushMatrix();
				 gl.glTranslatef(posx,-.1f,posz+1f);
				 
				 gl.glScalef(.05f,.05f,.05f);
				 tr.draw(gl, false, true);

				 gl.glPopMatrix();
				 
				
				//Draw the "ground" in strips
				for(int i=0;i<10;i++){
					gl.glPushMatrix();
					gl.glTranslatef(posx,-2f,posz+(6f*i));
					gl.glRotatef(90f,1,0,0);
					gl.glScalef(120f,10f,1f);
					ground.draw(gl,false);
					gl.glPopMatrix();
				}
				
				
				
				//THE SCORE
				//Draw the scoreboard
				 gl.glPushMatrix();
				 gl.glTranslatef(posx+(6f*(.8f/2.5f)),2.5f*(.8f/2.5f),posz+.8f);
				 gl.glRotatef(180,0,0,1);
				 gl.glScalef(1.5f,1,1);
				 gl.glScalef((.8f/2.5f),(.8f/2.5f),(.8f/2.5f));
				 score.draw(gl,false);
				 gl.glScalef(.5f,0f,0f);
				 gl.glPopMatrix();
				 
				
				 
				 //Find out how many digits in the score
				digits=0;
				anss=playerScore;
				while(anss>=1f){
				digits+=1;
				anss=anss/10f;
				}

				//draw the answer indicator
				if(correct){
					if(frameCount-ansFrame<45){
						 gl.glPushMatrix();
						 gl.glTranslatef(posx,.75f,posz+2.5f);
						 gl.glRotatef(180,0,0,1);
						 scoreGood.draw(gl);
			
						 gl.glPopMatrix();
					}
					
					else
						correct=false;
				}
				
				if(wrong){
					if(frameCount-ansFrame<45){
						 gl.glPushMatrix();
						 gl.glTranslatef(posx,1.5f,posz+2.5f);
						 gl.glRotatef(180,0,0,1);
						 scoreBad.draw(gl);
						 gl.glPopMatrix();
					}
					
					else
						wrong=false;
				}
				
				
				//draw the score
				gl.glPushMatrix();
				gl.glTranslatef(posx+(5f*(.8f/2.5f)),2.5f*(.8f/2.5f),posz+.8f);
				gl.glScalef((.4f/2.5f),(.8f/2.5f),(.8f/2.5f));
				for(int i=digits;i>0;i--){
					gl.glTranslatef(-1.6f*i,0,0);
					
				    temp=playerScore/(int)(Math.pow(10,digits-i));
					rr = temp/10;
				    gg = temp-(rr*10);
				    f=5;
					gl.glRotatef(180,0,0,1);
					switch(gg){
					
					case 0:
						zero.draw(gl,false);
						break;
						
					case 1:
						one.draw(gl,false);
						break;
						
					case 2:
						two.draw(gl,false);
						break;
						
					case 3:
						three.draw(gl,false);
						break;
						
					case 4:
						four.draw(gl,false);
						break;
						
					case 5:
						five.draw(gl,false);
						break;
						
					case 6:
						six.draw(gl,false);
						break;
						
					case 7:
					    seven.draw(gl,false);
						break;
						
					case 8:
						eight.draw(gl,false);
						break;
						
					case 9:
						nine.draw(gl,false);
						break;
					}
					gl.glRotatef(-180,0,0,1);
					gl.glTranslatef(1.6f*i,0,0);
					
				}
				 gl.glPopMatrix();
				 
				//GAME INSTRUCTIONS
					if(frameCount/4<=700&&frameCount/4>50){
						if(frameCount/4<150){
							//Display steering message
							gl.glPushMatrix();
							 gl.glTranslatef(posx,.75f,posz+2.5f);
							 gl.glRotatef(180,0,0,1);
							 gl.glScalef(4,1,1);
							 swipeScreenMessage.draw(gl,false);			
							 gl.glPopMatrix();
						}
						if(frameCount/4>=150&&frameCount/4<250){
							//Display building crash message
							gl.glPushMatrix();
							 gl.glTranslatef(posx,.75f,posz+2.5f);
							 gl.glRotatef(180,0,0,1);
							 gl.glScalef(4,1,1);
							 dodgeBuildingsMessage.draw(gl,false);			
							 gl.glPopMatrix();
						}
						if(frameCount/4>=250&&frameCount/4<350){
							//Display gate message
							gl.glPushMatrix();
							 gl.glTranslatef(posx,.75f,posz+2.5f);
							 gl.glRotatef(180,0,0,1);
							 gl.glScalef(4,1,1);
							 flyGatesMessage.draw(gl,false);			
							 gl.glPopMatrix();
						}
						if(frameCount/4>=350&&frameCount/4<450){
							//Display gate message
							gl.glPushMatrix();
							 gl.glTranslatef(posx,.75f,posz+2.5f);
							 gl.glRotatef(180,0,0,1);
							 gl.glScalef(4,1,1);
							 heartMessage.draw(gl,false);			
							 gl.glPopMatrix();
						}
						if(frameCount/4>=450&&frameCount/4<500){
							//Display 3 message
							gl.glPushMatrix();
							 gl.glTranslatef(posx,.75f,posz+2.5f);
							 gl.glRotatef(180,0,0,1);
	
							 three.draw(gl,false);			
							 gl.glPopMatrix();
						}
						if(frameCount/4>=500&&frameCount/4<550){
							//Display 2 message
							gl.glPushMatrix();
							 gl.glTranslatef(posx,.75f,posz+2.5f);
							 gl.glRotatef(180,0,0,1);
							 two.draw(gl,false);			
							 gl.glPopMatrix();
						}
						if(frameCount/4>=550&&frameCount/4<600){
							//Display 1 message
							gl.glPushMatrix();
							 gl.glTranslatef(posx,.75f,posz+2.5f);
							 gl.glRotatef(180,0,0,1);
							 one.draw(gl,false);			
							 gl.glPopMatrix();
						}
						if(frameCount/4>=600&&frameCount/4<650){
							//Display begin message
							gl.glPushMatrix();
							 gl.glTranslatef(posx,.75f,posz+2.5f);
							 gl.glRotatef(180,0,0,1);
							 gl.glScalef(1.5f,1,1);
							 beginsMessage.draw(gl,false);			
							 gl.glPopMatrix();
						}
					}
					
					if(frameCount/4>=600)
						initiate=true;
				
					
					//Crash message
					if(frameCount-crashStartFrame<=20){
						gl.glPushMatrix();
						 gl.glTranslatef(posx,-1f,posz+2.5f);
						 gl.glRotatef(180,0,0,1);
						 gl.glScalef(4,1,1);
						 crashMessage.draw(gl,false);			
						 gl.glPopMatrix();
						
					}
					
					 //pig
					 gl.glPushMatrix();
					 gl.glTranslatef(posx,-.55f,posz+.65f);
					 gl.glScalef(.2f,.2f,.2f);
					 gl.glRotatef(180,0,0,1);
				     gl.glAlphaFunc(GL10.GL_GREATER, 0.1f );
				     gl.glEnable( GL10.GL_ALPHA_TEST );
					 pigBack.draw(gl,true);
					 
					 gl.glPopMatrix();
					
				//Draw
				for(int i=startVal;i<startVal+7;i++){
					int count=0;
					//Draw normal buildings
					if(i%3!=0){
						for(int j=startX; j<endX; j++){
							//Check if there's something there
							if(location[i][j]==true){
								//Draw a cube and its "city block"
								gl.glPushMatrix();
								float x = (float)j-300f;
								float z= (float)i*6f+100f;
								gl.glTranslatef(x,-1f,z);
								gl.glRotatef(90,0,0,1);					
								if(j%2==0){
									cube.draw(gl, 2);
									gl.glPopMatrix();
									gl.glPushMatrix();
									gl.glTranslatef(x-.5f,-1.99f,z);
									gl.glRotatef(90,1,0,0);
									gl.glColor4f(222f/255f,223f/255f,184f/255f,1f);
									gl.glScalef(3f,2f,1);
									ground.draw(gl,true);
									gl.glColor4f(1f,1f,1f,1f);
									gl.glPopMatrix();
								
								}
								else {
									gl.glTranslatef(0,-1f,0);
									cube2.draw(gl, 2);
									gl.glPopMatrix();
									gl.glPushMatrix();
									gl.glTranslatef(x+1f,-1.99f,z);
									gl.glRotatef(90,1,0,0);
									gl.glColor4f(222f/255f,223f/255f,184f/255f,1f);
									gl.glScalef(3f,2f,1);
									ground.draw(gl,true);
									gl.glColor4f(1f,1f,1f,1f);
									gl.glPopMatrix();
									
								}


							}
						}
					}
					//Draw skyscrapers
					if(i%3==0){
						for(int j=startX; j<endX; j++){
							//Check if there's something there
							if(location[i][j]==true){
								//Draw a cube
								gl.glPushMatrix();
								float x = (float)j-300f;
								float z= (float)i*6f+100f;
								gl.glTranslatef(x,0f,z);
								s.draw(gl,2);
								gl.glPopMatrix();
								gl.glPushMatrix();
								gl.glTranslatef(x-.5f,-1.99f,z);
								gl.glRotatef(90,1,0,0);
								gl.glColor4f(222f/255f,223f/255f,184f/255f,1f);
								gl.glScalef(3f,2f,1);
								ground.draw(gl,true);
								gl.glColor4f(1f,1f,1f,1f);
								gl.glPopMatrix();

							}
						}
						
						//Draw heart pickups
						for(int j=startX; j<endX; j++){
							//Check if there's something there
							if(gate[i][j]==true){
								//Draw a gate
								gl.glPushMatrix();
								float x = (float)j-300f;
								float z= (float)i*6f+100f;
								gl.glTranslatef(x,0f,z);
							
								g.draw(gl, (int)(frameCount/12)%4);
								gl.glPopMatrix();
							}

					}

					}
					

					//Draw heart pickups
					for(int j=startX; j<endX; j++){
						//Check if there's something there
						if(gate[i][j]==true){
							//Draw a gate
							gl.glPushMatrix();
							float x = (float)j-300f;
							float z= (float)i*6f+100f;
							gl.glTranslatef(x,0f,z);
							if(i%5==4)
							g.draw(gl, i%5);
							gl.glPopMatrix();
						}

				}
					
				}



				if(gameState==10){
				frameCount+=1;
				
				}
				else
					posz=0f;
				
				if(initiate)
					posz+=.015f*deltaTime;
				
				
				}		
			
			
			
			
			
			
///////////////////DELTA SLALOM /////////////////////////////////////////////////////////////////////////////////////////////////SLALOM
			
	if(gameState==9){
	if(frameCount%4==0){
		dx=0;
	}
	if(frameCount==0){
		posz=50f;
		lives=4;
	}
	gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
  
	gl.glMatrixMode(GL10.GL_MODELVIEW);
	gl.glFogf(GL10.GL_FOG_MODE, fogMode[1]);
	gl.glLoadIdentity();
	GLU.gluLookAt(gl, posx,0,posz, posx,0,posz+5f,0,2f,0);
	frameCount++;
	
	//Draw the bullseye
	 gl.glPushMatrix();
	 gl.glTranslatef(posx,-.1f,posz+1f);
	 gl.glScalef(.05f,.05f,.05f);
	 gl.glTranslatef(0,.25f,0);
	 gl.glScalef(4f,4f,4f);
	 g.draw(gl,6);
	 gl.glPopMatrix();
	
	
	

	deltaTime = SystemClock.elapsedRealtime()-oldTime;
	oldTime = SystemClock.elapsedRealtime();
	
	//float angle = .090f * ((int)time);
	
	//get slide history and average values
	for(int i=slideHistory.length-1;i>0;i--){
			slideHistory[i]=slideHistory[i-1];
			avg+=slideHistory[i];
	}
	slideHistory[0]=.2f*dx;	
	avg*=.6f;
	posx+=(avg/slideHistory.length);
	
	if(posz<100f){
		startVal=0;
	
	}
	else{
	startVal=((int)(posz-100f)/6);

	}
	
	if(posz<400f){
		lineStartVal=0;
	
	}
	else{
	lineStartVal=((int)(posz-400f)/300);

	}
	
	
	
	 startX = 300+(int)posx-60;
	 endX = startX+120;
	 
	 detectCollision(startVal);
	
	 
	
	
	//Draw the player
	 gl.glPushMatrix();
	 gl.glTranslatef(posx,-.75f,posz+.9f);
	 gl.glRotatef(90,1,0,0);
	 gl.glRotatef(-6f*(avg/slideHistory.length),0,0,1);
	 gl.glScalef(.3f,.3f,.3f);
	 playerTri.draw(gl,false);
	 playerTri.draw(gl,true);
	 gl.glRotatef(180,0,1,0);
	 playerTri.draw(gl,false);
	 playerTri.draw(gl,true);
	 gl.glRotatef(90,0,1,0);
	 playerTri.draw(gl,false);
	 playerTri.draw(gl,true);
	 gl.glPopMatrix();
	 
	 
	 
	 //Draw the hearts
		 for(int i=0; i<lives; i++){
		 gl.glPushMatrix();
		 gl.glTranslatef(posx-1f,.7f,posz+.8f);
		 gl.glTranslatef(-.4f*i,0f,0f);
		 h.draw(gl);
		 gl.glPopMatrix();
		 }
		 
	 //Draw the bullseye
	 gl.glPushMatrix();
	 gl.glTranslatef(posx,-.1f,posz+1f);
	 
	 gl.glScalef(.05f,.05f,.05f);
	 tr.draw(gl, false, true);

	 gl.glPopMatrix();
	 
	
	//Draw the "ground" in strips
	for(int i=0;i<10;i++){
		gl.glPushMatrix();
		gl.glTranslatef(posx,-2f,posz+(6f*i));
		gl.glRotatef(90f,1,0,0);
		gl.glScalef(120f,10f,1f);
		ground.draw(gl,false);
		gl.glPopMatrix();
	}
	
	
	//draw the answer indicator
	if(correct){
		if(frameCount-ansFrame<15){
			 gl.glPushMatrix();
			 gl.glTranslatef(posx,1.5f,posz+2.5f);
			 gl.glRotatef(180,0,0,1);
			 scoreGood.draw(gl);
			 gl.glPopMatrix();
		}
		
		else
			correct=false;
	}
	
	if(wrong){
		if(frameCount-ansFrame<15){
			 gl.glPushMatrix();
			 gl.glTranslatef(posx,1.5f,posz+2.5f);
			 gl.glRotatef(180,0,0,1);
			 scoreBad.draw(gl);
			 gl.glPopMatrix();
		}
		
		else
			wrong=false;
	}
	
	//THE SCORE
	//Draw the scoreboard
	 gl.glPushMatrix();
	 gl.glTranslatef(posx+(6f*(.8f/2.5f)),2.5f*(.8f/2.5f),posz+.8f);
	 gl.glRotatef(180,0,0,1);
	 gl.glScalef(1.5f,1,1);
	 gl.glScalef((.8f/2.5f),(.8f/2.5f),(.8f/2.5f));
	 score.draw(gl,false);
	 gl.glScalef(.5f,0f,0f);
	 gl.glPopMatrix();
	 
	
	 
	 //Find out how many digits in the score
	digits=0;
	anss=playerScore;
	while(anss>=1f){
	digits+=1;
	anss=anss/10f;
	}

	
	//draw the score
	gl.glPushMatrix();
	gl.glTranslatef(posx+(5f*(.8f/2.5f)),2.5f*(.8f/2.5f),posz+.8f);
	gl.glScalef((.4f/2.5f),(.8f/2.5f),(.8f/2.5f));
	for(int i=digits;i>0;i--){
		gl.glTranslatef(-1.6f*i,0,0);
		
	    temp=playerScore/(int)(Math.pow(10,digits-i));
		rr = temp/10;
	    gg = temp-(rr*10);
	    f=5;
		gl.glRotatef(180,0,0,1);
		switch(gg){
		
		case 0:
			zero.draw(gl,false);
			break;
			
		case 1:
			one.draw(gl,false);
			break;
			
		case 2:
			two.draw(gl,false);
			break;
			
		case 3:
			three.draw(gl,false);
			break;
			
		case 4:
			four.draw(gl,false);
			break;
			
		case 5:
			five.draw(gl,false);
			break;
			
		case 6:
			six.draw(gl,false);
			break;
			
		case 7:
		    seven.draw(gl,false);
			break;
			
		case 8:
			eight.draw(gl,false);
			break;
			
		case 9:
			nine.draw(gl,false);
			break;
		}
		gl.glRotatef(-180,0,0,1);
		gl.glTranslatef(1.6f*i,0,0);
		
	}
	 gl.glPopMatrix();
	 
	//GAME INSTRUCTIONS
		if(frameCount/4<=750&&frameCount/4>50){
			if(frameCount/4<150){
				//Display steering message
				gl.glPushMatrix();
				 gl.glTranslatef(posx,.75f,posz+2.5f);
				 gl.glRotatef(180,0,0,1);
				 gl.glScalef(4,1,1);
				 followLineMessage.draw(gl,false);			
				 gl.glPopMatrix();
			}
			if(frameCount/4>=150&&frameCount/4<250){
				//Display building crash message
				gl.glPushMatrix();
				 gl.glTranslatef(posx,.75f,posz+2.5f);
				 gl.glRotatef(180,0,0,1);
				 gl.glScalef(4,1,1);
				 blueMessage.draw(gl,false);			
				 gl.glPopMatrix();
			}
			if(frameCount/4>=250&&frameCount/4<350){
				//Display gate message
				gl.glPushMatrix();
				 gl.glTranslatef(posx,.75f,posz+2.5f);
				 gl.glRotatef(180,0,0,1);
				 gl.glScalef(4,1,1);
				 swipeScreenMessage.draw(gl,false);			
				 gl.glPopMatrix();
			}
			if(frameCount/4>=350&&frameCount/4<450){
				//Display gate message
				gl.glPushMatrix();
				 gl.glTranslatef(posx,.75f,posz+2.5f);
				 gl.glRotatef(180,0,0,1);
				 gl.glScalef(4,1,1);
				 dodgeBuildingsMessage.draw(gl,false);			
				 gl.glPopMatrix();
			}
			if(frameCount/4>=450&&frameCount/4<550){
				//Display gate message
				gl.glPushMatrix();
				 gl.glTranslatef(posx,.75f,posz+2.5f);
				 gl.glRotatef(180,0,0,1);
				 gl.glScalef(4,1,1);
				 heartMessage.draw(gl,false);			
				 gl.glPopMatrix();
			}
			if(frameCount/4>=550&&frameCount/4<600){
				//Display 3 message
				gl.glPushMatrix();
				 gl.glTranslatef(posx,.75f,posz+2.5f);
				 gl.glRotatef(180,0,0,1);

				 three.draw(gl,false);			
				 gl.glPopMatrix();
			}
			if(frameCount/4>=600&&frameCount/4<650){
				//Display 2 message
				gl.glPushMatrix();
				 gl.glTranslatef(posx,.75f,posz+2.5f);
				 gl.glRotatef(180,0,0,1);
				 two.draw(gl,false);			
				 gl.glPopMatrix();
			}
			if(frameCount/4>=650&&frameCount/4<700){
				//Display 1 message
				gl.glPushMatrix();
				 gl.glTranslatef(posx,.75f,posz+2.5f);
				 gl.glRotatef(180,0,0,1);
				 one.draw(gl,false);			
				 gl.glPopMatrix();
			}
			if(frameCount/4>=700&&frameCount/4<750){
				//Display begin message
				gl.glPushMatrix();
				 gl.glTranslatef(posx,.75f,posz+2.5f);
				 gl.glRotatef(180,0,0,1);
				 beginsMessage.draw(gl,false);			
				 gl.glPopMatrix();
			}
		}
		
		if(frameCount/4>700)
			initiate=true;
	
		//Crash message
		if(frameCount-crashStartFrame<=20){
			gl.glPushMatrix();
			 gl.glTranslatef(posx,-1f,posz+2.5f);
			 gl.glRotatef(180,0,0,1);
			 gl.glScalef(4,1,1);
			 crashMessage.draw(gl,false);			
			 gl.glPopMatrix();
			
		}	
		
		 //pig
		 gl.glPushMatrix();
		 gl.glTranslatef(posx,-.55f,posz+.65f);
		 gl.glScalef(.2f,.2f,.2f);
		 gl.glRotatef(180,0,0,1);
	     gl.glAlphaFunc(GL10.GL_GREATER, 0.1f );
	     gl.glEnable( GL10.GL_ALPHA_TEST );
		 pigBack.draw(gl,true);
		 
		 gl.glPopMatrix();
		
	//Draw
	for(int i=startVal;i<startVal+7;i++){
		int count=0;
		//Draw normal buildings
		if(i%3!=0){
			for(int j=startX; j<endX; j++){
				//Check if there's something there
				if(location[i][j]==true){
					//Draw a cube and its "city block"
					gl.glPushMatrix();
					float x = (float)j-300f;
					float z= (float)i*6f+100f;
					gl.glTranslatef(x,-1f,z);
					gl.glRotatef(90,0,0,1);					
					if(j%2==0){
						cube.draw(gl, 2);
						gl.glPopMatrix();
						gl.glPushMatrix();
						gl.glTranslatef(x-.5f,-1.99f,z);
						gl.glRotatef(90,1,0,0);
						gl.glColor4f(222f/255f,223f/255f,184f/255f,1f);
						gl.glScalef(3f,2f,1);
						ground.draw(gl,true);
						gl.glColor4f(1f,1f,1f,1f);
						gl.glPopMatrix();
					
					}
					else {
						gl.glTranslatef(0,-1f,0);
						cube2.draw(gl, 2);
						gl.glPopMatrix();
						gl.glPushMatrix();
						gl.glTranslatef(x+1f,-1.99f,z);
						gl.glRotatef(90,1,0,0);
						gl.glColor4f(222f/255f,223f/255f,184f/255f,1f);
						gl.glScalef(3f,2f,1);
						ground.draw(gl,true);
						gl.glColor4f(1f,1f,1f,1f);
						gl.glPopMatrix();
						
					}


				}
			}
		}
		//Draw skyscrapers
		if(i%3==0){
			for(int j=startX; j<endX; j++){
				//Check if there's something there
				if(location[i][j]==true){
					//Draw a cube
					gl.glPushMatrix();
					float x = (float)j-300f;
					float z= (float)i*6f+100f;
					gl.glTranslatef(x,0f,z);
					s.draw(gl,2);
					gl.glPopMatrix();
					gl.glPushMatrix();
					gl.glTranslatef(x-.5f,-1.99f,z);
					gl.glRotatef(90,1,0,0);
					gl.glColor4f(222f/255f,223f/255f,184f/255f,1f);
					gl.glScalef(3f,2f,1);
					ground.draw(gl,true);
					gl.glColor4f(1f,1f,1f,1f);
					gl.glPopMatrix();

				}
			}
			
			//Draw slalom gates
			gl.glPushMatrix();
			gl.glTranslatef(lineLocation[lineStartVal]-180f,0,lineStartVal*300f+400f);
			g.draw(gl,2);
			gl.glPopMatrix();
			gl.glPushMatrix();
			gl.glTranslatef(lineLocation[lineStartVal+1]-180f,0,(lineStartVal+1)*300f+400f);
			g.draw(gl,2);
			gl.glPopMatrix();
		}
		

		//Draw heart pickups
		for(int j=startX; j<endX; j++){
			//Check if there's something there
			if(gate[i][j]==true){
				//Draw a gate
				gl.glPushMatrix();
				float x = (float)j-300f;
				float z= (float)i*6f+100f;
				gl.glTranslatef(x,0f,z);
				if(i%5==4)
				g.draw(gl, i%5);
				gl.glPopMatrix();
			}

	}
		
	}
	
   //draw slalom course
	gl.glPushMatrix();
	gl.glTranslatef(0,-1.9f,0);
	gl.glRotatef(90,1,0,0);
   slalom.draw(gl);
   gl.glPopMatrix();
	
	if(frameCount==0){
		posz=0f;
		setSequence();
	}

	if(gameState==9){
	frameCount+=1;
	
	}
	
	
	if(initiate)
		posz+=.015f*deltaTime;
	
	}
			
///////////////////DELTA VENOM ///////////////////////////////////////////////////////////////////////////////////////////////// VENOM
		
		if(gameState==1){
		if(frameCount%3==0){
			dx=0;
		}
		if(frameCount==0){
			posz=0;
			lives=4;
		}
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
	    
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glFogf(GL10.GL_FOG_MODE, fogMode[1]);
		gl.glLoadIdentity();

		
		
		GLU.gluLookAt(gl, posx,0,posz, posx,0,posz+5f,0,2f,0);

		 //Draw the bullseye
		 gl.glPushMatrix();
		 gl.glTranslatef(posx,-.1f,posz+1f);
		 gl.glScalef(.05f,.05f,.05f);
		 gl.glTranslatef(0,.25f,0);
		 gl.glScalef(4f,4f,4f);
		 g.draw(gl,6);
		 gl.glPopMatrix();
		
		
		
		
		deltaTime = SystemClock.elapsedRealtime()-oldTime;
		oldTime = SystemClock.elapsedRealtime();
		
		//float angle = .090f * ((int)time);
		
		//get slide history and average values
		for(int i=slideHistory.length-1;i>0;i--){
				slideHistory[i]=slideHistory[i-1];
				avg+=slideHistory[i];
		}
		slideHistory[0]=.2f*dx;	
		avg*=.6f;
		posx+=(avg/slideHistory.length);
		
		if(posz<100f)
			startVal=0;
		else
		startVal=((int)(posz-100f)/6);
		
		
		 startX = 300+(int)posx-60;
		 endX = startX+120;
		 
		 detectCollision(startVal);
		
		 /*
		 //Draw the scoreboard
		 gl.glPushMatrix();
		 gl.glTranslatef(posx+5f,2.5f,posz+2.5f);
		 gl.glRotatef(180,0,0,1);
		 score.draw(gl);
		 gl.glTranslatef(-3f,0,0);
		 score.draw(gl);
		 gl.glTranslatef(4.5f,0,0);
		 gl.glColor4f(215f/255f, 241f/255f, 242f/255f, 1f);
		 ground.draw(gl,true);
		 gl.glPopMatrix();
		 */
		//Draw the player
		 gl.glPushMatrix();
		 gl.glTranslatef(posx,-.75f,posz+.9f);
		 gl.glRotatef(90,1,0,0);
		 gl.glRotatef(-6f*(avg/slideHistory.length),0,0,1);
		 gl.glScalef(.3f,.3f,.3f);
		 playerTri.draw(gl,false);
		 playerTri.draw(gl,true);
		 gl.glRotatef(180,0,1,0);
		 playerTri.draw(gl,false);
		 playerTri.draw(gl,true);
		 gl.glRotatef(90,0,1,0);
		 playerTri.draw(gl,false);
		 playerTri.draw(gl,true);
		 gl.glPopMatrix();
		 
		 
		 
		 //Draw the bullseye centerpiece
		 gl.glPushMatrix();
		 gl.glTranslatef(posx,-.1f,posz+1f);
		 gl.glScalef(.05f,.05f,.05f);
		 tr.draw(gl, false, true);
		 gl.glPopMatrix();
		 
		 
		 //Draw the hearts
		 for(int i=0; i<lives; i++){
		 gl.glPushMatrix();
		 gl.glTranslatef(posx-1f,.7f,posz+.8f);
		 gl.glTranslatef(-.4f*i,0f,0f);
		 h.draw(gl);
		 gl.glPopMatrix();
		 }
		

		slalom.draw(gl);
		
		//draw the sequence
		if(sequenceStart){
			t = frameCount-startFrame;

			
			//Get the current element to indicate to the player and draw it
			if(t<sequenceLength*30){
				ans = sequence[t/30];
				gl.glPushMatrix();
				gl.glTranslatef(posx,0,posz+3f);
				if(t<sequence.length*30)
				if(ans!=5)
				g.draw(gl,ans);
				gl.glPopMatrix();
			}
			
			else
				sequenceStart=false;
			
		}
		

		//Draw the "ground" in strips
		for(int i=0;i<10;i++){
			gl.glPushMatrix();
			gl.glTranslatef(posx,-2f,posz+(6f*i));
			gl.glRotatef(90f,1,0,0);
			gl.glScalef(120f,10f,1f);
			ground.draw(gl,false);
			gl.glPopMatrix();
		}
		
		
		//draw the answer indicator
		if(correct){
			if(frameCount-ansFrame<15){
				 gl.glPushMatrix();
				 gl.glTranslatef(posx,1.5f,posz+2.5f);
				 gl.glRotatef(180,0,0,1);
				 scoreGood.draw(gl);
				 gl.glPopMatrix();
			}
			
			else
				correct=false;
		}
		
		if(wrong){
			if(frameCount-ansFrame<15){
				 gl.glPushMatrix();
				 gl.glTranslatef(posx,1.5f,posz+2.5f);
				 gl.glRotatef(180,0,0,1);
				 scoreBad.draw(gl);
				 gl.glPopMatrix();
			}
			
			else
				wrong=false;
		}
		
		//THE SCORE
		//Draw the scoreboard
		 gl.glPushMatrix();
		 gl.glTranslatef(posx+(6f*(.8f/2.5f)),2.5f*(.8f/2.5f),posz+.8f);
		 gl.glRotatef(180,0,0,1);
		 gl.glScalef(1.5f,1,1);
		 gl.glScalef((.8f/2.5f),(.8f/2.5f),(.8f/2.5f));
		 score.draw(gl,false);
		 gl.glScalef(.5f,0f,0f);
		 gl.glPopMatrix();
		 
		
		 
		 //Find out how many digits in the score
		digits=0;
		anss=playerScore;
		while(anss>=1f){
		digits+=1;
		anss=anss/10f;
		}

		
		//draw the score
		gl.glPushMatrix();
		gl.glTranslatef(posx+(5f*(.8f/2.5f)),2.5f*(.8f/2.5f),posz+.8f);
		gl.glScalef((.4f/2.5f),(.8f/2.5f),(.8f/2.5f));
		for(int i=digits;i>0;i--){
			gl.glTranslatef(-1.6f*i,0,0);
			
		    temp=playerScore/(int)(Math.pow(10,digits-i));
			rr = temp/10;
		    gg = temp-(rr*10);
		    f=5;
			gl.glRotatef(180,0,0,1);
			switch(gg){
			
			case 0:
				zero.draw(gl,false);
				break;
				
			case 1:
				one.draw(gl,false);
				break;
				
			case 2:
				two.draw(gl,false);
				break;
				
			case 3:
				three.draw(gl,false);
				break;
				
			case 4:
				four.draw(gl,false);
				break;
				
			case 5:
				five.draw(gl,false);
				break;
				
			case 6:
				six.draw(gl,false);
				break;
				
			case 7:
			    seven.draw(gl,false);
				break;
				
			case 8:
				eight.draw(gl,false);
				break;
				
			case 9:
				nine.draw(gl,false);
				break;
			}
			gl.glRotatef(-180,0,0,1);
			gl.glTranslatef(1.6f*i,0,0);
			
		}
		 gl.glPopMatrix();
		 
		//Crash message
			if(frameCount-crashStartFrame<=20){
				gl.glPushMatrix();
				 gl.glTranslatef(posx,-1f,posz+2.5f);
				 gl.glRotatef(180,0,0,1);
				 gl.glScalef(4,1,1);
				 crashMessage.draw(gl,false);			
				 gl.glPopMatrix();
				
			}
		
		//Draw
		for(int i=startVal;i<startVal+7;i++){
			int count=0;
			//Draw normal buildings
			if(i%3!=0){
				for(int j=startX; j<endX; j++){
					//Check if there's something there
					if(location[i][j]==true){
						//Draw a cube and its "city block"
						gl.glPushMatrix();
						float x = (float)j-300f;
						float z= (float)i*6f+100f;
						gl.glTranslatef(x,-1f,z);
						gl.glRotatef(90,0,0,1);					
						if(j%2==0){
							cube.draw(gl, 2);
							gl.glPopMatrix();
							gl.glPushMatrix();
							gl.glTranslatef(x-.5f,-1.99f,z);
							gl.glRotatef(90,1,0,0);
							gl.glColor4f(222f/255f,223f/255f,184f/255f,1f);
							gl.glScalef(3f,2f,1);
							ground.draw(gl,true);
							gl.glColor4f(1f,1f,1f,1f);
							gl.glPopMatrix();
						
						}
						else {
							gl.glTranslatef(0,-1f,0);
							cube2.draw(gl, 2);
							gl.glPopMatrix();
							gl.glPushMatrix();
							gl.glTranslatef(x+1f,-1.99f,z);
							gl.glRotatef(90,1,0,0);
							gl.glColor4f(222f/255f,223f/255f,184f/255f,1f);
							gl.glScalef(3f,2f,1);
							ground.draw(gl,true);
							gl.glColor4f(1f,1f,1f,1f);
							gl.glPopMatrix();
							
						}


					}
				}
			}
			//Draw skyscrapers
			if(i%3==0){
				for(int j=startX; j<endX; j++){
					//Check if there's something there
					if(location[i][j]==true){
						//Draw a cube
						gl.glPushMatrix();
						float x = (float)j-300f;
						float z= (float)i*6f+100f;
						gl.glTranslatef(x,0f,z);
						s.draw(gl,2);
						gl.glPopMatrix();
						gl.glPushMatrix();
						gl.glTranslatef(x-.5f,-1.99f,z);
						gl.glRotatef(90,1,0,0);
						gl.glColor4f(222f/255f,223f/255f,184f/255f,1f);
						gl.glScalef(3f,2f,1);
						ground.draw(gl,true);
						gl.glColor4f(1f,1f,1f,1f);
						gl.glPopMatrix();

					}
				}
			}
			

			//Draw gates
			for(int j=startX; j<endX; j++){
				//Check if there's something there
				if(gate[i][j]==true){
					//Draw a gate
					gl.glPushMatrix();
					float x = (float)j-300f;
					float z= (float)i*6f+100f;
					gl.glTranslatef(x,0f,z);
					g.draw(gl, i%5);
					gl.glPopMatrix();
				}

		}
		}
	
		
		if(frameCount==0){
			posz=0f;
			setSequence();
		}

		if(gameState==1){
		frameCount+=1;
		posz+=.015f*deltaTime;
		}
		else
			posz=0f;
		}
		
		frameCount+=1;
	}
	
	public static void startGame(){
		posz=0;
		gameState=1;
	}


	public void onSurfaceChanged(GL10 gl, int width, int height){

		
	}

	public void onSurfaceCreated(GL10 gl, EGLConfig eglConfig){
		
		//Blending
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		
		//Camera mode
		gl.glViewport(0, 0, width,height);
		float ratio = (float) width/height;
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glFrustumf(-ratio,ratio,-1,1,.5f,45);
		
		//Fog and mist
		gl.glFogf(GL10.GL_FOG_MODE, fogMode[fogFilter]);  //Fog mode
		gl.glFogfv(GL10.GL_FOG_COLOR, fogColorBuffer);	  //Set fog color
		gl.glFogf(GL10.GL_FOG_DENSITY, 0.04f);			  //Set fog density
		gl.glHint(GL10.GL_FOG_HINT, GL10.GL_DONT_CARE);   //Fog hint
		gl.glFogf(GL10.GL_FOG_START, 1.0f);				  //Fog start depth
		gl.glFogf(GL10.GL_FOG_END, 42.0f);				  //Fog end depth
		gl.glEnable(GL10.GL_FOG);						  //Enable fog
		
		// Load the textures for everything
		gl.glDisable(GL10.GL_DITHER);
		cube.loadGLTexture(gl, context);
		cube2.loadGLTexture(gl, context);
		scoreGood.loadGLTexture(gl, context);
		scoreBad.loadGLTexture(gl, context);
		mainMenu.loadGLTexture(gl, context);
		aboutMenu.loadGLTexture(gl, context);
		howtoMenu.loadGLTexture(gl, context);
		playMenu.loadGLTexture(gl, context);
		deadMessage.loadGLTexture(gl, context);
	    deltaRun.loadGLTexture(gl, context);
	    deltaSlalom.loadGLTexture(gl, context);
		deltaHero.loadGLTexture(gl, context);
		deltaVenom.loadGLTexture(gl, context);
		s.loadGLTexture(gl, context);
		score.loadGLTexture(gl, context);
		pigFace.loadGLTexture(gl, context);
		pigBack.loadGLTexture(gl, context);
		
		//Numbers
		zero.loadGLTexture(gl, context);
		one.loadGLTexture(gl, context);
		two.loadGLTexture(gl, context);
		three.loadGLTexture(gl, context);
		four.loadGLTexture(gl, context);
		five.loadGLTexture(gl, context);
		six.loadGLTexture(gl, context);
		seven.loadGLTexture(gl, context);
		eight.loadGLTexture(gl, context);
		nine.loadGLTexture(gl, context);
		
		
		//messages
		beginsMessage.loadGLTexture(gl, context); 
		dodgeBuildingsMessage.loadGLTexture(gl, context);
		swipeScreenMessage.loadGLTexture(gl, context);
		followLineMessage.loadGLTexture(gl, context);
		flyGatesMessage.loadGLTexture(gl, context);
		coltrioneMessage.loadGLTexture(gl, context);
		coltritwoMessage.loadGLTexture(gl, context);
		coltrithreeMessage.loadGLTexture(gl, context);
		coltrifourMessage.loadGLTexture(gl, context);
		crashMessage.loadGLTexture(gl, context);
		heartMessage.loadGLTexture(gl, context);
		blueMessage .loadGLTexture(gl,context);
		
		
		
		
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glShadeModel(GL10.GL_SMOOTH);
		gl.glClearColor(215f/255f, 241f/255f, 242f/255f, 1f);
		gl.glClearDepthf(1f);
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glDepthFunc(GL10.GL_LEQUAL);
		
		//Perspective calculations
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
		
		

	}
}
