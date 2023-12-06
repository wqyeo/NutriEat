package com.mygdx.nutrieats.adventurescene;

import com.badlogic.gdx.math.Vector2;

import com.mygdx.nutrieats.adventurescene.AdventureWorld;

import eatengine.Component;
import eatengine.Util;

/**
* A composition class to help animate/move the AdventureWorld object.
* @author wqyeo
*/
class WorldMover extends Component {

  private AdventureWorld worldReference;

  private float moveProgress;
  private float moveSpeed;

  private boolean isMoving;

  private Vector2 initialPosition;
  private Vector2 endPosition;

  public WorldMover(AdventureWorld worldReference){
    this.worldReference = worldReference;

    moveProgress = 0f;
    moveSpeed = 0f;

    isMoving = false;
    initialPosition = new Vector2(0,0);
    endPosition = new Vector2(0,0);
  }
  
  @Override
  public void update(float deltaTime){
    if (!isMoving){
      return;
    }

    if (moveProgress >= 1f){
      worldReference.setPosition(endPosition);
      isMoving = false;
      return;
    }

    Vector2 newPosition = Util.lerpVec2(initialPosition, endPosition, moveProgress);
    worldReference.setPosition(newPosition);
    
    moveProgress += deltaTime * moveSpeed;
  }

  /**
  * Invoke the movement of the world by 1 step, either forward or backwards.
  * The world that it will move is based off the world that is passed during initalization.
  */
  public void invokeMove(float speed, boolean forward){
    // Was moving, so just end the current movement and start next;
    if (isMoving){
      worldReference.setPosition(endPosition);
    }

    moveSpeed = speed;
    moveProgress = 0f;

    initialPosition = worldReference.getWorldPosition();

    if (forward) {
      endPosition = Util.subVec2(initialPosition, new Vector2(AdventureWorld.TILE_INCREMENTAL_POSITION, 0f));
    } else {
      endPosition = Util.addVec2(initialPosition, new Vector2(AdventureWorld.TILE_INCREMENTAL_POSITION, 0f));
    }

    isMoving = true;
  }
}
