package com.entitysc.euclase.fe.payload;

import java.util.List;
import lombok.Data;

/**
 *
 * @author briano
 */
@Data
public class ExceptionPayload {

    private String dateTime;
    private String status;
    private int responseCode;
    private String message;
    private List<String> error;
}
