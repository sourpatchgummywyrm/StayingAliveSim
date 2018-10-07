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
  
  void loadMap(String map) {
    int lineLocation = 0;
    line = loadStrings(map); //map is "map.txt"
    
    for (int i = 0; i < xRes; i++) {
      for (int j = 0; j < yRes; j++) {
        colored[i][j] = boolean(line[lineLocation]);
        lineLocation++;
      }
    }
  }
  
  void displayMap() {
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
  
  void customCollision() {
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
