package com.pifrans.auth.exceptions.treatments;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StandardTreatment {
    private String target;
    private String type;
    private String message;
    private String method;
    private String path;

    public StandardTreatment(String target, String type, String message) {
        this.target = target;
        this.type = type;
        this.message = message;
    }

    public StandardTreatment(String target, String type, String message, String method, String path) {
        this.target = target;
        this.type = type;
        this.message = message;
        this.method = method;
        this.path = path;
    }
}
