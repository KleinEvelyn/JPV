/*
 * Copyright © 2018 Dennis Schulmeister-Zimolong
 * 
 * E-Mail: dhbw@windows3.de
 * Webseite: https://www.wpvs.de/
 * 
 * Dieser Quellcode ist lizenziert unter einer
 * Creative Commons Namensnennung 4.0 International Lizenz.
 */
package dhbw.jpv.projekte.jpa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.TableGenerator;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Kategorien, die den Aufgaben zugeordnet werden können.
 */
@Entity
public class Abteilung implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "abteilung_ids")
    @TableGenerator(name = "abteilung_ids", initialValue = 0, allocationSize = 50)
    private long id;

    @Column(length = 30)
    @NotNull(message = "Der Abteilungsname darf nicht leer sein.")
    @Size(min = 3, max = 30, message = "Der Name muss zwischen drei und 30 Zeichen lang sein.")
    private String name;
    
    @Column(length = 30)
    @NotNull(message = "Das Abteilungskürzel darf nicht leer sein.")
    @Size(min = 2, max = 5, message = "Das Kürzel muss zwischen zwei und fünf Zeichen lang sein.")
    private String kuerzel;
    
    @Column(length = 30)
    @NotNull(message = "Die Abteilungsbeschreibung darf nicht leer sein.")
    @Size(min = 10, max = 100, message = "Die Beschreibung muss zwischen 10 und 100 Zeichen lang sein.")
    private String beschreibung;

    @OneToMany(mappedBy = "abteilung", fetch = FetchType.LAZY)
    List<Projekt> projekte = new ArrayList<>();;
    
    

    //<editor-fold defaultstate="collapsed" desc="Konstruktoren">
    public Abteilung() {
    }

    public Abteilung(String name, String kuerzel, String beschreibung) {
        this.projekte = new ArrayList<>();
        this.name = name;
        this.kuerzel = kuerzel;
        this.beschreibung = beschreibung;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Setter und Getter">
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKuerzel() {
        return kuerzel;
    }

    public void setKuerzel(String kuerzel) {
        this.kuerzel = kuerzel;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public List<Projekt> getProjekte() {
        return projekte;
    }

    public void setProjekte(List<Projekt> projekte) {
        this.projekte = projekte;
    }
    //</editor-fold>

}
