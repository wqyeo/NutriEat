package eatengine.components;

import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;

import eatengine.Component;
import eatengine.Log;

public class PhysicsBody extends Component {
	
	private BodyType bodyType;
	private Body body;
	private Shape collisionShape;
	private FixtureDef fixtureDef;
	private Fixture bodyFixture;

	private Set<PhysicsBodyCollisionListener> collisionListeners;
	
	public PhysicsBody(BodyType bodyType, FixtureDef fixtureDef, Shape collisionShape) {
		this.bodyType = bodyType;
		this.fixtureDef = fixtureDef;
		this.collisionShape = collisionShape;
		
		collisionListeners = new HashSet<PhysicsBodyCollisionListener>();
	}
	
	@Override
	public void start() {
		if (getGameObject() == null) {
			Log.Warning("No gameobject attached to PhysicsBody component.");
			return;
		}
		
		BodyDef bodyDef = createBodyDef();
		body = createBody(bodyDef);
		
		getGameObject().getSceneReference().addBodyObjectPairing(body, this);
	}
	
	private BodyDef createBodyDef() {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = bodyType;
		bodyDef.position.set(getGameObject().getPosition());
		return bodyDef;
	}
	
	private Body createBody(BodyDef bodyDef) {
		body = getGameObject().getSceneReference().getWorld().createBody(bodyDef);
		body.setFixedRotation(true);
		bodyFixture = body.createFixture(fixtureDef);
		return body;
	}
	
	public final void setVelocity(Vector2 velocity) {
		body.setLinearVelocity(velocity);
	}
	
	public final void setVelocity(float x, float y) {
		body.setLinearVelocity(x, y);
	}
	
	
	public final Vector2 getVelocity() {
		return body.getLinearVelocity();
	}
	
	public final float getMass() {
		return body.getMass();
	}
	
	public final void applyLinearImpulse(Vector2 impulse) {
		body.applyLinearImpulse(impulse, body.getWorldCenter(), true);
	}
	
	public final void applyForceToCenter(Vector2 force) {
		body.applyForceToCenter(force, true);
	}
	
	public final void setPosition(Vector2 position) {
		body.getTransform().setPosition(position);
	}
	
	@Override
	public void update(float fixedDeltaTime) {
		getGameObject().setPosition(body.getPosition());
	}
	
	@Override
	public void dispose() {
		getGameObject().getSceneReference().removeBodyObjectPairing(body);
		collisionListeners.clear();
		collisionShape.dispose();
	}
	
	public final boolean addCollisionListener(PhysicsBodyCollisionListener listener) {
		if (listener == null) {
			Log.Warning("Input PhysicsBodyCollisionListener was null when adding listeners.");
			return false;
		}
		return collisionListeners.add(listener);
	}
	
	public final boolean removeCollisionListener(PhysicsBodyCollisionListener listener) {
		if (listener == null) {
			Log.Warning("Input PhysicsBodyCollisionListener was null when removing listeners.");
			return false;
		}
		return collisionListeners.remove(listener);
	}
	
	public final void notifyCollisionEnterWith(PhysicsBody other) {
		for (PhysicsBodyCollisionListener listener: collisionListeners) {
			listener.onCollisionEnter(other);
		}
	}
	
	public final void notifyCollisionExitWith(PhysicsBody other) {
		for (PhysicsBodyCollisionListener listener: collisionListeners) {
			listener.onCollisionExit(other);
		}
	}
}
