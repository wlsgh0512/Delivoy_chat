package com.example.testcode.model;

import java.util.List;

public class Chat_room_Response {
    public String uiRoomNo;
    public String acRoomTitle;
    public Object acImageUrl;
    public String acDateTime;
}

class Items{
    public List<Chat_room_Response> astRooms;
}

class Root{
    public int code;
    public String funcCode;
    public Items items;
    public int usReturnValue;
    public int usArrayCount;
}
