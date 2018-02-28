package dst.ass1.jpa.model;

public interface IMembershipKey {

	public IEventMaster getEventMaster();

	public void setEventMaster(IEventMaster eventMaster);

	public IMOSPlatform getMOSPlatform();

	public void setMOSPlatform(IMOSPlatform platform);

}
