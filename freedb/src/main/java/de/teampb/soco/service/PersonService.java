package de.teampb.soco.service;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import de.teampb.soco.entity.Person;

@ApplicationScoped
public class PersonService {

    @Inject
    EntityManager entityManager;

    public Person getPersonToId(Long id) {
        Person person = entityManager.find(Person.class, id);
        return person;
    }

    @Transactional
    public Person createPerson(String name)  {
        if(name == null) {return null;}

        Person person = new Person();
        person.setName(name);
        person.setId(getMaxId() + 1);

        entityManager.persist(person);
        
        return person;

    }

    private Long getMaxId(){
        TypedQuery<Long> query = entityManager.createQuery("select max(p.id) from Person p", Long.class);
        
        return query.getSingleResult();
    }

    public List<Person> getAllPersons() {
        return entityManager.createQuery("select p from Person p",Person.class).getResultList();
    }

}
