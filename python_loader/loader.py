from flask import Flask, jsonify, request
from doc_generator import GenerateDocument
from couchbase_ops import CouchbaseObject, CouchbaseCurdOperation

app = Flask("__name__")
couchbase_objects = {}


@app.route('/createClient', methods=['POST'])
def create_client():
    # TODO: Add error handling of request here
    host = request.json['host']
    username = request.json['username']
    password = request.json['password']
    if host in couchbase_objects:
        return jsonify({'result': 'Done'})
    couchbase_object = CouchbaseObject(host, username, password)
    couchbase_objects[host] = couchbase_object
    return jsonify({'result': 'Done'})


@app.route('/connectCollection', methods=['POST'])
def connect_collection():
    host = request.json['host']
    bucket = request.json['bucket']
    scope = request.json['scope']
    collection = request.json['collection']
    couchbase_object = couchbase_objects[host]
    couchbase_object.connect_collection(bucket, scope, collection)
    return jsonify({'result': 'Done'})


@app.route('/createDocuments', methods=['POST'])
def create_documents():
    response = {}
    # TODO: Add error handling of request  here
    host = request.json['host']
    count = request.json['count']
    key_prefix = request.json['key_prefix']
    seed = request.json['seed']
    bucket = request.json['bucket']
    scope = request.json['scope']
    collection = request.json['collection']
    couchbase_object = couchbase_objects[host]
    couchbase_curd_object = CouchbaseCurdOperation(couchbase_object)
    document_generator = GenerateDocument(seed)
    documents = document_generator.generate_documents(count)
    for i in range(count):
        document = documents[i]
        key = "{0}{1}".format(key_prefix, seed + i)
        try:
            res = couchbase_curd_object.upsert_doc(key, document, bucket,
                                             scope, collection)
            response[key] = {"success": res.success,
                             "cas": res.cas,
                             "mutationToken": res.mutationToken.__dict__}
        except Exception as e:
            response[key] = {"success": False, "cas": 0}
        # response[key] = res.__dict__
    return jsonify(response)


@app.route('/getDocuments', methods=['POST'])
def get_documents():
    host = request.json['host']
    keys = request.json['keys']
    bucket = request.json['bucket']
    scope = request.json['scope']
    collection = request.json['collection']
    couchbase_object = couchbase_objects[host]
    couchbase_curd_object = CouchbaseCurdOperation(couchbase_object)
    result = {}
    for key in keys:
        try:
            res = couchbase_curd_object.get_doc(key, bucket, scope,
                                            collection)
            result[key] = {"success": True, "cas": res.cas,
                           "document": res.content}
        except Exception as e:
            result[key] = {'success': False,
                           "cas": 0, "document": None}
    return jsonify(result)


@app.route('/validateDocuments', methods=['GET'])
def validate_documents():
    host = request.json['host']
    count = request.json['count']
    key_prefix = request.json['key_prefix']
    seed = request.json['seed']
    updated_count = request.json['updated_count']
    bucket = request.json['bucket']
    scope = request.json['scope']
    collection = request.json['collection']
    couchbase_object = couchbase_objects[host]
    couchbase_curd_object = CouchbaseCurdOperation(couchbase_object)
    document_generator = GenerateDocument(seed)
    documents = document_generator.generate_documents(count)
    result = {}
    for i in range(count):
        original_document = documents[i]
        key = "{0}{1}".format(key_prefix, seed + i)
        res = couchbase_curd_object.get_doc(key, bucket, scope,
                                            collection)
        updated_document = document_generator.update_document(
            original_document, updated_count)
        document_in_db = res.content
        compare = document_generator.compare_documents(
            updated_document, document_in_db)
        if not compare:
            result[key] = {"Result": False,
                           "Expected":updated_document,
                           "Actual": document_in_db}
        else:
            result[key] = {"Result": True}
    return jsonify(result)


if __name__ == '__main__':
    app.run(debug=True)