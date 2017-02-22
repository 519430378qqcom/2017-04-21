package cn.youmi.framework.util;

import java.util.HashMap;

public class SingletonFactory {
	private static HashMap<Class<?>,Object> objectPool = new HashMap<Class<?>, Object>();
	@SuppressWarnings("unchecked")
	public static <T> T getInstance(Class<T> clz){
		T obj;
		synchronized (objectPool) {
			obj = (T) objectPool.get(clz);
			if(obj == null){
				try {
					obj = clz.newInstance();
					objectPool.put(clz, obj);
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		return obj;
	}
}
