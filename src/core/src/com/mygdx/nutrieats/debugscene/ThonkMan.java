package com.mygdx.nutrieats.debugscene;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import eatengine.GameObject;
import eatengine.components.PhysicsBody;
import eatengine.components.SpriteRenderer;

public class ThonkMan extends GameObject {
	private static int THONK_COUNT = 0;
	
	private PhysicsBody physicsBody;
	private Vector2 velocity;
	private float speed;

	public ThonkMan(float speed) {
		super("ThonkMan " + THONK_COUNT);
		++THONK_COUNT;
		createSpriteRendererComponent();
		createPhysicsBodyComponent();
		velocity = new Vector2(0,0);
		this.speed = speed;
	}
	
	private void createSpriteRendererComponent() {
		Texture thonkTexture = new Texture("sprites\\notThonk.png");
		Sprite thonkSprite = new Sprite(thonkTexture);
		
		SpriteRenderer spriteRendererComponent = new SpriteRenderer(thonkSprite);
		this.addComponent(spriteRendererComponent);
	}
	
	private void createPhysicsBodyComponent() {
		CircleShape circle = new CircleShape();
		circle.setRadius(6f);
		
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = circle;
		fixtureDef.density = 0.5f;
		fixtureDef.friction = 0.4f;
		fixtureDef.restitution = 0.6f;
		
		physicsBody = new PhysicsBody(BodyType.KinematicBody, fixtureDef, circle);
		this.addComponent(physicsBody);
	}
	
	@Override
	protected void onStart() {
		setRandomPosition();
	}
	
	@Override
	protected void onUpdate(float deltaTime) {
		float x = 0;
		float y = 0;
		if (Gdx.input.isKeyPressed(Keys.A)) {			
			x -= 1;
		}
		if (Gdx.input.isKeyPressed(Keys.D)) {
			x += 1;
		}
		
		if (Gdx.input.isKeyPressed(Keys.W)) {
			y += 1;
		}
		if (Gdx.input.isKeyPressed(Keys.S)) {
			y -= 1;
		}
		
		velocity = new Vector2(x * speed, y * speed);
	}
	
	@Override
	protected void onPhysicsUpdate(float fixedDeltaTime) {
		physicsBody.setVelocity(velocity);
	}
	
	
	private void setRandomPosition() {
		Random r = new Random();
		float xRandom = 0 + r.nextFloat() * (500 - 0);
		float yRandom = 0 + r.nextFloat() * (300 - 0);
		
		this.setPosition(new Vector2(xRandom, yRandom));
	}
}
