/* 
 * Copyright © 2019  Evelyn Klein, Sven Hornung, Kevin Pfeffer
 * 
 * DHBW Karlsruhe - @KleinEvelyn, @SvenHornung, @kpfeffer
 * 
 * Dieser Quellcode ist lizenziert unter einer
 * Creative Commons Namensnennung 4.0 International Lizenz.
 */
"use strict";

class AbteilungResource {

    constructor(url) {
        this.url = url || "https://localhost:8443/JPV/app/api/Abteilungen";
    }
    
    async findAbteilungen(query) {
        let url = this.url;

        if (query !== undefined) {
            url += "?query=" + encodeURI(query);
        }

        let response = await fetch(url, {
            headers: {
                "accept": "application/json"
            }
        });

        return await response.json();
    }

    async getAbteilung(id) {
        let response = await fetch(this.url + id + "/", {
            headers: {
                "accept": "application/json"     
            }
        });

        return await response.json();
    }
}

