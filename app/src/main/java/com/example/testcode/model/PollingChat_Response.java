package com.example.testcode.model;

import java.util.List;

public class PollingChat_Response {

    public int code;
    public String funcCode;
    public Items items;
    public int usReturnValue;
    public int usArrayCount;

    public class Items {
        public List<Rooms> astRooms;

        public class Rooms {
            public int uiTalkNo;
            public String uiRoomNo;
            public String ucAreaNo;
            public String ucDistribId;
            public String ucAgencyId;
            public String ucMemCourId;
            public String ucConfirmFlag;
            public String acTalkMesg;
            public String acDateTime;
            public String acUserId;
            public String acRealName;
            public String acNickName;
            public String acCellNo;
            public String acEmailAddress;
            public String ucAccessFlag;
        }
    }
}