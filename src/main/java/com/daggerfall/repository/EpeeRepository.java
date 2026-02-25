package com.daggerfall.repository;

import com.daggerfall.domaine.Epee;
import com.daggerfall.domaine.Qualite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EpeeRepository extends JpaRepository<Epee, Long> {

    List<Epee> findByQualite(Qualite qualite);

    List<Epee> findByMateriau(String materiau);
}
