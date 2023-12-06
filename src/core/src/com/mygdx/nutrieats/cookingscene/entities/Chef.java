package com.mygdx.nutrieats.cookingscene.entities;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.nutrieats.adventurescene.entities.CharacterMood;
import com.mygdx.nutrieats.cookingscene.CookingSceneManager;

import eatengine.GameObject;
import eatengine.Util;
import eatengine.components.SpriteRenderer;

public class Chef extends GameObject implements IEntity {
	private static final Map<EntityMood, String> CHEF_TEXTURE_LOCATIONS_MAPPING;

	static {
		CHEF_TEXTURE_LOCATIONS_MAPPING = new HashMap<EntityMood, String>();
		CHEF_TEXTURE_LOCATIONS_MAPPING.put(EntityMood.IDLE, "sprites\\player\\chef\\chefIdle.png");
		CHEF_TEXTURE_LOCATIONS_MAPPING.put(EntityMood.HAPPY, "sprites\\player\\chef\\chefHappy.png");
		CHEF_TEXTURE_LOCATIONS_MAPPING.put(EntityMood.ROLL, "sprites\\player\\chef\\chefRoll.png");
		CHEF_TEXTURE_LOCATIONS_MAPPING.put(EntityMood.HURT, "sprites\\player\\chef\\chefHurt.png");
		CHEF_TEXTURE_LOCATIONS_MAPPING.put(EntityMood.FALLEN, "sprites\\player\\chef\\chefFallen.png");
		CHEF_TEXTURE_LOCATIONS_MAPPING.put(EntityMood.CHOP, "sprites\\player\\chef\\chefChop.png");
	}
	
	private static final Vector2 POSITION_OFFSET = new Vector2(-200f, -400f);
	
	private Map<EntityMood, String> chefTextureIdMapping;
	private EntityMood currentMood;
	
	private CookingSceneManager sceneManager;
	
	private SpriteRenderer renderer;
	private EntityAnimator animator;
	
	
	public Chef(CookingSceneManager sceneManager) {
		super("Cooking Chef");
		this.sceneManager = sceneManager;
		
		currentMood = EntityMood.IDLE;
		createTextureIds();
		createRendererComponent();
		
		animator = new EntityAnimator(this, new Vector2(1f, 1f));
		
		Vector2 finalPosition = Util.addVec2(POSITION_OFFSET, sceneManager.getScreenSize());
		this.setPosition(finalPosition);
	}
	
	@Override
	public void onUpdate(float deltaTime) {
		animator.update(deltaTime);
	}
	
	private void createTextureIds() {
		chefTextureIdMapping = new HashMap<EntityMood, String>();

		for (Entry<EntityMood, String> entry : CHEF_TEXTURE_LOCATIONS_MAPPING.entrySet()) {
			String textureId = sceneManager.createTexture(entry.getValue());
			chefTextureIdMapping.put(entry.getKey(), textureId);
		}
	}
	
	private void createRendererComponent() {
		Sprite idleSprite = sceneManager.createSpriteByTextureId(chefTextureIdMapping.get(EntityMood.IDLE));
		renderer = new SpriteRenderer(idleSprite);
		this.addComponent(renderer);
	}
	
	@Override
	public void changeMood(EntityMood mood) {
		String textureId = chefTextureIdMapping.get(mood);
		renderer.changeTexture(textureId);
		currentMood = mood;
		// TODO: Scale orientation
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
	
	public void doCookAnimation() {
		
		OnAnimationDone callback = new OnAnimationDone() {	
			@Override
			public void callback(AnimationType fromAnimation) {
				changeMood(EntityMood.IDLE);
			}
		};
		
		changeMood(EntityMood.CHOP);
		animator.triggerPause(1.2f, callback);
	}
	
}
