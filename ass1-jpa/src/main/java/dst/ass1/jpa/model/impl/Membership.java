package dst.ass1.jpa.model.impl;

import dst.ass1.jpa.model.IMembership;
import dst.ass1.jpa.model.IMembershipKey;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by amra.
 */

@Entity
public class Membership implements IMembership {

    @EmbeddedId
    private MembershipKey membershipId;

    private Date registration;
    private double discount;


    @Override
    public IMembershipKey getId() {
        return membershipId;
    }


    @Override
    public void setId(IMembershipKey id) {
        this.membershipId=(MembershipKey) id;

    }

    @Override
    public Date getRegistration() {
        return registration;
    }

    @Override
    public void setRegistration(Date registration) {
        this.registration = registration;

    }

    @Override
    public Double getDiscount() {
        return discount;
    }

    @Override
    public void setDiscount(Double discount) {
        this.discount=discount;

    }
}
