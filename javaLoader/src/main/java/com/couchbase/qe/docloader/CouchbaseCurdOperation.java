package com.couchbase.qe.docloader;
import com.couchbase.client.java.*;
import com.couchbase.client.java.json.JsonObject;
import com.couchbase.client.java.kv.*;


public class CouchbaseCurdOperation {
    private CouchbaseObject couchbaseObject;

    public CouchbaseCurdOperation(CouchbaseObject cbObject){
        couchbaseObject = cbObject;
    }

    public MutationResult upsert_doc(String key, JsonObject document, String bucket, String scope, String collection){
        Collection client = couchbaseObject.clients.get(bucket).get(scope).get(collection);
        return client.upsert(key, document);
    }

    public GetResult get_doc(String key, String bucket, String scope, String collection) {
        Collection client = couchbaseObject.clients.get(bucket).get(scope).get(collection);
        return client.get(key);
    }

    public MutationResult delete_doc(String key, String bucket, String scope, String collection) {
        Collection client = couchbaseObject.clients.get(bucket).get(scope).get(collection);
        return client.remove(key);
    }
}
