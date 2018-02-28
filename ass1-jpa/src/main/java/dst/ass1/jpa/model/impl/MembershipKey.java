package dst.ass1.jpa.model.impl;

import dst.ass1.jpa.model.IEventMaster;
import dst.ass1.jpa.model.IMOSPlatform;
import dst.ass1.jpa.model.IMembershipKey;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;

/**
 * Created by amra.
 */


@Embeddable
public class MembershipKey implements IMembershipKey, Serializable{

    private static final long serialVersionUID = 17854L;

    @ManyToOne(targetEntity = EventMaster.class)
    private IEventMaster eventMaster;

    @ManyToOne(targetEntity = MOSPlatform.class)
    private IMOSPlatform mosPlatform;


    @Override
    public IEventMaster getEventMaster() {
        return eventMaster;
    }

    @Override
    public void setEventMaster(IEventMaster eventMaster) {
        this.eventMaster=eventMaster;

    }

    @Override
    public IMOSPlatform getMOSPlatform() {
        return mosPlatform;
    }

    @Override
    public void setMOSPlatform(IMOSPlatform platform) {
        this.mosPlatform=platform;

    }
}
