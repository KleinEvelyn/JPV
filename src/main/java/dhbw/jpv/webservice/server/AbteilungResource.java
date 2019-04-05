/*
 * Copyright © 2019  Evelyn Klein, Sven Hornung, Kevin Pfeffer
 * 
 * DHBW Karlsruhe - @KleinEvelyn, @SvenHornung, @kpfeffer
 * 
 * Dieser Quellcode ist lizenziert unter einer
 * Creative Commons Namensnennung 4.0 International Lizenz.
 */
package dhbw.jpv.webservice.server;

import dhbw.jpv.projekte.ejb.AbteilungBean;
import dhbw.jpv.projekte.jpa.Abteilung;
import java.util.List;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

/**
 * 
 */
@Path("Abteilungen")
@Consumes("application/json")
@Produces("application/json")
public class AbteilungResource {
    
    @EJB
    private AbteilungBean abteilungBean;
    
    @GET
    public List<Abteilung> findAbteilungen(){
        return this.abteilungBean.findAll();
    }
    
    @GET 
    @Path("{id}")
    public Abteilung getAbteilung(@PathParam("id") long id){
        return this.abteilungBean.findById(id);
    }
}
