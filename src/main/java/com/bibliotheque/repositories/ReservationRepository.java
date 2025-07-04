package com.bibliotheque.repositories;

import com.bibliotheque.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

   
    public List<Reservation> findByAdherentId(Integer adherentId);

 
    public List<Reservation> findByLivreId(Integer livreId);


    public List<Reservation> findByStatutId(Integer statutId);


    List<Reservation> findByLivreIdAndStatutId(Integer idLivre, Integer idStatut);


    public Optional<Reservation> findById(Integer id);
}