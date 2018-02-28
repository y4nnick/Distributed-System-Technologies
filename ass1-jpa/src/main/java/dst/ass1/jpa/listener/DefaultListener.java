package dst.ass1.jpa.listener;

import javax.persistence.*;
import java.util.Date;
import java.util.HashMap;


public class DefaultListener {
	/*
    Now your task is to implement a simple default listener. This listener should record the number of
    entities loaded, updated, and removed. This listener also records how often a persist operation was
    successfully called and how much time all store-operations took in total. Implement your listener
    by completing the class dst.ass1.jpa.listener.DefaultListener. Make sure that your listener
    implementation is thread-safe and concurrent!
     */
	private static int operationLoadCount;
	private static int operationRemoeCount;
	private static int operationPersistCount;
	private static int operationUpdateCount;

    //thread lock object
	private static Object syn = new Object();

	private static long persistTime;

	private static final HashMap<Object, Date> persistDate = new HashMap<>();
	private static final HashMap<Object, Date> updateDate = new HashMap<>();
	private static final HashMap<Object, Date> removeDate = new HashMap<>();


	public static int getLoadOperations() {
		synchronized(syn){
			return operationLoadCount;
		}
	}

	public static int getUpdateOperations() {
		synchronized(syn){
			return operationUpdateCount;
		}
	}

	public static int getRemoveOperations() {
		synchronized(syn){
			return operationRemoeCount;
		}
	}

	public static int getPersistOperations() {
		synchronized(syn){
			return operationPersistCount;
		}
	}

	public static long getOverallTimeToPersist() {
		synchronized(syn){
			return persistTime;
		}
	}

	public static double getAverageTimeToPersist() {
		synchronized(syn){
			return (double) persistTime / (double) operationPersistCount;
		}
	}


	@PrePersist
	public void prePersist(Object object) {
		synchronized (syn) {
			persistDate.put(object, new Date())	;

		}
	}

	@PostPersist
	public void postPersist(Object object) {
		synchronized (syn) {

			if (persistDate.containsKey(object)) {

				persistTime += new Date().getTime() - persistDate.get(object).getTime();
				persistDate.remove(object);
				operationPersistCount ++;
			}
		}
	}


	@PreUpdate
	public void preUpdate(Object object) {
		synchronized (syn) {
			updateDate.put(object, new Date())	;

		}
	}

	@PostUpdate
	public void postUpdate(Object object) {
		synchronized (syn) {
			if (updateDate.containsKey(object)) {

				persistTime += new Date().getTime() - updateDate.get(object).getTime();
				updateDate.remove(object);
				operationUpdateCount ++;
			}
		}
	}


	@PreRemove
	public void preRemove(Object object) {
		synchronized (syn) {
			removeDate.put(object, new Date())	;

		}
	}

	@PostRemove
	public void postRemove(Object object) {
		synchronized (syn) {
			if (removeDate.containsKey(object)) {

				persistTime += new Date().getTime() - removeDate.get(object).getTime();
				removeDate.remove(object);
				operationRemoeCount ++;
			}
		}
	}


	@PostLoad
	public void postLoad(Object object) {
		synchronized (syn) {
			operationLoadCount++;
		}
	}



	/**
	 * Clears the internal data structures that are used for storing the
	 * operations.
	 */

	public static void clear() {
		synchronized(syn){

			operationLoadCount = 0;
			operationRemoeCount = 0;
			operationPersistCount = 0;
			operationUpdateCount = 0;
		    persistTime = 0;
			persistDate.clear();
			removeDate.clear();
			updateDate.clear();

		}
	}

}
