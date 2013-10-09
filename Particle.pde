
class Particle { 
  
  // Decide private variable
  private int DURATION_MAX = 1000;
  private float duration;
  
  // Decide constructor
  Particle ()
  {
      // Decide this duration
      duration = -1;
  }
  
  void setDuration (ControlManager c){
    if(duration <= 0){
      duration = DURATION_MAX;
     }
     duration--;
      // Set alpha
     noStroke();
     fill(255, 255, 255, c.particleFillAlpha);
     sphereDetail(10);
     sphere(30);
  }  
}
