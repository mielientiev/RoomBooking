package com.roombooking.restapi;

import com.roombooking.listener.BookingAsyncListener;
import com.roombooking.listener.BookingEvent;
import com.roombooking.listener.BookingListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.AsyncContext;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class BookingLongPolingServlet extends HttpServlet {

    @Autowired
    private BookingEvent bookingEvent;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        WebApplicationContext context = WebApplicationContextUtils
                .getWebApplicationContext(config.getServletContext());
        AutowireCapableBeanFactory ctx = context.getAutowireCapableBeanFactory();
        ctx.autowireBean(this);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getRequestURI();
        if (!isValidPath(req.getRequestURI())) {
            resp.setStatus(404);
            return;
        }

        String dateStr = path.replaceAll("/api/booking/polling/", "").substring(0, 10);
        Date date = convertStringTotDate(dateStr);
        if (date == null) {
            resp.setStatus(400);
            return;
        }

        String id = path.replaceAll("/api/booking/polling/[0-9]{4}-[0-9]{2}-[0-9]{2}/", "").replaceAll("/", "");
        int roomId;
        try {
            roomId = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            resp.setStatus(400);
            return;
        }

        final AsyncContext asyncContext = req.startAsync(req, resp);
        asyncContext.setTimeout(30000);
        BookingListener bookingListener = new BookingListener(asyncContext);
        asyncContext.addListener(new BookingAsyncListener(roomId, date, bookingListener, bookingEvent));
        bookingEvent.register(roomId, date, bookingListener);
    }

    private Date convertStringTotDate(String dateStr) {
        try {
            return Date.valueOf(dateStr);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private boolean isValidPath(String path) {
        Matcher matcher = Pattern.compile("/api/booking/polling/[0-9]{4}-[0-9]{2}-[0-9]{2}/[0-9]{1,8}/?").matcher(path);
        return matcher.matches();
    }
}
