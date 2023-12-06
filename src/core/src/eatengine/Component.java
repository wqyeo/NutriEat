package eatengine;

public abstract class Component {
	
	private GameObject gameObject;
	
	public Component() {
	}
	
	/**
	 * Bind this component to the input GameObject.
	 * Operation fails if this component is already attached to another GameObject.
	 * @param gameObject The GameObject to attach to.
	 */
	public final void bindToGameObject(GameObject gameObject) {
		if (gameObject == null) {return;}
		if (this.gameObject != null) {
			String logMessage = String.format("Attempted to bind component to %s gameobject when it is already binded to %s", 
					gameObject.name, this.gameObject.name);
			Log.Severe(logMessage);
			return;
		}
		
		this.gameObject = gameObject;
		if (gameObject.hasStarted()) {
			this.start();
		}
	}
	
	/**
	 * Get the gameobject this component was attached to.
	 * @return
	 */
	public final GameObject getGameObject() {
		return gameObject;
	}
	
	public final void detachFromGameObject() {
		if (gameObject == null) {return;}
		
		gameObject.detachComponent(this);
	}
	
//region Optional Overrides
	public void start() {}
	public void update(float deltaTime) {}
	public void physicsUpdate(float fixedDeltaTime) {}
	public void dispose() {}
//endregion
}
