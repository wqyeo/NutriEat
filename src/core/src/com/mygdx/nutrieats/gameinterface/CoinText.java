package com.mygdx.nutrieats.gameinterface;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;

import eatengine.GameObject;
import eatengine.components.gameinterface.Text;

class CoinText extends GameObject {
	private Text textComponent;
	
	public CoinText(Vector2 position, float textScale, int initialCoinCount) {
		super("Coin Text");
		
		createText(textScale, initialCoinCount);
		this.setPosition(position);
	}
	
	private void createText(float textScale,  int initialCoinCount) {
		BitmapFont font = new BitmapFont(Gdx.files.internal("fonts\\Ubuntu-L.fnt"));
		textComponent = new Text(font, String.valueOf(initialCoinCount));
		
		textComponent.setAlignment(Align.right);
		textComponent.setScale(textScale);
		
		this.addComponent(textComponent);
	}
	
	public void incrementAmount() {
		String str = textComponent.getTextContent();
		int value = Integer.parseInt(str);
		++value;
		str = Integer.toString(value);
		textComponent.setText(str);
	}
	
	public void decrementAmount() {
		String str = textComponent.getTextContent();
		int value = Integer.parseInt(str);
		--value;
		str = Integer.toString(value);
		textComponent.setText(str);
	}
	
	public void setNewAmount(int amount) {
		textComponent.setText(Integer.toString(amount));
	}
}
