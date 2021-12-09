package com.example.testcode.dto;

public class SendDto {
//    testUiRoomNo,
//    ucAreaNo,
//    ucDistribId,
//    ucAgencyId,
//    ucMemCourId,
//    message

    private String testUiRoomNo;
    private String ucAreaNo;
    private String ucDistribId;
    private String ucAgencyId;
    private String ucMemCourId;
    private String message;

    public SendDto(String testUiRoomNo, String ucAreaNo, String ucDistribId, String ucAgencyId,
                   String ucMemCourId, String message) {
        this.testUiRoomNo = testUiRoomNo;
        this.ucAreaNo = ucAreaNo;
        this.ucDistribId = ucDistribId;
        this.ucAgencyId = ucAgencyId;
        this.ucMemCourId = ucMemCourId;
        this.message = message;
    }

    public String getTestUiRoomNo() { return testUiRoomNo; }

    public String getUcAreaNo() { return ucAreaNo; }

    public String getUcDistribId() { return ucDistribId; }

    public String getUcAgencyId() { return ucAgencyId; }

    public String getUcMemCourId() { return ucMemCourId; }

    public String getMessage() { return message; }
}
