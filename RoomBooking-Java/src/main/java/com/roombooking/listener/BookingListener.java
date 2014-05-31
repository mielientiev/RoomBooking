package com.roombooking.listener;

import javax.servlet.AsyncContext;
import java.io.PrintWriter;

public class BookingListener {

    private final AsyncContext context;

    public BookingListener(AsyncContext context) {
        this.context = context;
    }

    public void update(int timetableId, String method) {
        try {
            String jsonPattern = "\"" + method + "\": " + timetableId;
            PrintWriter writer = context.getResponse().getWriter();
            writer.write(jsonPattern);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            context.complete();
        }
    }

}
