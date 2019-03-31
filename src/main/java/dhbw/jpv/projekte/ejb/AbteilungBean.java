/*
 * Copyright © 2018 Dennis Schulmeister-Zimolong
 * 
 * E-Mail: dhbw@windows3.de
 * Webseite: https://www.wpvs.de/
 * 
 * Dieser Quellcode ist lizenziert unter einer
 * Creative Commons Namensnennung 4.0 International Lizenz.
 */
package dhbw.jpv.projekte.ejb;

import dhbw.jpv.common.ejb.EntityBean;
import dhbw.jpv.projekte.jpa.Abteilung;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;

/**
 * Einfache EJB mit den üblichen CRUD-Methoden für Kategorien.
 */
@Stateless
@RolesAllowed("app-user")
public class AbteilungBean extends EntityBean<Abteilung, Long> {

    public AbteilungBean() {
        super(Abteilung.class);
    }

    /**
     * Auslesen aller Kategorien, alphabetisch sortiert.
     *
     * @return Liste mit allen Kategorien
     */
    public List<Abteilung> findAllSorted() {
        return this.em.createQuery("SELECT c FROM Abteilung c ORDER BY c.name").getResultList();
    }
}
