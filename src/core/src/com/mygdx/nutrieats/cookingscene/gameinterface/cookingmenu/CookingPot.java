package com.mygdx.nutrieats.cookingscene.gameinterface.cookingmenu;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.nutrieats.cookingscene.CookingSceneManager;
import com.mygdx.nutrieats.cookingsystem.Recipe;

import eatengine.GameObject;
import eatengine.Util;
import eatengine.components.gameinterface.Image;

class CookingPot extends GameObject {
	
	private static final String COOKING_POT_TEXTURE_PATH = "sprites\\cooking_pot.png";
	private static final Vector2 POSITION_OFFSET = new Vector2(-500f, -600f);
	
	private static final float TARGET_SPRITE_SIZE = 64f;
	
	private static String textureId;
	
	private Image targetRecipeImage;
	static final Vector2 RECIPE_IMAGE_OFFSET = new Vector2(240f, 240f);
	
	private CookingSceneManager sceneRef;
	private Image potImage;
	
	public CookingPot(Vector2 screenSize, CookingSceneManager sceneRef) {
		super("Cooking Pot");
		this.sceneRef = sceneRef;
		
		createImageComponent();
		Vector2 finalPosition = Util.addVec2(screenSize, POSITION_OFFSET);
		this.setPosition(finalPosition);
		
		sceneRef.getScene().addGameObjectToRoot(this);
	}
	
	void clearRecipeDisplay() {
		if (targetRecipeImage == null) { 
			return;
		}
		targetRecipeImage.detachFromGameObject();
		targetRecipeImage.dispose();
		targetRecipeImage = null;
		potImage.setAlpha(1f);
	}
	
	void setRecipeDisplay(String recipeTextureId) {
		Sprite sprite = sceneRef.createSpriteByTextureId(recipeTextureId);
		scaleSpriteToTargetSize(sprite);
		
		clearRecipeDisplay();
		
		targetRecipeImage = new Image(sprite);
		targetRecipeImage.setOffset(RECIPE_IMAGE_OFFSET);
		targetRecipeImage.setRenderLayer(-1);
		potImage.setAlpha(0.5f);
		this.addComponent(targetRecipeImage);
	}
	
	private void createImageComponent() {
		Sprite potSprite = getPotSprite();
		scaleSpriteToTargetSize(potSprite);
		potImage = new Image(potSprite);
		this.addComponent(potImage);
	}
	
	private Sprite getPotSprite() {
		if (textureId == null) {
			textureId = sceneRef.createTexture(COOKING_POT_TEXTURE_PATH);
		}
		return sceneRef.createSpriteByTextureId(textureId);
	}
	
	private static void scaleSpriteToTargetSize(Sprite sprite) {
		Texture texture = sprite.getTexture();
		float scaleX = TARGET_SPRITE_SIZE / texture.getWidth();
		float scaleY = TARGET_SPRITE_SIZE / texture.getHeight();
		sprite.setScale(scaleX, scaleY);
	}
}
