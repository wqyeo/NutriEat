package eatengine;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.nutrieats.cookingsystem.FoodTag;

public final class Util {

	/**
	 * Does some thing in old style.
	 *
	 * @deprecated Request type, then use 'instanceof' instead.
	 */
	@Deprecated
	public static <T> boolean tryCast(Object o) {
	    try {
	       T temp = (T) o;
	       return temp != null;
	    } catch (ClassCastException e) {
	        return false;
	    }
	}

	/**
	 * Subtract two vector without affecting their references.
	 * @param a The value to be subtracted.
	 * @param b The subtracting value.
	 * @return A - B; A new Vector2 object.
	 */
	public static Vector2 subVec2(Vector2 a, Vector2 b) {
		return new Vector2(a.x - b.x, a.y - b.y);
	}

	/**
	 * Adds two vector without affecting their references.
	 * @param a
	 * @param b
	 * @return A + B; A new Vector2 object.
	 */
	public static Vector2 addVec2(Vector2 a, Vector2 b) {
		return new Vector2(a.x + b.x, a.y + b.y);
	}
	
	public static Vector2 multiplyVec2(Vector2 a, float b) {
		return new Vector2(a.x * b, a.y * b);
	}
	
	public static boolean isClose(Vector2 v1, Vector2 v2, float threshold) {
	    return v1.dst2(v2) < threshold * threshold;
	}

	/**
	 * Get the four corner vertices from the sprite.
	 * @param sprite
	 * @return
	 */
	public static Vector2[] getBoxVerticesFromSprite(Sprite sprite) {
		float spriteX = sprite.getX();
		float spriteY = sprite.getY();
		float spriteWidth = sprite.getWidth();
		float spriteHeight = sprite.getHeight();

		// Calculate the coordinates of the four corners
		Vector2[] vertices = new Vector2[4];
		vertices[0] = new Vector2(spriteX, spriteY);
		vertices[1] = new Vector2(spriteX + spriteWidth, spriteY);
		vertices[2] = new Vector2(spriteX + spriteWidth, spriteY + spriteHeight);
		vertices[3] = new Vector2(spriteX, spriteY + spriteHeight);

		return vertices;
	}

	public static Vector2 lerpVec2(Vector2 start, Vector2 end, float t){
		float x = lerp(start.x, end.x, t);
		float y = lerp(start.y, end.y, t);
		return new Vector2(x, y);
	}

	public static float lerp(float start, float end, float t) {
	    return start * (1 - t) + end * t;
	}
	
	public static <T> Set<T> shallowCloneSet(Set<T> set) {
	    Set<T> clone = new HashSet<T>();
	    for (T element : set) {
	        clone.add(element);
	    }
	    return clone;
	}
	
	public static <T extends Enum<T>> String enumArrayToString(T[] array) {
	    StringBuilder sb = new StringBuilder();
	    for (int i = 0; i < array.length; i++) {
	        if (i > 0) {
	            sb.append(", ");
	        }
	        sb.append(array[i].name());
	    }
	    return sb.toString();
	}
	
	/**
	 * Replaces all '_' with ' '. First letter of each word is then capitalized, the rest of the characters are lower case.
	 * @param e
	 * @return
	 */
	public static String enumToReadableString(Enum<?> e) {
	    String enumName = e.name();
	    String[] words = enumName.split("_");

	    StringBuilder sb = new StringBuilder();
	    for (int i = 0; i < words.length; i++) {
	        String word = words[i];
	        sb.append(word.substring(0, 1).toUpperCase());
	        sb.append(word.substring(1).toLowerCase());
	        if (i != words.length - 1) {
	            sb.append(" ");
	        }
	    }

	    return sb.toString();
	}
	
	
	public static <T extends Enum<T>> String enumCollectionToReadableString(Collection<T> collectionE) {
		List<String> enumString = new java.util.ArrayList<String>();
		
		for (T e : collectionE) {
			enumString.add(enumToReadableString(e));
		}
		
		return String.join(", ", enumString);
	}
	
	public static <T extends Enum<T>> String enumArrayToReadableString(T[] collectionE) {
		List<String> enumString = new java.util.ArrayList<String>();
		
		for (T e : collectionE) {
			enumString.add(enumToReadableString(e));
		}
		
		return String.join(", ", enumString);
	}
	
	public static <T extends Enum<T>> List<String> enumCollectionToReadableStringList(Collection<T> collectionE) {
		List<String> enumString = new java.util.ArrayList<String>();
		
		for (T e : collectionE) {
			enumString.add(enumToReadableString(e));
		}
		
		return enumString;
	}
	
	/**
	 * Join strings by comma; If string hit max length, it will insert a newline without breaking apart the comma.
	 * @param strings
	 * @param maxLineLength
	 * @return
	 */
	public static String joinStrings(List<String> strings, int maxLineLength) {
	    StringBuilder sb = new StringBuilder();
	    int lineLength = 0;

	    for (String s : strings) {
	        if (lineLength + s.length() + 2 > maxLineLength) {
	            sb.append(System.lineSeparator());
	            lineLength = 0;
	        }
	        if (lineLength > 0) {
	            sb.append(", ");
	            lineLength += 2;
	        }
	        sb.append(s);
	        lineLength += s.length();
	    }

	    return sb.toString();
	}
	
}
