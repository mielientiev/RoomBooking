package com.roombooking.restapi;

import com.roombooking.entity.Room;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.spi.spring.container.servlet.SpringServlet;
import com.sun.jersey.test.framework.AppDescriptor;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;
import com.sun.jersey.test.framework.spi.container.TestContainerFactory;
import com.sun.jersey.test.framework.spi.container.grizzly.web.GrizzlyWebTestContainerFactory;
import org.junit.Test;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.request.RequestContextListener;

import javax.ws.rs.core.MediaType;

import static org.junit.Assert.assertEquals;

public class RoomRestResourceTest extends JerseyTest {

    @Override
    protected AppDescriptor configure() {
        return new WebAppDescriptor.Builder("com.roombooking.restapi")
                .contextParam("contextConfigLocation", "classpath:applicationContext.xml")
                .contextListenerClass(ContextLoaderListener.class)
                .requestListenerClass(RequestContextListener.class)
                .initParam("com.sun.jersey.api.json.POJOMappingFeature", "true")
                .initParam("com.sun.jersey.spi.container.ContainerRequestFilters", "com.roombooking.filter.AuthenticationFilter")
                .initParam("com.sun.jersey.spi.container.ResourceFilters", "com.sun.jersey.api.container.filter.RolesAllowedResourceFilterFactory")
                .servletClass(SpringServlet.class)
                .build();
    }

    @Override
    public TestContainerFactory getTestContainerFactory() {
        return new GrizzlyWebTestContainerFactory();
    }

    @Test
    public void testAdminComputerClass() throws Exception {
        WebResource webResource = resource().path("/room-service/room/1");
        ClientResponse response = webResource
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Basic bWVsaToxMjM0NQ")
                .get(ClientResponse.class);
        Room room = response.getEntity(Room.class);
        System.out.println(room.getRoomType().getRights().get(0).getCanBookRoom());
        assertEquals(true, room.getRoomType().getRights().get(0).getCanBookRoom());
    }

    @Test
    public void testAdminClassroom() throws Exception {
        WebResource webResource = resource().path("/room-service/room/2");
        ClientResponse response = webResource
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Basic bWVsaToxMjM0NQ")
                .get(ClientResponse.class);
        Room room = response.getEntity(Room.class);
        System.out.println(room.getRoomType().getRights().get(0).getCanBookRoom());
        assertEquals(true, room.getRoomType().getRights().get(0).getCanBookRoom());
    }

    @Test
    public void testAdminAuditorium() throws Exception {
        WebResource webResource = resource().path("/room-service/room/3");
        ClientResponse response = webResource
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Basic bWVsaToxMjM0NQ")
                .get(ClientResponse.class);
        Room room = response.getEntity(Room.class);
        System.out.println(room.getRoomType().getRights().get(0).getCanBookRoom());
        assertEquals(true, room.getRoomType().getRights().get(0).getCanBookRoom());
    }

    @Test
    public void testProfessorComputerClass() throws Exception {
        WebResource webResource = resource().path("/room-service/room/1");
        ClientResponse response = webResource
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Basic dmFoYToxMjM0NTY")
                .get(ClientResponse.class);
        Room room = response.getEntity(Room.class);
        System.out.println(room.getRoomType().getRights().get(0).getCanBookRoom());
        assertEquals(true, room.getRoomType().getRights().get(0).getCanBookRoom());
    }

    @Test
    public void testProfessorClassroom() throws Exception {
        WebResource webResource = resource().path("/room-service/room/2");
        ClientResponse response = webResource
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Basic dmFoYToxMjM0NTY")
                .get(ClientResponse.class);
        Room room = response.getEntity(Room.class);
        System.out.println(room.getRoomType().getRights().get(0).getCanBookRoom());
        assertEquals(true, room.getRoomType().getRights().get(0).getCanBookRoom());
    }

    @Test
    public void testProfessorAuditorium() throws Exception {
        WebResource webResource = resource().path("/room-service/room/3");
        ClientResponse response = webResource
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Basic dmFoYToxMjM0NTY")
                .get(ClientResponse.class);
        Room room = response.getEntity(Room.class);
        System.out.println(room.getRoomType().getRights().get(0).getCanBookRoom());
        assertEquals(true, room.getRoomType().getRights().get(0).getCanBookRoom());
    }




    @Test
    public void testSeniorLecturerComputerClass() throws Exception {
        WebResource webResource = resource().path("/room-service/room/1");
        ClientResponse response = webResource
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Basic ZGltYXNpazoxMjM0")
                .get(ClientResponse.class);
        Room room = response.getEntity(Room.class);
        System.out.println(room.getRoomType().getRights().get(0).getCanBookRoom());
        assertEquals(true, room.getRoomType().getRights().get(0).getCanBookRoom());
    }

    @Test
    public void testSeniorLecturerClassroom() throws Exception {
        WebResource webResource = resource().path("/room-service/room/2");
        ClientResponse response = webResource
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Basic ZGltYXNpazoxMjM0")
                .get(ClientResponse.class);
        Room room = response.getEntity(Room.class);
        System.out.println(room.getRoomType().getRights().get(0).getCanBookRoom());
        assertEquals(true, room.getRoomType().getRights().get(0).getCanBookRoom());
    }

    @Test
    public void testSeniorLecturerAuditorium() throws Exception {
        WebResource webResource = resource().path("/room-service/room/3");
        ClientResponse response = webResource
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Basic ZGltYXNpazoxMjM0")
                .get(ClientResponse.class);
        Room room = response.getEntity(Room.class);
        System.out.println(room.getRoomType().getRights().get(0).getCanBookRoom());
        assertEquals(false, room.getRoomType().getRights().get(0).getCanBookRoom());
    }


    @Test
    public void testAssistantComputerClass() throws Exception {
        WebResource webResource = resource().path("/room-service/room/1");
        ClientResponse response = webResource
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Basic ZXZnZXNoYToxMjM0")
                .get(ClientResponse.class);
        Room room = response.getEntity(Room.class);
        System.out.println(room.getRoomType().getRights().get(0).getCanBookRoom());
        assertEquals(false, room.getRoomType().getRights().get(0).getCanBookRoom());
    }

    @Test
    public void testAssistantClassroom() throws Exception {
        WebResource webResource = resource().path("/room-service/room/2");
        ClientResponse response = webResource
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Basic ZXZnZXNoYToxMjM0")
                .get(ClientResponse.class);
        Room room = response.getEntity(Room.class);
        System.out.println(room.getRoomType().getRights().get(0).getCanBookRoom());
        assertEquals(true, room.getRoomType().getRights().get(0).getCanBookRoom());
    }

    @Test
    public void testAssistantAuditorium() throws Exception {
        WebResource webResource = resource().path("/room-service/room/3");
        ClientResponse response = webResource
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Basic ZXZnZXNoYToxMjM0")
                .get(ClientResponse.class);
        Room room = response.getEntity(Room.class);
        System.out.println(room.getRoomType().getRights().get(0).getCanBookRoom());
        assertEquals(false, room.getRoomType().getRights().get(0).getCanBookRoom());
    }
}
