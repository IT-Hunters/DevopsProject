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

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ChambreServiceJUnitTest {

    @Autowired
    private ChambreRepository chambreRepository;

    private ChambreServiceImpl chambreService;

    @BeforeEach
    public void setUp() {
        chambreService = new ChambreServiceImpl(chambreRepository);
    }

    @Test
    public void testFindAvailableChambresForCapacity() {
        // Arrange: Prepare test data
        Chambre chambre1 = new Chambre();
        chambre1.setNumeroChambre(101L);
        chambre1.setTypeC(TypeChambre.DOUBLE);
        Reservation res1 = new Reservation();
        res1.setIdReservation("R1");
        res1.setEstValide(true);
        chambre1.setReservations(Set.of(res1)); // 1 reservation, 1 place available

        Chambre chambre2 = new Chambre();
        chambre2.setNumeroChambre(102L);
        chambre2.setTypeC(TypeChambre.DOUBLE);
        Reservation res2a = new Reservation();
        res2a.setIdReservation("R2a");
        res2a.setEstValide(true);
        Reservation res2b = new Reservation();
        res2b.setIdReservation("R2b");
        res2b.setEstValide(true);
        chambre2.setReservations(Set.of(res2a, res2b)); // 2 reservations, 0 places available

        Chambre chambre3 = new Chambre();
        chambre3.setNumeroChambre(103L);
        chambre3.setTypeC(TypeChambre.SIMPLE);
        chambre3.setReservations(Set.of()); // 0 reservations, 1 place available

        chambreRepository.saveAll(List.of(chambre1, chambre2, chambre3));

        // Act: Call the method
        List<Chambre> result = chambreService.findAvailableChambresForCapacity(1, TypeChambre.DOUBLE);

        // Assert: Verify results
        assertEquals(1, result.size(), "There should be one available room");
        assertEquals(101L, result.get(0).getNumeroChambre(), "Room 101 should be returned");
    }

    @Test
    public void testFindAvailableChambresForCapacityWithNegativeCapacity() {
        // Act & Assert: Verify exception
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            chambreService.findAvailableChambresForCapacity(-1, TypeChambre.SIMPLE);
        });
        assertEquals("La capacité minimale ne peut pas être négative", exception.getMessage());
    }
}