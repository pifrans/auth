package com.pifrans.auth.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HeadersValues {
    BEARER(1, "Bearer ");


    private final Integer id;
    private final String description;

    public static HeadersValues toEnum(Integer id) {
        if (id == null) {
            return null;
        }

        for (HeadersValues x : HeadersValues.values()) {
            if (id.equals(x.getId())) {
                return x;
            }
        }
        throw new IllegalArgumentException("ID inválido: " + id);
    }
}
