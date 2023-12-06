package com.mygdx.nutrieats.gameinterface;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.mygdx.nutrieats.inventorysystem.Item;

import eatengine.GameObject;
import eatengine.Util;
import eatengine.components.gameinterface.Image;
import eatengine.components.gameinterface.Text;

/**
 * The text box that will be shown at the bottom of the screen
 *
 */
public class DescriptionBox extends GameObject {
	
	private static final String BOX_TEXTURE_LOCATION = "sprites\\interface\\InventoryBag.png";
	
	private static final Vector2 DESCRIPTION_TEXT_POSITION_OFFSET = new Vector2(170f, 490f);
	private static final Vector2 TITLE_TEXT_POSITION_OFFSET = new Vector2(170f, 525f);
	
	private static final Vector2 POSITION_OFFSET = new Vector2(-1100f, -1000f);

	private Text descriptionText;
	private Text titleText;
	
	public DescriptionBox(Vector2 screenSize, IGameInterfaceManager interfaceManager) {
		super("Item description box");
		init(screenSize.x, screenSize.y, interfaceManager);
	}
	
	public DescriptionBox(float screenWidth, float screenHeight, IGameInterfaceManager interfaceManager) {
		super("Item description box");
		init(screenWidth, screenHeight, interfaceManager);
	}
	
	private void init(float screenWidth, float screenHeight, IGameInterfaceManager interfaceManager) {
		createDescriptionTextComponent();
		createTitleTextComponent();
		
		String textureId = interfaceManager.createTextureId(BOX_TEXTURE_LOCATION);
		Sprite boxSprite = interfaceManager.getSpriteByTextureId(textureId);
		createImageComponent(boxSprite);
		
		Vector2 position = Util.addVec2(POSITION_OFFSET, new Vector2(screenWidth, screenHeight));
		this.setPosition(position);
	}
	
	private void createImageComponent(Sprite sprite) {
		sprite.setScale(0.7f);
		Image image = new Image(sprite);
		image.setRenderLayer(-1);
		this.addComponent(image);
	}
	
	private void createDescriptionTextComponent() {
		BitmapFont font = new BitmapFont(Gdx.files.internal("fonts\\Ubuntu-L.fnt"));
		descriptionText = new Text(font, "");
		descriptionText.setOffset(DESCRIPTION_TEXT_POSITION_OFFSET);
		descriptionText.setAlignment(Align.left);
		descriptionText.setScale(0.5f);
		descriptionText.layer = 100;
		
		this.addComponent(descriptionText);
	}
	
	private void createTitleTextComponent() {
		BitmapFont font = new BitmapFont(Gdx.files.internal("fonts\\Ubuntu-L.fnt"));
		titleText = new Text(font, "");
		titleText.setOffset(TITLE_TEXT_POSITION_OFFSET);
		titleText.setAlignment(Align.left);
		titleText.setScale(0.8f);
		titleText.layer = 100;
		
		this.addComponent(titleText);
	}
	
	public void fillDescriptionWithItem(Item item) {
		// TODO: Do test wrapping for description text, if text is too long.
		descriptionText.setText(item.getDescription());
		titleText.setText(item.getName());
	}
	
	public void fillDescriptionBox(String mainText, String subText) {
		descriptionText.setText(subText);
		titleText.setText(mainText);
	}
	
}
