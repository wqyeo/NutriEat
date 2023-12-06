package eatengine.components.gameinterface;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;

public class Text extends GameInterface {

	protected BitmapFont font;
	protected String content;
	protected GlyphLayout layout;
	
	protected int currentAlignment;
	protected Color currentColor;
	
	public Text(BitmapFont font, String content) {
		this.font = font;
		this.content = content;
		layout = new GlyphLayout();
		
		currentAlignment = Align.center;
		currentColor = Color.WHITE;
		layout.setText(font, content, currentColor, 0, currentAlignment, false);
	}
	
	@Override
	public void onRender(SpriteBatch spriteBatch) {
		Vector2 renderPosition = getRenderPosition();
		font.draw(spriteBatch, layout, renderPosition.x, renderPosition.y);
	}
	
	@Override
	public void dispose() {
		if (font != null) {
			font.dispose();
		}
	}
	
	public void setScale(float scaleAmount) {
		font.getData().setScale(scaleAmount);
		layout.setText(font, content, currentColor, 0, currentAlignment, false);
	}
	
	public void setAlignment(int alignment) {
		currentAlignment = alignment;
		layout.setText(font, content, currentColor, 0, currentAlignment, false);
	}
	
	public void setColor(Color color) {
		currentColor = color;
		layout.setText(font, content, currentColor, 0, currentAlignment, false);
	}
	
	public void setText(String newText) {
		this.content = newText;
		layout.setText(font, newText, currentColor, 0, currentAlignment, false);
	}
	
	public String getTextContent() {
		return content;
	}
}
