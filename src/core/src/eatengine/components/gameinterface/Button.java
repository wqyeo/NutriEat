package eatengine.components.gameinterface;

import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import eatengine.Log;

/**
 * Interface for TextButton.
 * TODO: Add
 * @author wqyeo
 *
 */
public class Button extends GameInterface {
	private TextButton.TextButtonStyle style;
	
	private TextButton textButton;
	private TextButtonClickListener listenerManager;
	
	public Button(TextButton.TextButtonStyle style) {
		this.style = style;
		createButton("");
	}
	
	public Button(TextButton.TextButtonStyle style, String text) {
		this.style = style;
		createButton(text);
	}
	
	private void createButton(String text) {
		textButton = new TextButton(text, style);
		
		listenerManager = new TextButtonClickListener(this);
		textButton.addListener(listenerManager);
	}

	@Override
	public void start() {
		Stage stage;
		try {
			stage = this.getGameObject().getSceneReference().getEngineReference().getStage();
		} catch (NullPointerException e) {
			Log.Error("(" + this.getGameObject().name + ") :: TextButton failed to fetch engine's stage.");
			return;
		}
		
		stage.addActor(textButton);
	}
	
	@Override
	protected void onRender(SpriteBatch spriteBatch) {
		Vector2 position = this.getGameObject().getWorldPosition();
		textButton.setPosition(position.x, position.y);
	}
	
	public void setText(String newText) {
		textButton.setText(newText);
	}
	
	public String getText() {
		return textButton.getText().toString();
	}
	
	public boolean addListener(ButtonEventListener listener) {
		return listenerManager.getListeners().add(listener);
	}
	
	public boolean removeListener(ButtonEventListener listener) {
		return listenerManager.getListeners().remove(listener);
	}
	
	@Override
	public void dispose() {
		textButton.remove();
	}
	
	private class TextButtonClickListener extends ClickListener {
		
		private Set<ButtonEventListener> listeners;
		Button buttonRef;
		
		public TextButtonClickListener(Button buttonRef) {
			listeners = new HashSet<ButtonEventListener>();
			this.buttonRef = buttonRef;
		}
		
	    @Override
	    public void clicked(InputEvent event, float x, float y) {
	        for (ButtonEventListener listener :  listeners) {
	        	listener.onButtonClicked(buttonRef);
	        }
	    }

	    public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
	        for (ButtonEventListener listener :  listeners) {
	        	listener.onButtonHover(buttonRef, true);
	        }
	    }
	    
	    @Override
	    public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
	        for (ButtonEventListener listener :  listeners) {
	        	listener.onButtonHover(buttonRef, false);
	        }
	    }
	    
		public Set<ButtonEventListener> getListeners(){
			return listeners;
		}
	}
	
	
}
