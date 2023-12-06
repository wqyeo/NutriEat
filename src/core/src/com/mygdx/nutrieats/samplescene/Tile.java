package com.mygdx.nutrieats.samplescene;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import eatengine.GameObject;
import eatengine.Util;
import eatengine.components.PhysicsBody;
import eatengine.components.SpriteRenderer;

public class Tile extends GameObject {

	private static int TILE_COUNT = 0;
	
	public static final float TILE_SIZE = 16;
	public static final float TILE_SCALE = 4;
	public static final String TILE_TEXTURE_LOCATION= "sprites\\tile.png";
	
	private SpriteRenderer spriteRenderer;
	
	public Tile(Vector2 position) {
		super("TILE_" + TILE_COUNT);
		++TILE_COUNT;
		setPosition(position);
		addSpriteRenderer();
		addPhysicsBody();
	}
	
//region adding components
	private void addSpriteRenderer() {
		Texture tileTexture = new Texture(TILE_TEXTURE_LOCATION);
		Sprite tileSprite = new Sprite(tileTexture);
		tileSprite.scale(TILE_SCALE);
		
		spriteRenderer = new SpriteRenderer(tileSprite);
		this.addComponent(spriteRenderer);
	}
	
	private void addPhysicsBody() {	
		PolygonShape tileCollider = createTileCollider();
		
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = tileCollider;
		fixtureDef.friction = 0.2f;
		
		PhysicsBody physicsBody = new PhysicsBody(BodyType.StaticBody, fixtureDef, tileCollider);
		this.addComponent(physicsBody);
	}
	
	private PolygonShape createTileCollider() {
		PolygonShape squareShape = new PolygonShape();
		Vector2[] vertices = spriteRenderer.getSpriteVertices();
		// Tiles collision sprite too small, scale it up a bit.
		for (int i = 0; i < vertices.length; ++i) {
		    vertices[i].scl(3f);
		}
		squareShape.set(vertices);
		
		return squareShape;
	}
//endregion
}
