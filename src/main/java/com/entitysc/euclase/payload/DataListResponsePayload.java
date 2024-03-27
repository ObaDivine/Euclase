package com.entitysc.euclase.payload;

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

    private String responseCode;
    private String responseMessage;
    private List<PylonPayload> data;
    private PylonPayload payload;
    private List<PylonPayload> workflowData;
}
