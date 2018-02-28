package dst.ass1.jpa.model;

import java.util.List;

public interface IEventMaster extends IPerson {

	public String getEventMasterName();

	public void setEventMasterName(String eventMasterName);

	public byte[] getPassword();

	public void setPassword(byte[] password);

	public String getAccountNo();

	public void setAccountNo(String accountNo);

	public String getBankCode();

	public void setBankCode(String bankCode);

	public List<IEvent> getEvents();

	public void setEvents(List<IEvent> events);

	public void addEvent(IEvent event);

	public List<IMembership> getMemberships();

	public void setMemberships(List<IMembership> memberships);

	public void addMembership(IMembership membership);

}