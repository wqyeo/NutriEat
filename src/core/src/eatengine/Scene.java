package eatengine;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import eatengine.components.IDrawable;
import eatengine.components.PhysicsBody;

import com.badlogic.gdx.physics.box2d.Body;

public class Scene {
	/**
	 * For 'waitingToRemove' list.
	 * @author wqyeo
	 *
	 */
	private class RemoveDispose {
		public GameObject toRemove;
		public boolean dispose;
		public RemoveDispose(GameObject toRemove, boolean dispose){
			this.toRemove = toRemove;
			this.dispose = dispose;
		}
	}
	
	private String name;
	private World world;
	private Set<GameObject> rootObjects;
	
	private OrthographicCamera sceneCamera;
	
	// Bind LibGDX's physics body to a PhysicsBody component.
	private HashMap<Body, PhysicsBody> bodyComponentPairing;
	private BodyContactListener worldContactListener;
	
	private boolean started;
	private boolean disposed;
	
	EatEngine engineRef;
	
	/**
	 * Set of gameobjects that are waiting to be added to scene.
	 */
	private Set<GameObject> waitingToAdd;
	
	/**
	 * Set of game-objects that are waiting to be removed and disposed from scene.
	 */
	private Set<RemoveDispose> waitingToRemove;
	
	public Scene(String name, float cameraSizeX, float cameraSizeY) {
		sceneCamera = new OrthographicCamera();
		sceneCamera.setToOrtho(false, cameraSizeX, cameraSizeY);
		
		world = new World(PhysicsEngine.WORLD_GRAVITY, true);
		worldContactListener = new BodyContactListener(this);
		world.setContactListener(worldContactListener);
		
		rootObjects = new HashSet<GameObject>();
		waitingToAdd = new HashSet<GameObject>();
		waitingToRemove = new HashSet<RemoveDispose>();
		
		bodyComponentPairing = new HashMap<Body, PhysicsBody>();
		started = false;
		disposed = false;
		this.name = name;
	}
	
//region GameLoop
	
	final void start() {
		if (warnIfInvalidState(true, false, true, "start()")) {return;}
		
		for (GameObject gameObject: rootObjects) {
			gameObject.start();
		}
		started = true;
	}
	
	final void update(float deltaTime, PhysicsEngine physicsEngine) {
		if (warnIfInvalidState(true, false, true, "update()")) {return;}
		
		for (GameObject gameObject: rootObjects) {
			if (gameObject == null) {
				continue;
			}
			gameObject.update(deltaTime);
			
			// User interrupts the scene, warn if the action was disposing.
			if (!sceneIsActive()) {
				if (disposed) {
					String logMessage = String.format("%s scene was disrupted during update(). If you want to end a scene, do so after the end of rendering!", name);
					Log.Warning(logMessage);
					return;
				}
			}
		}
		
		// TODO: Warn scene state disrupted during physics update.
		physicsEngine.update(deltaTime, world, rootObjects);
		
		updateWaitingQueue();
		updateRemoveQueue();
	}
	// Add all objects that are waiting, then clear.
	private final void updateWaitingQueue() {
		for (GameObject toAdd: Util.shallowCloneSet(waitingToAdd)) {
			rootObjects.add(toAdd);
			toAdd.setSceneReference(this);
			toAdd.start();
		}
		waitingToAdd.clear();
	}
	
	private final void updateRemoveQueue() {
		for (RemoveDispose removeObject: Util.shallowCloneSet(waitingToRemove)) {
			rootObjects.remove(removeObject.toRemove);
			
			if (removeObject.dispose) {
				removeObject.toRemove.dispose();
			}
		}
		waitingToRemove.clear();
	}
	
	final void dispose() {
		if (warnIfInvalidState(true, false, true, "dispose()")) {return;}
		
		for (GameObject gameObject: new HashSet<>(rootObjects)) {
			gameObject.dispose();
		}
		
		world.setContactListener(null);
		world.dispose();
		disposed = true;
	}
	
//endregion
	
	public final World getWorld() {
		// TODO: Probably refactor into functions that add/remove objects into the world,
		// rather than giving a direct reference to the world object.
		return world;
	}
	
	/**
	 * Search for a GameObject by name, search result includes children.
	 * @param name Case sensitive name.
	 * @return First matching result, or null.
	 */
	public final GameObject getGameObjectByName(String name) {
		for (GameObject gameObject: rootObjects) {
			if (gameObject.name.equals(name)) {
				return gameObject;
			}
			
			GameObject matchingChild = gameObject.findChildByName(name);
			if (matchingChild != null) {
				return matchingChild;
			}
		}
		return null;
	}
	
	/**
	 * Search GameObject by type, search result includes children.
	 * @param <T> Type to check against. (Casting will be performed)
	 * @return First matching result, or null
	 */
	public final <T extends GameObject> T getGameObjectByType(Class<T> type) {
		for (GameObject gameObject: rootObjects) {
			if (type.isInstance(gameObject)) {
				return (T) gameObject;
			}
			
			T childAsT = gameObject.<T>findChildByType(type);
			if (childAsT != null) {
				return childAsT;
			}
		}
		return null;
	}
	
	/**
	 * Search GameObject by type, search result includes children.
	 * @param <T> Type to check against. (Casting will be performed)
	 * @return All matching results in an array.
	 */
	public final <T extends GameObject> T[] getGameObjectsByType(Class<T> type){
		ArrayList<T> result = new ArrayList<T>();
		for (GameObject gameObject: rootObjects) {
			if (type.isInstance(gameObject)) {
				result.add((T) gameObject);
			}
			
			result.addAll(Arrays.asList(gameObject.<T>findAllChildByType(type)));
		}
		
		return result.toArray((T[]) Array.newInstance(type, result.size()));
	}
	
