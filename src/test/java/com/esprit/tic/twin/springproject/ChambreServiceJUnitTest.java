package com.esprit.tic.twin.springproject;





import com.esprit.tic.twin.springproject.entities.Chambre;
import com.esprit.tic.twin.springproject.entities.Reservation;
import com.esprit.tic.twin.springproject.entities.TypeChambre;
import com.esprit.tic.twin.springproject.repositories.ChambreRepository;
import com.esprit.tic.twin.springproject.services.ChambreServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ChambreServiceJUnitTest {
/*
    @Autowired
    private ChambreRepository chambreRepository;

    private ChambreServiceImpl chambreService;

    @BeforeEach
    public void setUp() {
        chambreService = new ChambreServiceImpl(chambreRepository);
    }

    @Test
    public void testFindAvailableChambresForCapacity() {
        // Arrange : Préparer les données
        Chambre chambre1 = new Chambre();
        chambre1.setNumeroChambre(101L);
        chambre1.setTypeC(TypeChambre.DOUBLE);
        Reservation res1 = new Reservation("R1", LocalDate.now(), true, Set.of());
        chambre1.setReservations(Set.of(res1)); // 1 réservation, 1 place dispo

        Chambre chambre2 = new Chambre();
        chambre2.setNumeroChambre(102L);
        chambre2.setTypeC(TypeChambre.DOUBLE);
        Reservation res2a = new Reservation("R2a", LocalDate.now(), true, Set.of());
        Reservation res2b = new Reservation("R2b", LocalDate.now(), true, Set.of());
        chambre2.setReservations(Set.of(res2a, res2b)); // 2 réservations, 0 place dispo

        Chambre chambre3 = new Chambre();
        chambre3.setNumeroChambre(103L);
        chambre3.setTypeC(TypeChambre.SIMPLE);
        chambre3.setReservations(Set.of()); // 0 réservation, 1 place dispo

        chambreRepository.saveAll(List.of(chambre1, chambre2, chambre3));

        // Act : Appeler la méthode
       // List<Chambre> result = chambreService.findAvailableChambresForCapacity(1, TypeChambre.DOUBLE);

        // Assert : Vérifier les résultats
        //assertEquals(1, result.size(), "Il devrait y avoir une chambre disponible");
       // assertEquals(101L, result.get(0).getNumeroChambre(), "La chambre 101 devrait être retournée");
    }

    @Test
    public void testFindAvailableChambresForCapacityWithNegativeCapacity() {
        // Act & Assert : Vérifier l’exception
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
           // chambreService.findAvailableChambresForCapacity(-1, TypeChambre.SIMPLE);
        });
        assertEquals("La capacité minimale ne peut pas être négative", exception.getMessage());
    }*/

}