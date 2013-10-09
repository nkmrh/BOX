import fullscreen.*;
import japplemenubar.*;
import processing.opengl.*;
import javax.media.opengl.*;

// Decide scene manager
SceneManager    sceneManager;

// Decide control manager
ControlManager  ctr;

// Decide the fullscreen object
SoftFullScreen  fs;

// Decide screen size
int myWIDTH = 720;
int myHEIGHT = 400;

float degreeX, degreeY, degreeZ;

// Decide PGraphicsOpenGL and GL objects
PGraphicsOpenGL pgl;
GL  gl;
//GL2  gl;

void setup()
{ 
  // Create controlmanager
  ctr = new ControlManager(this);
  
  // Create scenemanager
  sceneManager = new SceneManager();
  
  // Create the fullscreen object
  fs = new SoftFullScreen(this);
  
  // Configure screen
  size(myWIDTH, myHEIGHT, OPENGL);
  
  // Define GL objects
  pgl = (PGraphicsOpenGL)g;
  gl = pgl.gl;
//gl = pgl.beginPGL().gl.getGL2();
  
  background(0);
  frameRate(30);
  smooth();
  
  degreeX = degreeY = degreeZ = 0;
}

void draw()
{
  // Re-creates the default perspective
    float fov = PI/ctr.fovLevel;
    float cameraZ = (height/2.0) / tan(fov/2.0);
    perspective(fov, float(width)/float(height), cameraZ/10.0, cameraZ*10.0);
  
    background(0);
  
    pgl.beginGL();
//    pgl.beginPGL();
    
    // This fixes the overlap issue
    gl.glDisable(GL.GL_DEPTH_TEST);
    
    // Turn on the blend mode
    gl.glEnable(GL.GL_BLEND);
    
    // Define the blend mode
    gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE);
    
    pgl.endGL();
//    pgl.endPGL();
    
    // Configure lights
    pointLight(100, 102, 126, 0, 0, 0);
    ambientLight(102, 102, 102);
    lightSpecular(204, 204, 204);
    specular(255, 255, 255);
    
    // Configure world
    translate(width/2, height/2, 0);
    
    // Configure camera position
    if(!ctr.DOLLY) {
      camera(ctr.eyeX, ctr.eyeY, ctr.eyeZ, ctr.centerX, ctr.centerY, ctr.centerZ, 0.0, 1.0, 0.0);
    }
    else {
      // Update degreeX
      if(degreeX > 360){
        degreeX = 0;
      }
      else {
        degreeX+=0.0001;
      }
      
      // Update degreeY
      if(degreeY > 360){
        degreeY = 0;
      }
      else {
        degreeY+=0.0005;
      }
      
      // Update degreeZ
      if(degreeZ > 360){
        degreeZ = 0;
      }
      else {
        degreeZ+=0.0007;
      }
      
      // Configure distances
      float distanceX, distanceY, distanceZ;
      distanceX = sin(degreeX) * 10000;
      distanceY = sin(degreeY) * 10000;
      distanceZ = sin(degreeZ) * 10000;
      camera(distanceX, distanceY, distanceZ, ctr.centerX, ctr.centerY, ctr.centerZ, 0.0, 1.0, 0.0);
    }
    
    // Animate Camera
//    beginCamera();
//    endCamera();
    
    // Decide global rotate animation
    rotateX(ctr.grX);
    rotateY(ctr.grY);
    rotateZ(ctr.grZ);
    
    sceneManager.drawScene(ctr);
    
         //    filter(THRESHOLD);
//    filter(GRAY);
//    filter(INVERT);
//    filter(POSTERIZE, 4);
//    filter(BLUR, 6); 
    
    // Update global varibles
    ctr.grX += ctr.grXSpeed;
    ctr.grY += ctr.grYSpeed;
    ctr.grZ += ctr.grZSpeed;
    
    if(ctr.REC){
      saveFrame();
    }
}



