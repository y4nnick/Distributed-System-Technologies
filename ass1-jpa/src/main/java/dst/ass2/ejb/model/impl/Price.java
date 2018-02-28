package dst.ass2.ejb.model.impl;

import dst.ass2.ejb.model.IPrice;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;

/**
 * Created by amra.
 */
@Entity
public class Price implements IPrice {

    @Id
    @GeneratedValue
    private Long id;
    private Integer nrOfHistoricalEvents;
    private BigDecimal price;


    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id=id;

    }

    @Override
    public Integer getNrOfHistoricalEvents() {
        return nrOfHistoricalEvents;
    }

    @Override
    public void setNrOfHistoricalEvents(Integer nrOfHistoricalEvents) {
        this.nrOfHistoricalEvents=nrOfHistoricalEvents;

    }

    @Override
    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public void setPrice(BigDecimal price) {
        this.price=price;

    }
}
