package eatengine.components;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface IDrawable {
	public void render(SpriteBatch spritebatch);
	
	public int getRenderLayer();
	public void setRenderLayer(int layer);
}
