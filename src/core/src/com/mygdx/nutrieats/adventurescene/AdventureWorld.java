package com.mygdx.nutrieats.adventurescene;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.Sprite;

import com.badlogic.gdx.math.Vector2;
import eatengine.GameObject;
import java.util.Random;
import eatengine.Util;
import eatengine.components.audio.MusicSource;

public class AdventureWorld extends GameObject {
	

	public static final int TILES_COUNT = 32;
	private static final Map<TileType, String> TILE_TEXTURE_LOCATIONS_MAPPING;

	private static final float TILE_SCALE = 0.8f;
	/**
	* Increment positional by this X amount when new tiles are placed side-by-side.
	*/
	static final float TILE_INCREMENTAL_POSITION = 128 * TILE_SCALE;

	private static final Vector2 START_PLACEMENT_OFFSET = new Vector2(325f, 115f);

	static {
		TILE_TEXTURE_LOCATIONS_MAPPING = new HashMap<TileType, String>();
		TILE_TEXTURE_LOCATIONS_MAPPING.put(TileType.VEGETABLE, "sprites\\tile\\Vegetable.png");
		TILE_TEXTURE_LOCATIONS_MAPPING.put(TileType.STAR, "sprites\\tile\\Star.png");
		TILE_TEXTURE_LOCATIONS_MAPPING.put(TileType.RANDOM, "sprites\\tile\\Random.png");
		TILE_TEXTURE_LOCATIONS_MAPPING.put(TileType.MEAT, "sprites\\tile\\Meat.png");
		TILE_TEXTURE_LOCATIONS_MAPPING.put(TileType.END, "sprites\\tile\\End.png");
		TILE_TEXTURE_LOCATIONS_MAPPING.put(TileType.BACK_MOVE, "sprites\\tile\\BackMove.png");
		TILE_TEXTURE_LOCATIONS_MAPPING.put(TileType.WATER, "sprites\\tile\\Water.png");
		TILE_TEXTURE_LOCATIONS_MAPPING.put(TileType.NEUTRAL, "sprites\\tile\\Neutral.png");
	}

	private WorldMover worldMover;

	private HashMap<TileType, String> tileTextureIdMapping;

	private AdventureSceneManager managerRef;

	private Tile[] worldTiles;
	private final Vector2 tileStartPlacementPosition;

	/**
	*	Where is the player at in this world now.
	*/
	private int currentWorldStep;

	public AdventureWorld(AdventureSceneManager managerRef) {
		super("Adventure World");
		this.managerRef = managerRef;
		tileStartPlacementPosition = new Vector2((managerRef.getScreenWidth() / 2f) - START_PLACEMENT_OFFSET.x, (managerRef.getScreenHeight() / 2f) - START_PLACEMENT_OFFSET.y);

		this.setPosition(new Vector2(0,0));
		createTileTextures();
		generateWorld();
		
		worldMover = new WorldMover(this);
		this.addComponent(worldMover);
		currentWorldStep = 0;
	}

	private void createTileTextures() {
		tileTextureIdMapping = new HashMap<TileType, String>();

		for (Entry<TileType, String> entry : TILE_TEXTURE_LOCATIONS_MAPPING.entrySet()) {
			String textureId = managerRef.createTextureId(entry.getValue());
			tileTextureIdMapping.put(entry.getKey(), textureId);
		}
	}

	private void generateWorld() {
		worldTiles = new Tile[TILES_COUNT];

		createTileOnIndex(TileType.NEUTRAL, 0);

		List<TileType> worldTileType = generateTilesList(TILES_COUNT - 1);
		
		// Ensure that the first tile the player encounter will never be a backtrack tile.
		worldTileType.set(1, TileType.STAR);
		for (int i = 1;  i < TILES_COUNT - 1; ++i) {
			createTileOnIndex(worldTileType.get(i), i);
		}

		createTileOnIndex(TileType.END, TILES_COUNT - 1);
	}

	private void createTileOnIndex(TileType tileType, int index){
		Sprite tileSprite = managerRef.requestSpriteByTexture(tileTextureIdMapping.get(tileType));
		tileSprite.setScale(TILE_SCALE);

		worldTiles[index] = new Tile("Tile_" + index, tileType, tileSprite);
		Vector2 tileWorldPosition = Util.addVec2(tileStartPlacementPosition, new Vector2(TILE_INCREMENTAL_POSITION * index, 0));
		worldTiles[index].setPosition(tileWorldPosition);
		worldTiles[index].setParent(this);
	}
	
