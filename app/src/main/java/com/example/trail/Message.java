package com.example.trail;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Message")
public class Message extends ParseObject {
    public String getUserId() {
        return getString("userId");
    }

    public String getBody() {
        return getString("body");
    }

    public String getReceiverId(){return getString("receiverId");}

    public void setReceiverId(String receiverId){put("receiverId", receiverId);}

    public void setUserId(String userId) {
        put("userId", userId);
    }
    public void setBody(String body) {
        put("body", body);
    }
}

