package com.roombooking.listener;

import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import java.io.IOException;
import java.sql.Date;

public class BookingAsyncListener implements AsyncListener {

    private int roomId;
    private Date date;
    private BookingListener listener;

    public BookingAsyncListener(int roomId, Date date, BookingListener listener) {
        this.roomId = roomId;
        this.date = date;
        this.listener = listener;
    }

    @Override
    public void onComplete(AsyncEvent event) throws IOException {
        BookingManager.unRegister(roomId, date, listener);
    }

    @Override
    public void onTimeout(AsyncEvent event) throws IOException {
        BookingManager.unRegister(roomId, date, listener);
    }

    @Override
    public void onError(AsyncEvent event) throws IOException {
        BookingManager.unRegister(roomId, date, listener);
    }

    @Override
    public void onStartAsync(AsyncEvent event) throws IOException {
    }

}
