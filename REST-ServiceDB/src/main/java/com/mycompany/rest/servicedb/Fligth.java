/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.rest.servicedb;

// import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author studente
 */
@XmlRootElement(name = "Fligth")
// @JacksonXmlRootElement(localName = "Fligth")
public class Fligth {

    private int id;
    private String name;
    private int destinationId;

    public int getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(int destinationId) {
        this.destinationId = destinationId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public int hashCode() {
        return id + name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Fligth) && (id == ((Fligth) obj).getId()) && (name.equals(((Fligth) obj).getName()));
    }
    @Override
    public String toString() {
        return "fligth " + id + " " + name;
    }
}
