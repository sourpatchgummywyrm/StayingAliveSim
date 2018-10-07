class Player{
  String name;
  float health;
  float comboMultiplier;
  
  public Player(String n) {
    name = n;
    
    health = 100;
    comboMultiplier = 1;
    
    position = new PVector(width / 2, height * 2.5 / 6);
    leftVel = new PVector(0, 0);
    rightVel = new PVector(0, 0);
    downVel = new PVector(0, 0);
    
    left = false;
    right = false;
    onGround = false;
  
  }
  
  void move() {
    if (left) {
      leftVel = new PVector(-incVel, 0);
    } else {
      leftVel = new PVector(0, 0);
    }
    if (right) {
      rightVel = new PVector(incVel, 0);
    } else {
      rightVel = new PVector(0, 0);
    }
    if (!onGround) {
      downVel.y += gravity;
    } else {
      downVel = new PVector(0, 0);
    }
  
    position = position.add(leftVel);
    position = position.add(rightVel);
    position = position.add(downVel);
    
    downVel.limit(15);
  }
  
  void jump() {
    if (downVel.y == 0) {
      onGround = false;
      downVel = new PVector(0, -13);
      //System.out.print("JUMP");
    }  
  }
  void display() {
    stroke(1);
    rectMode(CORNER);
    fill(100, 150, 175);
    for(int h = 0; h < e.size(); h++) {
      if(e.get(h).airHit == true){
        fill(255, 0, 0);
      } 
    }
    rect(position.x, position.y, w, h);
    
    fill(0);
    text(str(health), position.x, position.y - 10);
  }
  
  //-------------------------------------------------------
  
  void testPlayer() {
    if (health < 0) {
      die();
    }    
  }
  
  void testDamage() {
    //if (timeStart > 1000) {
      health -= airEnemyDamage;
    //  timeSaved = millis();
    //}
  }
  
  void die() {
    reset();
  }
  
}
