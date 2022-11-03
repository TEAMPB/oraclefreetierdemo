package de.teampb.soco.webaccess.beans;

import java.io.StringReader;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.json.JsonObject;
import jakarta.ws.rs.client.Client;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import de.teampb.soco.webaccess.pojo.PersonPojo;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@RequestScoped
public class PersonenWsClient {

    @Inject
    @ConfigProperty(name = "PERSONEN_SERVICE_ROOT_URL", defaultValue = "http://localhost:8080")
    String rootUrl;

    public List<PersonPojo> callPersonenGet() {
        Client client = createConsumerClient();
        try {
            Response response = createPersonenWebTarget(client).get();
            String jsonResponse = response.readEntity(String.class);

            JsonArray array = Json.createReader(new StringReader(jsonResponse)).readArray();

            return array.stream()
                    .map(e -> new PersonPojo((long) e.asJsonObject().getInt("id"), e.asJsonObject().getString("name")))
                    .sorted(Comparator.comparing(PersonPojo::getId))
                    .collect(Collectors.toList());
        } finally {
            client.close();
        }
    }

    public PersonPojo callPersonPost(String name) {
        final JsonObject param = Json.createObjectBuilder().add("name", name).build();

        Client client = createConsumerClient();
        try {
            Invocation.Builder createPersonenWebTarget = createPersonenWebTarget(client);
            Entity<JsonObject> postBody = Entity.json(param);
            try (Response postResponse = createPersonenWebTarget.post(postBody)) {
                final JsonObject result = postResponse.readEntity(JsonObject.class);
                if (result == null) {
                    throw new IllegalStateException("keine Entity zurückgekommen");
                }
                final long newId = result.getInt("id");
                final String newName = result.getString("name");
                FacesContext.getCurrentInstance()
                        .addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                                "Erfolg",
                                "Person '" + newName + "' wurde mit der ID '" + newId + "' angelegt!"));
                return new PersonPojo(newId, newName);
            } catch (Exception e) {
                FacesContext.getCurrentInstance()
                        .addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                "Fehler!",
                                "Daten konnten nicht gespeichert werden!"));
                e.printStackTrace();
                return null;
            }
        } finally {
            client.close();
        }
    }

    private Client createConsumerClient() {
        return ClientBuilder.newClient();
    }

    private Invocation.Builder createPersonenWebTarget(Client client) {
        return ClientBuilder.newClient().target(rootUrl).path("/person").request(MediaType.APPLICATION_JSON);
    }

}
