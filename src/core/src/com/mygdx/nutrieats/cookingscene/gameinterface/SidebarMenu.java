package com.mygdx.nutrieats.cookingscene.gameinterface;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.nutrieats.cookingscene.CookingAudioType;
import com.mygdx.nutrieats.gameinterface.GameButton;
import com.mygdx.nutrieats.gameinterface.GameButtonClickedCallback;
import com.mygdx.nutrieats.gameinterface.GameButtonProperties;

import eatengine.Util;

/**
 * Represents and handles the menu at the left side in cooking menu.
 * @author wqyeo
 *
 */
class SidebarMenu {
	
	private enum ButtonType {
		RECIPE,
		SHOP
	}
	
	private static final float BUTTON_SCALE = 0.3f;
	private static final Vector2 BUTTON_CLICK_SIZE = new Vector2(200f, 200f);
	private static final Vector2 BUTTON_CLICK_OFFSET=  new Vector2(25f, 230f);
	
//region RECIPE_SIDEBAR
	private static final String RECIPE_BUTTON_TEXTURE_PATH_STRING = "sprites\\interface\\RecipeSideBar.png";
	private static final Vector2 RECIPE_SIDEBAR_POSITION_OFFSET = new Vector2(-1240f, -475f);
//endregion
	
//region SHOP_SIDBAR
	private static final String SHOP_BUTTON_TEXTURE_PATH_STRING = "sprites\\interface\\ShopSideBar.png";
	private static final Vector2 SHOP_SIDEBAR_POSITION_OFFSET = new Vector2(-1240f, -700f);
//region
	
	private CookingGameInterfaceManager interfaceManager;
	
	private GameButton recipeSidebar;
	private GameButton shopSidebar;
	
	public SidebarMenu(CookingGameInterfaceManager interfaceManager) {
		this.interfaceManager = interfaceManager;
		
		createShopSidebar();
		createRecipeSidebar();
	}
	
	private void createShopSidebar() {
		String textureId = interfaceManager.createTextureId(SHOP_BUTTON_TEXTURE_PATH_STRING);
		Sprite recipeBtnSprite = interfaceManager.getSpriteByTextureId(textureId);
		// Calculate Position;
		Vector2 position = Util.addVec2(SHOP_SIDEBAR_POSITION_OFFSET, interfaceManager.getScreenSize());
		// Callback
		SideBarButtonClicked clickCallback = new SideBarButtonClicked(ButtonType.SHOP);
		
		GameButtonProperties properties = new GameButtonProperties(position, recipeBtnSprite, BUTTON_SCALE, BUTTON_CLICK_SIZE, BUTTON_CLICK_OFFSET, clickCallback);
		shopSidebar = new GameButton("Shop Sidebar", properties);
		interfaceManager.addObjectToScene(shopSidebar);
	}
	
	private void createRecipeSidebar() {
		// Create sprite
		String textureId = interfaceManager.createTextureId(RECIPE_BUTTON_TEXTURE_PATH_STRING);
		Sprite recipeBtnSprite = interfaceManager.getSpriteByTextureId(textureId);
		// Calculate Position;
		Vector2 position = Util.addVec2(RECIPE_SIDEBAR_POSITION_OFFSET, interfaceManager.getScreenSize());
		// Callback
		SideBarButtonClicked clickCallback = new SideBarButtonClicked(ButtonType.RECIPE);
		
		GameButtonProperties properties = new GameButtonProperties(position, recipeBtnSprite, BUTTON_SCALE, BUTTON_CLICK_SIZE, BUTTON_CLICK_OFFSET, clickCallback);
		recipeSidebar = new GameButton("Recipe Sidebar", properties);
		interfaceManager.addObjectToScene(recipeSidebar);
	}
	
	private class SideBarButtonClicked extends GameButtonClickedCallback {
		private ButtonType buttonType;
		
		private SideBarButtonClicked(ButtonType buttonType) {
			this.buttonType = buttonType;
		}
		
		@Override
		public void invoke(GameButton gameButton) {
			if (buttonType == ButtonType.RECIPE) {
				interfaceManager.triggerShowRecipeList();
			} else if (buttonType == ButtonType.SHOP) {
				interfaceManager.triggerShowShop();
			} else {			
				System.out.println("Cooking sidebar, unimplemented of " + gameButton);
			}
			
			interfaceManager.playSoundByType(CookingAudioType.BUTTON_CLICK);
		}
		
	}
}
