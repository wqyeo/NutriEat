package com.mygdx.nutrieats.debugscene;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;

import eatengine.GameObject;
import eatengine.components.gameinterface.Text;

public class Wordy extends GameObject {

	public Wordy() {
		super("Am a word");
		createTextComponent();
	}
	
	private void createTextComponent() {
		BitmapFont font = new BitmapFont();
		
		Text textComponent = new Text(font, "Lmaooooo");
		this.addComponent(textComponent);
	}
	
	@Override
	protected void onStart() {
		setPosition(new Vector2(100, 100));
	}
	
	@Override
	protected void onUpdate(float deltaTime) {
	}
}
