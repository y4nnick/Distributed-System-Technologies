package dst.ass1.jpa.model.impl;

/**
 * Created by amra.
 */

import dst.ass1.jpa.model.IEvent;
import dst.ass1.jpa.model.IEventMaster;
import dst.ass1.jpa.model.IMembership;
import dst.ass1.jpa.util.Constants;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;



@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {Constants.M_ACCOUNT, Constants.M_BANKCODE}))

@NamedQueries({
        @NamedQuery(name = Constants.Q_MOSTACTIVEEVENTMASTER,
                query = "SELECT em FROM EventMaster em WHERE em.events.size = " +
                        "(SELECT MAX(eem.events.size) FROM EventMaster eem) AND em.events.size > 0)"),
       })


public class EventMaster extends Person implements IEventMaster {

    @Column(unique = true, nullable= false)
    private String eventMasterName;

    @Column(columnDefinition="BINARY(16)")
    private byte[] password;

    private String accountNo;
    private String bankCode;


    @OneToMany(targetEntity = Membership.class, mappedBy = "membershipId.eventMaster")
    private List<IMembership> memberships =new ArrayList<>();

    @OneToMany(targetEntity=Event.class, mappedBy="eventMaster")
    private List<IEvent> events = new ArrayList<>();


    @Override
    public String getEventMasterName() {
        return eventMasterName;
    }

    @Override
    public void setEventMasterName(String eventMasterName) {
        this.eventMasterName=eventMasterName;

    }

    @Override
    public byte[] getPassword() {
        return password;
    }

    @Override
    public void setPassword(byte[] password) {
        this.password=password;

    }

    @Override
    public String getAccountNo() {

        return accountNo;
    }

    @Override
    public void setAccountNo(String accountNo) {
        this.accountNo=accountNo;

    }

    @Override
    public String getBankCode() {

        return bankCode;
    }

    @Override
    public void setBankCode(String bankCode) {
        this.bankCode=bankCode;

    }

    @Override
    public List<IEvent> getEvents() {

        return events;
    }

    @Override
    public void setEvents(List<IEvent> events) {
        this.events=events;

    }

    @Override
    public void addEvent(IEvent event) {
        this.events.add(event);

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

}
