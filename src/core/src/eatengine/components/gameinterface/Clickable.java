package eatengine.components.gameinterface;

import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Clickable extends GameInterface {

	/**
	 * Hitbox starts from top-left of position, extends towards bottom-right.
	 */
	public Vector2 size;
	
	protected Set<ClickEventListener> onClickEvents;
	
	public Clickable(Vector2 size) {
		super();
		onClickEvents = new HashSet<ClickEventListener>();
		this.size = size;
	}
	
	@Override
	public void onRender(SpriteBatch spriteBatch) {}

	@Override
	public void update(float deltaTime) {
		if (!Gdx.input.isButtonJustPressed(Buttons.LEFT)) {
			return;
		}
		
		Vector3 mousePosition = getGameWorldMousePosition();
		if (mouseInXbounds(mousePosition) && mouseInYbounds(mousePosition)) {
			triggerAllListeners();
		}
	}
	
	protected Vector3 getGameWorldMousePosition() {
		// Mouse position given by LibGDX is relative to the window's position,
		// rather than game's world position. So translation is required.
		Camera sceneCamera = getGameObject().getSceneReference().getSceneCamera();
		Vector3 mousePosition = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
		return sceneCamera.unproject(mousePosition);
	}
	
	private boolean mouseInXbounds(Vector3 mousePosition) {
		float mouseXposition = mousePosition.x;
		float currentXPosition = getGameObject().getPosition().x + getOffset().x;
		
		boolean withinLeftBounds = mouseXposition >= currentXPosition;
		boolean withinRightBounds = mouseXposition <= (currentXPosition + size.x);
		return withinLeftBounds && withinRightBounds;
	}
	
	private boolean mouseInYbounds(Vector3 mousePosition) {
		float mouseYposition = mousePosition.y;
		float currentYposition = getGameObject().getPosition().y + getOffset().y;
		
		boolean withinUpperBounds = mouseYposition >= currentYposition;
		boolean withinLowerBounds = mouseYposition <= (currentYposition + size.y);
		return withinUpperBounds && withinLowerBounds;
	}
	
	protected void triggerAllListeners() {
		for (ClickEventListener listener: onClickEvents) {
			if (listener != null) {
				listener.onClickEvent(this);
			}
		}
	}
	
	public void addOnClickListener(ClickEventListener onClickListener) {
		onClickEvents.add(onClickListener);
	}
	
	public void removeOnClickListener(ClickEventListener onClickListener) {
		onClickEvents.remove(onClickListener);
	}
}
