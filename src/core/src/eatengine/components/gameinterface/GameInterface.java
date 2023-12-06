package eatengine.components.gameinterface;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import eatengine.Component;
import eatengine.Util;
import eatengine.components.IDrawable;

public abstract class GameInterface extends Component implements IDrawable {
	public boolean isVisible;
	public int layer;
	
	private Vector2 offset;

	public GameInterface() {
		super();
		isVisible = true;
		offset = new Vector2(0, 0);
	}
	
    public final void render(SpriteBatch spriteBatch) {
    	if (!isVisible) {
    		return;
    	}
    	
    	onRender(spriteBatch);
    }
    
    protected abstract void onRender(SpriteBatch spriteBatch);
	
    public int getRenderLayer(){
        return this.layer;
    }
    public void setRenderLayer(int layer){
        this.layer = layer;
    }
    
    public final Vector2 getOffset() {
    	return offset;
    }
    
    public final void setOffset(Vector2 offset) {
    	this.offset = offset;
    }
    
    /***
     * Use this to get the position of where this interface should render at
     * @return
     */
    protected final Vector2 getRenderPosition() {
    	return Util.addVec2(offset, getGameObject().getPosition());
    }
}
