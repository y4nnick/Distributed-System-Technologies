package dst.ass1.jpa.model.impl;

import dst.ass1.jpa.model.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by amra.
 */

@Entity
public class StreamingServer implements IStreamingServer {

    @Id
    @GeneratedValue
    private long id;

    @Column(unique = true)
    private String name;

    private Date lastMaintenance;
    private Date nextMaintenance;


    @OneToMany(targetEntity=Uplink.class)
    private List<IUplink> uplinks =new ArrayList<>();


    @OneToMany(targetEntity = StreamingServer.class)
    @JoinTable(name = "composed_of",
            joinColumns = {@JoinColumn(name = "partOf_id")})
    private List <IStreamingServer> composedOf =new ArrayList<>();


    @ManyToMany(targetEntity = StreamingServer.class, mappedBy = "composedOf")
    private List<IStreamingServer> partOf = new ArrayList<>();



    @ManyToOne(targetEntity=Moderator.class)
    private IModerator moderator;

    @ManyToOne(targetEntity=MOSPlatform.class)
    private IMOSPlatform mosPlatform;




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
    public Date getLastMaintenance() {
        return lastMaintenance;
    }

    @Override
    public void setLastMaintenance(Date lastMaintenance) {
        this.lastMaintenance=lastMaintenance;

    }

    @Override
    public Date getNextMaintenance() {
        return nextMaintenance;
    }

    @Override
    public void setNextMaintenance(Date nextMaintenance) {
        this.nextMaintenance=nextMaintenance;

    }

    @Override
    public List<IStreamingServer> getComposedOf() {
        return composedOf;
    }

    @Override
    public void setComposedOf(List<IStreamingServer> composedOf) {
        this.composedOf=composedOf;

    }

    @Override
    public void addComposedOf(IStreamingServer streamingServer) {
        this.composedOf.add(streamingServer);

    }

    @Override
    public List<IStreamingServer> getPartOf() {
        return partOf;
    }

    @Override
    public void setPartOf(List<IStreamingServer> partOf) {

        this.partOf=partOf;
    }

    @Override
    public void addPartOf(IStreamingServer streamingServer) {
        this.partOf.add(streamingServer);

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
    public void addUplink(IUplink uplink) {
        this.uplinks.add(uplink);

    }

    @Override
    public IModerator getModerator() {
        return moderator;
    }

    @Override
    public void setModerator(IModerator moderator) {
        this.moderator=moderator;

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
