package com.daggerfall.domaine;

import jakarta.persistence.*;

@Entity
@Table(name = "epee")
public class Epee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(name = "longueur_lame_cm", nullable = false)
    private int longueurLameCm;

    @Column(nullable = false)
    private String materiau;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Qualite qualite;

    public Epee() {}

    public Epee(String nom, int longueurLameCm, String materiau, Qualite qualite) {
        this.nom = nom;
        this.longueurLameCm = longueurLameCm;
        this.materiau = materiau;
        this.qualite = qualite;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public int getLongueurLameCm() { return longueurLameCm; }
    public void setLongueurLameCm(int longueurLameCm) { this.longueurLameCm = longueurLameCm; }

    public String getMateriau() { return materiau; }
    public void setMateriau(String materiau) { this.materiau = materiau; }

    public Qualite getQualite() { return qualite; }
    public void setQualite(Qualite qualite) { this.qualite = qualite; }
}
