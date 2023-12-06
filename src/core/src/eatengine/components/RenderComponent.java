package eatengine.components;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import eatengine.Component;

public abstract class RenderComponent extends Component implements IDrawable {


    protected boolean isVisible;
    protected int layer;

    public RenderComponent(){
        super();
        this.isVisible = true;
    }
    public RenderComponent(boolean isVisible){
        super();
        this.isVisible = isVisible;
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
    
    public final void setVisible(boolean visibility) {
    	isVisible = visibility;
    }
    
    public final boolean isVisible() {
    	return isVisible;
    }
}
