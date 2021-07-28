from couchbase.cluster import Cluster, PasswordAuthenticator, ClusterOptions


class CouchbaseObject:
    def __init__(self, host, username, password):
        auth = PasswordAuthenticator(username, password)
        self.cluster = Cluster('couchbase://{}'.format(host),
                               ClusterOptions(auth))
        self.clients = {}

    def connect_collection(self, bucket, scope, collection):
        if bucket not in self.clients:
            self.clients[bucket] = {}
        if scope not in self.clients[bucket]:
            self.clients[bucket][scope] = {}
        if collection not in self.clients[bucket][scope]:
            client = self.cluster.bucket(bucket).scope(
                scope).collection(collection)
            self.clients[bucket][scope][collection] = client


class CouchbaseCurdOperation:
    def __init__(self, couchbase_object:CouchbaseObject):
        self.couchbase_object = couchbase_object

    def upsert_doc(self, key, document, bucket, scope, collection):
        client = self.couchbase_object.clients[bucket][scope][collection]
        res = client.upsert(key, document)
        return res

    def get_doc(self, key, bucket, scope, collection):
        client = self.couchbase_object.clients[bucket][scope][collection]
        res = client.get(key)
        return res

    def delete_doc(self, key, bucket, scope, collection):
        client = self.couchbase_object.clients[bucket][scope][
            collection]
        res = client.remove(key)
        return res
