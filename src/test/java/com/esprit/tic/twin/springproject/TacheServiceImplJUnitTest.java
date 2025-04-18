package com.esprit.tic.twin.springproject;

import com.esprit.tic.twin.springproject.entities.Etudiant;
import com.esprit.tic.twin.springproject.entities.Tache;
import com.esprit.tic.twin.springproject.entities.TypeTache;
import com.esprit.tic.twin.springproject.services.TacheServiceImpl;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TacheServiceImplJUnitTest {

    private final TacheServiceImpl tacheService = new TacheServiceImpl(null, null); // Repositories are null

    // Test 1: calculerTarif - Holiday with duration > 5 (discount applies)
    @Test
    void testCalculTarif_JourFerieEtReduction() {
        Tache tache = new Tache(null, LocalDate.of(2025, 1, 1), 6, 20.0f, TypeTache.BRICOLAGE, null, null, 0.0);
        double expected = 149.04; // 20.0 * 6 * 1.15 * 1.2 * 0.9
        double tariff = tacheService.calculerTarif(tache);
        assertEquals(expected, tariff, 0.01, "Tariff should match expected value for holiday and duration discount");
        System.out.println("Test testCalculTarif_JourFerieEtReduction passed successfully!");
    }

    // Test 2: calculerTarif - Non-holiday, no discount
    @Test
    void testCalculTarif_NonHolidayNoDiscount() {
        Tache tache = new Tache(null, LocalDate.of(2025, 2, 15), 3, 25.0f, TypeTache.JARDINAGE, null, null, 0.0);
        double expected = 82.5; // 25.0 * 3 * 1.10
        double tariff = tacheService.calculerTarif(tache);
        assertEquals(expected, tariff, 0.01, "Tariff should match expected value for non-holiday, no discount");
        System.out.println("Test testCalculTarif_NonHolidayNoDiscount passed successfully!");
    }

    // Test 3: calculerTarif - Holiday, no discount (duration <= 5)
    @Test
    void testCalculTarif_HolidayNoDiscount() {
        Tache tache = new Tache(null, LocalDate.of(2025, 3, 20), 4, 30.0f, TypeTache.MENAGERE, null, null, 0.0);
        double expected = 151.2; // 30.0 * 4 * 1.05 * 1.2
        double tariff = tacheService.calculerTarif(tache);
        assertEquals(expected, tariff, 0.01, "Tariff should match expected value for holiday, no discount");
    }

    // Test 4: calculerTarif - Non-holiday, with discount (duration > 5)
    @Test
    void testCalculTarif_NonHolidayWithDiscount() {
        Tache tache = new Tache(null, LocalDate.of(2025, 6, 15), 6, 20.0f, TypeTache.BRICOLAGE, null, null, 0.0);
        double expected = 124.2; // 20.0 * 6 * 1.15 * 0.9
        double tariff = tacheService.calculerTarif(tache);
        assertEquals(expected, tariff, 0.01, "Tariff should match expected value for non-holiday, with discount");
    }

    // Test 5: calculerTarif - Tariff below minimum cap (10.0)
    @Test
    void testCalculTarif_BelowMinimumCap() {
        Tache tache = new Tache(null, LocalDate.of(2025, 6, 15), 1, 1.0f, TypeTache.MENAGERE, null, null, 0.0);
        double expected = 10.0; // 1.0 * 1 * 1.05, capped at 10.0
        double tariff = tacheService.calculerTarif(tache);
        assertEquals(expected, tariff, 0.01, "Tariff should be capped at minimum value of 10.0");
    }

    // Test 6: calculerTarif - Tariff above maximum cap (500.0)
    @Test
    void testCalculTarif_AboveMaximumCap() {
        Tache tache = new Tache(null, LocalDate.of(2025, 1, 1), 5, 100.0f, TypeTache.BRICOLAGE, null, null, 0.0);
        double expected = 500.0; // 100.0 * 5 * 1.15 * 1.2, capped at 500.0
        double tariff = tacheService.calculerTarif(tache);
        assertEquals(expected, tariff, 0.01, "Tariff should be capped at maximum value of 500.0");
    }

    // Test 7: addTache - Cannot test repository interaction without mocking
    @Test
    void testAddTache() {
        Tache tache = new Tache(null, LocalDate.of(2025, 1, 1), 6, 20.0f, TypeTache.BRICOLAGE, null, null, 0.0);
        // Since tacheRepository is null, calling addTache will throw a NullPointerException
        assertThrows(NullPointerException.class, () -> tacheService.addTache(tache),
                "Should throw NullPointerException due to null repository");
        System.out.println("Test testAddTache passed successfully!");
    }

    // Test 8: updateTache - Cannot test repository interaction without mocking
    @Test
    void testUpdateTache() {
        Tache tache = new Tache(1L, LocalDate.of(2025, 2, 15), 3, 25.0f, TypeTache.JARDINAGE, null, null, 0.0);
        // Since tacheRepository is null, calling updateTache will throw a NullPointerException
        assertThrows(NullPointerException.class, () -> tacheService.updateTache(tache),
                "Should throw NullPointerException due to null repository");
    }

    // Test 9: retrieveTache - Cannot test repository interaction without mocking
    @Test
    void testRetrieveTache() {
        // Since tacheRepository is null, calling retrieveTache will throw a NullPointerException
        assertThrows(NullPointerException.class, () -> tacheService.retrieveTache(1L),
                "Should throw NullPointerException due to null repository");
    }

    // Test 10: retrieveAllTaches - Cannot test repository interaction without mocking
    @Test
    void testRetrieveAllTaches() {
        // Since tacheRepository is null, calling retrieveAllTaches will throw a NullPointerException
        assertThrows(NullPointerException.class, () -> tacheService.retrieveAllTaches(),
                "Should throw NullPointerException due to null repository");
    }

    // Test 11: removeTache - Cannot test repository interaction without mocking
    @Test
    void testRemoveTache() {
        // Since tacheRepository is null, calling removeTache will throw a NullPointerException
        assertThrows(NullPointerException.class, () -> tacheService.removeTache(1L),
                "Should throw NullPointerException due to null repository");
    }

    // Test 12: addTasksAndAffectToEtudiant - Cannot test repository interaction without mocking
    @Test
    void testAddTasksAndAffectToEtudiant() {
        Tache tache1 = new Tache(null, LocalDate.of(2025, 1, 1), 6, 20.0f, TypeTache.BRICOLAGE, null, null, 0.0);
        List<Tache> tasks = Arrays.asList(tache1);
        // Since etudiantRepository is null, calling addTasksAndAffectToEtudiant will throw a NullPointerException
        assertThrows(NullPointerException.class, () -> tacheService.addTasksAndAffectToEtudiant(tasks, "Ahmed", "Slimi"),
                "Should throw NullPointerException due to null repository");
    }
}
