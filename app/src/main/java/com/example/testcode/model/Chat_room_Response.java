package com.example.testcode.model;

import java.util.List;

public class Chat_room_Response {

    public int code;
    public String funcCode;
    public Items items;
    public int usReturnValue;
    public int usArrayCount;

    public class Items {
        public List<Rooms> astRooms;

        public class Rooms {
            public String uiRoomNo;
            public String acRoomTitle;
            public Object acImageUrl;
            public String acDateTime;
        }
    }
}
