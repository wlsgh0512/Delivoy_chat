package com.example.testcode.model;

import java.util.List;

public class FriendsResponse {
    public String ucAreaNo;
    public String ucDistribId;
    public String ucAgencyId;
    public String ucMemCourId;
    public String acRealName;
    public String acNickName;
    public String acCellNo;
    public String acEmailAddress;
    public String ucAccessFlag;


    public class Items {
        public List<FriendsResponse> friends;
    }

    public class Root {
        public int code;
        public String funcCode;
        public Items items;
        public int usReturnValue;
        public int usArrayCount;
    }
}