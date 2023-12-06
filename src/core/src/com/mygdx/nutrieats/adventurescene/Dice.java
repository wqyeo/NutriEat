package com.mygdx.nutrieats.adventurescene;

import java.util.Random;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

import eatengine.EatEngine;
import eatengine.GameObject;
import eatengine.components.SpriteRenderer;

public class Dice extends GameObject {
	
	private static final float DICE_ROTATION_AMOUNT = 270f;
	
	private static final float DICE_MAX_SPEED = 100f;
	private static final float DICE_MIN_SPEED = 70f;
	
	private static final float DICE_MAX_TOSS_ANGLE = 0.55f;
	private static final float DICE_MIN_TOSS_ANGLE = 0.35f;
	
	private static final Vector2 DICE_ACCELERATION = new Vector2(0, -255f);
	
	private static final String[] DICE_TEXTURE_LOCATIONS = {
			"sprites\\dice\\Dice_1.png",
			"sprites\\dice\\Dice_2.png",
			"sprites\\dice\\Dice_3.png",
			"sprites\\dice\\Dice_4.png",
			"sprites\\dice\\Dice_5.png",
			"sprites\\dice\\Dice_6.png"
	};
	
	private static final Vector2 POSITION_OFFSET = new Vector2(-200f, 10f);
	
	private final Vector2 defaultPosition;
	private String[] diceTextureId;
	private AdventureSceneManager sceneRef;
	
	private SpriteRenderer spriteRenderer;
	
	private Vector2 velocity;
	
	private boolean isRollingDice;
	
	/**
	 * If the dice falls below this y value, it will stop and generate a rolled value.
	 */
	private float yLimit;
	
	/**
	 * Used to count time before randomly switching sprite during rolling animation.
	 */
	private float rollTimer;
	
	private int currentDiceValue;
	
	private OnDiceRolled rolledCallback;
	
	public Dice(AdventureSceneManager sceneRef) {
		super("Dice");
		this.sceneRef = sceneRef;
		
		defaultPosition = new Vector2(POSITION_OFFSET.x + (sceneRef.getScreenWidth() / 2f), (sceneRef.getScreenHeight() / 2f) + POSITION_OFFSET.y);
		setPosition(defaultPosition);
		
		generateDiceTextures();
		createSpriteRenderer();
		
		velocity = new Vector2(0, 0);
		yLimit = defaultPosition.y - 60f;
		isRollingDice = false;
		
		currentDiceValue = 1;
	}
	
	private void generateDiceTextures() {
		diceTextureId = new String[6];
		
		for (int i = 0; i < DICE_TEXTURE_LOCATIONS.length; ++i) {
			diceTextureId[i] = sceneRef.createTextureId(DICE_TEXTURE_LOCATIONS[i]);
		}
	}
	
	private void createSpriteRenderer() {
		Sprite sprite = sceneRef.requestSpriteByTexture(diceTextureId[0]);
		spriteRenderer = new SpriteRenderer(sprite);
		// Render on top of everyone else
		spriteRenderer.setRenderLayer(1000);
		
		addComponent(spriteRenderer);
	}
	
	@Override
	public void onUpdate(float deltaTime) {
		if (!isRollingDice) {
			return;
		}
		rollTimer += deltaTime;
		if (rollTimer >= 0.15f) {
			rollTimer = 0f;
			
			currentDiceValue = randomlySetDiceValue() + 1;
		}
	}
	
	@Override
	public void onPhysicsUpdate(float fixedDeltaTime) {
		if (!isRollingDice) {
			return;
		}
		
		spriteRenderer.rotateSprite(fixedDeltaTime * DICE_ROTATION_AMOUNT);
		evaluateVelocity(fixedDeltaTime);
		evaluatePosition(fixedDeltaTime);
		
		if (getWorldPosition().y <= yLimit) {
			spriteRenderer.setSpriteRotation(0f);
			isRollingDice = false;
			
			if (rolledCallback != null) {
				rolledCallback.onDiceRolledCallback(currentDiceValue);
			}
			sceneRef.playSoundByType(AdventureAudioType.DICE_LAND);
		}
	}
	
	private int randomlySetDiceValue() {
		Random rand = new Random();
		int randDice = rand.nextInt(diceTextureId.length);
		spriteRenderer.changeTexture(diceTextureId[randDice]);
		return randDice;
	}
	
	private void evaluateVelocity(float fixedDeltaTime) {
		float xVel = velocity.x + (DICE_ACCELERATION.x * fixedDeltaTime);
		float yVel = velocity.y + (DICE_ACCELERATION.y * fixedDeltaTime);
		velocity = new Vector2(xVel, yVel);
	}
	
	private void evaluatePosition(float fixedDeltaTime) {
		float xPos = getWorldPosition().x + (velocity.x * fixedDeltaTime);
		float yPos = getWorldPosition().y + (velocity.y * fixedDeltaTime);
		this.setPosition(new Vector2(xPos, yPos));
	}
	
	public void rollDice(OnDiceRolled callback) {
		resetPosition();
		isRollingDice = true;
		
		Random rand = new Random();
        float angle =  rand.nextFloat() * (DICE_MAX_TOSS_ANGLE - DICE_MIN_TOSS_ANGLE) + DICE_MIN_TOSS_ANGLE;
        float speed = rand.nextFloat() * (DICE_MAX_SPEED - DICE_MIN_SPEED) + DICE_MIN_SPEED;
		velocity = new Vector2( speed * (float) Math.cos(angle), speed * (float) Math.sin(angle));
		
		rolledCallback = callback;
		setVisible(true);
		sceneRef.playSoundByType(AdventureAudioType.DICE_THROW);
	}
	
	private void resetPosition() {
		setPosition(defaultPosition);
	}
	
	public int getCurrentDiceValue() {
		return currentDiceValue;
	}
	
	public void setVisible(boolean visible) {
		spriteRenderer.setVisible(visible);
	}
}
