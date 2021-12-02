package com.kristovski.seaunitsmonitoring.seaunits;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BarentswatchResponse {

    private String access_token;
    private int expires_in;
    private String token_type;
    private String scope;

}
