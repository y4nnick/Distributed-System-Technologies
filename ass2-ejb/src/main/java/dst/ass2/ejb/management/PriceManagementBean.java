package dst.ass2.ejb.management;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import dst.ass2.ejb.management.interfaces.IPriceManagementBean;
import dst.ass2.ejb.model.impl.Price;

import javax.annotation.PostConstruct;
import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

//singelton - initialized at application startup and is used with synchronized state
//singelton jer ce samo jednom biti izcitana iz db

@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
// moze biti samo lokalno koristena, ne od udaljenog clienta
@Local(IPriceManagementBean.class)
@Singleton
@Startup
public class PriceManagementBean implements IPriceManagementBean {

	// methoda ce biti pozvana nakon sto je bean napravljen
	@PersistenceContext
	private EntityManager entityManager;
	private final HashMap<Integer, BigDecimal> prices = new HashMap<>();

	@PostConstruct
	public void init() {
		List<Price> priceList = entityManager.unwrap(org.hibernate.Session.class).createCriteria(Price.class).list();

		for (Price price : priceList) {
			prices.put(price.getNrOfHistoricalEvents(), price.getPrice());
		}
	}

	//declares a concurency lock for singeltonSession bean with container managed concurrency (da samo jedan moze u methode iako ih ima vise)
	@Lock
	@Override
	public BigDecimal getPrice(Integer nrOfHistoricalEvents) {
		if (prices.containsKey(nrOfHistoricalEvents)) return prices.get(nrOfHistoricalEvents);
		return new BigDecimal(0.0);
	}

	@Override
	public void setPrice(Integer nrOfHistoricalEvents, BigDecimal price) {


        if (prices.containsKey(nrOfHistoricalEvents)) {
            return;
        }

        prices.put(nrOfHistoricalEvents, price);
        Price priceobject = new Price();
        priceobject.setPrice(price);
        priceobject.setNrOfHistoricalEvents(nrOfHistoricalEvents);


        entityManager.persist(priceobject);
        entityManager.flush();
	}
	
	
	@Override
	public void clearCache() {
		prices.clear();
	}
}
