package com.couchbase.qe.docloader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication(scanBasePackages = {"com.couchbase.qe.docloader"})
public class DocloaderApplication {

    static Map<String, CouchbaseObject> hosts = new HashMap<>();

    public static void main(String[] args) {
        SpringApplication.run(DocloaderApplication.class, args);
    }

}
