package com.roombooking.restapi;

import com.sun.jersey.api.client.ClientResponse;
import org.junit.Test;

import javax.ws.rs.core.MediaType;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Random;

import static org.junit.Assert.assertEquals;

public class BookingResourceTest extends JerseyConfiguration {

    @Test
    public void testAddBookingByDateMoreNow() throws Exception {
        long date = 100000000 + new Random().nextInt(1000000000) + new java.util.Date().getTime();
        Date newDate = Date.valueOf(new SimpleDateFormat("yyyy-MM-dd").format(date));
        String b = "{\"id\":1,\"date\":\"" + newDate.toString() + "\",\"timetable\":{\"id\":5},\"room\":{\"id\":1}}";
        ClientResponse response = resource().path("/booking").header("Authorization", "Basic bWVsaToxMjM0NQ")
                .type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).put(ClientResponse.class,b);
        assertEquals(200,response.getStatus());
    }

    @Test
    public void testAddBookingByWrongDate() throws Exception {
        long date = 100000000 + new Random().nextInt(1000000000) + new java.util.Date().getTime();
        Date newDate = Date.valueOf(new SimpleDateFormat("yyyy-MM-dd").format(date));
        String b = "{\"id\":1,\"date\":\"" + newDate.toString()+"5" + "\",\"timetable\":{\"id\":5}," +
                "\"room\":{\"id\":1}}";
        ClientResponse response = resource().path("/booking").header("Authorization", "Basic bWVsaToxMjM0NQ")
                .type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).put(ClientResponse.class, b);
        assertEquals(400,response.getStatus());
    }
}
