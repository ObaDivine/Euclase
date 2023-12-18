package com.entitysc.onex.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author briano
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PylonResponsePayload {

    private String responseCode;
    private String responseMessage;
    private PylonPayload data;
    private String path;
    private String error;
    private String status;
}
