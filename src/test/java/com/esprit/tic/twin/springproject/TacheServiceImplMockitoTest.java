package com.esprit.tic.twin.springproject;

import com.esprit.tic.twin.springproject.entities.Etudiant;
import com.esprit.tic.twin.springproject.entities.Tache;
import com.esprit.tic.twin.springproject.entities.TypeTache;
import com.esprit.tic.twin.springproject.repositories.EtudiantRepository;
import com.esprit.tic.twin.springproject.repositories.TacheRepository;
import com.esprit.tic.twin.springproject.services.TacheServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TacheServiceImplMockitoTest {

    @InjectMocks
    private TacheServiceImpl tacheService;

    @Mock
    private TacheRepository tacheRepository;

    @Mock
    private EtudiantRepository etudiantRepository;

    @Mock
    private Tache tacheMock;

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
    void testCalculTarif_NonHolidayNoDiscount_Mock() {
        when(tacheMock.getTarifHoraire()).thenReturn(25.0f);
        when(tacheMock.getDuree()).thenReturn(3);
        when(tacheMock.getTypeTache()).thenReturn(TypeTache.JARDINAGE);
        when(tacheMock.getDateTache()).thenReturn(LocalDate.of(2025, 2, 15));

        double expected = 82.5; // 25.0 * 3 * 1.10
        double tariff = tacheService.calculerTarif(tacheMock);
        assertEquals(expected, tariff, 0.01, "Tariff should match expected value for non-holiday, no discount");

        verify(tacheMock, times(1)).getTarifHoraire();
        verify(tacheMock, times(2)).getDuree();
        verify(tacheMock, times(1)).getTypeTache();
        verify(tacheMock, times(1)).getDateTache();
        System.out.println("Test testCalculTarif_NonHolidayNoDiscount_Mock passed successfully!");
    }

    // Test 3: calculerTarif - Holiday, no discount (duration <= 5)
    @Test
    void testCalculTarif_HolidayNoDiscount_Mock() {
        when(tacheMock.getTarifHoraire()).thenReturn(30.0f);
        when(tacheMock.getDuree()).thenReturn(4);
        when(tacheMock.getTypeTache()).thenReturn(TypeTache.MENAGERE);
        when(tacheMock.getDateTache()).thenReturn(LocalDate.of(2025, 3, 20));

        double expected = 151.2; // 30.0 * 4 * 1.05 * 1.2
        double tariff = tacheService.calculerTarif(tacheMock);
        assertEquals(expected, tariff, 0.01, "Tariff should match expected value for holiday, no discount");

        verify(tacheMock, times(1)).getTarifHoraire();
        verify(tacheMock, times(2)).getDuree();
        verify(tacheMock, times(1)).getTypeTache();
        verify(tacheMock, times(1)).getDateTache();
    }

    // Test 4: calculerTarif - Non-holiday, with discount (duration > 5)
    @Test
    void testCalculTarif_NonHolidayWithDiscount_Mock() {
        when(tacheMock.getTarifHoraire()).thenReturn(20.0f);
        when(tacheMock.getDuree()).thenReturn(6);
        when(tacheMock.getTypeTache()).thenReturn(TypeTache.BRICOLAGE);
        when(tacheMock.getDateTache()).thenReturn(LocalDate.of(2025, 6, 15));

        double expected = 124.2; // 20.0 * 6 * 1.15 * 0.9
        double tariff = tacheService.calculerTarif(tacheMock);
        assertEquals(expected, tariff, 0.01, "Tariff should match expected value for non-holiday, with discount");

        verify(tacheMock, times(1)).getTarifHoraire();
        verify(tacheMock, times(2)).getDuree();
        verify(tacheMock, times(1)).getTypeTache();
        verify(tacheMock, times(1)).getDateTache();
    }

    // Test 5: calculerTarif - Tariff below minimum cap (10.0)
    @Test
    void testCalculTarif_BelowMinimumCap_Mock() {
        when(tacheMock.getTarifHoraire()).thenReturn(1.0f);
        when(tacheMock.getDuree()).thenReturn(1);
        when(tacheMock.getTypeTache()).thenReturn(TypeTache.MENAGERE);
        when(tacheMock.getDateTache()).thenReturn(LocalDate.of(2025, 6, 15));

        double expected = 10.0; // 1.0 * 1 * 1.05, capped at 10.0
        double tariff = tacheService.calculerTarif(tacheMock);
        assertEquals(expected, tariff, 0.01, "Tariff should be capped at minimum value of 10.0");

        verify(tacheMock, times(1)).getTarifHoraire();
        verify(tacheMock, times(2)).getDuree();
        verify(tacheMock, times(1)).getTypeTache();
        verify(tacheMock, times(1)).getDateTache();
    }

    // Test 6: calculerTarif - Tariff above maximum cap (500.0)
    @Test
    void testCalculTarif_AboveMaximumCap_Mock() {
        when(tacheMock.getTarifHoraire()).thenReturn(100.0f);
        when(tacheMock.getDuree()).thenReturn(5);
        when(tacheMock.getTypeTache()).thenReturn(TypeTache.BRICOLAGE);
        when(tacheMock.getDateTache()).thenReturn(LocalDate.of(2025, 1, 1));

        double expected = 500.0; // 100.0 * 5 * 1.15 * 1.2, capped at 500.0
        double tariff = tacheService.calculerTarif(tacheMock);
        assertEquals(expected, tariff, 0.01, "Tariff should be capped at maximum value of 500.0");

        verify(tacheMock, times(1)).getTarifHoraire();
        verify(tacheMock, times(2)).getDuree();
        verify(tacheMock, times(1)).getTypeTache();
        verify(tacheMock, times(1)).getDateTache();
    }

    // Test 7: addTache
    @Test
    void testAddTache() {
        // Arrange
        Tache tache = new Tache(null, LocalDate.of(2025, 1, 1), 6, 20.0f, TypeTache.BRICOLAGE, null, null, 0.0);
        Tache savedTache = new Tache(1L, LocalDate.of(2025, 1, 1), 6, 20.0f, TypeTache.BRICOLAGE, null, null, 0.0);
        savedTache.setTarifFinal(149.04); // 20.0 * 6 * 1.15 * 1.2 * 0.9

        when(tacheRepository.save(any(Tache.class))).thenReturn(savedTache);

        // Act
        Tache result = tacheService.addTache(tache);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getIdTache());
        assertEquals(149.04, result.getTarifFinal(), 0.01);
        verify(tacheRepository, times(1)).save(any(Tache.class));
        System.out.println("Test testAddTache passed successfully!");
    }

    // Test 8: updateTache - Success
    @Test
    void testUpdateTache_Success() {
        // Arrange
        Tache tache = new Tache(1L, LocalDate.of(2025, 2, 15), 3, 25.0f, TypeTache.JARDINAGE, null, null, 0.0);
        Tache updatedTache = new Tache(1L, LocalDate.of(2025, 2, 15), 3, 25.0f, TypeTache.JARDINAGE, null, null, 0.0);
        updatedTache.setTarifFinal(82.5); // 25.0 * 3 * 1.10

        when(tacheRepository.existsById(1L)).thenReturn(true);
        when(tacheRepository.save(any(Tache.class))).thenReturn(updatedTache);

        // Act
        Tache result = tacheService.updateTache(tache);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getIdTache());
        assertEquals(82.5, result.getTarifFinal(), 0.01);
        verify(tacheRepository, times(1)).existsById(1L);
        verify(tacheRepository, times(1)).save(any(Tache.class));
    }

    // Test 9: updateTache - Not Found
    @Test
    void testUpdateTache_NotFound() {
        // Arrange
        Tache tache = new Tache(1L, LocalDate.of(2025, 2, 15), 3, 25.0f, TypeTache.JARDINAGE, null, null, 0.0);

        when(tacheRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> tacheService.updateTache(tache));
        assertEquals("Tache not found with id: 1", exception.getMessage());
        verify(tacheRepository, times(1)).existsById(1L);
        verify(tacheRepository, never()).save(any(Tache.class));
    }

    // Test 10: retrieveTache - Success
    @Test
    void testRetrieveTache_Success() {
        // Arrange
        Tache tache = new Tache(1L, LocalDate.of(2025, 1, 1), 6, 20.0f, TypeTache.BRICOLAGE, null, null, 0.0);

        when(tacheRepository.findById(1L)).thenReturn(Optional.of(tache));

        // Act
        Tache result = tacheService.retrieveTache(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getIdTache());
        verify(tacheRepository, times(1)).findById(1L);
    }

    // Test 11: retrieveTache - Not Found
    @Test
    void testRetrieveTache_NotFound() {
        // Arrange
        when(tacheRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> tacheService.retrieveTache(1L));
        assertEquals("Tache not found with id: 1", exception.getMessage());
        verify(tacheRepository, times(1)).findById(1L);
    }

    // Test 12: retrieveAllTaches
    @Test
    void testRetrieveAllTaches() {
        // Arrange
        Tache tache1 = new Tache(1L, LocalDate.of(2025, 1, 1), 6, 20.0f, TypeTache.BRICOLAGE, null, null, 0.0);
        Tache tache2 = new Tache(2L, LocalDate.of(2025, 2, 15), 3, 25.0f, TypeTache.JARDINAGE, null, null, 0.0);
        List<Tache> taches = Arrays.asList(tache1, tache2);

        when(tacheRepository.findAll()).thenReturn(taches);

        // Act
        List<Tache> result = tacheService.retrieveAllTaches();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getIdTache());
        assertEquals(2L, result.get(1).getIdTache());
        verify(tacheRepository, times(1)).findAll();
    }

    // Test 13: removeTache - Success
    @Test
    void testRemoveTache_Success() {
        // Arrange
        when(tacheRepository.existsById(1L)).thenReturn(true);

        // Act
        tacheService.removeTache(1L);

        // Assert
        verify(tacheRepository, times(1)).existsById(1L);
        verify(tacheRepository, times(1)).deleteById(1L);
    }

    // Test 14: removeTache - Not Found
    @Test
    void testRemoveTache_NotFound() {
        // Arrange
        when(tacheRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> tacheService.removeTache(1L));
        assertEquals("Tache not found with id: 1", exception.getMessage());
        verify(tacheRepository, times(1)).existsById(1L);
        verify(tacheRepository, never()).deleteById(anyLong());
    }

    // Test 15: addTasksAndAffectToEtudiant - Success
    @Test
    void testAddTasksAndAffectToEtudiant_Success() {
        // Arrange
        Tache tache1 = new Tache(null, LocalDate.of(2025, 1, 1), 6, 20.0f, TypeTache.BRICOLAGE, null, null, 0.0);
        Tache tache2 = new Tache(null, LocalDate.of(2025, 2, 15), 3, 25.0f, TypeTache.JARDINAGE, null, null, 0.0);
        List<Tache> tasks = Arrays.asList(tache1, tache2);

        Etudiant etudiant = new Etudiant();
        etudiant.setIdEtudiant(1L);
        etudiant.setNomEt("Ahmed");
        etudiant.setPrenomEt("Slimi");

        Tache savedTache1 = new Tache(1L, LocalDate.of(2025, 1, 1), 6, 20.0f, TypeTache.BRICOLAGE, etudiant, null, 0.0);
        savedTache1.setTarifFinal(149.04); // 20.0 * 6 * 1.15 * 1.2 * 0.9
        Tache savedTache2 = new Tache(2L, LocalDate.of(2025, 2, 15), 3, 25.0f, TypeTache.JARDINAGE, etudiant, null, 0.0);
        savedTache2.setTarifFinal(82.5); // 25.0 * 3 * 1.10
        List<Tache> savedTasks = Arrays.asList(savedTache1, savedTache2);

        when(etudiantRepository.findByNomEtAndPrenomEt("Ahmed", "Slimi")).thenReturn(Optional.of(etudiant));
        when(tacheRepository.saveAll(anyList())).thenReturn(savedTasks);

        // Act
        List<Tache> result = tacheService.addTasksAndAffectToEtudiant(tasks, "Ahmed", "Slimi");

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getIdTache());
        assertEquals(149.04, result.get(0).getTarifFinal(), 0.01);
        assertEquals(2L, result.get(1).getIdTache());
        assertEquals(82.5, result.get(1).getTarifFinal(), 0.01);
        assertEquals(etudiant, result.get(0).getEtudiantOrdinaire());
        assertEquals(etudiant, result.get(1).getEtudiantOrdinaire());
        verify(etudiantRepository, times(1)).findByNomEtAndPrenomEt("Ahmed", "Slimi");
        verify(tacheRepository, times(1)).saveAll(anyList());
    }

    // Test 16: addTasksAndAffectToEtudiant - Etudiant Not Found
    @Test
    void testAddTasksAndAffectToEtudiant_EtudiantNotFound() {
        // Arrange
        Tache tache1 = new Tache(null, LocalDate.of(2025, 1, 1), 6, 20.0f, TypeTache.BRICOLAGE, null, null, 0.0);
        List<Tache> tasks = Arrays.asList(tache1);

        when(etudiantRepository.findByNomEtAndPrenomEt("Unknown", "User")).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> tacheService.addTasksAndAffectToEtudiant(tasks, "Unknown", "User"));
        assertEquals("Etudiant not found with nomEt: Unknown and prenomEt: User", exception.getMessage());
        verify(etudiantRepository, times(1)).findByNomEtAndPrenomEt("Unknown", "User");
        verify(tacheRepository, never()).saveAll(anyList());
    }
}
