package com.pifrans.auth.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HeadersKeys {
    X_AUTHORIZATION(1, "X-Authorization");

    private final Integer id;
    private final String description;

    public static HeadersKeys toEnum(Integer id) {
        if (id == null) {
            return null;
        }

        for (HeadersKeys x : HeadersKeys.values()) {
            if (id.equals(x.getId())) {
                return x;
            }
        }
        throw new IllegalArgumentException("ID inválido: " + id);
    }
}
