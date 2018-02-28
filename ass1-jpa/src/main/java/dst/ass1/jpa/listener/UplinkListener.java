package dst.ass1.jpa.listener;

import dst.ass1.jpa.model.impl.Uplink;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.util.Date;

/**
 * Created by amra.
 */
public class UplinkListener {

    @PrePersist
    public void setDateUpdates(Uplink uplink){

        Date date = new Date();
        uplink.setActivated(date);
        uplink.setLastUpdate(date);
    }


    @PreUpdate
    public void setLastDateUpdate(Uplink uplink){

        Date date = new Date();
        uplink.setLastUpdate(date);
    }

}

