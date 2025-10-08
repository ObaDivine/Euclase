package com.entitysc.euclase.fe.payload;

import java.util.List;
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
public class DataListResponsePayload {

    String responseCode;
    String responseMessage;
    EuclasePayload payload;
    List<EuclasePayload> data;
    List<EuclasePayload> versions;
    List<EuclasePayload> workflowData;
}
