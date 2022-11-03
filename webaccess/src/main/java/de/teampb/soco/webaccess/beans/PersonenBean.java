package de.teampb.soco.webaccess.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.teampb.soco.webaccess.pojo.PersonPojo;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.primefaces.component.tabview.Tab;
import org.primefaces.event.TabChangeEvent;

@ApplicationScoped
@Named(value = "personenBean")
public class PersonenBean implements Serializable{

    String name;

    List<PersonPojo> personenListe = new ArrayList<>();

    @Inject
    PersonenWsClient personenClient;

    public PersonenBean() {
    }

    @SuppressWarnings("rawtypes")
    public void onTabChange(TabChangeEvent event) {
        Tab activeTab = event.getTab();
        if("personenlisteTab".equals(activeTab.getId())){
            reloadPersonen();
        }
        else {
            name = null;
        }
    }


    public void reloadPersonen(){
        personenListe = personenClient.callPersonenGet();
    }

    public void save(){
        if(name != null) {
            personenClient.callPersonPost(name);
        }
    }

    // GETTERS AND SETTERS

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PersonPojo> getPersonenListe(){
        return personenListe;
    }

    public void setPersonenListe(List<PersonPojo> personenListe) {
        this.personenListe = personenListe;
    }

}
