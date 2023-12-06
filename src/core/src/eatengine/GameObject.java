package eatengine;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;

import eatengine.components.IDrawable;
import eatengine.components.PhysicsBody;

public abstract class GameObject {
	/**
	 * Case sensitive; Mostly for logging.
	 */
	public String name;

	private Vector2 position;
	public boolean isActive;

	private boolean started;
	private Set<GameObject> children;
	private GameObject parent;

	private Set<Component> components;
	
	/* *
	 * Used to put components that are waiting to be detached.
	 */
	private Set<Component> componentDetachQueue;

	private Scene sceneRef;

	public GameObject(String name) {
		components = new HashSet<Component>();
		children = new HashSet<GameObject>();
		componentDetachQueue = new HashSet<Component>();
		isActive = true;
		this.name = name;
		parent = null;
		sceneRef = null;
		started = false;
		position = new Vector2(0, 0);
	}

//region Components

	/**
	 * Add component into this game-object.
	 * @param component The component to add.
	 * @return True if the component was added.
	 */
	public final boolean addComponent(Component component) {
		boolean componentAdded = components.add(component);
		if (componentAdded) {
			component.bindToGameObject(this);
		} else {
			String message = String.format("[%s] Failed to add component of %s. Component already exists?", name, component.getClass().getName());
			Log.Warning(message);
		}
		return componentAdded;
	}

	/**
	 * Get component by the given generic type.
	 * @param <T> The type of component to find.
	 * @return The first component of matching type, null if none.
	 */
	public final <T extends Component> T getComponent(Class<T> type) {
		for (Component component: components) {
			if (type.isInstance(component)) {
				return (T) component;
			}
		}
		return null;
	}

	/**
	 * Get all components by the given generic type.
	 * @param <T> The type of component to find.
	 * @return All components of matching type.
	 */
	public final <T extends Component> T[] getComponents(Class<T> type) {
		ArrayList<T> foundComponents = new ArrayList<T>();

		for (Component component: components) {
			if (type.isInstance(component)) {
				foundComponents.add((T)component);
			}
		}

		return foundComponents.toArray((T[]) Array.newInstance(type, foundComponents.size()));
	}

//endregion

	public final void start() {
		if (started) {return;}

		for (Component component : components) {
			component.start();
		}

		onStart();

		for (GameObject child : children) {
			child.start();
		}

		started = true;
	}

	public final void update(float deltaTime) {
		if (!isActive) {return;}
		
		for (Component detached : componentDetachQueue) {
			components.remove(detached);
		}
		componentDetachQueue.clear();

		for (Component component : components) {
			if (component == null) {continue;}
			component.update(deltaTime);
			
			if (this.sceneRef == null) {
				return;
			}

			if (IDrawable.class.isAssignableFrom(component.getClass())) {
				sceneRef.addToRenderQueue((IDrawable) component);
			}
		}

		onUpdate(deltaTime);

		for (GameObject child : children) {
			child.update(deltaTime);
		}
	}

	public final void physicsUpdate(float fixedDeltaTime) {
		if (!isActive) {return;}
		
		for (Component detached : componentDetachQueue) {
			components.remove(detached);
		}
		componentDetachQueue.clear();

		for (Component component : components) {
			if (component == null) {continue;}
			component.physicsUpdate(fixedDeltaTime);
		}

		onPhysicsUpdate(fixedDeltaTime);

		for (GameObject child : children) {
			child.onPhysicsUpdate(fixedDeltaTime);
		}
	}

	public final void dispose() {
		for (GameObject child : children) {
			child.dispose();
		}

		for (Component component : components) {
			component.dispose();
		}

		onDispose();

		if (sceneRef != null) {
			sceneRef.removeFromRoot(this, false);
		}
		sceneRef = null;
	}

//region Optional overrides
	protected void onStart() {}
	protected void onUpdate(float deltaTime) {}
	protected void onPhysicsUpdate(float fixedDeltaTime) {}
	protected void onDispose() {}
//endregion

	/**
	 * Calculate the world position of this GameObject. (Not the position relative to the parent)
	 * @return
	 */
	public final Vector2 getWorldPosition() {
		if (parent != null) {
			Vector2 parentPos = sumParentPositions(this);
			return new Vector2(parentPos.x + position.x, parentPos.y + position.y);
		} else {
			return position;
		}
	}

	/**
	 * Get the sum of all the parent's position.
	 * Used to calculate World position, as a child's parent might be another child.
	 * @param gameObject
	 * @return
	 */
	private static Vector2 sumParentPositions(GameObject gameObject) {
	    Vector2 result = new Vector2();
	    GameObject parent = gameObject.parent;
	    while (parent != null) {
	        result.add(parent.getPosition());
	        parent = parent.getParent();
	    }
	    return result;
	}

