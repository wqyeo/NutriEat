package eatengine.components;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;

public class Animator extends RenderComponent  {

    private ArrayList<SpriteFrame> animationFrames;

    private float currentDuration;

    public Animator() {
        super();
        animationFrames = new ArrayList<SpriteFrame>();
        currentDuration = 0.0f;
    }

    @Override
    public void update(float deltaTime){
        currentDuration += deltaTime;
    }

    @Override
    public void onRender(SpriteBatch batch) {
    	SpriteFrame frameToRender = fetchCurrentSpriteFrame();
    	frameToRender.getSprite().setPosition(getGameObject().getPosition().x, getGameObject().getPosition().y);
    	frameToRender.getSprite().draw(batch);
    }
    
    private SpriteFrame fetchCurrentSpriteFrame() {
        float checkAgainstFrame = 0.0f;
        for (SpriteFrame frame : animationFrames){
            checkAgainstFrame += frame.duration;
            if (currentDuration <= checkAgainstFrame){
                return frame;
            }
        }
        
        // Current duration exceeded entire animation, loop from start again.
        currentDuration = 0.0f;
        return animationFrames.get(0);
    }
    
	@Override
	public void dispose() {
		for (SpriteFrame spriteFrame: animationFrames) {
			spriteFrame.dispose();
		}
	}
	

    public void addSpriteFrame(SpriteFrame spriteFrame){
        animationFrames.add(spriteFrame);
    }

    public void removeSpriteFrame(SpriteFrame spriteFrame){
        animationFrames.remove(spriteFrame);
    }
    
    public SpriteFrame[] getSpriteFrames() {
    	SpriteFrame[] result = new SpriteFrame[animationFrames.size()];
    	return animationFrames.toArray(result);
    }
}
