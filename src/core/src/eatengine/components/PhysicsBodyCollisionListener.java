package eatengine.components;

import eatengine.GameObject;

public interface PhysicsBodyCollisionListener {
	public void onCollisionEnter(PhysicsBody other);
	public void onCollisionExit(PhysicsBody other);
}
