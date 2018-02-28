package dst.ass2.ejb.session;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import dst.ass1.jpa.dao.DAOFactory;
import dst.ass1.jpa.model.*;
import dst.ass1.jpa.model.impl.*;
import dst.ass2.ejb.dto.AssignmentDTO;
import dst.ass2.ejb.interceptor.AuditInterceptor;
import dst.ass2.ejb.session.exception.AssignmentException;
import dst.ass2.ejb.session.interfaces.IEventManagementBean;


import javax.ejb.*;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

// statefull da bean moze zapamtiti sta je za neki odredjeni evenmg moze zapamtiti sta je radila
@Stateful
@Remote
// za ovu bean postoji interceoptor  pod imenom auditint. on dobiva sve inform. sta se upravo desava kod bean
@Interceptors(AuditInterceptor.class)

public class EventManagementBean implements IEventManagementBean {

	@PersistenceContext
    private EntityManager entityManager;

    private List<AssignmentDTO> assignmentDTOs = new ArrayList<>();
    private IEventMaster eventmaseter = null;


	@Override
	public void login(String eventMasterName, String password)
			throws AssignmentException {
        DAOFactory daoFactory = new DAOFactory(entityManager);
        List<IEventMaster> eventMasterlist = daoFactory.getEventMasterDAO().findByName(eventMasterName);


        byte[] pwd;

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            pwd = md.digest(password.getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new AssignmentException("could not found md5", e);

        }


        for (IEventMaster eventMaster : eventMasterlist) {
           if (Arrays.equals(pwd, eventMaster.getPassword())){
               this.eventmaseter = eventMaster;
               return;
            }
        }
        throw new AssignmentException("wrong password");
	}

    @Remove(retainIfException = true)
	@Override
	public void submitAssignments() throws AssignmentException {

        if (eventmaseter == null) {
            throw new AssignmentException("Not logged in!");
        }

        DAOFactory daoFactory = new DAOFactory(entityManager);
        ArrayList<IEvent> events = new ArrayList<>();

        for (AssignmentDTO assignmentDTO : assignmentDTOs) {

            //trebamo za spajanje, inace uplink ne zna da pripada eventu, gledamo jel stream jos uvijek prazan i dodamo ga
            //imamo assignment za svaki uplink id uzimammo uplink id
            for (Long uplinkid : assignmentDTO.getUplinkIds()) {
                IUplink uplink = daoFactory.getUplinkDAO().findById(uplinkid);

                for (IEventStreaming es : uplink.getEventStreamings()) {

                    //ne smije biti streamed ili scheduled
                    if (es.getStatus().equals(EventStatus.STREAMING) ||
                            es.getStatus().equals(EventStatus.SCHEDULED)) {
                        throw new AssignmentException("Error uplink no longer available");
                    }
                }
            }


            IMetadata metadata = new Metadata();
            metadata.setGame(assignmentDTO.getGame());
            metadata.setSettings(assignmentDTO.getSettings());

            IEventStreaming eventstreaming = new EventStreaming();
            eventstreaming.setStart(new Date());
            eventstreaming.setStatus(EventStatus.SCHEDULED);

            IEvent event = new Event();
            event.setEventMaster(this.eventmaseter);
            event.setAttendingViewers(assignmentDTO.getNumViewers());
            event.setEventStreaming(eventstreaming);
            event.setMetadata(metadata);
            event.setEventStreaming(eventstreaming);

            eventstreaming.setEvent(event);
            events.add(event);

            // dodaje uplink eventstr.,time imamo reservirane uplinks u tom dogovarajucem streamu
            for (Long uplinkid : assignmentDTO.getUplinkIds()) {
                IUplink uplink = daoFactory.getUplinkDAO().findById(uplinkid);
                eventstreaming.addUplinks(uplink);
            }
        }


        for (IEvent event : events) {
            entityManager.persist(event.getMetadata());
            entityManager.persist(event.getEventStreaming());
            entityManager.persist(event);
        }

        entityManager.flush();
        assignmentDTOs.clear();
    }



	@Override
	public void addEvent(Long platformId, Integer numViewers, String game,
			List<String> settings) throws AssignmentException {



        DAOFactory daoFactory = new DAOFactory(entityManager);
        IMOSPlatform platform = daoFactory.getPlatformDAO().findById(platformId);

        if (platform == null) {
            throw  new AssignmentException("Platform doesn't exist");
        }

       ArrayList<Long> useduplinksids = new ArrayList<>();

        //trazi slobodne uplinks i napravi listu slobodnih uplinks
        int numofup=0;

        for (IStreamingServer server : platform.getStreamingServers()) {

            for (IUplink uplink : server.getUplinks()) {
                boolean availableuplink = true;

                for (IEventStreaming es : uplink.getEventStreamings()) {

                    if (es.getStatus().equals(EventStatus.STREAMING) ||
                        es.getStatus().equals(EventStatus.SCHEDULED)) {
                        availableuplink = false;
                        break;
                    }
                }
                if (availableuplink) {
                    numofup += uplink.getViewerCapacity();
                    //pamti koje uplinks su vec koristeni
                    useduplinksids.add(uplink.getId());
                }

                //vise capacity nadjeno nego viewer(potrebno)
                if (numofup >= numViewers) break;
            }

            if (numofup >= numViewers) break;
        }

        if(numofup < numViewers) {
            throw new AssignmentException("could not find enough free uplinks for event");
        }

        AssignmentDTO assignmentDTO = new AssignmentDTO(platformId, numViewers, game, settings, useduplinksids);
        assignmentDTOs.add(assignmentDTO);
	}

	@Override
	public List<AssignmentDTO> getCache() {

		return assignmentDTOs;
	}

    //iterirati preko orginala i njega sa kopijom zamjenuti

	@Override
	public void removeEventsForPlatform(Long id) {

        ArrayList<AssignmentDTO> assignmentDTOs = new ArrayList<>();
        for (AssignmentDTO assignment : this.assignmentDTOs) {
            if (!assignment.getPlatformId().equals(id)) {

                assignmentDTOs.add(assignment);
            }
        }

        //staru listu sa novom zamjenuti
        this.assignmentDTOs = assignmentDTOs;
	}
}
