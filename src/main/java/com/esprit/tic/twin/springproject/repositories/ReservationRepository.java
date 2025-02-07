package com.esprit.tic.twin.springproject.repositories;

import com.esprit.tic.twin.springproject.entities.Chambre;
import com.esprit.tic.twin.springproject.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, String> {
    List<Reservation> findByAnneeUniversitaireBetween(Date dateDebut, Date dateFin );

}
