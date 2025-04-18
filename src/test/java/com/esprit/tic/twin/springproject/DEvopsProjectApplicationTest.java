package com.esprit.tic.twin.springproject;

import com.esprit.tic.twin.springproject.entities.Chambre;
import com.esprit.tic.twin.springproject.entities.Etudiant;
import com.esprit.tic.twin.springproject.entities.Reservation;
import com.esprit.tic.twin.springproject.entities.TypeChambre;
import com.esprit.tic.twin.springproject.repositories.ChambreRepository;
import com.esprit.tic.twin.springproject.repositories.EtudiantRepository;
import com.esprit.tic.twin.springproject.repositories.ReservationRepository;
import com.esprit.tic.twin.springproject.services.ChambreServiceImpl;
import com.esprit.tic.twin.springproject.services.ReservationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DEvopsProjectApplicationTest {

	@Mock
	private ChambreRepository chambreRepository;

	@InjectMocks
	private ChambreServiceImpl chambreService;

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
		chambre = new Chambre();
		chambre.setIdChambre(1L);
		chambre.setNumeroChambre(101L);
		chambre.setTypeC(TypeChambre.DOUBLE);
		chambre.setReservations(new HashSet<>());

		etudiant = new Etudiant();
		etudiant.setIdEtudiant(1L);
		etudiant.setCin(12345678L);
		etudiant.setReservations(new HashSet<>());

		reservation = new Reservation();
		reservation.setIdReservation("1");
		reservation.setAnneeUniversitaire(LocalDate.now());
		reservation.setEtudiants(new HashSet<>());
	}

	@Test
	void testAjouterReservationEtAssignerAChambreEtAEtudiant_Success() {
		when(chambreRepository.chercherParNumero(101L)).thenReturn(Optional.of(chambre));
		when(etudiantRepository.findByCin(12345678L)).thenReturn(Optional.of(etudiant));
		when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

		Reservation result = reservationService.ajouterReservationEtAssignerAChambreEtAEtudiant(reservation, 101L, 12345678L);

		System.out.println("Test AjouterReservationEtAssignerAChambreEtAEtudiant Succès");
		System.out.println("Résultat: " + result);

		verify(chambreRepository, times(1)).chercherParNumero(101L);
		verify(etudiantRepository, times(1)).findByCin(12345678L);
		verify(reservationRepository, times(1)).save(reservation);
		verify(chambreRepository, times(1)).save(chambre);
	}

	@Test
	void testAjouterReservationEtAssignerAChambreEtAEtudiant_ChambreNonTrouvee() {
		when(chambreRepository.chercherParNumero(999L)).thenReturn(Optional.empty());

		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			reservationService.ajouterReservationEtAssignerAChambreEtAEtudiant(reservation, 999L, 12345678L);
		});

		assertEquals("Chambre non trouvée avec le numéro : 999", exception.getMessage());
		verify(chambreRepository, times(1)).chercherParNumero(999L);
		verify(etudiantRepository, never()).findByCin(anyLong());
	}

	@Test
	void testAjouterReservationEtAssignerAChambreEtAEtudiant_EtudiantNonTrouve() {
		when(chambreRepository.chercherParNumero(101L)).thenReturn(Optional.of(chambre));
		when(etudiantRepository.findByCin(99999999L)).thenReturn(Optional.empty());

		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			reservationService.ajouterReservationEtAssignerAChambreEtAEtudiant(reservation, 101L, 99999999L);
		});

		assertEquals("Étudiant non trouvé avec le CIN : 99999999", exception.getMessage());
		verify(etudiantRepository, times(1)).findByCin(99999999L);
	}

	@Test
	void testGetReservationParAnneeUniversitaire() {
		Date dateDebut = java.sql.Date.valueOf(LocalDate.of(2022, 10, 1));
		Date dateFin = java.sql.Date.valueOf(LocalDate.of(2023, 9, 30));
		Reservation reservation1 = new Reservation();
		reservation1.setIdReservation("1");
		reservation1.setAnneeUniversitaire(LocalDate.of(2022, 11, 15));
		Reservation reservation2 = new Reservation();
		reservation2.setIdReservation("2");
		reservation2.setAnneeUniversitaire(LocalDate.of(2023, 6, 20));
		Reservation reservation3 = new Reservation();
		reservation3.setIdReservation("3");
		reservation3.setAnneeUniversitaire(LocalDate.of(2024, 2, 10));
		when(reservationRepository.findByAnneeUniversitaireBetween(dateDebut, dateFin))
				.thenReturn(Arrays.asList(reservation1, reservation2));

		List<Reservation> result = reservationService.getReservationParAnneeUniversitaire(dateDebut, dateFin);

		System.out.println("Test GetReservationParAnneeUniversitaire Succès");
		System.out.println("Nombre de réservations trouvées: " + result.size());
		result.forEach(r -> System.out.println("Réservation: " + r.getIdReservation() + " Date: " + r.getAnneeUniversitaire()));
		assertEquals(2, result.size());
		assertTrue(result.contains(reservation1));
		assertTrue(result.contains(reservation2));
		assertFalse(result.contains(reservation3));

		verify(reservationRepository, times(1)).findByAnneeUniversitaireBetween(dateDebut, dateFin);
	}

	@Test
	void testPourcentageChambreParTypeChambre() {
		List<Chambre> chambres = Arrays.asList(
				new Chambre(1L, TypeChambre.SIMPLE),
				new Chambre(2L, TypeChambre.SIMPLE),
				new Chambre(3L, TypeChambre.DOUBLE),
				new Chambre(4L, TypeChambre.TRIPLE)
		);
		when(chambreRepository.findAll()).thenReturn(chambres);

		Map<TypeChambre, Double> result = chambreService.pourcentageChambreParTypeChambre();

		Map<TypeChambre, Double> expected = new EnumMap<>(TypeChambre.class);
		expected.put(TypeChambre.SIMPLE, 50.0);
		expected.put(TypeChambre.DOUBLE, 25.0);
		expected.put(TypeChambre.TRIPLE, 25.0);

		assertEquals(expected, result);
	}

	@Test
	void testPourcentageChambreParTypeChambre_AucuneChambre() {
		when(chambreRepository.findAll()).thenReturn(List.of());

		Map<TypeChambre, Double> result = chambreService.pourcentageChambreParTypeChambre();

		assertTrue(result.isEmpty());
	}
}