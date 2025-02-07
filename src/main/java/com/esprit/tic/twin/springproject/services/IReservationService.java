package com.esprit.tic.twin.springproject.services;

import com.esprit.tic.twin.springproject.entities.Reservation;

import java.util.Date;
import java.util.List;

public interface IReservationService {
    List<Reservation> retrieveAllReservations();
    Reservation addReservation(Reservation r);
    Reservation updateReservation(Reservation r);
    Reservation retrieveReservation(String idReservation);
    void removeReservation(String idReservation);
    List<Reservation> getReservationParAnneeUniversitaire(Date dateDebut, Date dateFin );
    Reservation ajouterReservationEtAssignerAChambreEtAEtudiant (Reservation res, Long numChambre, long cin);
}
