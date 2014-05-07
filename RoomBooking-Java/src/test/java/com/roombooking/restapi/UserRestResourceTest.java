package com.roombooking.restapi;

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
import  org.springframework.web.context.request.RequestContextListener;

import javax.ws.rs.core.MediaType;

import static org.junit.Assert.assertEquals;

public class UserRestResourceTest extends JerseyTest {

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
    public void testUserWithAdminRole() throws Exception {
        WebResource webResource = resource().path("/user-service/user/1");
        ClientResponse response = webResource
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Basic bWVsaToxMjM0NQ")
                .get(ClientResponse.class);
        String jsonStr = response.getEntity(String.class);
        System.out.println(jsonStr);
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
                .header("Authorization", "Basic ZGRkOjEyMw==")
                .get(ClientResponse.class);
        String jsonStr = response.getEntity(String.class);
        assertEquals(200, response.getStatus());
    }

    @Test
    public void testRequestedUserNotExist() throws Exception {
        WebResource webResource = resource().path("/user-service/user/100");
        ClientResponse response = webResource
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Basic YWRzOjEyMw")
                .get(ClientResponse.class);
        assertEquals(404, response.getStatus());
    }


}