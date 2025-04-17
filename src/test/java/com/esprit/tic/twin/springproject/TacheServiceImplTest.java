package com.esprit.tic.twin.springproject;



import com.esprit.tic.twin.springproject.entities.Etudiant;
import com.esprit.tic.twin.springproject.entities.Tache;
import com.esprit.tic.twin.springproject.entities.TypeTache;
import com.esprit.tic.twin.springproject.services.TacheServiceImpl;
import com.esprit.tic.twin.springproject.repositories.EtudiantRepository;
import com.esprit.tic.twin.springproject.repositories.TacheRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TacheServiceImplTest {

    @Mock
    private TacheRepository tacheRepository;

    @Mock
    private EtudiantRepository etudiantRepository;

    @InjectMocks
    private TacheServiceImpl tacheService;

    private Tache tache;
    private Etudiant etudiant;

    @BeforeEach
    void setUp() {
        tache = new Tache();
        tache.setIdTache(1L);
        tache.setTarifHoraire(10.0f);
        tache.setDuree(3);
        tache.setTypeTache(TypeTache.MENAGERE);
        tache.setDateTache(LocalDate.of(2025, 1, 2));

        etudiant = new Etudiant();
        etudiant.setNomEt("Doe");
        etudiant.setPrenomEt("John");
    }

    @Test
    void testAddTache() {
        when(tacheRepository.save(any(Tache.class))).thenReturn(tache);

        Tache result = tacheService.addTache(tache);

        assertNotNull(result);
        assertEquals(1L, result.getIdTache());
        assertEquals(31.5, result.getTarifFinal(), 0.01); // 10 * 3 * 1.05
        verify(tacheRepository).save(tache);
    }

    @Test
    void testUpdateTache_Success() {
        when(tacheRepository.existsById(1L)).thenReturn(true);
        when(tacheRepository.save(any(Tache.class))).thenReturn(tache);

        Tache result = tacheService.updateTache(tache);

        assertNotNull(result);
        assertEquals(31.5, result.getTarifFinal(), 0.01);
        verify(tacheRepository).save(tache);
    }

    // Add test for updateTache failure case
    @Test
    void testUpdateTache_NotFound() {
        when(tacheRepository.existsById(1L)).thenReturn(false);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            tacheService.updateTache(tache);
        });

        assertEquals("Tache not found with id: 1", exception.getMessage());
        verify(tacheRepository, never()).save(any(Tache.class));
    }

    // Test for retrieveTache success case
    @Test
    void testRetrieveTache_Success() {
        when(tacheRepository.findById(1L)).thenReturn(Optional.of(tache));

        Tache result = tacheService.retrieveTache(1L);

        assertNotNull(result);
        assertEquals(1L, result.getIdTache());
        verify(tacheRepository).findById(1L);
    }

    // Test for retrieveTache failure case
    @Test
    void testRetrieveTache_NotFound() {
        when(tacheRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            tacheService.retrieveTache(1L);
        });

        assertEquals("Tache not found with id: 1", exception.getMessage());
        verify(tacheRepository).findById(1L);
    }

    // Test for retrieveAllTaches
    @Test
    void testRetrieveAllTaches() {
        when(tacheRepository.findAll()).thenReturn(List.of(tache));

        List<Tache> result = tacheService.retrieveAllTaches();

        assertEquals(1, result.size());
        assertEquals(tache, result.get(0));
        verify(tacheRepository).findAll();
    }

    // Test for removeTache success case
    @Test
    void testRemoveTache_Success() {
        when(tacheRepository.existsById(1L)).thenReturn(true);

        tacheService.removeTache(1L);

        verify(tacheRepository).deleteById(1L);
    }

    // Test for removeTache failure case
    @Test
    void testRemoveTache_NotFound() {
        when(tacheRepository.existsById(1L)).thenReturn(false);

        tacheService.removeTache(1L);

        verify(tacheRepository, never()).deleteById(1L);
    }

    // Test for addTasksAndAffectToEtudiant success case
    @Test
    void testAddTasksAndAffectToEtudiant_Success() {
        List<Tache> tasks = List.of(tache);
        when(etudiantRepository.findByNomEtAndPrenomEt("Doe", "John")).thenReturn(Optional.of(etudiant));
        when(tacheRepository.saveAll(anyList())).thenReturn(tasks);

        List<Tache> result = tacheService.addTasksAndAffectToEtudiant(tasks, "Doe", "John");

        assertEquals(1, result.size());
        assertEquals(31.5, result.get(0).getTarifFinal(), 0.01);
        assertEquals(etudiant, result.get(0).getEtudiantOrdinaire());
        verify(tacheRepository).saveAll(tasks);
    }

    // Test for addTasksAndAffectToEtudiant failure case
    @Test
    void testAddTasksAndAffectToEtudiant_EtudiantNotFound() {
        List<Tache> tasks = List.of(tache);
        when(etudiantRepository.findByNomEtAndPrenomEt("Doe", "John")).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            tacheService.addTasksAndAffectToEtudiant(tasks, "Doe", "John");
        });

        assertEquals("Etudiant not found with nomEt: Doe and prenomEt: John", exception.getMessage());
        verify(tacheRepository, never()).saveAll(anyList());
    }

    // Test for calculerTarif with MENAGERE type
    @Test
    void testCalculerTarif_Menagere() {
        tache.setDateTache(LocalDate.of(2025, 1, 2)); // Not a holiday
        double tarif = tacheService.calculerTarif(tache);

        assertEquals(31.5, tarif, 0.01); // 10 * 3 * 1.05
    }

    // Test for calculerTarif with JARDINAGE type on a holiday
    @Test
    void testCalculerTarif_Jardinage_Holiday() {
        tache.setTypeTache(TypeTache.JARDINAGE);
        tache.setDateTache(LocalDate.of(2025, 1, 1)); // Holiday

        double tarif = tacheService.calculerTarif(tache);

        assertEquals(39.6, tarif, 0.01); // 10 * 3 * 1.10 * 1.2
    }

    // Test for calculerTarif with BRICOLAGE type and long duration
    @Test
    void testCalculerTarif_Bricolage_LongDuration() {
        tache.setTypeTache(TypeTache.BRICOLAGE);
        tache.setDuree(6); // > 5 hours
        tache.setDateTache(LocalDate.of(2025, 1, 2));

        double tarif = tacheService.calculerTarif(tache);

        assertEquals(62.1, tarif, 0.01); // 10 * 6 * 1.15 * 0.9
    }

    // Test for calculerTarif with min/max bounds
    @Test
    void testCalculerTarif_MinMaxBounds() {
        tache.setTarifHoraire(1000.0f); // Force exceeding max
        tache.setDuree(1);
        tache.setTypeTache(TypeTache.MENAGERE);

        double tarif = tacheService.calculerTarif(tache);

        assertEquals(500.0, tarif, 0.01); // Should cap at 500

        tache.setTarifHoraire(1.0f); // Force below min
        double tarifMin = tacheService.calculerTarif(tache);

        assertEquals(10.0, tarifMin, 0.01); // Should floor at 10
    }
}
