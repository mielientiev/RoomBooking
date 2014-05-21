package com.roombooking.restapi;

import com.roombooking.entity.Booking;
import com.sun.jersey.api.client.ClientResponse;
import org.junit.Test;

import javax.ws.rs.core.MediaType;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Random;

public class BookingResourceTest extends JerseyConfiguration {

    @Test
    public void testAddBookingByDateMoreNow() throws Exception {
        long date = 100000000 + new Random().nextInt(1000000000) + new java.util.Date().getTime();
        System.out.println(date);
        Date newDate = Date.valueOf(new SimpleDateFormat("yyyy-MM-dd").format(date));
        System.out.println(newDate.toString());
        String b = "{\"id\":1,\"date\":\"" + newDate.toString() + "\",\"timetable\":{\"id\":5},\"room\":{\"id\":1}}";


        ClientResponse response = resource().path("/booking").header("Authorization", "Basic bWVsaToxMjM0NQ")
                .type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).put(ClientResponse.class,
                        b);
        if (response.getStatus() == 200) {
            Booking booking = response.getEntity(Booking.class);
            System.out.println(booking.getId());
            System.out.println(booking.getRoom().getId());
            System.out.println(booking.getTimetable().getId());
        }
//        assertEquals(true, room.getRoomType().getRights().iterator().next().getCanBookRoom());
    }
}
