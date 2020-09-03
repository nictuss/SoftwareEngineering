/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.rest.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.xml.bind.JAXB;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

// Resource Folder path (Create in file tab): src/main/resources

public class Client {

    private static final String BASE_URL = "http://localhost:8080/";
    private static CloseableHttpClient client;

    public static void main(String[] args) throws IOException{
    client = HttpClients.createDefault();
    
    // Example GET
    Fligth fligth = getFligth(1);
    System.out.println(fligth);
    System.out.println(fligth.getDestinationId());
    
    // Example POST/PUT
    Destination destination = getDestination(1, 1);
    System.out.println(destination);
    createValidDestination();
    destination = getDestination(1, 4);
    System.out.println(destination);
    
    client.close();
    
        
    }

      private static Destination getDestination(int fligthId, int destinationId) throws IOException {
        /*final URL url = new URL(BASE_URL + "fligths/" + fligthId + "/destinations/" + destinationId);
        final InputStream input = url.openStream();
        return JAXB.unmarshal(new InputStreamReader(input), Destination.class);*/
        
        // JSON Marshaling
        ObjectMapper mapper = new ObjectMapper();
        final URL url = new URL(BASE_URL + "fligths/" + fligthId + "/destinations/" + destinationId);
        InputStream input = url.openStream();
        Destination dest = (Destination)mapper.readValue(input, Destination.class);        
        return dest;
    }

      private static Fligth getFligth(int id) throws IOException {
        /*final URL url = new URL(BASE_URL + "fligths/" + id);
        final InputStream input = url.openStream();
        return JAXB.unmarshal(new InputStreamReader(input), Fligth.class);*/
        
        // JSON Marshaling
        ObjectMapper mapper = new ObjectMapper();
        final URL url = new URL(BASE_URL + "fligths/" + id);
        InputStream input = url.openStream();
        Fligth fl = (Fligth)mapper.readValue(input, Fligth.class);        
        return fl;
    }

      
      
    private static void createValidDestination() throws IOException {
        /*final HttpPost httpPost = new HttpPost(BASE_URL + "fligths/1/destinations");
        final InputStream resourceStream =  Client.class.getClassLoader().getResourceAsStream("newDestination.xml");
        httpPost.setEntity(new InputStreamEntity(resourceStream));
        httpPost.setHeader("Content-Type", "text/xml");

        final HttpResponse response = client.execute(httpPost);*/
        
        // JSON Marshaling
        ObjectMapper objectMapper = new ObjectMapper();
        Destination newDest = new Destination();
        
        newDest.setId(4);
        
        String json = objectMapper.writeValueAsString(newDest); 
        
        HttpPost httpPost = new HttpPost(BASE_URL + "fligths/1/destinations");
        
        StringEntity entity = new StringEntity(json);
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");
        HttpResponse response = client.execute(httpPost);
        System.out.println(response);
    }

}  