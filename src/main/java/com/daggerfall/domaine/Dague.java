package com.daggerfall.domaine;

import jakarta.persistence.*;

@Entity
@Table(name = "dague")
public class Dague {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(name = "longueur_lame_cm", nullable = false)
    private int longueurLameCm;

    @Column(nullable = false)
    private String materiau;

    @Column(nullable = false)
    private boolean empoisonnee;

    public Dague() {}

    public Dague(String nom, int longueurLameCm, String materiau, boolean empoisonnee) {
        this.nom = nom;
        this.longueurLameCm = longueurLameCm;
        this.materiau = materiau;
        this.empoisonnee = empoisonnee;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public int getLongueurLameCm() { return longueurLameCm; }
    public void setLongueurLameCm(int longueurLameCm) { this.longueurLameCm = longueurLameCm; }

    public String getMateriau() { return materiau; }
    public void setMateriau(String materiau) { this.materiau = materiau; }

    public boolean isEmpoisonnee() { return empoisonnee; }
    public void setEmpoisonnee(boolean empoisonnee) { this.empoisonnee = empoisonnee; }
}
