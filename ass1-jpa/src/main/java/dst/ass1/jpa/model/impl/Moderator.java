package dst.ass1.jpa.model.impl;

/**
 * Created by amra.
 */

import dst.ass1.jpa.model.IModerator;
import dst.ass1.jpa.model.IStreamingServer;
import dst.ass1.jpa.util.Constants;

import javax.persistence.*;
import javax.validation.Constraint;
import java.util.ArrayList;
import java.util.List;


@Entity
//problems: db changes possible between query 1 and 2
@NamedQueries({
        @NamedQuery(name = Constants.Q_STREAMINGSERVERSOFMODERATOR, query = "Select m FROM Moderator m WHERE m.firstname LIKE 'Alex%'"),
        @NamedQuery(name = "MINStreamingServer", query = "Select MIN(nextMaintenance) FROM StreamingServer s WHERE  s.moderator.id=:mid")
        //join fetch
})

@Table(name=Constants.T_MODERATOR)

public class Moderator extends Person implements IModerator {


    @OneToMany(targetEntity = StreamingServer.class)
    private List<IStreamingServer> streamingServers = new ArrayList<>();

    @Override
    public List<IStreamingServer> getAdvisedStreamingServers() {

        return streamingServers;
    }

    @Override
    public void setAdvisedStreamingServers(List<IStreamingServer> streamingServers) {
        this.streamingServers=streamingServers;

    }

    @Override
    public void addAdvisedStreamingServers(IStreamingServer streamingServer) {

        this.streamingServers.add(streamingServer);
    }


}
