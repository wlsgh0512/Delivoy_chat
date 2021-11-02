package com.example.testcode.model;

import java.util.List;

public class User_Response {
    public int code;
    public String funcCode;
    public Items items;
    public int usReturnValue;
    public int usArrayCount;

    public class Items {
        public List<Rooms> astUsers;

        public class Rooms {
            public String ucAreaNo;
            public String ucDistribId;
            public String ucAgencyId;
            public String ucMemCourId;
            public String acUserId;
            public String acPassword;
            public String acRealName;
            public String acNickName;
            public String acCellNo;
            public String acEmailAddress;
            public String ucAccessFlag;
            public String ucAgreeOption;
            public String ucThirdPartyOption;
            public String ucIsDeleted;
            public String acEntryDateTime;
            public String acUpdateDateTime;
            public Object acDeleteDateTime;
        }
    }
}
