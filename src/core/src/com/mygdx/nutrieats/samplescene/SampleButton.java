package com.mygdx.nutrieats.samplescene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.nutrieats.debugscene.ClickRat;

import eatengine.GameObject;
import eatengine.components.audio.SoundSource;
import eatengine.components.gameinterface.ClickEventListener;
import eatengine.components.gameinterface.Clickable;
import eatengine.components.gameinterface.Image;
import eatengine.components.gameinterface.Text;

public class SampleButton extends GameObject {
	
	private static final String BUTTON_TEXTURE_PATH = "sprites\\interface\\blue_button05.png";
	private static final String BUTTON_SOUND_PATH = "Clack.mp3";

	public SampleButton(String name, String buttonText, Vector2 buttonPosition, OnSampleButtonClicked onButtonClicked) {
		super(name);
		
		this.setPosition(buttonPosition);
		Texture buttonTexture = new Texture(BUTTON_TEXTURE_PATH);
		Sprite buttonSprite = new Sprite(buttonTexture);
		createImageComponent(buttonSprite);
		
		Vector2 offset = new Vector2(buttonSprite.getWidth()/2.8f, buttonSprite.getHeight()/1.9f);
		createTextComponent(buttonText, offset);
		
		Sound sound = Gdx.audio.newSound(Gdx.files.internal(BUTTON_SOUND_PATH));
		SoundSource soundSource = new SoundSource(sound, 10, 0.1f);
		this.addComponent(soundSource);
		onButtonClicked.setButtonSound(soundSource);
		
		Vector2 clickSize = new Vector2(buttonSprite.getWidth(), buttonSprite.getHeight());
		createClickableComponent(clickSize, onButtonClicked);
	}
	
	private void createImageComponent(Sprite sprite) {
		Image buttonImage = new Image( sprite);
		this.addComponent(buttonImage);
	}
	
	private void createTextComponent(String buttonText, Vector2 offset) {
		BitmapFont font = new BitmapFont();
		
		Text textComponent = new Text(font, buttonText);
		textComponent.setOffset(offset);
		// To render on top of the button image.
		textComponent.setRenderLayer(1);
		this.addComponent(textComponent);
	}
	
	private void createClickableComponent(Vector2 clickSize, OnSampleButtonClicked onButtonClicked) {
		Clickable clickable = new Clickable(new Vector2(140, 84));
		clickable.addOnClickListener(onButtonClicked);
		this.addComponent(clickable);
	}
}