	public final GameObject[] getAllRootObject() {
		return (GameObject[]) rootObjects.toArray();
	}
	
	/**
	 * Add a GameObject to root of this scene.
	 * @param gameObject
	 * @return True if object was successfully added.
	 */
	public final boolean addGameObjectToRoot(GameObject gameObject) {
		if (gameObject == null) {return false;}
		if (gameObject.getParent() != null) {
			// TODO: Warn not needing to add if its a child object.
			return false;
		}
		
		boolean added;
		if (!started) {
			added = rootObjects.add(gameObject);
			if (added) {
				gameObject.setSceneReference(this);
				gameObject.start();
			}
			return added;
		} else {
			// Don't add during the middle of an update call.
			// Adds after the update call is done.
			return waitingToAdd.add(gameObject);
		}
	}
	
	/**
	 * Remove a GameObject from the root of the scene. (Do NOT call when attaching GameObject to parent, it will be automatically handled.)
	 * @param gameObject
	 * @param dispose True if 'dispose()' of GameObject should be called. (Will recursively call for children as well.)
	 * @return True if removal was successful
	 */
	public final boolean removeFromRoot(GameObject gameObject, boolean dispose) {
		if (gameObject == null) { return false; }
		
		if (!started) {
			if (dispose) {
				gameObject.dispose();
			}
			return rootObjects.remove(gameObject);
		} else {
			return waitingToRemove.add(new RemoveDispose(gameObject, dispose));
		}
	}
	
	/**
	 * Package/Internal call. Used when a gameObject has a new parent. (GameObject no longer in root)
	 * @param gameObject
	 * @return
	 */
	final boolean detachFromRoot(GameObject gameObject) {
		if (gameObject == null) { return false; }
		
		if (!started) {
			return rootObjects.remove(gameObject);
		} else {
			return waitingToRemove.add(new RemoveDispose(gameObject, false));
		}
	}
	
	public final Camera getSceneCamera() {
		return sceneCamera;
	}
	
	public final void addToRenderQueue(IDrawable drawable) {
		if (!sceneIsActive()) {return;}
		
		engineRef.addToRenderQueue(drawable);
	}
	
	public final boolean started() {
		return started;
	}
	
	public final boolean disposed() {
		return disposed;
	}
	
	public final String getName() {
		return name;
	}
	
	
	private boolean warnIfInvalidState(boolean checkEngine, boolean checkStart, boolean checkDisposed, String functionName) {
		if (checkEngine && engineRef == null) {
			String logMessage = String.format("%s scene has no engine attached, but %s was called", name, functionName);
			Log.Warning(logMessage);
			return true;
		}
		if (checkStart && started) {
			String logMessage = String.format("%s scene has already started, but %s was called on it.", name, functionName);
			Log.Warning(logMessage);
			return true;
		}
		if (checkDisposed && disposed) {
			String logMessage = String.format("%s scene has already been disposed, but %s was called on it.", name, functionName);
			Log.Warning(logMessage);
			return true;
		}
		return false;
	}
	
	public final void addBodyObjectPairing(Body body, PhysicsBody physicsBody) {
		bodyComponentPairing.put(body, physicsBody);
	}
	
	/**
	 * Find the PhysicsBody component that has the LibGDX Body
	 * @param body
	 * @return Tagged body; Null if none was found.
	 */
	public final PhysicsBody fetchPhysicsBodyByBody(Body body) {
		if (bodyComponentPairing.containsKey(body)) {
			return bodyComponentPairing.get(body);
		}
		return null;
	}
	
	public final void removeBodyObjectPairing(Body body) {
		bodyComponentPairing.remove(body);
	}
	
	public final boolean sceneIsActive() {
		return !disposed && engineRef != null;
	}
	
	/**
	 * Get the engine that is handling this scene.
	 * @return Null if there is no engine tied to this scene.
	 */
	public final EatEngine getEngineReference() {
		return engineRef;
	}
	
	
	private class BodyContactListener implements ContactListener {
		private Scene sceneRef;
		public BodyContactListener(Scene sceneRef) {
			this.sceneRef = sceneRef;
		}
		
	    @Override
	    public void beginContact(Contact contact) {
	        PhysicsBody firstBody = sceneRef.fetchPhysicsBodyByBody(contact.getFixtureA().getBody());
	        PhysicsBody secondBody = sceneRef.fetchPhysicsBodyByBody(contact.getFixtureB().getBody());
	        
	        if (firstBody == null || secondBody == null) {
	        	Log.Error("Collision enter detected, but a body did not have a PhysicsBody tagged to it!");
	        	return;
	        }
	        firstBody.notifyCollisionEnterWith(secondBody);
	        secondBody.notifyCollisionEnterWith(firstBody);
	    }

	    @Override
	    public void endContact(Contact contact) {
	        PhysicsBody firstBody = sceneRef.fetchPhysicsBodyByBody(contact.getFixtureA().getBody());
	        PhysicsBody secondBody = sceneRef.fetchPhysicsBodyByBody(contact.getFixtureB().getBody());
	        
	        if (firstBody == null || secondBody == null) {
	        	Log.Error("Collision exit detected, but a body did not have a PhysicsBody tagged to it!");
	        	return;
	        }
	        firstBody.notifyCollisionExitWith(secondBody);
	        secondBody.notifyCollisionExitWith(firstBody);
	    }

	    @Override
	    public void preSolve(Contact contact, Manifold oldManifold) {
	        // TODO: Called just before a collision is resolved
	    }

	    @Override
	    public void postSolve(Contact contact, ContactImpulse impulse) {
	        // TODO: Called just after a collision is resolved
	    }
	}
}
