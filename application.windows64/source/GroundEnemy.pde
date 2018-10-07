class GroundEnemy {
  PVector location;
  PVector velocity;
  float r;
  float appliedX, appliedY = 0;
  float maxforce;
  float maxspeed;
  boolean onGround;
  int checkRadius = 5;
  GroundEnemy(float x, float y, float mf, float ms) {
    velocity = new PVector(0,0);
    location = new PVector(x,y);
    r = 13.0;
    maxforce = mf;
    maxspeed = ms;
  }
  
  void applyForce(float forceX) {
    velocity.y += appliedY/2;
    velocity.x += forceX;
  }
  
  void update() {
    collision();
    if(location.y - h/5 > position.y && onGround == true) {
      velocity.y = -12; 
    }
    if(!onGround) {
      appliedY = 0;
      if(velocity.y <= 20){
        velocity.y += gravity;
      }
   }
   
    //velocity.limit(maxspeed);
    location.add(velocity);
  }

  void seek(PVector target) {
    PVector desired = PVector.sub(target, location);
    desired.normalize();
    //desired.mult(maxspeed);
    PVector steer = PVector.sub(desired, velocity);
    //steer.limit(maxforce);
    //if(position.y > location.y) {
    //  steer.y = 1;
    //}
    applyForce(steer.x);
  }
  
  void display() {
    //float theta = velocity.heading() + PI/2;
    fill(100);
    stroke(0);
    rect(location.x, location.y - h/5, 25, 40);
  }
  
  void collision() {
    //int xGridPos = floor(position.x / incSize);
    //int yGridPos = floor(position.y / incSize);
  //  int xStart = (int)location.x - checkRadius;
  //  int xEnd = (int)location.x + checkRadius + 1;
  //  int yStart = (int)location.y - checkRadius;
  //  int yEnd = (int)location.y + checkRadius + 1;
  //  for (int i = xStart; i < xEnd; i++) {
  //    for (int j = yStart; j < yEnd; j++) {
  //      if (i >= 0 && i < xRes && j >= 0 && j < yRes) {
  //        if (colored[i][j]) {
            
  //          if (position.y >= j*incSize - h && position.y + h <= j*incSize+1*incSize && 
  //          position.x < i*incSize+incSize && position.x + w > i*incSize && downVel.y > 0) { //+5 is for priority for other collisions
  //            position.y = j*incSize - h;
  //            downVel.y = 0;
  //            //onGround = true;
  //          }
            
  //          else if (position.y < j*incSize+incSize && position.y > j*incSize &&
  //          position.x < i*incSize+incSize && position.x + w > i*incSize && downVel.y < 0) { //bottom collision
  //            downVel.y *= -1;
  //            position.y = j*incSize+incSize;
  //          }
            
  //          if (position.x + w >= i*incSize && position.x + w <= i*incSize+1*incSize &&
  //          position.y + h > j*incSize && position.y < j*incSize+1*incSize && rightVel.x > 0 ) { //move right collision
  //            position.x = i*incSize - w;
  //            //leftVel.x = -incVel;
  //            //println(position.x + w + " " + i*incSize);
  //          }
            
  //          else if (position.x < i*incSize+incSize && position.x > i*incSize &&
  //          position.y + h > j*incSize && position.y < j*incSize+1*incSize && leftVel.x < 0) { //move left collision
  //            position.x = i*incSize+incSize;
  //          }
  //        }
  //      }
  //    }
  //  }
  }
}
