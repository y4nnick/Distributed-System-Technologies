package dst.ass1.jpa.model.impl;

import dst.ass1.jpa.listener.UplinkListener;
import dst.ass1.jpa.model.IEventStreaming;
import dst.ass1.jpa.model.IStreamingServer;
import dst.ass1.jpa.model.IUplink;
import dst.ass1.jpa.validator.ViewerCapacity;

import javax.persistence.EntityListeners;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by amra.
 */

@EntityListeners(UplinkListener.class)
public class Uplink implements IUplink {


    private long id;

    @Size(min = 5, max = 25)
    private String name;

    @ViewerCapacity(min=40, max=80)
    private int viewerCapacity;

    @Pattern(regexp = "[A-Z]{3,3}-[A-Z]{3,3}@[0-9]{4,}")
    private String region;

    private IStreamingServer streamingServer;

    @Past
    private Date activated;
    @Past
    private Date lastUpdate;

    private List<IEventStreaming> eventStreamings = new ArrayList<>();



    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {

        this.id=id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name=name;

    }

    @Override
    public Integer getViewerCapacity() {
        return viewerCapacity;
    }

    @Override
    public void setViewerCapacity(Integer viewerCapacity) {
        this.viewerCapacity=viewerCapacity;

    }

    @Override
    public String getRegion() {
        return region;
    }

    @Override
    public void setRegion(String region) {

        this.region=region;
    }

    @Override
    public Date getActivated() {
        return activated;
    }

    @Override
    public void setActivated(Date activated) {
        this.activated=activated;

    }

    @Override
    public Date getLastUpdate() {
        return lastUpdate;
    }

    @Override
    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate=lastUpdate;

    }

    @Override
    public IStreamingServer getStreamingServer() {
        return streamingServer;
    }

    @Override
    public void setStreamingServer(IStreamingServer streamingServer) {
        this.streamingServer=streamingServer;

    }

    @Override
    public List<IEventStreaming> getEventStreamings() {
        return eventStreamings;
    }

    @Override
    public void setEventStreamings(List<IEventStreaming> streamings) {
        this.eventStreamings=streamings;

    }

    @Override
    public void addEventStreaming(IEventStreaming streaming) {
        this.eventStreamings.add(streaming);

    }
}
