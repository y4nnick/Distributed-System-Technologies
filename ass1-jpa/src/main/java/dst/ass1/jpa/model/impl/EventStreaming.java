package dst.ass1.jpa.model.impl;

import dst.ass1.jpa.model.EventStatus;
import dst.ass1.jpa.model.IEvent;
import dst.ass1.jpa.model.IEventStreaming;
import dst.ass1.jpa.model.IUplink;
import dst.ass1.jpa.util.Constants;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by amra.
 */

@Entity
public class EventStreaming implements IEventStreaming {

    @Id
    @GeneratedValue
    private long id;

    private Date start;
    private Date end;

    @Enumerated(EnumType.STRING)
    private EventStatus status;



    @OneToOne(targetEntity = Event.class, mappedBy = "eventStreaming")
    private IEvent event;


    @ManyToMany(targetEntity = Uplink.class)
    @JoinTable(name = Constants.J_STREAMING_UPLINK, joinColumns={
            @JoinColumn(name="eventstreamings_id")},
            inverseJoinColumns= {@JoinColumn(name = "uplinks_id")
            })

    private List<IUplink> uplinks =new ArrayList<>();


    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id=id;

    }

    @Override
    public Date getStart() {
        return start;
    }

    @Override
    public void setStart(Date start) {
        this.start=start;

    }

    @Override
    public Date getEnd() {
        return end;
    }

    @Override
    public void setEnd(Date end) {
        this.end=end;

    }

    @Override
    public EventStatus getStatus() {
        return status;
    }

    @Override
    public void setStatus(EventStatus eventStatus) {
        this.status=eventStatus;

    }

    @Override
    public List<IUplink> getUplinks() {
        return uplinks;
    }

    @Override
    public void setUplinks(List<IUplink> uplinks) {
        this.uplinks=uplinks;

    }

    @Override
    public void addUplinks(IUplink uplink) {
        this.uplinks.add(uplink);

    }

    @Override
    public IEvent getEvent() {
        return event;
    }

    @Override
    public void setEvent(IEvent event) {

        this.event=event;
    }
}
