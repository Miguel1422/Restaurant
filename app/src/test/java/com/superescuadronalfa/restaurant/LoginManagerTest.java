package com.superescuadronalfa.restaurant;

import com.superescuadronalfa.restaurant.dbEntities.helpers.LoginManager;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LoginManagerTest {
    @Test
    public void hashingMethodIsCorrect() {
        String hash = LoginManager.ByteToString(LoginManager.HashSHA1("test"));
        String hash2 = "A94A8FE5CCB19BA61C4C0873D391E987982FBBD3";
        assertEquals(hash, hash2);
    }

    private boolean byteEquals(byte[] a, byte[] b) {
        if (a == null || b == null || a.length != b.length) return false;
        for (int i = 0; i < a.length; i++) {
            if (a[i] != b[i]) {
                return false;
            }
        }
        return true;
    }
}
