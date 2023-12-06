package com.mygdx.nutrieats.adventurescene;

import com.badlogic.gdx.graphics.g2d.Sprite;

import eatengine.GameObject;
import eatengine.components.SpriteRenderer;

class Tile extends GameObject {

	private TileType tileType;
	private SpriteRenderer renderer;
	
	public Tile(String tileName, TileType tileType, Sprite tileSprite) {
		super(tileName);
		
		this.tileType = tileType;
		createSpriteRenderer(tileSprite);
	}
	
	private void createSpriteRenderer(Sprite tileSprite) {
		renderer = new SpriteRenderer(tileSprite);
		this.addComponent(renderer);
	}
	
	public TileType getTileType() {
		return tileType;
	}
	
	public void changeTile(TileType newTile, String newTexture) {
		tileType = newTile;
		renderer.changeTexture(newTexture);
	}
}
