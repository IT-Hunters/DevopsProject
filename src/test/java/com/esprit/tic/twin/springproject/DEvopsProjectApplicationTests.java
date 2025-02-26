package com.esprit.tic.twin.springproject;

import com.esprit.tic.twin.springproject.entities.Chambre;
import com.esprit.tic.twin.springproject.entities.Etudiant;
import com.esprit.tic.twin.springproject.entities.Reservation;
import com.esprit.tic.twin.springproject.entities.TypeChambre;
import com.esprit.tic.twin.springproject.repositories.ChambreRepository;
import com.esprit.tic.twin.springproject.repositories.EtudiantRepository;
import com.esprit.tic.twin.springproject.repositories.ReservationRepository;
import com.esprit.tic.twin.springproject.services.ReservationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.Year;
import java.util.HashSet;
import java.util.Optional;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Year;
import java.util.HashSet;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)


@SpringBootTest
class DEvopsProjectApplicationTests {

	@Test
	void contextLoads() {
	}

	@Mock
	private ChambreRepository chambreRepository;

	@Mock
	private EtudiantRepository etudiantRepository;

	@Mock
	private ReservationRepository reservationRepository;

	@InjectMocks
	private ReservationServiceImpl reservationService;

	private Chambre chambre;
	private Etudiant etudiant;
	private Reservation reservation;

	@BeforeEach
	void setUp() {
		// Initialisation des objets pour les tests
		chambre = new Chambre();
		chambre.setIdChambre(1L);
		chambre.setNumeroChambre(101L);
		chambre.setTypeC(TypeChambre.DOUBLE); // Capacité = 2
		chambre.setReservations(new HashSet<>()); // Initialisation de la collection

		etudiant = new Etudiant();
		etudiant.setIdEtudiant(1L);
		etudiant.setCin(12345678L);
		etudiant.setReservations(new HashSet<>()); // Initialisation de la collection

		reservation = new Reservation();
		reservation.setIdReservation("1");
		reservation.setAnneeUniversitaire(LocalDate.now());
		reservation.setEtudiants(new HashSet<>());
	}

	@Test
	void testAjouterReservationEtAssignerAChambreEtAEtudiant_Success() {
		// Arrange
		when(chambreRepository.chercherParNumero(101L)).thenReturn(Optional.of(chambre)); // Simulation de la recherche de chambre
		when(etudiantRepository.findByCin(12345678L)).thenReturn(Optional.of(etudiant)); // Simulation de la recherche d'étudiant
		when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation); // Simulation de la sauvegarde de réservation

		// Act
		Reservation result = reservationService.ajouterReservationEtAssignerAChambreEtAEtudiant(reservation, 101L, 12345678L);

		// Assert (vérifications avec Mockito)
		verify(chambreRepository, times(1)).chercherParNumero(101L); // Vérification que la méthode a été appelée
		verify(etudiantRepository, times(1)).findByCin(12345678L); // Vérification que la méthode a été appelée
		verify(reservationRepository, times(1)).save(reservation); // Vérification que la méthode a été appelée

		// Vérification de l'interaction supplémentaire avec chambreRepository (si nécessaire)
		verify(chambreRepository, times(1)).save(chambre); // Ajoutez cette ligne si la méthode save est appelée sur chambreRepository

		// Si vous utilisez verifyNoMoreInteractions, assurez-vous que toutes les interactions sont vérifiées
		// verifyNoMoreInteractions(chambreRepository, etudiantRepository, reservationRepository); // Décommentez si nécessaire
	}
}
