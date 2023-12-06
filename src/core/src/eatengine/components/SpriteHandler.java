package eatengine.components;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import eatengine.EatEngine;

public interface SpriteHandler {
	public void changeTexture(String textureId);
    /**
     * Should be called by EatEngine only
     * @param texture
     */
	public void setTexture(Texture texture, EatEngine eatEngineRef);
	
	public void setSpriteScale(float scale);
	
	public Vector2[] getSpriteVertices();
}
