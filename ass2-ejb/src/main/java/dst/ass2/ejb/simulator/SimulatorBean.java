package dst.ass2.ejb.simulator;

import dst.ass1.jpa.dao.DAOFactory;
import dst.ass1.jpa.model.EventStatus;
import dst.ass1.jpa.model.IEvent;
import dst.ass1.jpa.model.IEventStreaming;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

//only one simulator neededppp
@Singleton
public class SimulatorBean {

    @PersistenceContext
    private EntityManager entityManager;


	@Schedule(second = "*/5", minute = "*", hour = "*")
	public void simulate() {

        DAOFactory facotory =new DAOFactory(entityManager);

        List<IEvent> events = facotory.getEventDAO().findAll();

        for (IEvent event : events) {
            Date now = new Date();
            IEventStreaming eventStreaming = event.getEventStreaming();

            Date start = eventStreaming.getStart();
            if (start != null && start.getTime() < now.getTime() && eventStreaming.getEnd() == null) {
                eventStreaming.setEnd(now);
                eventStreaming.setStatus(EventStatus.FINISHED);
                //in seconds
                event.setStreamingTime((int) (now.getTime() - start.getTime()) / 1000);

            }

        }
        entityManager.flush();
	}

}
