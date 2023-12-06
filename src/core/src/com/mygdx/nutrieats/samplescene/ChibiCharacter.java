package com.mygdx.nutrieats.samplescene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import eatengine.EatEngine;
import eatengine.GameObject;
import eatengine.Util;
import eatengine.components.PhysicsBody;
import eatengine.components.PhysicsBodyCollisionListener;
import eatengine.components.SpriteRenderer;

public class ChibiCharacter extends GameObject {
	
	public static final Vector2 SIZE = new Vector2(240, 353);
	public static final float SCALE = 0.15f;
	public static final float SPEED = 1000f;
	
	private static final String HAPPY_TEXTURE_LOCATION = "sprites\\chibi_happy.png";
	private static final String IDLE_TEXTURE_LOCATION = "sprites\\chibi_idle.png";
	private static final String STUN_TEXTURE_LOCATION = "sprites\\chibi_stun.png";
	
	public static final Vector2 JUMP_FORCE = new Vector2(1f, 100000000f);
	// Max number of updates the jump force is continually applied (after letting go of jump key)
	public static final int MAX_JUMP_STEPS = 5;
	// How many frames the jump button can be held.
	public static final int MAX_JUMP_HOLD = 100;
	// True if jumping
	private boolean jumpTrigger;
	// Jump force is applied over time, count the number of update() before stop applying force.
	private int jumpSteps;
	private boolean canJump;
	
	private int currJumpHoldCount;
	
	enum MoodState {
		IDLE,
		STUN,
		HAPPY
	}
	
	private Vector2 moveVelocity;
	private PhysicsBody physicsBody;
	
	private SpriteRenderer spriteRenderer;
	
	private String happyTextureId, idleTextureId, stunTextureId;
	private EatEngine engineRef;
	

	public ChibiCharacter(Vector2 startPosition, EatEngine engineRef) {
		super("Chibi");
		this.engineRef = engineRef;
		setPosition(startPosition);
		addSpriteRenderers();
		addPhysicsBody();
		
		moveVelocity = new Vector2(0,0);
		jumpTrigger = false;
		jumpSteps = 0;
		canJump = false;
		currJumpHoldCount = 0;
	}

//region create components
	private void addSpriteRenderers() {
		idleTextureId = engineRef.createTexutre(IDLE_TEXTURE_LOCATION);
		Sprite idleSprite = engineRef.createSpriteFromTextureId(idleTextureId);
		idleSprite.scale(SCALE);
		spriteRenderer = new SpriteRenderer(idleSprite);
		this.addComponent(spriteRenderer);
		
		happyTextureId = engineRef.createTexutre(HAPPY_TEXTURE_LOCATION);
		stunTextureId = engineRef.createTexutre(STUN_TEXTURE_LOCATION);
		spriteRenderer.setRenderLayer(100);
	}
	
	private void addPhysicsBody() {	
		PolygonShape collider = createCollider();
		
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = collider;
		fixtureDef.density = 15f;
		fixtureDef.friction = 0.6f;
		fixtureDef.restitution = 0.0f;
		
		physicsBody = new PhysicsBody(BodyType.DynamicBody, fixtureDef, collider);
		physicsBody.addCollisionListener(new ChibiCollisionListener(this));
		this.addComponent(physicsBody);
	}
	
	
	private PolygonShape createCollider() {
		Vector2[] vertices = spriteRenderer.getSpriteVertices();
		
		PolygonShape squareShape = new PolygonShape();
		squareShape.set(vertices);
		return squareShape;
	}
//endregion
	
	@Override
	protected void onUpdate(float deltaTime) {
		float x = 0.0f;
		if (Gdx.input.isKeyPressed(Keys.A)) {			
			x -= 1;
		}
		if (Gdx.input.isKeyPressed(Keys.D)) {
			x += 1;
		}
		
		if (Gdx.input.isKeyPressed(Keys.SPACE)) {
			if (canJump && currJumpHoldCount <= MAX_JUMP_HOLD) {
				jumpTrigger = true;
				jumpSteps = 0;
				changeState(MoodState.HAPPY);
				++currJumpHoldCount;
			}
			if (currJumpHoldCount >= MAX_JUMP_HOLD) {
				canJump = false;
			}
		} else if (jumpTrigger) {
			// Let go of space bar
			canJump = false;
		}
		
		
		moveVelocity = new Vector2(x * SPEED, physicsBody.getVelocity().y);
	}
	
	@Override
	protected void onPhysicsUpdate(float fixedDeltaTime) {
		if (jumpTrigger) {
			float force = JUMP_FORCE.y * physicsBody.getMass();
			
		    physicsBody.applyLinearImpulse(new Vector2(0, force));
		    ++jumpSteps;
			if (jumpSteps >= MAX_JUMP_STEPS) {
				jumpTrigger = false;
				currJumpHoldCount = 0;
			}
		} 

		// TODO: Should fall equally fast even when moving left/right.
		physicsBody.setVelocity(moveVelocity.x, physicsBody.getVelocity().y);
	}
	
	private void changeState(MoodState newState) {
		if (newState == MoodState.HAPPY) {
			spriteRenderer.changeTexture(happyTextureId);
		} else if (newState == MoodState.IDLE) {
			spriteRenderer.changeTexture(idleTextureId);
		} else if (newState == MoodState.STUN) {
			spriteRenderer.changeTexture(stunTextureId);
		}
	}
	
	private class ChibiCollisionListener implements PhysicsBodyCollisionListener {
		private ChibiCharacter chibiRef;
		
		public ChibiCollisionListener(ChibiCharacter chibiRef) {
			this.chibiRef = chibiRef;
		}
		
		@Override
		public void onCollisionEnter(PhysicsBody other) {
			System.out.println("Chibi character entered collision with " + other.getGameObject().name);
			chibiRef.canJump = true;
			chibiRef.changeState(MoodState.IDLE);
		}

		@Override
		public void onCollisionExit(PhysicsBody other) {
			System.out.println("Chibi character exit collision with " + other.getGameObject().name);
		}
		
	}
}
