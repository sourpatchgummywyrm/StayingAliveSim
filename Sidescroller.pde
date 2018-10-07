//Mover
//Player Data
PVector position;
PVector leftVel, rightVel, downVel;
boolean left, right;
boolean onGround; //mechanic broken currently
float w = 32, h = 55; //width and height for hitbox player
//AirEnemy Data
float airEnemyDamage = 10;
ArrayList <AirEnemy> e; 
int enemies;
boolean redFlag;
float enemyx, enemyy;
//GroundEnemy Data
float groundEnemyDamage = 25;
//Settings
float incVel = 3;
float gravity = 0.7;
Map map1;
Player mebibit;
void setup() {
  size(1000, 600);
  enemies = 0;
  e = new ArrayList <AirEnemy>();
  
  mebibit = new Player("Mebibit");
  
  map1 = new Map("mapV2.txt");
}
void draw() {
  mebibit.move();
  map1.customCollision();
  map1.displayMap(); //displays map
  
  for(int f = 0; f < e.size(); f++) {
    e.get(f).seek(position);
    e.get(f).update();
    e.get(f).display();
    e.get(f).collision();
    //System.out.println(e.get(f).location);
  }
  mebibit.display(); //displays character
  
  for(int h = 0; h < e.size(); h++) {
    e.get(h).collision();
    e.get(h).enemyCollision();
  }
}
//--------------------------Inputs-----------------------
void keyPressed() {
  switch(key) {
    //Movement input:
    case 'a':
      left = true;
      break;
    case 'd':
      right = true;
      break;
    case 'w':
      mebibit.jump();
      break;
    //Command keys:
    case 'r':
      reset();
      break;
    case '0':
      spawnAirEnemy();
      break;
    
  }
}
void keyReleased() {
  switch(key) {
    case 'a':
      left = false;
      break;
    case 'd':
      right = false;
      break;
    case 'w':
      
      break;
  }
}
//--------------------------Commands-----------------------
void reset() {
  setup();
  e.clear();
}
void spawnAirEnemy() {
  enemyx = random(0, 1000);
  enemyy = random(-50, 0);
  maxspeedpu = random(1.5, 6);
  maxforcepu = random(.05, .1);
  e.add(new AirEnemy(enemyx, enemyy, .05, maxspeedpu)); 
  
  enemies++;
  System.out.println(enemies);
}
