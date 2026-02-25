package com.daggerfall.controller;

import com.daggerfall.domaine.Dague;
import com.daggerfall.domaine.Epee;
import com.daggerfall.domaine.Qualite;
import com.daggerfall.service.ForgeronService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/forge")
public class ForgeronController {

    private final ForgeronService forgeronService;

    public ForgeronController(ForgeronService forgeronService) {
        this.forgeronService = forgeronService;
    }

    // --- Épées ---

    @GetMapping("/epees")
    public List<Epee> listerEpees() {
        return forgeronService.listerEpees();
    }

    @GetMapping("/epees/{id}")
    public ResponseEntity<Epee> trouverEpee(@PathVariable Long id) {
        return forgeronService.trouverEpee(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/epees/qualite/{qualite}")
    public List<Epee> epeesByQualite(@PathVariable Qualite qualite) {
        return forgeronService.listerEpeesByQualite(qualite);
    }

    @PostMapping("/epees")
    @ResponseStatus(HttpStatus.CREATED)
    public Epee forgerEpee(@RequestBody Epee epee) {
        return forgeronService.forgerEpee(epee);
    }

    @DeleteMapping("/epees/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void fondreEpee(@PathVariable Long id) {
        forgeronService.fondreEpee(id);
    }

    // --- Dagues ---

    @GetMapping("/dagues")
    public List<Dague> listerDagues() {
        return forgeronService.listerDagues();
    }

    @GetMapping("/dagues/{id}")
    public ResponseEntity<Dague> trouverDague(@PathVariable Long id) {
        return forgeronService.trouverDague(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/dagues/empoisonnees")
    public List<Dague> daguesEmpoisonnees() {
        return forgeronService.listerDaguesEmpoisonnees();
    }

    @PostMapping("/dagues")
    @ResponseStatus(HttpStatus.CREATED)
    public Dague forgerDague(@RequestBody Dague dague) {
        return forgeronService.forgerDague(dague);
    }

    @DeleteMapping("/dagues/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void fondreDague(@PathVariable Long id) {
        forgeronService.fondreDague(id);
    }
}
