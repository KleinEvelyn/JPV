/*
 * Copyright © 2019  Evelyn Klein, Sven Hornung, Kevin Pfeffer
 * 
 * DHBW Karlsruhe - @KleinEvelyn, @SvenHornung, @kpfeffer
 * 
 * Dieser Quellcode ist lizenziert unter einer
 * Creative Commons Namensnennung 4.0 International Lizenz.
 */
package dhbw.jpv.webservice.server;

import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;


@ApplicationPath("api")
public class ProjektRestAPI extends Application{
    @Override 
    public Set<Class<?>> getClasses(){
        Set<Class<?>> resources = new HashSet<>();
        
        resources.add(ProjektResource.class);
        //resources.add(AbteilungResource.class);
        
        return resources;
    }
}
