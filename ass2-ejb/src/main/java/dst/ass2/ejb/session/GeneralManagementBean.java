package dst.ass2.ejb.session;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import dst.ass1.jpa.dao.impl.EventMasterDAO;
import dst.ass1.jpa.model.*;
import dst.ass1.jpa.model.impl.Event;
import dst.ass2.ejb.dao.impl.AuditLogDao;
import dst.ass2.ejb.dto.AuditLogDTO;
import dst.ass2.ejb.dto.BillDTO;

import dst.ass2.ejb.management.interfaces.IPriceManagementBean;
import dst.ass2.ejb.model.IAuditLog;
import dst.ass2.ejb.session.interfaces.IGeneralManagementBean;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
@Remote
public class GeneralManagementBean implements IGeneralManagementBean {

    //injects the price mgm bean
    @EJB
    private IPriceManagementBean priceManagement;

    @PersistenceContext
    private EntityManager entityManager;

	@Override
	public void addPrice(Integer nrOfHistoricalEvents, BigDecimal price) {
        price = price.add(priceManagement.getPrice(nrOfHistoricalEvents));
        priceManagement.setPrice(nrOfHistoricalEvents, price);
	}

    //non blocking method call; control is returned to caller immediatley
    @Override
    @Asynchronous
    public Future<BillDTO> getBillForEventMaster(String eventMasterName)
			throws Exception {


        IEventMaster eventMaster;
        List<IEventMaster> eventMasters = new EventMasterDAO(entityManager).findByName(eventMasterName);
        if (eventMasters.size() == 0) {
            throw new Exception("could not finde eventmaster with name " + eventMasterName);
        } else {
            eventMaster = eventMasters.get(0);
        }


        //2.1
        Criteria criteria = entityManager.unwrap(org.hibernate.Session.class).createCriteria(Event.class);
        criteria.createCriteria("eventMaster").add(Restrictions.eq("eventMasterName" , eventMasterName));
        criteria.add(Restrictions.eq("isPaid", true));
        int eventCount = criteria.list().size();


        BillDTO result = new BillDTO();
        result.setEventMasterName(eventMasterName);
        result.setTotalPrice(BigDecimal.ZERO);
        result.setBills(new ArrayList<BillDTO.BillPerEventMaster>());

        criteria = entityManager.unwrap(org.hibernate.Session.class).createCriteria(Event.class);
        criteria.createCriteria("eventMaster").add(Restrictions.eq("eventMasterName" , eventMasterName));
        criteria.add(Restrictions.eq("isPaid", false));
        criteria.createCriteria("eventStreaming").add(Restrictions.eq("status", EventStatus.FINISHED));
        List<IEvent> eventList = criteria.list();


        for (IEvent events : eventList) {

            List<IUplink> uplinks = events.getEventStreaming().getUplinks();

            BillDTO.BillPerEventMaster billPerEventMaster = result.new BillPerEventMaster();
            billPerEventMaster.setEventId(events.getId());
            billPerEventMaster.setNumberOfUplinks(uplinks.size());
            billPerEventMaster.setEventCosts(BigDecimal.ZERO);
            billPerEventMaster.setStreamingCosts(BigDecimal.ZERO);
            BigDecimal discount = BigDecimal.ONE;

            Date esEnd = events.getEventStreaming().getEnd();
            Date esStart = events.getEventStreaming().getStart();
            long streamingDuration = TimeUnit.MILLISECONDS.toMinutes(esEnd.getTime() - esStart.getTime());
            BigDecimal streamingTime = new BigDecimal(streamingDuration);


            for (IUplink uplink : uplinks) {

                //discount - memebrships pretraziti da li ima bolji dcount od postojeceg
                for (IMembership membership : uplink.getStreamingServer().getMOSPlatform().getMemberships()) {

                    if (membership.getId().getEventMaster().equals(eventMaster)) {
                        discount = new BigDecimal(1-membership.getDiscount());
                        break;
                    }
                }

                //2.3
                BigDecimal costpermin = uplink.getStreamingServer().getMOSPlatform().getCostsPerStreamingMinute();
                BigDecimal streamingcosts = streamingTime.multiply(costpermin).multiply(discount);
                billPerEventMaster.setStreamingCosts(streamingcosts.setScale(2, BigDecimal.ROUND_HALF_UP));

                break;
            }

            //2.2
            BigDecimal setupCosts = priceManagement.getPrice(eventCount).multiply(discount);
            billPerEventMaster.setSetupCosts(setupCosts.setScale(2, BigDecimal.ROUND_HALF_UP));
            eventCount++;

            //2.4
            BigDecimal totalcosts = setupCosts.add(billPerEventMaster.getStreamingCosts());
            billPerEventMaster.setEventCosts(totalcosts.setScale(2, BigDecimal.ROUND_HALF_UP));
            //2.5
            events.setPaid(true);

            //2.6
            result.getBills().add(billPerEventMaster);
            result.setTotalPrice(result.getTotalPrice().add(billPerEventMaster.getEventCosts()));
            System.out.println(billPerEventMaster);

        }

        entityManager.flush();
		return new AsyncResult<>(result);
	}


	@Override
	public List<AuditLogDTO> getAuditLogs() {

        ArrayList<AuditLogDTO> auditLogDTOs = new ArrayList<>();

        for(IAuditLog auditlog: new AuditLogDao(entityManager).findAll()){
            auditLogDTOs.add(new AuditLogDTO(auditlog));
        }

        return auditLogDTOs;
    }

	@Override
	public void clearPriceCache() {
		priceManagement.clearCache();
	}
}
