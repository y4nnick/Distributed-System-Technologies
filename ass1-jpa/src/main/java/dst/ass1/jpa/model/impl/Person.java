package dst.ass1.jpa.model.impl;

import dst.ass1.jpa.model.IAddress;
import dst.ass1.jpa.model.IPerson;

import javax.persistence.*;

/**
 * Created by amra.
 */



@MappedSuperclass

/*
TABLE_PER_CLASS:
- efficient when operating on instances of a known class and known number of classes
- never requires joining to superclass or subclass tables
- no null fields/columns
*/

@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Person implements IPerson {

    @Id
    @GeneratedValue
    private long id;

    private String firstname;
    private String lastname;

    @Embedded
    private IAddress address;


    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id=id;

    }

    @Override
    public String getLastName() {
        return lastname;
    }

    @Override
    public void setLastName(String lastName) {
        this.lastname=lastName;

    }

    @Override
    public String getFirstName() {
        return firstname;
    }

    @Override
    public void setFirstName(String firstName) {
        this.firstname = firstName;

    }

    @Override
    public IAddress getAddress() {
        return address;
    }

    @Override
    public void setAddress(IAddress address) {
        this.address=address;

    }

}
