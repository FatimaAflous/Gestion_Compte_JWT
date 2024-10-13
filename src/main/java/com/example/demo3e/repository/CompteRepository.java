package com.example.demo3e.repository;

import com.example.demo3e.entity.Compte;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompteRepository extends JpaRepository<Compte,Integer> {
}
