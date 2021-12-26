package com.pifrans.auth.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReflectMethods {
    GET_ID(1, "getId");

    private final Integer id;
    private final String description;

    public static ReflectMethods toEnum(Integer id) {
        if (id == null) {
            return null;
        }

        for (ReflectMethods x : ReflectMethods.values()) {
            if (id.equals(x.getId())) {
                return x;
            }
        }
        throw new IllegalArgumentException("ID inválido: " + id);
    }
}
