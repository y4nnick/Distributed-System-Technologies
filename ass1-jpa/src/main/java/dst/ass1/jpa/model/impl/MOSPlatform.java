package dst.ass1.jpa.model.impl;

import dst.ass1.jpa.model.IMOSPlatform;
import dst.ass1.jpa.model.IMembership;
import dst.ass1.jpa.model.IStreamingServer;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by amra.
 */

@Entity
public class MOSPlatform implements IMOSPlatform {

    @Id
    @GeneratedValue
    private long id;

    @Column(unique = true)
    private String name;

    private String url;
    private BigDecimal costsPerStreamingMinute;


    @OneToMany(targetEntity = Membership.class, mappedBy = "membershipId.mosPlatform")
    private List<IMembership> memberships =new ArrayList<>();

    @OneToMany(targetEntity = StreamingServer.class)
    private List<IStreamingServer> streamingServers =new ArrayList<>();


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
    public String getUrl() {
        return url;
    }

    @Override
    public void setUrl(String url) {
        this.url=url;

    }

    @Override
    public BigDecimal getCostsPerStreamingMinute() {
        return costsPerStreamingMinute;
    }

    @Override
    public void setCostsPerStreamingMinute(BigDecimal costsPerStreamingMinute) {
        this.costsPerStreamingMinute=costsPerStreamingMinute;

    }

    @Override
    public List<IMembership> getMemberships() {
        return memberships;
    }

    @Override
    public void setMemberships(List<IMembership> memberships) {
        this.memberships=memberships;

    }


    @Override
    public void addMembership(IMembership membership) {
        this.memberships.add(membership);

    }

    @Override
    public List<IStreamingServer> getStreamingServers() {

        return streamingServers;
    }

    @Override
    public void setStreamingServers(List<IStreamingServer> streamingServers) {
        this.streamingServers=streamingServers;

    }

    @Override
    public void addStreamingServer(IStreamingServer streamingServer) {

        this.streamingServers.add(streamingServer);

    }
}
