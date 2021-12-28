package com.pifrans.auth.responses;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;

public final class SuccessResponse {

    private SuccessResponse() {
    }

    public static ResponseEntity<?> handle(HttpStatus status) {
        return new ResponseEntity<>("", status);
    }

    public static ResponseEntity<?> handle(Object object, HttpStatus status) {
        return new ResponseEntity<>(object, status);
    }

    public static ResponseEntity<?> handle(String[] keys, Object[] objects, HttpStatus status) {
        final HashMap<String, Object> map = new HashMap<>();

        if (keys.length > 0 && keys.length == objects.length) {
            for (int i = 0; i < keys.length; i++) {
                map.put(keys[i], objects[i]);
            }
        }
        return new ResponseEntity<>(map, status);
    }
}
