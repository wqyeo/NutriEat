package com.mygdx.nutrieats.gameinterface;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mygdx.nutrieats.PlayerData;

import eatengine.Scene;

public interface IGameInterfaceManager {

	/**
	 * Interface call to create a Texture; Returns a ID to reference the Texture.
	 * @param texturePath
	 * @return
	 */
	public String createTextureId(String texturePath);
	
	/**
	 * Interface call to create a Sprite with ID of texture.
	 * @param textureId
	 * @return
	 */
	public Sprite getSpriteByTextureId(String textureId);
	
	public Scene getSceneReference();

	public PlayerData getPlayerData();
}
