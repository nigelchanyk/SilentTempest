package ca.nigelchan.operationbanana.util;

import java.util.Stack;

import org.andengine.entity.IEntity;

public abstract class Pool<T extends IEntity> {
	
	private Stack<T> available = new Stack<T>();
	
	public void dispose() {
		while (!available.empty()) {
			T item = available.pop();
			if (item.isDisposed())
				continue;
			if (item.hasParent())
				item.detachSelf();
			item.dispose();
		}
	}
	
	public T get() {
		return available.empty() ? create() : available.pop();
	}
	
	public void recycle(T item) {
		available.push(item);
	}
	
	protected void initialize(int initialSize) {
		for (int i = 0; i < initialSize; ++i)
			available.push(create());
	}
	
	protected abstract T create();

}
