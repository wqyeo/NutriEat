package com.mygdx.nutrieats.cookingscene.entities;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.nutrieats.cookingscene.CookingAudioType;
import com.mygdx.nutrieats.cookingscene.CookingSceneManager;
import com.mygdx.nutrieats.cookingsystem.Recipe;

import eatengine.GameObject;
import eatengine.Util;
import eatengine.components.SpriteRenderer;

public class Waiter extends GameObject implements IEntity {
	private static final Map<EntityMood, String> WAITER_TEXTURE_LOCATIONS_MAPPING;

	static {
		WAITER_TEXTURE_LOCATIONS_MAPPING = new HashMap<EntityMood, String>();
		WAITER_TEXTURE_LOCATIONS_MAPPING.put(EntityMood.IDLE, "sprites\\player\\chris\\chrisIdle.png");
		WAITER_TEXTURE_LOCATIONS_MAPPING.put(EntityMood.HAPPY, "sprites\\player\\chris\\chrisHappy.png");
		WAITER_TEXTURE_LOCATIONS_MAPPING.put(EntityMood.ROLL, "sprites\\player\\chris\\chrisRoll.png");
		WAITER_TEXTURE_LOCATIONS_MAPPING.put(EntityMood.HURT, "sprites\\player\\chris\\chrisHurt.png");
		WAITER_TEXTURE_LOCATIONS_MAPPING.put(EntityMood.FALLEN, "sprites\\player\\chris\\chrisFallen.png");
		WAITER_TEXTURE_LOCATIONS_MAPPING.put(EntityMood.CHOP, "sprites\\player\\chris\\chrisAttack.png");
	}
	
	private static final Vector2 ORDER_COLLECTION_POSITION = new Vector2(65f, 200f);
	
	/**
	 * Place to serve food from.s
	 */
	private static final Vector2 OFFSCREEN_POSITION = new Vector2(-203f, 200f);
	
	private Map<EntityMood, String> waiterTextureIdMapping;
	private EntityMood currentMood;
	
	private CookingSceneManager sceneManager;

	private SpriteRenderer renderer;
	private EntityAnimator animator;
	
	public Waiter(CookingSceneManager sceneManager) {
		super("Waiter");
		this.sceneManager = sceneManager;
		animator = new EntityAnimator(this, new Vector2(-1f,1f));
		currentMood = EntityMood.IDLE;
		
		createTextureIds();
		createRendererComponent();
		
		this.setPosition(OFFSCREEN_POSITION);
		moveToCollectOrder();
	}
	
	@Override
	public void onUpdate(float deltaTime) {
		animator.update(deltaTime);
	}
	
	private void createTextureIds() {
		waiterTextureIdMapping = new HashMap<EntityMood, String>();

		for (Entry<EntityMood, String> entry : WAITER_TEXTURE_LOCATIONS_MAPPING.entrySet()) {
			String textureId = sceneManager.createTexture(entry.getValue());
			waiterTextureIdMapping.put(entry.getKey(), textureId);
		}
	}
	
	private void createRendererComponent() {
		Sprite idleSprite = sceneManager.createSpriteByTextureId(waiterTextureIdMapping.get(EntityMood.IDLE));
		renderer = new SpriteRenderer(idleSprite);
		renderer.setSpriteScale(animator.getCurrentScale());
		this.addComponent(renderer);
	}
	
	private void moveToCollectOrder() {
		OnAnimationDone movementDone = new OnAnimationDone() {
			@Override
			public void callback(AnimationType fromAnimation) {
				sceneManager.setOrderForChef();
			}
		};
		
		animator.triggerHopMove(ORDER_COLLECTION_POSITION, movementDone);
	}
	
	@Override
	public void changeMood(EntityMood mood) {
		String textureId = waiterTextureIdMapping.get(mood);
		renderer.changeTexture(textureId);
		currentMood = mood;
		renderer.setSpriteScale(animator.getCurrentScale());
	}

	@Override
	public EntityMood getCurrentMood() {
		return currentMood;
	}

	@Override
	public Vector2 getEntityPosition() {
		return this.getPosition();
	}

	@Override
	public void setEntityPosition(Vector2 position) {
		this.setPosition(position);
	}
	
	@Override
	public void setEntityScale(Vector2 scale) {
		renderer.setSpriteScale(scale);
	}
	
	public void invokeServeFood(Recipe food) {
		OnAnimationDone callback = new OnAnimationDone() {
			@Override
			public void callback(AnimationType fromAnimation) {
				changeMood(EntityMood.IDLE);
				flipToServe();
			}
		};
		
		sceneManager.playSoundByType(CookingAudioType.INGREDIENT_LAND);
		
		changeMood(EntityMood.HAPPY);
		animator.triggerPause(0.5f, callback);
	}
	
	private void flipToServe() {
		OnAnimationDone callback = new OnAnimationDone() {
			@Override
			public void callback(AnimationType fromAnimation) {
				hopToServe();
			}
		};
		
		animator.triggerFlip(callback);
	}
	
	private void hopToServe() {
		OnAnimationDone callback = new OnAnimationDone() {
			@Override
			public void callback(AnimationType fromAnimation) {
				serve();
			}
		};
		
		animator.triggerHopMove(OFFSCREEN_POSITION, callback);
	}
	
	private void serve() {
		OnAnimationDone callback = new OnAnimationDone() {
			@Override
			public void callback(AnimationType fromAnimation) {
				moveToCollectOrder();
			}
		};
		
		// Align the player back to request order
		animator.triggerFlip(callback);
		sceneManager.notifyFoodServed();
		sceneManager.playSoundByType(CookingAudioType.ORDER_SERVED);
	}
}
