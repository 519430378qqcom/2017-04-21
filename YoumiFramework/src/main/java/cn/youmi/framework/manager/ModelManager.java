package cn.youmi.framework.manager;

import java.util.HashSet;
import java.util.List;

public class ModelManager<K,E> {

	public interface ModelStatusListener<K,E>{
		void onModelGet(K key, E models);
		void onModelUpdate(K key, E model);
		void onModelsGet(K key, List<E> models);
	}
	protected HashSet<ModelStatusListener<K,E>> listeners = new HashSet<ModelManager.ModelStatusListener<K,E>>();
	public void  registerListener(ModelStatusListener<K,E> listener){
		listeners.add(listener);
	}
	
	public void unregisterListener(ModelStatusListener<K,E> listener){
		listeners.remove(listener);
	}
	
	public void onModelGet(K key,E models) {
		for(ModelStatusListener<K,E> mModelStatusListener:listeners) {
			mModelStatusListener.onModelGet(key, models);
		}
	}
	
	public void onModelUpdate(K key,E model) {
		for(ModelStatusListener<K,E> mModelStatusListener:listeners) {
			mModelStatusListener.onModelUpdate(key, model);
		}
	}
	
	public void onModelsGet(K key,List<E>  models) {
		for(ModelStatusListener<K,E> mModelStatusListener:listeners) {
			mModelStatusListener.onModelsGet(key, models);
		}
	}
}
