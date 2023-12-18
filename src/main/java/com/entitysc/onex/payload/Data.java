package com.entitysc.onex.payload;

import java.util.Date;
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
public class Data {

    private String active;
    private String createdAt;
    private String currency;
    private String domain;
    private String id;
    private String name;
    private String recipient_code;
    private String type;
    private String updatedAt;
    private String is_deleted;
    private Details details;
    private String transfer_code;
    private String authorization_url;
    private String access_code;
    private String reference;
    private String amount;
    private String transaction_date;
    private String status;
    private String source;
    private String reason;
    private String transferred_at;
    private Date created_at;
    private Date updated_at;
}
