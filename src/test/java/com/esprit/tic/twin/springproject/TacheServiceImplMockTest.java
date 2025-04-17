package com.esprit.tic.twin.springproject;

import com.esprit.tic.twin.springproject.entities.Tache;
import com.esprit.tic.twin.springproject.entities.TypeTache;
import com.esprit.tic.twin.springproject.repositories.TacheRepository;
import com.esprit.tic.twin.springproject.services.TacheServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TacheServiceImplMockTest {  // Renamed for consistency

    @Mock
    private TacheRepository tacheRepository;

    @InjectMocks
    private TacheServiceImpl tacheService;

    @Test
    public void testCalculateTotalTaskCostForStudentWithMockito() {
        System.out.println("Starting Mockito test for calculateTotalTaskCostForStudent");

        // Arrange: Mock the repository
        Long studentId = 1L;

        Tache task1 = new Tache();
        task1.setDuree(4);           // 4 hours
        task1.setTarifHoraire(5.0f); // 5 per hour
        task1.setTypeTache(TypeTache.BRICOLAGE);
        System.out.println("Mocked Task 1: 4 hours * 5 = 20");

        Tache task2 = new Tache();
        task2.setDuree(1);           // 1 hour
        task2.setTarifHoraire(20.0f); // 20 per hour
        task2.setTypeTache(TypeTache.MENAGERE);
        System.out.println("Mocked Task 2: 1 hour * 20 = 20");

        List<Tache> mockedTasks = Arrays.asList(task1, task2);
        when(tacheRepository.findByEtudiantOrdinaireIdEtudiantOrEtudiantResponsableIdEtudiant(studentId, studentId))
                .thenReturn(mockedTasks);

        // Act: Call the method
        float totalCost = tacheService.calculateTotalTaskCostForStudent(studentId);
        System.out.println("Calculated total cost: " + totalCost);

        // Assert: Verify the result (4 * 5 + 1 * 20 = 20 + 20 = 40)
        assertEquals(40.0f, totalCost, 0.01f, "Total cost should be 40 for mocked tasks");
        System.out.println("Mockito test passed: Total cost is 40 as expected");
    }
}