package eatengine.util;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

public final class ObjectPool<T> {
	private Set<T> objectPool;
	
	public ObjectPool(){
		objectPool = new HashSet<T>();
	}
	
	public void foreach(Consumer<T> action) {
		for (T obj: objectPool) {
			action.accept(obj);
		}
	}
	
	public T findFirstOrNull(Function<T, Boolean> predicate) {
		for (T obj: objectPool) {
			if (predicate.apply(obj)) {
				return obj;
			}
		}
		
		return null;
	}
	
	public void removeByCondition(Function<T, Boolean> predicate) {
		for (T obj: new HashSet<T>(objectPool)) {
			if (predicate.apply(obj)) {
				remove(obj);
			}
		}
	}
	
	public boolean add(T obj) {
		return objectPool.add(obj);
	}
	
	public boolean remove(T obj) {
		return objectPool.remove(obj);
	}
	
	public int size() {
		return objectPool.size();
	} 
}
