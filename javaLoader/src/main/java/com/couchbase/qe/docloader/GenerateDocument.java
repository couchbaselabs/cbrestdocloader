package com.couchbase.qe.docloader;

import com.couchbase.client.java.json.JsonObject;
import com.couchbase.qe.docloader.BasicDocument;
import com.github.javafaker.Faker;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class GenerateDocument {
    private Faker faker;

    public GenerateDocument(long seed){
        Random random = new Random();
        random.setSeed(seed);
        faker = new Faker(random);
    }

    private String first_name(){
        return this.faker.name().firstName();
    }

    private String middle_name() {
        return this.faker.name().firstName();
    }

    private String last_name() {
        return this.faker.name().lastName();
    }

    private String first_line() {
        return faker.address().buildingNumber();
    }

    private String second_line() {
        return this.faker.address().streetName();
    }

    private String city() {
        return this.faker.address().city();
    }

    private String pin_code() {
        return this.faker.address().zipCode();
    }

    private String phone_number() {
        return this.faker.phoneNumber().cellPhone();
    }

    private String birthday() {
        return this.faker.date().birthday(5, 100).toString();
    }

    private Map<String, Object> address(){
        Map<String, Object> address = new HashMap<>();
        address.put("first_line", this.first_line());
        address.put("second_line", this.second_line());
        address.put("city", this.city());
        address.put("pin_code", this.pin_code());
        return address;
    }

    public BasicDocument[] generateDocuments(int count){
        BasicDocument[] documents = new BasicDocument[count];
        for(int i=0; i < count; i++){
            BasicDocument doc = new BasicDocument();
            doc.first_name = first_name();
            doc.middle_name = middle_name();
            doc.last_name = last_name();
            doc.address = address();
            doc.phone_number = phone_number();
            doc.birthday = birthday();
            documents[i] = doc;
        }
        return documents;
    }

    public BasicDocument updateDocument(BasicDocument document, int count){
        Field[] keys = document.getClass().getDeclaredFields();
        for(int i=0; i < count; i++){
            int key_index = faker.random().nextInt(0, keys.length);
            Field key = keys[key_index];
            switch (key.getName()){
                case "first_name":
                    document.first_name = first_name();
                    break;
                case "middle_name" :
                    document.middle_name = middle_name();
                    break;
                case "last_name" :
                    document.last_name = last_name();
                    break;
                case "address" :
                    String[] address_keys = (String[]) document.address.keySet().toArray();
                    String random_address_key = address_keys[faker.random().nextInt(0, address_keys.length)];
                    switch (random_address_key){
                        case "first_line":
                            document.address.replace(random_address_key, first_line());
                            break;
                        case "second_line":
                            document.address.replace(random_address_key, second_line());
                            break;
                        case "city":
                            document.address.replace(random_address_key, city());
                            break;
                        case "pin_code":
                            document.address.replace(random_address_key, pin_code());
                            break;
                    }
                    break;
                case "phone_number":
                    document.phone_number = phone_number();
                    break;
                case "birthday":
                    document.birthday = birthday();
                    break;

            }
        }
        return document;
    }

    public boolean compare_documents(JsonObject document1, JsonObject document2){
        return compare_documents(new BasicDocument(document1), new BasicDocument(document2));
    }

    public boolean compare_documents(BasicDocument document1, BasicDocument document2){
        boolean result;
        result = document1.first_name.equals(document2.first_name) & document1.middle_name.equals(document2.middle_name);
        result = result & document1.last_name.equals(document2.middle_name);
        result = result & document1.address == document2.address;
        result = result & document1.phone_number.equals(document2.phone_number);
        result = result & document1.birthday.equals(document2.birthday);
        return result;
    }

}
