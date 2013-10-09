import processing.opengl.*;

class SceneManager { 
  
  // Decide private variable
  private int  BOXCount = 5;
  private float   rx, ry, rz;
  private float xstart, ystart, zstart;
  private float xnoise, ynoise, znoise;
  private Particle[] particles;
  
  // Decide constructor
  SceneManager ()
  {
      // Set noise start variable
      xstart = random(10);
      ystart = random(10);
       
       // Create particle
      particles = new Particle[100];
     for(int i = 0; i < 100; i++){
        particles[i] = new Particle();
     }
  } 
  
  // Decide draw function
  void drawScene(ControlManager ctr)
  {
    switch((int)ctr.sceneNumber){
      case 0:
      scene0(ctr);
      break;
      case 1:
      scene1(ctr);
      break;
      case 2:
      scene2(ctr);
      break;
      case 3:
      scene3(ctr);
      break;
    }
  }
  
  // Scene0
  void scene0(ControlManager ctr)
  {
    updateNoise();
     for(int i = 0; i < 100; i++){
        rotateX(rx / ctr.speed);
        rotateY(ry / ctr.speed);
        rotateZ(rz / ctr.speed);
        pushMatrix();
        translate(0, 0, i * 100);
        setLooks(ctr, i*ctr.redOffset, i*ctr.greenOffset, i*ctr.blueOffset);
        box(ctr.objSize + ctr.customSize + (ctr.soundLeft() * i));
        popMatrix();
      
        rx += 1;
        ry += 5;
        rz += 7;
    }
    resetVariable();
  }
  
  // Scene1
  void scene1(ControlManager ctr)
  {
    updateNoise();
    for(int j = 0; j < 10; j++){
      ynoise += 0.1;
         for(int i = 0; i < 10; i++){
            xnoise += 0.1;
            float noiseFactor = noise(xnoise, ynoise) * ctr.noiseCo;
            rotateX(rx / ctr.speed);
            rotateY(ry / ctr.speed);
            rotateZ(rz / ctr.speed);
          pushMatrix();
          translate(i, j, 0);
          setLooks(ctr, i*ctr.redOffset, i*ctr.greenOffset, i*ctr.blueOffset);
          box(ctr.objSize + ctr.customSize + (ctr.soundLeft() * i * j) + noiseFactor);
          popMatrix();
          
          rx += 1;
          ry += 5;
          rz += 7;
         }
      }
      resetVariable();
  }
  // Scene2
  void scene2(ControlManager ctr)
  {
    updateNoise();
    
      for(int z = -BOXCount; z < BOXCount; z++){
        for(int j = -BOXCount; j < BOXCount; j++){
          ynoise += 0.1;
           for(int i = -BOXCount; i < BOXCount; i++){
            xnoise += 0.1;
            float noiseFactor = noise(xnoise, ynoise) * ctr.noiseCo;
//            println(noiseFactor);
            pushMatrix();
            translate(i * ctr.objSize * 2, j * ctr.objSize * 2, z * ctr.objSize * 2);
            rotateX(rx / ctr.speed + noiseFactor / 1000);
            rotateY(ry / ctr.speed + noiseFactor / 1000);
            rotateZ(rz / ctr.speed + noiseFactor / 1000);
            setLooks(ctr, i * ctr.redOffset + (i * ctr.rainbowLevel), i * ctr.greenOffset + (j * ctr.rainbowLevel), i * ctr.blueOffset + (z * ctr.rainbowLevel));
            box( ctr.objSize + ctr.customSize + (ctr.soundLeft() * i * j * z) + (noiseFactor / 3));
            if(ctr.PARTICLE){
              drawParticle(i, j, z);
            }
            popMatrix();
            
            rx += 0.003;
            ry += 0.005;
            rz += 0.007;
           }
        }
      }
  }

  // Scene3
  void scene3(ControlManager ctr)
  { 
    updateNoise();
      for(int z = -5; z < 5; z++){
        for(int j = -5; j < 5; j++){
          ynoise += 0.1;
           for(int i = -5; i < 5; i++){
             xnoise += 0.1;
            float noiseFactor = noise(xnoise, ynoise) * ctr.noiseCo;
            pushMatrix();
            translate(i * ctr.objSize*2, j * ctr.objSize*2, z * ctr.objSize*2);
            rotateX(rx / ctr.speed + noiseFactor / 1000);
            rotateY(ry / ctr.speed + noiseFactor / 1000);
            rotateZ(rz / ctr.speed + noiseFactor / 1000);
            setLooks(ctr, i*ctr.redOffset, i*ctr.greenOffset, i*ctr.blueOffset);
            sphereDetail(ctr.sd);
            sphere( ctr.objSize + ctr.customSize + (ctr.soundLeft() * i * j * z) + noiseFactor);
            if(ctr.PARTICLE){
              drawParticle(i, j, z);
            }
            popMatrix();
            
            rx += 0.003;
            ry += 0.005;
            rz += 0.007;
           }
        }
      }
  }
  
  void resetVariable()
  {
    // Configure rotation value
      if(rx > 2147483646){
        rx = 0;
      }
      if(ry > 2147483646){
        ry = 0;
      }
      if(rz > 2147483646){
        rz = 0;
      }
  }
  
  void setLooks(ControlManager ctr, float r, float g, float b)
  {
   // Configure color
    fill(ctr.fillRed + r, ctr.fillGreen + g, ctr.fillBlue + b, ctr.fillAlpha);
    stroke(ctr.lineRed + r, ctr.lineGreen + g, ctr.lineBlue + b, ctr.lineAlpha);
    strokeWeight(ctr.lineWeight);
    
    if(!ctr.FILL){
        noFill();
    }
    if(!ctr.STROKE){
        noStroke();
    }
    if(!ctr.LIGHT){
        noLights();
    } 
  }
  
  void updateNoise()
  {
    xstart += 0.01;
    ystart += 0.01;
    
    xnoise = xstart;
    ynoise = ystart;
  }
  
  void drawParticle(int i, int j, int z)
  {
    if(ctr.soundRight() > 2.0){
      particles[0].setDuration(ctr);
      stroke(255, ctr.particleLineAlpha);
      line(0, 0, 0, i * ctr.objSize * 2, j * ctr.objSize * 2, z * ctr.objSize * 2);
    } 
  }
}


