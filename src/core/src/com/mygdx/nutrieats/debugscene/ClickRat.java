package com.mygdx.nutrieats.debugscene;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

import eatengine.GameObject;
import eatengine.components.audio.SoundSource;
import eatengine.components.gameinterface.ClickEventListener;
import eatengine.components.gameinterface.Clickable;
import eatengine.components.gameinterface.Image;

public class ClickRat extends GameObject {
	
	private static int CLICK_RAT_COUNT = 0;
	
	private SoundSource soundSource;

	public ClickRat() {
		super("click_rat " + CLICK_RAT_COUNT);
		++CLICK_RAT_COUNT;
		createImageComponent();
		createClickableComponent();
		createSoundComponent();
	}
	
	private void createSoundComponent() {
		Sound sound = Gdx.audio.newSound(Gdx.files.internal("Clack.mp3"));
		soundSource = new SoundSource(sound, 10, 0.05f);
		
		this.addComponent(soundSource);
	}
	
	private void createImageComponent() {
		Texture ratTexture = new Texture("sprites\\rattest.png");
		Sprite ratSprite = new Sprite(ratTexture);
		
		Image imageComponent = new Image(ratSprite);
		this.addComponent(imageComponent);
	}

	private void createClickableComponent() {
		Clickable clickable = new Clickable(new Vector2(140, 84));
		
		OnRatClicked ratClickedEvent = new OnRatClicked(this);
		clickable.addOnClickListener(ratClickedEvent);
		this.addComponent(clickable);
	}
	
	@Override
	protected void onStart() {
		setRandomPosition();
	}
	
	
	private void setRandomPosition() {
		Random r = new Random();
		float xRandom = 0 + r.nextFloat() * (500 - 0);
		float yRandom = 0 + r.nextFloat() * (300 - 0);
		
		setPosition(new Vector2(xRandom, yRandom));
	}
	
	private class OnRatClicked implements ClickEventListener {
		
		private ClickRat ratRef;
		
		public OnRatClicked(ClickRat rat) {
			ratRef = rat;
		}

		@Override
		public void onClickEvent(Clickable clickedObject) {
			System.out.println(ratRef.name + " clicked on.");
			soundSource.play();
		}
		
	}
}
