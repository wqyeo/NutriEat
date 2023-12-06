package eatengine;

import com.badlogic.gdx.graphics.Texture;

public final class GameTexture {
	private String id;
	private String path;
	private Texture texture;
	
	public GameTexture(String id, String path) {
		this.id = id;
		this.path = path;
		texture = new Texture(path);
	}
	
	Texture getTexture() {
		return texture;
	}
	
	public String getId() {
		return id;
	}
	
	public String getPath() {
		return path;
	}
	
	void dispose() {
		if (texture != null) {
			texture.dispose();
		}
	}
}
