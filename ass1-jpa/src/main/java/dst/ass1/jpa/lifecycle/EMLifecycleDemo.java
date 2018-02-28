package dst.ass1.jpa.lifecycle;

import java.security.NoSuchAlgorithmException;
import javax.persistence.EntityManager;

import dst.ass1.jpa.model.*;

public class EMLifecycleDemo {

	private EntityManager entityManager;
	private ModelFactory modelFactory;

	public EMLifecycleDemo(EntityManager entityManager, ModelFactory modelFactory) {
		this.entityManager = entityManager;
		this.modelFactory = modelFactory;
	}

	/**
	 * Method to illustrate the persistence lifecycle. EntityManager is opened
	 * and closed by the Test-Environment! You have to use at least the
	 * following methods (listed in alphabetical order) provided by the EntityManager:
	 * - clear(..)
	 * - flush(..)
	 * - merge(..)
	 * - persist(..)
	 * - remove(..)
	 * 
	 * Keep in mind that this is not necessarily the correct order!
	 * 
	 * @throws NoSuchAlgorithmException
	 */


    //Entitysies are in one of four states: new transient, managed, detached, or removed.
    // http://stackoverflow.com/questions/1069992/jpa-entitymanager-why-use-persist-over-merge

	public void demonstrateEntityMangerLifecycle()
			throws NoSuchAlgorithmException {

        // 1. Creating java objects using the model factory
        IEvent event = modelFactory.createEvent();
		IEventMaster eventMaster = modelFactory.createEventMaster();
		IEventStreaming eventStreaming = modelFactory.createEventStreaming();
		IMetadata metadata = modelFactory.createMetadata();
		eventMaster.setEventMasterName("sds");

        // 2. Linking java objects
		event.setEventMaster(eventMaster);
		event.setEventStreaming(eventStreaming);
		event.setMetadata(metadata);
		eventMaster.addEvent(event);

        // 3. Persisting eventMaster -> managed state
        entityManager.persist(eventMaster);

        // 4. Flushing persisted eventMaster -> now in DB
		/*
		flush may be useful to persist the data in between transaction and then
		commit the changes afterwards. So you can also rollback the previous changes if there
		occurs some problem afterwards (insert/update)
		 */
        entityManager.getTransaction().begin();
        entityManager.flush();
        entityManager.getTransaction().commit();

        // 5. Creating a new eventMaster object
		IEventMaster eventMaster1 = modelFactory.createEventMaster();
		eventMaster1.setEventMasterName("blubb");
		eventMaster1.setId(eventMaster.getId());

        // 6. Detaching eventmaster -> eventMaster now in detached state
        entityManager.detach(eventMaster);

        // 7. Merging old and new eventMaster -> eventMaster now in managed state again
		IEventMaster e2 = entityManager.merge(eventMaster1);
		System.out.println(e2.getEventMasterName());

        // 8. Removing eventMaster -> eventMaster in removed state
        entityManager.remove(e2);

        // 9. Flushing removal to DB == delete
        entityManager.getTransaction().begin();
        entityManager.flush();
        entityManager.getTransaction().commit();

        // 10. Clearing entityManager -> setting all managed objects to detatched state
        entityManager.clear();
		System.out.println(entityManager.contains(e2));
	}

}
