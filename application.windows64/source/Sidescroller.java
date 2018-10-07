import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import processing.sound.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Sidescroller extends PApplet {

//Mover

//Sound Data

boolean hasFinished = true;
SoundFile alive; 
SoundFile dead; 

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
float enemyx, enemyy;

//GroundEnemy Data
float groundEnemyDamage = 25;

//Settings
float incVel = 3;
float gravity = 0.7f;

Map map1;
Player mebibit;

public void setup() {
  
  enemies = 0;
  
  e = new ArrayList <AirEnemy>();
  
  mebibit = new Player("Mebibit");
  map1 = new Map("mapV2.txt");
  alive = new SoundFile(this, "Deja Vu.mp3");
  dead = new SoundFile(this, "StayingAlive.mp3");
 
  //checkMusic();
  if(dead.isPlaying()==false)
  dead.stop();
  if(alive.isPlaying()==false) {
  alive.loop();
  }
}
public void draw() {
  mebibit.move();
  map1.customCollision();
  map1.displayMap(); //displays map
  
  for(int f = 0; f < e.size(); f++) {
    e.get(f).seek(position);
    e.get(f).update();
    e.get(f).display();
    e.get(f).collision();
    //if (e.get(f).airHit == true) {
    //  timeStart = millis() - timeSaved;
    //  //mebibit.testDamage();
    //}
  }
    mebibit.testPlayer();
    mebibit.display(); 
  for(int f = 0; f < e.size(); f++) {  
    e.get(f).enemyCollision();
  }
    //System.out.println(e.get(f).location);
    //displays character
  checkMusic();
}

public void checkMusic() {
  if(mebibit.health > 0 && dead.isPlaying()) {
    dead.stop();
  }
  if(mebibit.health == 0 && alive.isPlaying()) {
    alive.stop();
    dead.play();
  }
  
}
//--------------------------Inputs-----------------------

public void keyPressed() {
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
public void keyReleased() {
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

public void reset() {
  dead.stop();
  alive.stop();
  setup();
  e.clear();
}

public void spawnAirEnemy() {
  enemyx = random(0, 1000);
  enemyy = random(-50, 0);
  maxspeedpu = random(1.5f, 6);
  maxforcepu = random(.05f, .1f);
  e.add(new AirEnemy(enemyx, enemyy, .05f, maxspeedpu)); 
  
  enemies++;
  System.out.println(enemies);
}
float maxforcepu;
  float maxspeedpu;
  
class AirEnemy {
  PVector location;
  PVector velocity;
  PVector acceleration;
  float r;
  float maxforce;
  float maxspeed;
  boolean airHit;
  long timeStart;
  long timeSaved;
  AirEnemy(float x, float y, float mf, float ms) {
    acceleration = new PVector(0,0);
    velocity = new PVector(0,0);
    location = new PVector(x,y);
    r = 13.0f;
    maxforce = mf;
    maxspeed = ms;
  }
  
  public void applyForce(PVector force) {
    acceleration.add(force);
  }
  
  public void update() {
    velocity.add(acceleration);
    velocity.limit(maxspeed);
    location.add(velocity);
    acceleration.mult(0);
    
    //velocity.x *= 0.99;
    //velocity.y *= 0.99;
  }

  public void seek(PVector target) {
    PVector desired = PVector.sub(target, location);
    desired.normalize();
    desired.mult(maxspeed);
    PVector steer = PVector.sub(desired, velocity);
    steer.limit(maxforce);
    applyForce(steer);
  }
  
  public void display() {
    //float theta = velocity.heading() + PI/2;
    fill(150);
    stroke(0);
    pushMatrix();
    
    rect(location.x, location.y, 25, 40);
    popMatrix();
  }
  
  public void collision() {
    if(e.size() > 0) {
      for(int i = 0; i < e.size(); i++) {
        if(dist(position.x, position.y, location.x, location.y) <= w) {
          airHit = true;
          timeStart = millis() - timeSaved;
          if (timeStart > 1000) {
            mebibit.testDamage();
            timeSaved = millis();
          }
        }
      }
    }
  }
  
  public void enemyCollision() {
    for(int i = 0; i < e.size(); i++) {
        if(dist(position.x, position.y, e.get(i).location.x, e.get(i).location.y) > w) {
          airHit = false;
        }
     } 
  }
  
}
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
    r = 13.0f;
    maxforce = mf;
    maxspeed = ms;
  }
  
  public void applyForce(float forceX) {
    velocity.y += appliedY/2;
    velocity.x += forceX;
  }
  
  public void update() {
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

  public void seek(PVector target) {
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
  
  public void display() {
    //float theta = velocity.heading() + PI/2;
    fill(100);
    stroke(0);
    rect(location.x, location.y - h/5, 25, 40);
  }
  
  public void collision() {
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
class Map {
  boolean[][] colored;
  int xRes = 40;
  int yRes = 24;
  int incSize = 1000/xRes;
  int checkRadius = 4;
  
  String[] line = new String[xRes * yRes];
  
  public Map(String mapText) { //mapText = "map.txt"
    colored = new boolean[xRes][yRes];
    loadMap(mapText);
  }
  
  public void loadMap(String map) {
    int lineLocation = 0;
    line = loadStrings(map); //map is "map.txt"
    
    for (int i = 0; i < xRes; i++) {
      for (int j = 0; j < yRes; j++) {
        colored[i][j] = PApplet.parseBoolean(line[lineLocation]);
        lineLocation++;
      }
    }
  }
  
  public void displayMap() {
    noStroke();
    rectMode(CORNER);
    for (int i = 0; i < xRes; i++) {
      for (int j = 0; j < yRes; j++) {
        if (colored[i][j]) { //if tile is colored, fill black
          fill(0);
        }
        else { //if tile is not colored, no fill
          fill(255);
        }
        rect(i * incSize, j * incSize, incSize, incSize);
      }
    }
  }
  
  public void customCollision() {
    int xGridPos = floor(position.x / incSize);
    int yGridPos = floor(position.y / incSize);
    int xStart = xGridPos - checkRadius;
    int xEnd = xGridPos + checkRadius + 1;
    int yStart = yGridPos - checkRadius;
    int yEnd = yGridPos + checkRadius + 1;
    
    for (int i = xStart; i < xEnd; i++) {
      for (int j = yStart; j < yEnd; j++) {
        if (i >= 0 && i < xRes && j >= 0 && j < yRes) {
          if (colored[i][j]) {
            
            if (position.y >= j*incSize - h && position.y + h <= j*incSize+1*incSize && 
            position.x < i*incSize+incSize && position.x + w > i*incSize && downVel.y > 0) { //+5 is for priority for other collisions
              position.y = j*incSize - h;
              downVel.y = 0;
              //onGround = true;
            }
            
            else if (position.y < j*incSize+incSize && position.y > j*incSize &&
            position.x < i*incSize+incSize && position.x + w > i*incSize && downVel.y < 0) { //bottom collision
              downVel.y *= -1;
              position.y = j*incSize+incSize;
            }
            
            if (position.x + w >= i*incSize && position.x + w <= i*incSize+1*incSize &&
            position.y + h > j*incSize && position.y < j*incSize+1*incSize && rightVel.x > 0 ) { //move right collision
              position.x = i*incSize - w;
              //leftVel.x = -incVel;
              //println(position.x + w + " " + i*incSize);
            }
            
            else if (position.x < i*incSize+incSize && position.x > i*incSize &&
            position.y + h > j*incSize && position.y < j*incSize+1*incSize && leftVel.x < 0) { //move left collision
              position.x = i*incSize+incSize;
            }
          }
        }
      }
    }
    //System.out.print(" " + onGround + "\n");
    //System.out.print(xStart + " " + yStart + " ");
  }
  
}
class Player{
  String name;
  float health;
  float comboMultiplier;
  
  public Player(String n) {
    name = n;
    
    health = 100;
    comboMultiplier = 1;
    
    position = new PVector(width / 2, height * 2.5f / 6);
    leftVel = new PVector(0, 0);
    rightVel = new PVector(0, 0);
    downVel = new PVector(0, 0);
    
    left = false;
    right = false;
    onGround = false;
  
  }
  
  public void move() {
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
  
  public void jump() {
    if (downVel.y == 0) {
      onGround = false;
      downVel = new PVector(0, -13);
      //System.out.print("JUMP");
    }  
  }
  public void display() {
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
  
  public void testPlayer() {
    if (health < 0) {
      die();
    }    
  }
  
  public void testDamage() {
    //if (timeStart > 1000) {
      health -= airEnemyDamage;
    //  timeSaved = millis();
    //}
  }
  
  public void die() {
    reset();
  }
  
}
  public void settings() {  size(1000, 600); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Sidescroller" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
