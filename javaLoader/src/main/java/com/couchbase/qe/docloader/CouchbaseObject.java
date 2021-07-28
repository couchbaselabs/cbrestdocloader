package com.couchbase.qe.docloader;
import com.couchbase.client.java.*;

import java.util.HashMap;
import java.util.Map;


public class CouchbaseObject {
    Cluster cluster;
    Map<String, Map<String, Map<String, Collection>>> clients = new HashMap<>();

    public CouchbaseObject(String host, String username, String password){
        String connectionString = "couchbase://" + host;
        cluster = Cluster.connect(connectionString, username, password);
    }

    public void connectCollection(String bucket, String scope, String collection){
        if(!clients.containsKey(bucket)){
            Map<String, Map<String, Collection>> bucket_map = new HashMap<>();
            clients.put(bucket, bucket_map);
        }
        if(!clients.get(bucket).containsKey(scope)){
            Map<String, Collection> scope_map = new HashMap<>();
            clients.get(bucket).put(scope, scope_map);
        }
        if(!clients.get(bucket).get(scope).containsKey(collection)){
            Collection client = cluster.bucket(bucket).scope(scope).collection(collection);
            clients.get(bucket).get(scope).put(collection, client);
        }
    }
}
