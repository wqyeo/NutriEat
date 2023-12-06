package eatengine.components.gameinterface;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import eatengine.EatEngine;
import eatengine.Log;
import eatengine.Util;
import eatengine.components.SpriteHandler;

public class Image extends GameInterface implements SpriteHandler {

	private Sprite sprite;
	
	public Image(Sprite sprite) {
		super();
		
		this.sprite = sprite;
	}
	
	@Override
	public void onRender(SpriteBatch spriteBatch) {
		Vector2 renderPosition = getRenderPosition();
		sprite.setPosition(renderPosition.x, renderPosition.y);
		sprite.draw(spriteBatch);
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
 	   
 	   engineRef.loadTextureIntoImage(this, textureId);
    } 
    
    public void setTexture(Texture texture, EatEngine engineRef) {
 	   if (!engineRef.hasTexture(texture)) {
 		   Log.Error(getGameObject().name + " :: tried to assign texture onto sprite but texture is not loaded into the engine!");
 		   return;
 	   }
 	   sprite.setTexture(texture);
    }
    
    /**
     * Basically, transparency. (0.0f ~ 1.0f)
     * @param a
     */
    public void setAlpha(float a) {
    	sprite.setAlpha(a);
    }
    
    public void setSpriteScale(float scale) {
    	sprite.setScale(scale);
    }
   
	public void scaleSpriteToTargetSize(Vector2 targetSize) {
		Texture texture = sprite.getTexture();
		float scaleX = targetSize.x / texture.getWidth();
		float scaleY = targetSize.y / texture.getHeight();
		sprite.setScale(scaleX, scaleY);
	}
    
    
	public Vector2[] getSpriteVertices() {
		return Util.getBoxVerticesFromSprite(sprite);
	}
}
