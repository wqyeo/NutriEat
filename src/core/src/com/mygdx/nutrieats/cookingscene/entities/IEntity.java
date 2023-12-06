package com.mygdx.nutrieats.cookingscene.entities;

import com.badlogic.gdx.math.Vector2;

interface IEntity {
	/**
	 * To change the sprite of an entity.
	 * @param mood
	 */
	public void changeMood(EntityMood mood);
	
	public EntityMood getCurrentMood();
	
	public Vector2 getEntityPosition();
	
	public void setEntityPosition(Vector2 position);
	
	public void setEntityScale(Vector2 scale);
}
