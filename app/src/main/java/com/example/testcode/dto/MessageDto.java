package com.example.testcode.dto;

public class MessageDto {

        private String uiTalkNo;
        private String uiRoomNo;
        private String ucAreaNo;
        private String ucDistribId;
        private String ucAgencyId;
        private String ucMemCourId;

        public MessageDto(String uiTalkNo, String uiRoomNo, String ucAreaNo,
                       String ucDistribId, String ucAgencyId, String ucMemCourId) {
            this.uiTalkNo = uiTalkNo;
            this.uiRoomNo = uiRoomNo;
            this.ucAreaNo = ucAreaNo;
            this.ucDistribId = ucDistribId;
            this.ucAgencyId = ucAgencyId;
            this.ucMemCourId = ucMemCourId;
        }

        public String getUiTalkNo() {
            return uiTalkNo;
        }
        public String getuiRoomNo() {
            return uiRoomNo;
        }
        public String getucAreaNo() {
            return ucAreaNo;
        }
        public String getucDistribId() {
            return ucDistribId;
        }
        public String getucAgencyId() {
            return ucAgencyId;
        }
        public String getucMemCourId() {
            return ucMemCourId;
        }

}
