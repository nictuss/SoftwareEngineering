/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package soapclidb;

import com.mycompany.soapwsdb.Director;
import com.mycompany.soapwsdb.Movie;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author studente
 */
public class SOAPCLIDB {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println(getMovie(1));
        
        System.out.println(getDirector(1));
        
        List<Integer> movies = getMovies();
        for(Integer id: movies){
            System.out.println(id);
        }
    }

    private static Director getDirector(int arg0) {
        com.mycompany.soapwsdb.ExamImplService service = new com.mycompany.soapwsdb.ExamImplService();
        com.mycompany.soapwsdb.Exam port = service.getExamImplPort();
        return port.getDirector(arg0);
    }

    private static Movie getMovie(int arg0) {
        com.mycompany.soapwsdb.ExamImplService service = new com.mycompany.soapwsdb.ExamImplService();
        com.mycompany.soapwsdb.Exam port = service.getExamImplPort();
        return port.getMovie(arg0);
    }

    private static java.util.List<java.lang.Integer> getMovies() {
        com.mycompany.soapwsdb.ExamImplService service = new com.mycompany.soapwsdb.ExamImplService();
        com.mycompany.soapwsdb.Exam port = service.getExamImplPort();
        return port.getMovies();
    }
    
}
