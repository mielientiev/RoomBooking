package com.roombooking.utils;

import com.roombooking.dao.user.UserDao;
import com.roombooking.entity.User;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;

@Service
public class AuthorizationUtil {

    @Autowired
    private UserDao dao;

    public User userAuthorization(String auth) {
        if (auth == null) {
            return null;
        }

        String decodedAuth = decode(auth);
        if (decodedAuth.isEmpty())
            return null;

        int pos = decodedAuth.indexOf(":");
        String login = decodedAuth.substring(0, pos);
        String password = decodedAuth.substring(pos + 1);
        return dao.findByLoginPassword(login, password);
    }

    private String decode(String auth) {
        auth = auth.replaceFirst("Basic ", "");
        String decodedAuth = new String(Base64.decodeBase64(auth.trim()), Charset.forName("UTF-8"));
        int pos = decodedAuth.indexOf(":");
        if (pos <= 0) {
            return "";
        }
        return decodedAuth;
    }
}
