package com.esprit.tic.twin.springproject.services;

import com.esprit.tic.twin.springproject.entities.Chambre;
import com.esprit.tic.twin.springproject.entities.Etudiant;
import com.esprit.tic.twin.springproject.entities.Reservation;
import com.esprit.tic.twin.springproject.entities.TypeChambre;
import com.esprit.tic.twin.springproject.repositories.ChambreRepository;
import com.esprit.tic.twin.springproject.repositories.EtudiantRepository;
import com.esprit.tic.twin.springproject.repositories.ReservationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
@Slf4j
@Service
@AllArgsConstructor
public class ReservationServiceImpl implements IReservationService {
    ReservationRepository reservationRepository;
    ChambreRepository chambreRepository;
    EtudiantRepository etudiantRepository;
    @Override
    public List<Reservation> getReservationParAnneeUniversitaire(Date dateDebut, Date dateFin ){
        return reservationRepository.findByAnneeUniversitaireBetween(dateDebut,dateFin);
    }

    @Override
    public Reservation ajouterReservationEtAssignerAChambreEtAEtudiant(Reservation res, Long numChambre, long cin) {
        log.info("Début de la méthode ajouterReservationEtAssignerAChambreEtAEtudiant");
        log.info("Numéro de chambre : {}", numChambre);
        log.info("CIN de l'étudiant : {}", cin);

        // Recherche de la chambre
        Chambre ch = chambreRepository.chercherParNumero(numChambre)
                .orElseThrow(() -> new IllegalArgumentException("Chambre non trouvée avec le numéro : " + numChambre));
        log.info("✅ Chambre trouvée : {}", ch);

        // Recherche de l'étudiant
        Etudiant et = etudiantRepository.findByCin(cin)
                .orElseThrow(() -> new IllegalArgumentException("Étudiant non trouvé avec le CIN : " + cin));
        log.info("✅ Étudiant trouvé : {}", et);

        // Vérification de la capacité de la chambre
        int capacite = switch (ch.getTypeC()) {
            case SIMPLE -> 1;
            case DOUBLE -> 2;
            case TRIPLE -> 3;
        };
        log.info("Capacité de la chambre : {}", capacite);
        log.info("Nombre de réservations actuelles : {}", ch.getReservations().size());

        // Vérification de l'année universitaire
        if (res.getAnneeUniversitaire().getYear() != Year.now().getValue()) {
            throw new IllegalArgumentException("Année universitaire incorrecte : " + res.getAnneeUniversitaire().getYear());
        }

        // Vérification de la capacité
        if (ch.getReservations().size() >= capacite) {
            throw new IllegalStateException("La chambre est déjà pleine !");
        }

        // Initialisation de l'attribut etudiants si nécessaire
        if (res.getEtudiants() == null) {
            res.setEtudiants(new HashSet<>());
        }

        // Ajout de l'étudiant à la réservation
        res.getEtudiants().add(et);
        reservationRepository.save(res);

        // Mise à jour des associations bidirectionnelles
        ch.getReservations().add(res);
        chambreRepository.save(ch);

        et.getReservations().add(res);
        etudiantRepository.save(et);

        log.info("✅ Réservation ajoutée avec succès : {}", res);
        return res;
    }

    @Override
    public List<Reservation> retrieveAllReservations() {
        return reservationRepository.findAll();
    }

    @Override
    public Reservation addReservation(Reservation r) {
        return reservationRepository.save(r);
    }

    @Override
    public Reservation updateReservation(Reservation r) {
        return reservationRepository.save(r);
    }

    @Override
    public Reservation retrieveReservation(String idReservation) {
        return null;
    }

    @Override
    public void removeReservation(String idReservation) {

    }


}
