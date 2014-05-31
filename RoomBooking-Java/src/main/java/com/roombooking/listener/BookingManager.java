package com.roombooking.listener;

import java.sql.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BookingManager {

    private static final Map<String, HashSet<BookingListener>> listeners = new ConcurrentHashMap<>();

    public static void register(int roomId, Date date, BookingListener listener) {
        String key = "roomId: " + roomId + " Date" + date.toString();
        HashSet<BookingListener> bookingListeners = listeners.get(key);
        if (bookingListeners == null) {
            bookingListeners = new HashSet<>();
        }
        bookingListeners.add(listener);
        listeners.put(key, bookingListeners);
    }

    public static void unRegister(int roomId, Date date, BookingListener listener) {
        String key = "roomId: " + roomId + " Date" + date.toString();
        HashSet<BookingListener> bookingListeners = listeners.get(key);
        bookingListeners.remove(listener);
        listeners.put(key, bookingListeners);
    }

    public static void notify(final int roomId, final Date date, final int timetableId, final String method) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HashSet<BookingListener> bookingListeners;
                String key = "roomId: " + roomId + " Date" + date.toString();
                if (listeners.get(key) == null) {
                    return;
                }
                bookingListeners = new HashSet<>(listeners.get(key));
                for (BookingListener bookingList : bookingListeners) {
                    bookingList.update(timetableId, method);
                }
            }
        }).start();
    }

}
