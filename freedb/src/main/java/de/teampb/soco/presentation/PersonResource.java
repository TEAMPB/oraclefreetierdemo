package de.teampb.soco.presentation;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import de.teampb.soco.entity.Person;
import de.teampb.soco.service.PersonService;

@Path("/person")
public class PersonResource {

    @Inject
    PersonService personService;

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Person getPersonToId(@PathParam(value = "id") Long id) {
        return personService.getPersonToId(id);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Person> getPersons() {
        return personService.getAllPersons();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response createPerson(Person body){
        
        if(body == null) {
            return createErrorResponse();
        }
        String name = body.getName();
        Person person = personService.createPerson(name);

        if(person == null){
            return createErrorResponse();
        }

        return Response.ok().entity(person).build();
    }


    private Response createErrorResponse() {
        return Response.status(Status.BAD_REQUEST).build();
    }

}