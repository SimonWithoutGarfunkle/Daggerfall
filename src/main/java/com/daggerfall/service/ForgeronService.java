package com.daggerfall.service;

import com.daggerfall.domaine.Dague;
import com.daggerfall.domaine.Epee;
import com.daggerfall.domaine.Qualite;
import com.daggerfall.repository.DagueRepository;
import com.daggerfall.repository.EpeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ForgeronService {

    private final EpeeRepository epeeRepository;
    private final DagueRepository dagueRepository;

    public ForgeronService(EpeeRepository epeeRepository, DagueRepository dagueRepository) {
        this.epeeRepository = epeeRepository;
        this.dagueRepository = dagueRepository;
    }

    // --- Épées ---

    public List<Epee> listerEpees() {
        return epeeRepository.findAll();
    }

    public Optional<Epee> trouverEpee(Long id) {
        return epeeRepository.findById(id);
    }

    public List<Epee> listerEpeesByQualite(Qualite qualite) {
        return epeeRepository.findByQualite(qualite);
    }

    public Epee forgerEpee(Epee epee) {
        return epeeRepository.save(epee);
    }

    public void fondreEpee(Long id) {
        epeeRepository.deleteById(id);
    }

    // --- Dagues ---

    public List<Dague> listerDagues() {
        return dagueRepository.findAll();
    }

    public Optional<Dague> trouverDague(Long id) {
        return dagueRepository.findById(id);
    }

    public List<Dague> listerDaguesEmpoisonnees() {
        return dagueRepository.findByEmpoisonnee(true);
    }

    public Dague forgerDague(Dague dague) {
        return dagueRepository.save(dague);
    }

    public void fondreDague(Long id) {
        dagueRepository.deleteById(id);
    }
}
