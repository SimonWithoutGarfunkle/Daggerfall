package com.daggerfall.service;

import com.daggerfall.domaine.Dague;
import com.daggerfall.domaine.Epee;
import com.daggerfall.domaine.Qualite;
import com.daggerfall.repository.DagueRepository;
import com.daggerfall.repository.EpeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ForgeronServiceTest {

    @Mock
    private EpeeRepository epeeRepository;

    @Mock
    private DagueRepository dagueRepository;

    @InjectMocks
    private ForgeronService forgeronService;

    private Epee durandal;
    private Dague crocDeVipere;

    @BeforeEach
    void setUp() {
        durandal = new Epee("Durandal", 90, "Acier trempé", Qualite.LEGENDAIRE);
        durandal.setId(1L);

        crocDeVipere = new Dague("Croc de Vipère", 25, "Acier noir", true);
        crocDeVipere.setId(1L);
    }

    // --- Tests épées ---

    @Test
    void listerEpees_retourneToutesLesEpees() {
        when(epeeRepository.findAll()).thenReturn(List.of(durandal));

        List<Epee> epees = forgeronService.listerEpees();

        assertThat(epees).hasSize(1);
        assertThat(epees.get(0).getNom()).isEqualTo("Durandal");
        verify(epeeRepository).findAll();
    }

    @Test
    void trouverEpee_avecIdExistant_retourneLepee() {
        when(epeeRepository.findById(1L)).thenReturn(Optional.of(durandal));

        Optional<Epee> resultat = forgeronService.trouverEpee(1L);

        assertThat(resultat).isPresent();
        assertThat(resultat.get().getNom()).isEqualTo("Durandal");
    }

    @Test
    void trouverEpee_avecIdInexistant_retourneVide() {
        when(epeeRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Epee> resultat = forgeronService.trouverEpee(99L);

        assertThat(resultat).isEmpty();
    }

    @Test
    void listerEpeesByQualite_filtreCorrectement() {
        when(epeeRepository.findByQualite(Qualite.LEGENDAIRE)).thenReturn(List.of(durandal));

        List<Epee> legendaires = forgeronService.listerEpeesByQualite(Qualite.LEGENDAIRE);

        assertThat(legendaires).hasSize(1);
        assertThat(legendaires.get(0).getQualite()).isEqualTo(Qualite.LEGENDAIRE);
    }

    @Test
    void forgerEpee_sauvegardeEtRetourneLepee() {
        Epee nouvelleEpee = new Epee("Claymore", 110, "Fer forgé", Qualite.RARE);
        when(epeeRepository.save(nouvelleEpee)).thenReturn(nouvelleEpee);

        Epee forgee = forgeronService.forgerEpee(nouvelleEpee);

        assertThat(forgee.getNom()).isEqualTo("Claymore");
        verify(epeeRepository).save(nouvelleEpee);
    }

    @Test
    void fondreEpee_appelleDeleteById() {
        forgeronService.fondreEpee(1L);

        verify(epeeRepository).deleteById(1L);
    }

    // --- Tests dagues ---

    @Test
    void listerDagues_retourneToutesLesDagues() {
        when(dagueRepository.findAll()).thenReturn(List.of(crocDeVipere));

        List<Dague> dagues = forgeronService.listerDagues();

        assertThat(dagues).hasSize(1);
        assertThat(dagues.get(0).getNom()).isEqualTo("Croc de Vipère");
        verify(dagueRepository).findAll();
    }

    @Test
    void trouverDague_avecIdExistant_retourneLaDague() {
        when(dagueRepository.findById(1L)).thenReturn(Optional.of(crocDeVipere));

        Optional<Dague> resultat = forgeronService.trouverDague(1L);

        assertThat(resultat).isPresent();
        assertThat(resultat.get().isEmpoisonnee()).isTrue();
    }

    @Test
    void listerDaguesEmpoisonnees_retourneSeulementLesEmpoisonnees() {
        when(dagueRepository.findByEmpoisonnee(true)).thenReturn(List.of(crocDeVipere));

        List<Dague> empoisonnees = forgeronService.listerDaguesEmpoisonnees();

        assertThat(empoisonnees).hasSize(1);
        assertThat(empoisonnees).allMatch(Dague::isEmpoisonnee);
    }

    @Test
    void forgerDague_sauvegardeEtRetourneLaDague() {
        Dague nouvelleDague = new Dague("Stiletto", 20, "Acier", false);
        when(dagueRepository.save(nouvelleDague)).thenReturn(nouvelleDague);

        Dague forgee = forgeronService.forgerDague(nouvelleDague);

        assertThat(forgee.getNom()).isEqualTo("Stiletto");
        verify(dagueRepository).save(nouvelleDague);
    }

    @Test
    void fondreDague_appelleDeleteById() {
        forgeronService.fondreDague(1L);

        verify(dagueRepository).deleteById(1L);
    }
}
