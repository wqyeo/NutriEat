package eatengine.components;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class SpriteFrame {

    public float duration;
    protected Sprite sprite;

    public SpriteFrame() {
        super();

    }
    
	public void dispose() {
		if (sprite != null) {
			sprite.getTexture().dispose();
		}
	}
	

    public Sprite getSprite() {
        return sprite;
    }
}
