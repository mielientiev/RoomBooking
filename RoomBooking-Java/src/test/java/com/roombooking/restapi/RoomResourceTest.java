package com.roombooking.restapi;

import com.roombooking.entity.Room;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.junit.Test;

import javax.ws.rs.core.MediaType;

import static org.junit.Assert.assertEquals;

public class RoomResourceTest extends JerseyConfiguration {

    @Test
    public void testAdminComputerClass() throws Exception {
        WebResource webResource = resource().path("/room-service/room/1");
        ClientResponse response = webResource
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Basic bWVsaToxMjM0NQ")
                .get(ClientResponse.class);
        Room room = response.getEntity(Room.class);
        assertEquals(true, room.getRoomType().getRights().iterator().next().getCanBookRoom());
    }

    @Test
    public void testAdminClassroom() throws Exception {
        WebResource webResource = resource().path("/room-service/room/2");
        ClientResponse response = webResource
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Basic bWVsaToxMjM0NQ")
                .get(ClientResponse.class);
        Room room = response.getEntity(Room.class);
        assertEquals(true, room.getRoomType().getRights().iterator().next().getCanBookRoom());
    }

    @Test
    public void testAdminAuditorium() throws Exception {
        WebResource webResource = resource().path("/room-service/room/3");
        ClientResponse response = webResource
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Basic bWVsaToxMjM0NQ")
                .get(ClientResponse.class);
        Room room = response.getEntity(Room.class);
        assertEquals(true, room.getRoomType().getRights().iterator().next().getCanBookRoom());
    }

    @Test
    public void testProfessorComputerClass() throws Exception {
        WebResource webResource = resource().path("/room-service/room/1");
        ClientResponse response = webResource
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Basic dmFoYToxMjM0NTY")
                .get(ClientResponse.class);
        Room room = response.getEntity(Room.class);
        assertEquals(true, room.getRoomType().getRights().iterator().next().getCanBookRoom());
    }

    @Test
    public void testProfessorClassroom() throws Exception {
        WebResource webResource = resource().path("/room-service/room/2");
        ClientResponse response = webResource
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Basic dmFoYToxMjM0NTY")
                .get(ClientResponse.class);
        Room room = response.getEntity(Room.class);
        assertEquals(true, room.getRoomType().getRights().iterator().next().getCanBookRoom());
    }

    @Test
    public void testProfessorAuditorium() throws Exception {
        WebResource webResource = resource().path("/room-service/room/3");
        ClientResponse response = webResource
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Basic dmFoYToxMjM0NTY")
                .get(ClientResponse.class);
        Room room = response.getEntity(Room.class);
        assertEquals(true, room.getRoomType().getRights().iterator().next().getCanBookRoom());
    }

    @Test
    public void testSeniorLecturerComputerClass() throws Exception {
        WebResource webResource = resource().path("/room-service/room/1");
        ClientResponse response = webResource
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Basic ZGltYXNpazoxMjM0")
                .get(ClientResponse.class);
        Room room = response.getEntity(Room.class);
        assertEquals(true, room.getRoomType().getRights().iterator().next().getCanBookRoom());
    }

    @Test
    public void testSeniorLecturerClassroom() throws Exception {
        WebResource webResource = resource().path("/room-service/room/2");
        ClientResponse response = webResource
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Basic ZGltYXNpazoxMjM0")
                .get(ClientResponse.class);
        Room room = response.getEntity(Room.class);
        assertEquals(true, room.getRoomType().getRights().iterator().next().getCanBookRoom());
    }

    @Test
    public void testSeniorLecturerAuditorium() throws Exception {
        WebResource webResource = resource().path("/room-service/room/3");
        ClientResponse response = webResource
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Basic ZGltYXNpazoxMjM0")
                .get(ClientResponse.class);
        Room room = response.getEntity(Room.class);
        assertEquals(false, room.getRoomType().getRights().iterator().next().getCanBookRoom());
    }

    @Test
    public void testAssistantComputerClass() throws Exception {
        WebResource webResource = resource().path("/room-service/room/1");
        ClientResponse response = webResource
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Basic ZXZnZXNoYToxMjM0")
                .get(ClientResponse.class);
        Room room = response.getEntity(Room.class);
        assertEquals(false, room.getRoomType().getRights().iterator().next().getCanBookRoom());
    }

    @Test
    public void testAssistantClassroom() throws Exception {
        WebResource webResource = resource().path("/room-service/room/2");
        ClientResponse response = webResource
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Basic ZXZnZXNoYToxMjM0")
                .get(ClientResponse.class);
        Room room = response.getEntity(Room.class);
        assertEquals(true, room.getRoomType().getRights().iterator().next().getCanBookRoom());
    }

    @Test
    public void testAssistantAuditorium() throws Exception {
        WebResource webResource = resource().path("/room-service/room/3");
        ClientResponse response = webResource
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Basic ZXZnZXNoYToxMjM0")
                .get(ClientResponse.class);
        Room room = response.getEntity(Room.class);
        assertEquals(false, room.getRoomType().getRights().iterator().next().getCanBookRoom());
    }
}
