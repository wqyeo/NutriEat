package eatengine;

import java.util.Collection;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public final class PhysicsEngine {
	
	public static final float TIME_STEP = 1 / 60f;
	public static final int VELOCITY_ITERATIONS = 6;
	public static final int POSITION_ITERATIONS = 2;
	public static final Vector2 WORLD_GRAVITY = new Vector2(0, -98f);

	private float accumulator = 0;
	

	final void update(float deltaTime, World world, Collection<GameObject> gameObjects) {
		// https://libgdx.com/wiki/extensions/physics/box2d#creating-a-world
	    accumulator += Math.min(deltaTime, 0.25f);
	    while (accumulator >= TIME_STEP) {
	    	world.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
			for (GameObject gameObj: gameObjects) {
				gameObj.physicsUpdate(TIME_STEP);
			}
			
	        accumulator -= TIME_STEP;
	    }
	}
}
