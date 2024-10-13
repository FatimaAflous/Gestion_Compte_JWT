package com.example.demo3e.service;

import com.example.demo3e.entity.Compte;
import com.example.demo3e.repository.CompteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.nio.channels.FileChannel;

@Service
public class CompteService {
    @Autowired
    CompteRepository compteRepository;

    //creer un compte
    public Compte creer_compte(Compte compte){
        return compteRepository.save(compte);
    }

    //supprimer un compte
    public void delete_compte(int id){
         compteRepository.deleteById(id);
    }

    //modifier un compte
    public Compte update_compte(int id , Compte nouveau_compte){
        Compte lookFor_compte = compteRepository.findById(id).orElseThrow(()->new RuntimeException("compte not found"));
        lookFor_compte.setNom(nouveau_compte.getNom());
        lookFor_compte.setTel(nouveau_compte.getTel());
        lookFor_compte.setSolde(nouveau_compte.getSolde());
        return compteRepository.save(lookFor_compte);
    }

    //recuperer tous les comptes
    public List<Compte> consulter_tout_comptes(){
        return compteRepository.findAll();
    }

    //recuperer un compte par son id
    public Compte consulter_compte(int id){
        return compteRepository.findById(id).orElse(null);
    }

    //crediter un compte
public Compte crediter_compte(int id , Long montant){
    Compte lookFor_compte = compteRepository.findById(id).orElseThrow(()->new RuntimeException("compte not found"));
    lookFor_compte.setSolde(lookFor_compte.getSolde()+montant);
    return compteRepository.save(lookFor_compte);
}

   //debiter un compte
public Compte debiter_compte(int id , Long montant){
    Compte lookFor_compte = compteRepository.findById(id).orElseThrow(()->new RuntimeException("compte not found"));
    if (lookFor_compte.getSolde()<montant){
        throw new RuntimeException("solde insufisant ") ;
    }
    lookFor_compte.setSolde(lookFor_compte.getSolde() - montant);
    return compteRepository.save(lookFor_compte);
}

}
