package com.daggerfall.repository;

import com.daggerfall.domaine.Dague;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DagueRepository extends JpaRepository<Dague, Long> {

    List<Dague> findByEmpoisonnee(boolean empoisonnee);

    List<Dague> findByMateriau(String materiau);
}
