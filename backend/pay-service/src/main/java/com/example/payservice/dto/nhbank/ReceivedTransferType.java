package com.example.payservice.dto.nhbank;

public enum ReceivedTransferType {
    OTHER("/ReceivedTransferOtherBank.nh", "ReceivedTransferOtherBank"),
    NH("/ReceivedTransferAccountNumber.nh", "ReceivedTransferAccountNumber");
    private final String uri;
    private final String apiNm;

    ReceivedTransferType(String uri, String apiNm) {
        this.uri = uri;
        this.apiNm = apiNm;
    }

    public String getApiNm() {
        return apiNm;
    }

    public String getUri() {
        return uri;
    }
}
