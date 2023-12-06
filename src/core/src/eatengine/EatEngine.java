package eatengine;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import eatengine.components.IDrawable;
import eatengine.components.SpriteRenderer;
import eatengine.components.gameinterface.GameInterface;
import eatengine.components.gameinterface.Image;

public final class EatEngine {
	/**
	 * True to show collision boxes
	 */
	public boolean debugMode = false;
	
	private PhysicsEngine physicsEngine;
	private SpriteBatch spriteBatch;
	private TextureManager textureManager;
	private Stage gameStage;
	
	/**
	 * Should be sorted by render-layer first before rendering all.
	 */
	// Interface should be rendered last (to draw on top of game objects.)
	private ArrayList<IDrawable> gameObjectRenderList;
	private ArrayList<GameInterface> interfaceRenderList;
	
	private boolean disposed;
	private Scene currentScene;
	
	Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();
	
	public EatEngine(SpriteBatch batch) {
		initalize(batch);
	}
	
	public EatEngine(SpriteBatch batch, Scene scene) {
		initalize(batch);
		
		currentScene = scene;
		currentScene.engineRef = this;
	}
	
	private void initalize(SpriteBatch batch) {
		textureManager = new TextureManager();
		disposed = false;
		spriteBatch = batch;
		physicsEngine = new PhysicsEngine();
		gameObjectRenderList = new ArrayList<IDrawable>();
		interfaceRenderList = new ArrayList<GameInterface>();
		gameStage = new Stage(new ScreenViewport());
		Gdx.input.setInputProcessor(gameStage);
	}
	
//region GameLoop
	
	public void start() {
		if (disposed) {
			Log.Severe("Engine was disposed, but start() was called!");
			return;
		}
		if (currentScene == null) {
			Log.Error("No scene set in engine, but start() was called.");
			return;
		}
		
		currentScene.start();
	}
	
	public void update(float deltaTime) {
		if (disposed) {
			Log.Severe("Engine was disposed, but update() was called!");
			return;
		}
		if (currentScene == null) {
			Log.Error("No scene set in engine, but update() was called.");
			return;
		}
		
		currentScene.update(deltaTime, physicsEngine);
		render(deltaTime);
	}
	
	/**
	 * Draw all from render queue, then clear render queue.
	 */
	private void render(float deltaTime) {
		if (disposed) {
			Log.Severe("Engine was disposed, but render() was called!");
			return;
		}
		if (currentScene == null) {
			Log.Warning("No scene was set, but render() was called.");
			return;
		}
		
		ScreenUtils.clear(0.76f, 0.82f, 0.71f, 1);
		spriteBatch.begin();
		
		currentScene.getSceneCamera().update();
	
		if (debugMode) {
			debugRenderer.render(currentScene.getWorld(), currentScene.getSceneCamera().combined);
		}
		
		spriteBatch.setProjectionMatrix(currentScene.getSceneCamera().combined);
		
		renderObjectsInList(gameObjectRenderList);
		renderObjectsInList(interfaceRenderList);
		
		gameObjectRenderList.clear();
		interfaceRenderList.clear();
		
		spriteBatch.end();
		
	    gameStage.act(deltaTime);
	    gameStage.draw();
	}
	
	private <T extends IDrawable> void renderObjectsInList(ArrayList<T> listToRender){
		// Sort by render layer before drawing.
		listToRender.sort(new Comparator<IDrawable>() {
			@Override
			public int compare(IDrawable o1, IDrawable o2) {
				if ( o1.getRenderLayer()  == o2.getRenderLayer()) {
					return 0;
				}
				
				return o1.getRenderLayer() > o2.getRenderLayer() ? 1 : -1;
			}
		});
		
		for (IDrawable drawable: listToRender) {
			drawable.render(spriteBatch);
		}
	}
	
	/**
	 * Dispose the current scene. Engine still runs.
	 */
	public void disposeScene() {
		if (currentScene == null) {
			Log.Warning("No scene was set, but disposeScene() was called.");
			return;
		}
		
		currentScene.dispose();
	}
	
	/**
	 * Dispose the current engine. Not to be confused with 'disposeScene()'
	 */
	public void dispose() {
		if (currentScene != null) {
			currentScene.dispose();
			return;
		}
		spriteBatch.dispose();
		textureManager.dispose();
		Log.Dispose();
		disposed = true;
	}
	
//endregion
	
	public Stage getStage() {
		return gameStage;
	}
	
	/**
	 * Loads and start the new scene. Unloads the previous scene if any.
	 * @param scene
	 */
	public void loadScene(Scene scene) {
		if (disposed) {
			Log.Warning("Engine was disposed, but loadScene() was called.");
			return;
		}
		if (scene == null) {
			Log.Warning("Null reference was passed into loadScene().");
			return;
		}
		
		if (currentScene != null) {
			currentScene.engineRef = null;
		}
		
		currentScene = scene;
		scene.engineRef = this;
		scene.start();
	}
	
	public Scene getScene() {
		return currentScene;
	}
	
	void addToRenderQueue(IDrawable drawable) {
		if (GameInterface.class.isAssignableFrom(drawable.getClass())) {
			interfaceRenderList.add((GameInterface) drawable);
		} else {
			gameObjectRenderList.add(drawable);
		}
	}
	
	public boolean disposed() {
		return disposed;
	}
//region TextureManager
	/**
	 * Create a new Texture for the engine. Returns an existing ID if texture already exists.
	 * @param path Path to load the new Texture.
	 * @return ID used to access the Texture.
	 */
	public String createTexutre(String path) {
		return textureManager.createTexture(path);
	}
	
	public void loadTextureIntoRenderer(SpriteRenderer renderer, String id) {
		Texture texture = textureManager.getTextureById(id);
		renderer.setTexture(texture, this);
	}
	
	public void loadTextureIntoImage(Image image, String id) {
		Texture texture = textureManager.getTextureById(id);
		image.setTexture(texture, this);
	}
	
	public boolean hasTexture(Texture texture) {
		return textureManager.textureExists(texture);
	}
	
	public Sprite createSpriteFromTextureId(String id) {
		Texture texture = textureManager.getTextureById(id);
		
		if (texture == null) {
			Log.Warning("Couldn't find texture that has an id of " + id + ".");
			return null;
		}
		return new Sprite(texture);
	}
//endregion
}
