package dst.ass1.jpa.dao;

import javax.persistence.EntityManager;

import dst.ass1.jpa.dao.impl.*;
import dst.ass2.ejb.dao.IAuditLogDAO;
import dst.ass2.ejb.dao.IPriceDAO;
import dst.ass2.ejb.dao.impl.AuditLogDao;
import dst.ass2.ejb.dao.impl.PriceDao;

public class DAOFactory {

	/*
	 * HINT: When using the org.hibernate.Session in your DAOs you can extract
	 * it from the EntityManager reference with e.g.,
	 * em.unwrap(org.hibernate.Session.class). Do not store this
	 * org.hibernate.Session in your DAOs, but unwrap it every time you actually
	 * need it.
	 */


	private EntityManager em;

	public DAOFactory(EntityManager em) {
		this.em = em;
	}

	public IMOSPlatformDAO getPlatformDAO() {
		return new MOSPlatformDAO(em);
	}

	public IModeratorDAO getModeratorDAO() {
		return new ModeratorDAO(em);
	}

	public IStreamingServerDAO getStreamingServerDAO() {
		return new StreamingServerDAO(em);
	}

	public IUplinkDAO getUplinkDAO() {
		return new UplinkDao(em);
	}

	public IMetadataDAO getMetadataDAO() {
		return new MetadataDAO(em);
	}

	public IEventStreamingDAO getEventStreamingDAO() {
		return new EventStreamingDAO(em);
	}

	public IEventDAO getEventDAO() {
		return new EventDAO(em);
	}

	public IMembershipDAO getMembershipDAO() {
		return new MembershipDAO(em);
	}

	public IEventMasterDAO getEventMasterDAO() {
		return new EventMasterDAO(em);
	}

	/*
	 * Please note that the following methods are not needed for assignment 1,
	 * but will later be used in assignment 2. Hence, you do not have to
	 * implement it for the first submission.
	 */

	public IAuditLogDAO getAuditLogDAO() {
		return new AuditLogDao(em);
	}

	public IPriceDAO getPriceDAO() {
		return new PriceDao(em);
	}

}
