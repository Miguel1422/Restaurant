package com.superescuadronalfa.restaurant.dbEntities.helpers;

import com.superescuadronalfa.restaurant.dbEntities.Trabajador;
import com.superescuadronalfa.restaurant.dbEntities.User;
import com.superescuadronalfa.restaurant.dbEntities.control.ControlUsers;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginManager {

    public static Trabajador Login(String username, String pass) {
        User user = ControlUsers.getInstance().busca(username);
        if (user == null) return null;
        String salt = ByteToString(user.getSalt());
        String togen = salt + pass + salt;
        byte[] generatedHash = HashSHA1(togen);
        if (ByteToString(generatedHash).equals(ByteToString(user.getHash()))) {
            return user.getTrabajador();
        }
        return null;
    }

    public static String ByteToString(byte[] b) {
        StringBuilder result = new StringBuilder();
        for (byte aB : b) {
            result.append(Integer.toString((aB & 0xff) + 0x100, 16).substring(1));
        }
        return result.toString().toUpperCase();
    }

    private static byte[] HashSHA1(byte[] value) {
        MessageDigest crypt;
        try {
            crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(value);
            return crypt.digest();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }

    }

    public static byte[] HashSHA1(String value) {
        byte[] inputBytes = value.getBytes();
        return HashSHA1(inputBytes);
    }

}
