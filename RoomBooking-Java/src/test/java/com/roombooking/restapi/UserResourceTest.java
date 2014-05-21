package com.roombooking.restapi;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.junit.Test;

import javax.ws.rs.core.MediaType;

import static org.junit.Assert.assertEquals;

public class UserResourceTest extends JerseyConfiguration {

    @Test
    public void testUserWithAdminRole() throws Exception {
        WebResource webResource = resource().path("/user-service/user/1");
        ClientResponse response = webResource
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Basic bWVsaToxMjM0NQ")
                .get(ClientResponse.class);
        assertEquals(200, response.getStatus());
    }

    @Test
    public void testUnauthorizedUser() throws Exception {
        WebResource webResource = resource().path("/user-service/user/1");
        ClientResponse response = webResource
                .accept(MediaType.APPLICATION_JSON)
                .get(ClientResponse.class);
        assertEquals(401, response.getStatus());
    }

    @Test
    public void testUserWithUserRole() throws Exception {
        WebResource webResource = resource().path("/user-service/user/1");
        ClientResponse response = webResource
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Basic dmFoYToxMjM0NTY")
                .get(ClientResponse.class);
        assertEquals(200, response.getStatus());
    }

    @Test
    public void testRequestedUserNotExist() throws Exception {
        WebResource webResource = resource().path("/user-service/user/100");
        ClientResponse response = webResource
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Basic dmFoYToxMjM0NTY")
                .get(ClientResponse.class);
        assertEquals(404, response.getStatus());
    }

}