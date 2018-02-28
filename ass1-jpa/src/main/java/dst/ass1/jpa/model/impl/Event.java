package dst.ass1.jpa.model.impl;

import dst.ass1.jpa.model.IEvent;
import dst.ass1.jpa.model.IEventMaster;
import dst.ass1.jpa.model.IEventStreaming;
import dst.ass1.jpa.model.IMetadata;
import dst.ass1.jpa.util.Constants;

import javax.persistence.*;

/**
 * Created by amra.
 */

@Entity
@NamedQueries({
        @NamedQuery(name = Constants.Q_ALLFINISHEDEVENTS,
                query = "SELECT e FROM Event e WHERE e.eventStreaming.status = 'FINISHED'" )
})


public class Event implements IEvent {

    @Id
    @GeneratedValue
    private long id;

    private boolean isPaid;
    private int streamingTime;
    private int attendingViewers;


    @OneToOne(targetEntity = Metadata.class, optional=false)
    private IMetadata metadata;

    @ManyToOne(targetEntity =EventMaster.class)
    private IEventMaster eventMaster;


    /*
    * Please make sure that the Event entity is the owning side of the relation
    * with EventStreaming and therefore also propagates (cascades) EntityManager
    * operations to the relating entity.
    */

    @OneToOne(targetEntity=EventStreaming.class, cascade=CascadeType.ALL, optional=false)
    private IEventStreaming eventStreaming;


    @Override
    public Long getId() {

        return id;
    }

    @Override
    public void setId(Long id) {
        this.id=id;

    }

    @Override
    public Integer getStreamingTime() {
        return streamingTime;
    }

    @Override
    public void setStreamingTime(Integer streamingTime) {
        this.streamingTime=streamingTime;

    }

    @Override
    public Integer getAttendingViewers() {
        return attendingViewers;
    }

    @Override
    public void setAttendingViewers(Integer attendingViewers) {
        this.attendingViewers=attendingViewers;

    }

    @Override
    public boolean isPaid() {
        return isPaid;
    }

    @Override
    public void setPaid(boolean isPaid) {
        this.isPaid=isPaid;

    }

    @Override
    public IMetadata getMetadata() {
        return metadata;
    }

    @Override
    public void setMetadata(IMetadata metadata) {
        this.metadata=metadata;

    }

    @Override
    public IEventMaster getEventMaster() {
        return eventMaster;
    }

    @Override
    public void setEventMaster(IEventMaster eventMaster) {
        this.eventMaster=eventMaster;

    }

    @Override
    public IEventStreaming getEventStreaming() {
        return eventStreaming;
    }

    @Override
    public void setEventStreaming(IEventStreaming eventStreaming) {
        this.eventStreaming=eventStreaming;

    }
}
