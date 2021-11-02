package com.example.testcode.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/*
{
    "code": 0,
    "funcCode": "0x505",
    "items": {
        "astRooms": [
            {
                "ucAreaNo": "88",
                "ucDistribId": "17",
                "ucAgencyId": "1",
                "ucMemCourId": "2",
                "acRealName": "유비",
                "acNickName": "현덕",
                "acCellNo": "01012345678",
                "acEmailAddress": "youb@roadvoy.com",
                "ucAccessFlag": "1"
            },
            {
                "ucAreaNo": "88",
                "ucDistribId": "17",
                "ucAgencyId": "1",
                "ucMemCourId": "3",
                "acRealName": "관우",
                "acNickName": "운장",
                "acCellNo": "01023456789",
                "acEmailAddress": "kwanwoo@roadvoy.com",
                "ucAccessFlag": "0"
            },
            {
                "ucAreaNo": "88",
                "ucDistribId": "17",
                "ucAgencyId": "1",
                "ucMemCourId": "4",
                "acRealName": "장비",
                "acNickName": "익덕",
                "acCellNo": "01034567890",
                "acEmailAddress": "jangb@roadvoy.com",
                "ucAccessFlag": "0"
            },
            {
                "ucAreaNo": "88",
                "ucDistribId": "17",
                "ucAgencyId": "1",
                "ucMemCourId": "5",
                "acRealName": "제갈",
                "acNickName": "공명",
                "acCellNo": "01045678901",
                "acEmailAddress": "jaegal@roadvoy.com",
                "ucAccessFlag": "0"
            },
            {
                "ucAreaNo": "88",
                "ucDistribId": "17",
                "ucAgencyId": "1",
                "ucMemCourId": "6",
                "acRealName": "방통",
                "acNickName": "사원",
                "acCellNo": "01056789012",
                "acEmailAddress": "bangtong@roadvoy.com",
                "ucAccessFlag": "0"
            },
            {
                "ucAreaNo": "88",
                "ucDistribId": "17",
                "ucAgencyId": "1",
                "ucMemCourId": "7",
                "acRealName": "마초",
                "acNickName": "맹기",
                "acCellNo": "01067890123",
                "acEmailAddress": "macho@roadvoy.com",
                "ucAccessFlag": "0"
            },
            {
                "ucAreaNo": "88",
                "ucDistribId": "17",
                "ucAgencyId": "1",
                "ucMemCourId": "8",
                "acRealName": "서서",
                "acNickName": "원직",
                "acCellNo": "01001234567",
                "acEmailAddress": "seoseo@roadvoy.com",
                "ucAccessFlag": "0"
            },
            {
                "ucAreaNo": "88",
                "ucDistribId": "17",
                "ucAgencyId": "1",
                "ucMemCourId": "9",
                "acRealName": "손권",
                "acNickName": "중모",
                "acCellNo": "01078901234",
                "acEmailAddress": "sonkwon@roadvoy.com",
                "ucAccessFlag": "1"
            },
            {
                "ucAreaNo": "88",
                "ucDistribId": "17",
                "ucAgencyId": "1",
                "ucMemCourId": "10",
                "acRealName": "주유",
                "acNickName": "공근",
                "acCellNo": "01089012345",
                "acEmailAddress": "jooyou@roadvoy.com",
                "ucAccessFlag": "0"
            },
            {
                "ucAreaNo": "88",
                "ucDistribId": "17",
                "ucAgencyId": "1",
                "ucMemCourId": "11",
                "acRealName": "노숙",
                "acNickName": "자경",
                "acCellNo": "01090123456",
                "acEmailAddress": "nosuk@roadvoy.com",
                "ucAccessFlag": "0"
            },
            {
                "ucAreaNo": "88",
                "ucDistribId": "17",
                "ucAgencyId": "1",
                "ucMemCourId": "12",
                "acRealName": "육손",
                "acNickName": "백언",
                "acCellNo": "01001234567",
                "acEmailAddress": "sixhands@roadvoy.com",
                "ucAccessFlag": "0"
            },
            {
                "ucAreaNo": "88",
                "ucDistribId": "17",
                "ucAgencyId": "1",
                "ucMemCourId": "13",
                "acRealName": "조조",
                "acNickName": "맹덕",
                "acCellNo": "01012349876",
                "acEmailAddress": "jojo@roadvoy.com",
                "ucAccessFlag": "1"
            },
            {
                "ucAreaNo": "88",
                "ucDistribId": "17",
                "ucAgencyId": "1",
                "ucMemCourId": "14",
                "acRealName": "사마",
                "acNickName": "중달",
                "acCellNo": "01023458765",
                "acEmailAddress": "sama@roadvoy.com",
                "ucAccessFlag": "0"
            },
            {
                "ucAreaNo": "88",
                "ucDistribId": "17",
                "ucAgencyId": "1",
                "ucMemCourId": "15",
                "acRealName": "여포",
                "acNickName": "봉선",
                "acCellNo": "01034567654",
                "acEmailAddress": "yupo@roadvoy.com",
                "ucAccessFlag": "0"
            },
            {
                "ucAreaNo": "88",
                "ucDistribId": "17",
                "ucAgencyId": "1",
                "ucMemCourId": "16",
                "acRealName": "동승",
                "acNickName": "국구",
                "acCellNo": "01045676543",
                "acEmailAddress": "dongwin@roadvoy.com",
                "ucAccessFlag": "0"
            },
            {
                "ucAreaNo": "88",
                "ucDistribId": "17",
                "ucAgencyId": "1",
                "ucMemCourId": "25",
                "acRealName": "테스트이십오",
                "acNickName": "디그다",
                "acCellNo": "01011223344",
                "acEmailAddress": "www@naver.com",
                "ucAccessFlag": "0"
            }
        ]
    },
    "usReturnValue": 0,
    "usArrayCount": 16
}
 */

public class FriendsResponse {

    public int code;
    public String funcCode;
    public Items items;
    public int usReturnValue;
    public int usArrayCount;

    public class Items {
        public List<Rooms> astRooms;

        public class Rooms {
            public String ucAreaNo;
            public String ucDistribId;
            public String ucAgencyId;
            public String ucMemCourId;
            public String acRealName;
            public String acNickName;
            public String acCellNo;
            public String acEmailAddress;
            public String ucAccessFlag;
        }
    }

}