package de.teampb.soco.service;

import java.util.Arrays;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import de.teampb.soco.entity.Person;

@ApplicationScoped
public class PersonService {

    @Inject
    EntityManager entityManager;

    @ConfigProperty(name = "name.blacklist")
    String blacklist;

    private List<String> blacklistAsList;

    public Person getPersonToId(Long id) {
        Person person = entityManager.find(Person.class, id);
        return person;
    }

    @Transactional
    public Person createPerson(String name)  {
        if(name == null || isBlacklisted(name)) {return null;}

        Person person = new Person();
        person.setName(name);
        person.setId(getMaxId() + 1);

        entityManager.persist(person);
        
        return person;

    }

    public List<Person> getAllPersons() {
        return entityManager.createQuery("select p from Person p",Person.class).getResultList();
    }

    private Long getMaxId(){
        TypedQuery<Long> query = entityManager.createQuery("select max(p.id) from Person p", Long.class);
        
        Long maxId = query.getSingleResult();
	
	return maxId == null ? 0L : maxId;
    }

    private boolean isBlacklisted(String name) {
        if(blacklistAsList == null) {
            blacklistAsList = Arrays.asList(blacklist.toUpperCase().split(",", 0));
        }
        return blacklistAsList.contains(name.toUpperCase());
    }

}
