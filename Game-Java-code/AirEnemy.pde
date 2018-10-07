  float maxforcepu;
  float maxspeedpu;
  
class AirEnemy {
  PVector location;
  PVector velocity;
  PVector acceleration;
  float r;
  float maxforce;
  float maxspeed;
  AirEnemy(float x, float y, float mf, float ms) {
    acceleration = new PVector(0,0);
    velocity = new PVector(0,0);
    location = new PVector(x,y);
    r = 13.0;
    maxforce = mf;
    maxspeed = ms;
  }
  
  void applyForce(PVector force) {
    acceleration.add(force);
  }
  
  void update() {
    velocity.add(acceleration);
    velocity.limit(maxspeed);
    location.add(velocity);
    acceleration.mult(0);
    
    velocity.x *= 0.99;
    velocity.y *= 0.99;
  }

  void seek(PVector target) {
    PVector desired = PVector.sub(target, location);
    desired.normalize();
    desired.mult(maxspeed);
    PVector steer = PVector.sub(desired, velocity);
    steer.limit(maxforce);
    applyForce(steer);
  }
  
  void display() {
    //float theta = velocity.heading() + PI/2;
    fill(150);
    stroke(0);
    pushMatrix();
    
    rect(location.x, location.y, 25, 40);
    popMatrix();
  }
  void collision() {
    if(e.size() > 0) {
      for(int i = 0; i < e.size(); i++) {
        if(dist(position.x, position.y, location.x, location.y) <= w) {
          redFlag = true;
        }
      }
    }
  }
  
  void enemyCollision() {
    for(int i = 0; i < e.size(); i++) {
        if(dist(position.x, position.y, e.get(i).location.x, e.get(i).location.y) > w) {
          redFlag = false;
        }
     } 
  }
  
}
