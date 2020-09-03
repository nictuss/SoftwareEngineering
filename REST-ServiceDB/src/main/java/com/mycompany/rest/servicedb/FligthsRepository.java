/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.rest.servicedb;

/**
 *
 * @author studente
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("/fligths")
public class FligthsRepository {

    private Connection conn;

    /* Initial simple version, based on main memory
            
            final private Map<Integer, Fligth> fligths = new HashMap<>();
            {
            
            Fligth fl1 = new Fligth();
            Fligth fl2 = new Fligth();
            fl1.setId(1);
            fl1.setName("AZ140");
            fl2.setId(2);
            fl2.setName("LH9120");
            
            fligths.put(1, fl1);
            fligths.put(2, fl2);
            }
     */
    public void setConnection(String pos) {
        try {
            try {
                Class.forName("org.sqlite.JDBC");
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(FligthsRepository.class.getName()).log(Level.SEVERE, null, ex);
            }
            conn
                    = DriverManager.getConnection("jdbc:sqlite:" + pos);
        } catch (SQLException ex) {
            Logger.getLogger(FligthsRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @GET
    @Path("{fligthId}")
    // @Produces("text/xml")
    @Produces("application/json")
    public Fligth getFligth(@PathParam("fligthId") int fligthId) {

        return findFligthById(fligthId);
    }

    @PUT
    @Path("{fligthId}")
    // @Consumes("text/xml")
    @Consumes("application/json")
    public Response updateFligth(@PathParam("fligthId") int fligthId, Fligth fligth) {
        Fligth existing = findFligthById(fligthId);
        if (existing == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        if (existing.equals(fligth)) {
            return Response.notModified().build();
        }
        // fligths.put(fligthId, fligth);
        update(fligthId, fligth);
        return Response.ok().build();
    }

    private Fligth findFligthById(int id) {

        PreparedStatement stat = null;
        Fligth fl = null;
        try {
            stat = conn.prepareStatement("select * from fligth where id = ?");
            stat.setString(1, String.valueOf(id));

            ResultSet rs = stat.executeQuery();
            if (rs.next()) {
                fl = new Fligth();
                fl.setId(Integer.parseInt(rs.getString("id")));
                fl.setName(rs.getString("name"));
                fl.setDestinationId(Integer.parseInt(rs.getString("destinationId")));
                Logger.getLogger(FligthsRepository.class.getName()).log(Level.INFO, "Accessed : " + fl);
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(FligthsRepository.class.getName()).log(Level.SEVERE, null, ex);
        }

        /* simple version 
        for (Map.Entry<Integer, Fligth> fligth : fligths.entrySet()) {
            if (fligth.getKey() == id) {
                return fligth.getValue();
            }
        }
         */
        return fl;
    }

    private void update(int fligthId, Fligth fligth) {
        PreparedStatement stat = null;
        try {
            stat = conn.prepareStatement("update fligth set name = ? where id = ?");
            stat.setString(1, fligth.getName());
            stat.setString(2, String.valueOf(fligthId));

            int affectedRow = stat.executeUpdate();
            if (affectedRow == 1) {
                Logger.getLogger(FligthsRepository.class.getName()).log(Level.INFO, "Updated : " + fligth);
                return;
            } else {
                throw new RuntimeException();
            }
        } catch (Exception ex) {
            Logger.getLogger(FligthsRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @GET
    @Path("{fligthId}/destinations/{destinationId}")
    // @Produces("text/xml")
    @Produces("application/json")
    public Destination getDestination(@PathParam("fligthId") int fligthId, @PathParam("destinationId") int destinationId) {
        return findDestinationById(fligthId, destinationId);
    }

    @POST
    @Path("{fligthId}/destinations/")
    // @Produces("text/xml")
    @Consumes("application/json")
    public Response postDestination(@PathParam("fligthId") int fligthId, Destination destination) {
        Destination dest = null;
        Fligth fl = findFligthById(fligthId);
        if(fl.getDestinationId() != destination.getId()) {
            int id = createDestination(fligthId, destination.getId());
            dest = new Destination();
            dest.setId(id);
        } else {
            return Response.status(Response.Status.CONFLICT).build();
        }
        
        return Response.ok(dest).build();
    }
    
    private int createDestination(int fligthId, int destinationId) {
        PreparedStatement stat = null;
        try {
            stat = conn.prepareStatement("update fligth set destinationId = ? where id = ?");
            stat.setString(1, Integer.toString(destinationId));
            stat.setString(2, Integer.toString(fligthId));

            int affectedRow = stat.executeUpdate();
            if (affectedRow != 1) {
                throw new RuntimeException();
            }
            
            stat = conn.prepareStatement("insert or replace into destination values(?)");
            stat.setString(1, Integer.toString(destinationId));

            affectedRow = stat.executeUpdate();
            if (affectedRow != 1) {
                throw new RuntimeException();
            }
        } catch (Exception ex) {
            Logger.getLogger(FligthsRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return destinationId;
    }
    
    @PUT
    @Path("{fligthId}/destinations/{destinationId}")
    // @Consumes("text/xml")
    @Consumes("application/json")
    public Response putDestination(@PathParam("fligthId") int fligthId, @PathParam("destinationId") int destinationId, Destination destination) {
        Destination dest = null;
        Fligth fl = findFligthById(fligthId);
        if(fl.getDestinationId() == destinationId) {
            int id = updateDestination(fligthId, destinationId, destination.getId());
            dest = new Destination();
            dest.setId(id);
        } else {
            return Response.status(Response.Status.CONFLICT).build();
        }
        
        return Response.ok(dest).build();
    }
    
    private int updateDestination(int fligthId, int oldDestinationId, int newDestinationId) {
        PreparedStatement stat = null;
        try {
            stat = conn.prepareStatement("update fligth set destinationId = ? where id = ?");
            stat.setString(1, Integer.toString(newDestinationId));
            stat.setString(2, Integer.toString(fligthId));

            int affectedRow = stat.executeUpdate();
            if (affectedRow != 1) {
                throw new RuntimeException();
            }
            
            stat = conn.prepareStatement("update destination set id = ? where id = ?");
            stat.setString(1, Integer.toString(newDestinationId));
            stat.setString(2, Integer.toString(oldDestinationId));

            affectedRow = stat.executeUpdate();
            if (affectedRow != 1) {
                throw new RuntimeException();
            }
        } catch (Exception ex) {
            Logger.getLogger(FligthsRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return newDestinationId;
    }

    @DELETE
    @Path("{fligthId}/destinations/{destinationId}")
    public Response deleteDestination(@PathParam("fligthId") int fligthId, @PathParam("destinationId") int destinationId) {
        PreparedStatement stat = null;
        try {
            stat = conn.prepareStatement("delete from destination where id = ?");
            stat.setString(1, Integer.toString(destinationId));

            int affectedRow = stat.executeUpdate();
            if (affectedRow != 1) {
                throw new RuntimeException();
            }
            
            stat = conn.prepareStatement("update fligth set destinationId = 0 where id = ?");
            stat.setString(1, Integer.toString(fligthId));

            affectedRow = stat.executeUpdate();
            if (affectedRow != 1) {
                throw new RuntimeException();
            }
        } catch (Exception ex) {
            Logger.getLogger(FligthsRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.ok().build();
    }

    private Destination findDestinationById(int fligthId, int id) {
        Fligth fl = findFligthById(fligthId);
        
        if(fl != null) {
            PreparedStatement stat = null;
            Destination dest = null;
            try {
                stat = conn.prepareStatement("select * from destination where id = ?");
                stat.setString(1, String.valueOf(id));

                ResultSet rs = stat.executeQuery();
                if (rs.next()) {
                    dest = new Destination();
                    dest.setId(Integer.parseInt(rs.getString("id")));
                    Logger.getLogger(FligthsRepository.class.getName()).log(Level.INFO, "Accessed : " + fl);
                }
                rs.close();
            } catch (SQLException ex) {
                Logger.getLogger(FligthsRepository.class.getName()).log(Level.SEVERE, null, ex);
            }

            /* simple version 
            for (Map.Entry<Integer, Fligth> fligth : fligths.entrySet()) {
                if (fligth.getKey() == id) {
                    return fligth.getValue();
                }
            }
             */
            return dest;
        }
        return null;
    }

    /*
    TO ALLOW A NESTED ENTITY NOT SO USEFUL
    @Path("{courseId}/students")
    public Course pathToStudent(@PathParam("courseId") int courseId) {
        return findById(courseId);
    }

    private Course findById(int id) {
        for (Map.Entry<Integer, Course> course : courses.entrySet()) {
            if (course.getKey() == id) {
                return course.getValue();
            }
        }
        return null;
    }
     */
}
