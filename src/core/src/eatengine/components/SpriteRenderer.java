package eatengine.components;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import eatengine.EatEngine;
import eatengine.Log;
import eatengine.Util;

public class SpriteRenderer extends RenderComponent implements SpriteHandler {

    private Sprite sprite;

    public SpriteRenderer(Sprite sprite){
        this.sprite = sprite;
    }

    @Override
    public void onRender(SpriteBatch batch) {
		sprite.setPosition(getGameObject().getPosition().x, getGameObject().getPosition().y);
		sprite.draw(batch);
    }
    
	@Override
	public void dispose() {
	}

    protected Sprite getSprite() {
        return sprite;
    }

   public void changeTexture(String textureId){
	   EatEngine engineRef = getGameObject().getSceneReference().getEngineReference();
	   if (engineRef == null) {
		   Log.Warning(getGameObject().name + " :: tried to change texture on sprite but scene is not tied to a game engine!");
		   return;
	   }
	   
	   engineRef.loadTextureIntoRenderer(this, textureId);
   } 
   
   /**
    * Should be called by EatEngine only
    * @param texture
    */
   public void setTexture(Texture texture, EatEngine engineRef) {
	   if (!engineRef.hasTexture(texture)) {
		   Log.Error(getGameObject().name + " :: tried to assign texture onto sprite but texture is not loaded into the engine!");
		   return;
	   }
	   sprite.setTexture(texture);
   }
   
   public void setSpriteRotation(float rotation) {
	   sprite.setRotation(rotation);
   }


   public void rotateSprite(float amount) {
	   sprite.rotate(amount);
   }
   
   public void setSpriteScale(float scale) {
	   sprite.scale(scale);
   }

   public void setSpriteScale(Vector2 scale){
    sprite.setScale(scale.x, scale.y);
  }

   public void setSpriteScale(float x, float y) {
	   sprite.setScale(x, y);
   }

   public Vector2 getSpriteScale() {
	   return new Vector2(sprite.getScaleX(), sprite.getScaleY());
   }
   
	public Vector2[] getSpriteVertices() {
		return Util.getBoxVerticesFromSprite(sprite);
	}
}
