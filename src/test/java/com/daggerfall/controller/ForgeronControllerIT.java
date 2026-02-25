package com.daggerfall.controller;

import com.daggerfall.domaine.Dague;
import com.daggerfall.domaine.Epee;
import com.daggerfall.domaine.Qualite;
import com.daggerfall.repository.DagueRepository;
import com.daggerfall.repository.EpeeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ForgeronControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EpeeRepository epeeRepository;

    @Autowired
    private DagueRepository dagueRepository;

    @BeforeEach
    void setUp() {
        dagueRepository.deleteAll();
        epeeRepository.deleteAll();
    }

    // --- Tests épées ---

    @Test
    void getEpees_retourneListeVide_quandAucuneEpee() throws Exception {
        mockMvc.perform(get("/forge/epees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void getEpees_retourneLesEpees_quandEllesExistent() throws Exception {
        epeeRepository.save(new Epee("Durandal", 90, "Acier trempé", Qualite.LEGENDAIRE));
        epeeRepository.save(new Epee("Claymore", 110, "Fer forgé", Qualite.RARE));

        mockMvc.perform(get("/forge/epees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].nom", containsInAnyOrder("Durandal", "Claymore")));
    }

    @Test
    void getEpee_parId_retourneLepee() throws Exception {
        Epee epee = epeeRepository.save(new Epee("Excalibur", 85, "Acier enchanté", Qualite.LEGENDAIRE));

        mockMvc.perform(get("/forge/epees/{id}", epee.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom", is("Excalibur")))
                .andExpect(jsonPath("$.qualite", is("LEGENDAIRE")));
    }

    @Test
    void getEpee_avecIdInexistant_retourne404() throws Exception {
        mockMvc.perform(get("/forge/epees/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getEpees_parQualite_filtreCorrectement() throws Exception {
        epeeRepository.save(new Epee("Durandal", 90, "Acier", Qualite.LEGENDAIRE));
        epeeRepository.save(new Epee("Lame ordinaire", 70, "Fer", Qualite.COMMUNE));

        mockMvc.perform(get("/forge/epees/qualite/LEGENDAIRE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nom", is("Durandal")));
    }

    @Test
    void postEpee_creeLepeeEtRetourne201() throws Exception {
        Epee nouvelleEpee = new Epee("Excalibur", 85, "Acier enchanté", Qualite.LEGENDAIRE);

        mockMvc.perform(post("/forge/epees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nouvelleEpee)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nom", is("Excalibur")))
                .andExpect(jsonPath("$.id", notNullValue()));
    }

    @Test
    void deleteEpee_supprimeLepeeEtRetourne204() throws Exception {
        Epee epee = epeeRepository.save(new Epee("Lame à fondre", 60, "Fer", Qualite.COMMUNE));

        mockMvc.perform(delete("/forge/epees/{id}", epee.getId()))
                .andExpect(status().isNoContent());
    }

    // --- Tests dagues ---

    @Test
    void getDagues_retourneLesEmpoisonnees_seulement() throws Exception {
        dagueRepository.save(new Dague("Croc de Vipère", 25, "Acier noir", true));
        dagueRepository.save(new Dague("Stiletto", 20, "Acier", false));

        mockMvc.perform(get("/forge/dagues/empoisonnees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nom", is("Croc de Vipère")))
                .andExpect(jsonPath("$[0].empoisonnee", is(true)));
    }

    @Test
    void postDague_creeLaDagueEtRetourne201() throws Exception {
        Dague nouvelleDague = new Dague("Lame des Ombres", 18, "Obsidienne", true);

        mockMvc.perform(post("/forge/dagues")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nouvelleDague)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nom", is("Lame des Ombres")))
                .andExpect(jsonPath("$.empoisonnee", is(true)));
    }

    @Test
    void getDague_avecIdInexistant_retourne404() throws Exception {
        mockMvc.perform(get("/forge/dagues/999"))
                .andExpect(status().isNotFound());
    }
}
