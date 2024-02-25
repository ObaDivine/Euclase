package com.entitysc.euclase.payload;

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
public class EuclaseResponsePayload {

    private String responseCode;
    private String responseMessage;
    private String path;
    private String error;
    private String status;
}
