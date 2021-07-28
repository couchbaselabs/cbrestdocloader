package com.couchbase.qe.docloader;

import java.util.HashMap;
import java.util.Map;

import com.couchbase.client.java.json.*;

public class BasicDocument {
    String first_name;
    String middle_name;
    String last_name;
    Map<String, Object> address = new HashMap<>();
    String phone_number;
    String birthday;

    public BasicDocument() {
        address.put("first_line", "");
        address.put("second_line", "");
        address.put("city", "");
        address.put("pin_code", "");
    }

    public BasicDocument(JsonObject object) {
        if (object.containsKey("first_name")) {
            first_name = (String) object.get("first_name");
        }
        if (object.containsKey("middle_name")) {
            middle_name = (String) object.get("middle_name");
        }
        if (object.containsKey("last_name")) {
            last_name = (String) object.get("last_name");
        }
        if (object.containsKey("phone_number")) {
            phone_number = (String) object.get("phone_number");
        }
        if (object.containsKey("birthday")) {
            birthday = (String) object.get("birthday");
        }
        if (object.containsKey("address")) {
            JsonObject add = object.getObject("address");
            address = add.toMap();
        }
    }

    public JsonObject convertToJsonObject(){
        JsonObject to_return = JsonObject.create();
        to_return.put("first_name", first_name);
        to_return.put("middle_name", middle_name);
        to_return.put("last_name", last_name);
        to_return.put("address", address);
        to_return.put("phone_number", phone_number);
        to_return.put("birthday", birthday);
        return to_return;
    }
}
