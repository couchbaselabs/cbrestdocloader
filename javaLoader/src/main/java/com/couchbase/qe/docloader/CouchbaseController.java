package com.couchbase.qe.docloader;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class CouchbaseController {

    @RequestMapping(method = RequestMethod.POST, value="/createClient")

    @ResponseBody
    public void createClient(@RequestBody CouchbaseObject cbObject){

    }

}
