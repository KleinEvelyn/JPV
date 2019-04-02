/*
 * Copyright © 2019  Evelyn Klein, Sven Hornung, Kevin Pfeffer
 * 
 * DHBW Karlsruhe - @KleinEvelyn, @SvenHornung, @kpfeffer
 * 
 * Dieser Quellcode ist lizenziert unter einer
 * Creative Commons Namensnennung 4.0 International Lizenz.
 */
package dhbw.jpv.webservice.server;

import dhbw.jpv.projekte.ejb.ProjektBean;
import dhbw.jpv.projekte.jpa.Projekt;
import java.util.List;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * 
 */
@Path("Projekte")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProjektResource {
    
    @EJB
    private ProjektBean projektBean;
    
    @GET
    public List<Projekt> findProjekte(){
        return this.projektBean.findAll();
    }
    
    @GET 
    @Path("{id}")
    public Projekt getProjekt(@PathParam("id") long id){
        return this.projektBean.findById(id);
    }
}
