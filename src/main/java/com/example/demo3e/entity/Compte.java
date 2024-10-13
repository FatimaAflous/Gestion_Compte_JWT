package com.example.demo3e.entity;

import jakarta.persistence.*;

@Entity
public class Compte {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id ;
@Column(name = "nom" , length = 10)
    private String nom;
@Column(name="tel" , length = 10)
    private Long tel;
@Column(name="solde" , length = 10)
    private Long solde;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Long getTel() {
        return tel;
    }

    public void setTel(Long tel) {
        this.tel = tel;
    }

    public Long getSolde() {
        return solde;
    }

    public void setSolde(Long solde) {
        this.solde = solde;
    }


}
