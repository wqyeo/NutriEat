package eatengine;

import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.GdxRuntimeException;

final class TextureManager {
	private static int CREATION_ID = 0;
	private Set<GameTexture> loadedTextures;
	
	public TextureManager() {
		loadedTextures = new HashSet<GameTexture>();
	}
	
	/**
	 * Creates a new Texture from path. Returns ID of old texture if already loaded.
	 * @param path
	 * @return ID to the Texture, which can be used to fetch the texture again.
	 */
	public String createTexture(String path) {
		String id = searchForIdByPath(path);
		if (id != null) {
			return id;
		}
		GameTexture newTexture;
		try {
			newTexture = new GameTexture("T_" + CREATION_ID, path);
		} catch (GdxRuntimeException e) {
			Log.Severe("Failed to load texture from path, (" + path + "), Error Message: \r\n" + e.getMessage());
			return null;
		}
		++CREATION_ID;
		loadedTextures.add(newTexture);
		return newTexture.getId();
	}
	
	/**
	 * Fetech a texture by id, null if doesn't exists.
	 * @param id
	 * @return
	 */
	Texture getTextureById(String id) {
		for (GameTexture gameTexture: loadedTextures) {
			if (gameTexture.getId().equals(id)) {
				return gameTexture.getTexture();
			}
		}
		return null;
	}
	
	boolean textureExists(Texture texture) {
		for (GameTexture gameTexture: loadedTextures) {
			if (gameTexture.getTexture().equals(texture)) {
				return true;
			}
		}
		return false;
	}
	
	private String searchForIdByPath(String path) {
		for (GameTexture gameTexture: loadedTextures) {
			if (gameTexture.getPath().equals(path)) {
				return gameTexture.getId();
			}
		}
		
		return null;
	}
	
	void dispose() {
		for (GameTexture gameTexture: loadedTextures) {
			gameTexture.dispose();
		}
		loadedTextures.clear();
	}
}