	/**
	 * Move the world by 1 step
	 * @param stepSpeed How fast the world animates.
	 * @return True if this action is possible. False if action causes out of bounds.
	 */
	public boolean stepWorld(float stepSpeed) {
		return stepWorld(stepSpeed, true);
	}

	/**
	 * Move the world by 1 step.
	 * @param stepSpeed How fast the world animates.
	 * @param stepForward False if the world steps backwards.
	 * @return True if this action is possible. False if action causes out of bounds.
	 */
	public boolean stepWorld(float stepSpeed, boolean stepForward) {
		if (stepForward && currentWorldStep >= TILES_COUNT - 1){
			System.out.println("Requesting to step too far out into the world! Nothing will happen.");
			return false;
		} else if (!stepForward && currentWorldStep <= 0) {
			System.out.println("Requesting to reverse too far in the world! Nothing will happen.");
			return false;
		}
		worldMover.invokeMove(stepSpeed, stepForward);
		
		currentWorldStep += stepForward ? 1 : -1;
		return true;
	}

	public TileType getTileTypeOnIndex(int index) {
		return worldTiles[index].getTileType();
	}

	public int getCurrentWorldStep(){
		return currentWorldStep;
	}

	public TileType getCurrentWorldTile(){
		return worldTiles[currentWorldStep].getTileType();
	}
	
	public void convertTileToStar(int index) {
		String textureId = tileTextureIdMapping.get(TileType.STAR);
		worldTiles[index].changeTile(TileType.STAR, textureId);;
	}

//region Tile Generation

	/**
	 * This function generates a number of tiles with dynamic bias.
	 * Tiles that hasn't been recently generated will get a higher bias, and tiles that was recently generated gets a lower bias.
	 *
	 * Ensures that the tiles are evenly generated according to the initial bias.
	 * @param count
	 * @return
	 */
	private static List<TileType> generateTilesList(int count) {
	    // Initialize weights
	    Map<TileType, Float> typeWeights = new HashMap<>();
	    typeWeights.put(TileType.VEGETABLE, 0.20f);
	    typeWeights.put(TileType.MEAT, 0.20f);
	    typeWeights.put(TileType.STAR, 0.15f);
	    typeWeights.put(TileType.WATER, 0.20f);
	    typeWeights.put(TileType.RANDOM, 0.05f);
	    typeWeights.put(TileType.BACK_MOVE, 0.02f);

	    // Initialize queue and list of generated tiles
	    Queue<TileType> tileQueue = new LinkedList<>();
	    List<TileType> generatedTiles = new ArrayList<>();

	    // Generate tiles
	    for (int i = 0; i < count; i++) {
	        // Adjust weights based on frequency in queue
	        Map<TileType, Float> adjustedWeights = adjustWeights(typeWeights, tileQueue);

	        // Generate random tile
	        TileType tile = getRandomTile(adjustedWeights);

	        // Add tile to queue and list of generated tiles
	        tileQueue.offer(tile);
	        generatedTiles.add(tile);
	    }

	    return generatedTiles;
	}

	private static Map<TileType, Float> adjustWeights(Map<TileType, Float> weights, Queue<TileType> queue) {
	    Map<TileType, Float> adjustedWeights = new HashMap<>(weights);

	    for (TileType type : queue) {
	        float oldWeight = adjustedWeights.get(type);
	        float newWeight = oldWeight * 0.04f;
	        adjustedWeights.put(type, newWeight);
	    }

	    return adjustedWeights;
	}

	private static TileType getRandomTile(Map<TileType, Float> weights) {
	    float totalWeight = calculateTotalWeight(weights.values());
	    float randomValue = getRandomValue(totalWeight);
	    float cumulativeWeight = 0.0f;

	    for (Map.Entry<TileType, Float> entry : weights.entrySet()) {
	        TileType type = entry.getKey();
	        float weight = entry.getValue();
	        cumulativeWeight += weight;
	        if (randomValue <= cumulativeWeight) {
	            return type;
	        }
	    }

	    // If the random value is greater than the cumulative weight, return the first type in the map
	    return weights.keySet().iterator().next();
	}

	private static float calculateTotalWeight(Collection<Float> weights) {
	    float totalWeight = 0.0f;
	    for (float weight : weights) {
	        totalWeight += weight;
	    }
	    return totalWeight;
	}

	private static float getRandomValue(float totalWeight) {
	    Random random = new Random();
	    return random.nextFloat() * totalWeight;
	}
//endregion
}