	/**
	 * Get the position of this GameObject relative to it's parent GameObject.
	 * @return
	 */
	public final Vector2 getPosition() {
		return position;
	}

	/**
	 * Change the position of this GameObject, child objects are brought along as well.
	 * @param newPosition
	 */
	public final void setPosition(Vector2 newPosition) {
		Vector2 change = Util.subVec2(newPosition, this.position);

		for (GameObject child: children) {
			child.setPosition(Util.addVec2(change, child.position));
			child.setPhysicsPosition(child.position);
		}

		this.position = Util.addVec2(change, this.position);
		setPhysicsPosition(newPosition);
	}

	public final void setPositionForced(Vector2 newPosition) {
		this.position = newPosition;
	}

	private void setPhysicsPosition(Vector2 newPosition) {
		Component component = this.getComponent(PhysicsBody.class);
		if (component == null) {
			return;
		}

		PhysicsBody physicsBody = (PhysicsBody) component;
		physicsBody.setPosition(newPosition);
	}

//region Parent-Child hierachy

	/**
	 * Change the position of this gameobject, child objects are not affected.
	 * @return parent GameObject, null if no parent was attached.
	 */
	public final GameObject getParent() {
		return parent;
	}

	/**
	 * Excludes children of children.
	 * @return
	 */
	public final GameObject[] getChildren() {
		GameObject[] result = new GameObject[children.size()];
		return children.toArray(result);
	}

	/**
	 * Includes children of children.
	 * @return
	 */
	public final GameObject[] getChildrenRecursively() {
		ArrayList<GameObject> allChildren = new ArrayList<GameObject>();

		allChildren.addAll(Arrays.asList(getChildren()));
		for (GameObject gameObject: children) {
			allChildren.addAll(Arrays.asList(gameObject.getChildrenRecursively()));
		}

		GameObject[] result = new GameObject[children.size()];
		return allChildren.toArray(result);
	}

	public final void setParent(GameObject newParent) {
		detachFromParent();

		parent = newParent;
		newParent.addChild(this);
		sceneRef = newParent.sceneRef;
		this.position = Util.subVec2(this.position, parent.getWorldPosition());

		if (sceneRef != null) {
			sceneRef.detachFromRoot(this);
			if (sceneRef.started()) {
				start();
			}
		}
	}

	public final void detachFromParent() {
		if (parent != null) {
			if (parent.sceneRef != null) {
				sceneRef = parent.sceneRef;
			}

			this.position = Util.addVec2(this.position, parent.getWorldPosition());

			parent.removeChild(this);
			parent = null;
		}
	}

	private final void addChild(GameObject child) {
		children.add(child);
	}

	private final void removeChild(GameObject child) {
		children.remove(child);
	}

	/**
	 * Includes child of child.
	 * @param name
	 * @return
	 */
	public final GameObject findChildByName(String name) {
		for (GameObject gameObject : children) {
			if (gameObject.name.equals(name)) {
				return gameObject;
			}

			GameObject result = gameObject.findChildByName(name);
			if (result != null) {
				return result;
			}
		}

		return null;
	}

	/**
	 * Includes child of child
	 * @param <T>
	 * @return
	 */
	public final <T extends GameObject> T findChildByType(Class<T> type) {
		for (GameObject child : children) {
			if (type.isInstance(child)) {
				return (T) child;
			}

			T result = child.findChildByType(type);
			if (result != null) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Includes child of child
	 * @param <T>
	 * @return
	 */
	public final <T extends GameObject> T[] findAllChildByType(Class<T> type) {
		ArrayList<T> result = new ArrayList<T>();

		for (GameObject child : children) {
			if (type.isInstance(child)) {
				result.add((T) child);
			}

			result.addAll(Arrays.asList(child.findAllChildByType(type)));
		}

		return result.toArray((T[]) Array.newInstance(type, result.size()));
	}

	public final Scene getSceneReference() {
		return sceneRef;
	}

	public final void setSceneReference(Scene scene) {
		if (scene == null) {return;}
		this.sceneRef = scene;
		for (GameObject child: getChildrenRecursively()) {
			child.sceneRef = scene;
		}
	}
//endregion

	public final void setActive(boolean activeState) {
		this.isActive = activeState;
	}
	
	final boolean hasStarted() {
		return started;
	}
	
	final void detachComponent(Component component) {
		componentDetachQueue.add(component);
	}
}
