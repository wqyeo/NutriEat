package com.mygdx.nutrieats.cookingscene.gameinterface.cookingmenu;

import java.util.Collection;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.mygdx.nutrieats.cookingsystem.FoodTag;

import eatengine.GameObject;
import eatengine.Util;
import eatengine.components.gameinterface.Text;

class CookingTagsText extends GameObject {
	/**
	 * How many characters in 1 line before splitting it into a new line.
	 */
	private static final int MAX_LINE_LENGTH = 70;
	private static final Vector2 POSITION_OFFSET = new Vector2(-815f, -390f);
	private static final float TEXT_SCALE = 0.6f;
	
	private Text tagsText;
	private CookingMenu cookingMenu;
	
	public CookingTagsText(CookingMenu cookingMenu) {
		super("Cooking Tags List");
		
		createTextComponent();
		Vector2 finalPosition = Util.addVec2(POSITION_OFFSET, cookingMenu.getSceneManager().getScreenSize());
		this.setPosition(finalPosition);
	}
	
	public void clear() {
		tagsText.setText("");
	}
	
	public void fillTextWithTags(Collection<FoodTag> foodTags) {
		List<String> contents = Util.enumCollectionToReadableStringList(foodTags);
		tagsText.setText(Util.joinStrings(contents, MAX_LINE_LENGTH));
	}
	
	private void createTextComponent() {
		BitmapFont font = new BitmapFont(Gdx.files.internal("fonts\\Ubuntu-L.fnt"));
		tagsText = new Text(font, "");
		tagsText.setScale(TEXT_SCALE);
		tagsText.setAlignment(Align.left);
		this.addComponent(tagsText);
	}
}
