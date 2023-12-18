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
public class Details {

    private String authorization_code = null;
    private String account_number;
    private String account_name;
    private String bank_code;
    private String bank_name;
}
