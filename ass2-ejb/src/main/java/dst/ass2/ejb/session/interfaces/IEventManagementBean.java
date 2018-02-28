package dst.ass2.ejb.session.interfaces;

import java.util.List;

import dst.ass2.ejb.dto.AssignmentDTO;
import dst.ass2.ejb.session.exception.AssignmentException;

public interface IEventManagementBean {

	/**
	 * Adds an event with the given settings to the temporary
	 * list if there are enough free viewer resources left.
	 * 
	 * @param platformId
	 * @param numViewers
	 * @param game
	 * @param settings
	 * @throws AssignmentException
	 *             if the given platform does not exist or if 
	 *             there are not enough free viewer resources left.
	 */
	public void addEvent(Long platformId, Integer numViewers, String game,
						 List<String> settings) throws AssignmentException;

	/**
	 * @return the list of temporarily assigned events.
	 */
	public List<AssignmentDTO> getCache();

	/**
	 * Removes temporary assigned events.
	 */
	public void removeEventsForPlatform(Long id);

	/**
	 * Allows the eventMaster to login.
	 * 
	 * @param eventMasterName
	 * @param password
	 * @throws AssignmentException
	 *             if the eventMaster does not exist or the given eventMasterName/password
	 *             combination does not match.
	 */
	public void login(String eventMasterName, String password)
			throws AssignmentException;

	/**
	 * Final submission of the temporary assigned events.
	 * 
	 * @throws AssignmentException
	 *             if the eventMaster is not logged in or one of the events can't be
	 *             assigned anymore.
	 */
	public void submitAssignments() throws AssignmentException;

}
