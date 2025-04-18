// Importation des classes nécessaires pour les tests
package com.esprit.tic.twin.springproject;

import com.esprit.tic.twin.springproject.entities.Chambre;
import com.esprit.tic.twin.springproject.entities.TypeChambre;
import com.esprit.tic.twin.springproject.repositories.ChambreRepository;
import com.esprit.tic.twin.springproject.services.ChambreServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*; // Assertions pour les tests
import static org.mockito.Mockito.*;             // Méthodes de Mockito (when, verify, etc.)

// Extension JUnit pour permettre l’utilisation de Mockito
@ExtendWith(MockitoExtension.class)
class ChambreServiceImplTest {

    // Création d’un faux repository (mock)
    @Mock
    private ChambreRepository chambreRepository;

    // Injection du mock dans l’implémentation du service
    @InjectMocks
    private ChambreServiceImpl chambreService;

    // Test 1 : vérifier que si la liste est vide, le service retourne 0
    @Test
    void testNbChambreParTypeEtBloc_EmptyList() {
        TypeChambre type = TypeChambre.SIMPLE;
        long blocId = 1L;

        // Simuler un appel à findChambreByBloc_IdBlocAndTypeC qui retourne une liste vide
        when(chambreRepository.findChambreByBloc_IdBlocAndTypeC(blocId, type))
                .thenReturn(List.of());

        // Appeler la méthode du service
        long result = chambreService.nbChambreParTypeEtBloc(type, blocId);

        // Vérifier que le résultat est 0
        assertEquals(0, result);

        // Vérifier que la méthode du repository a bien été appelée
        verify(chambreRepository).findChambreByBloc_IdBlocAndTypeC(blocId, type);
    }

    // Test 2 : récupérer les chambres par nom du bloc
    @Test
    void testGetChambresParNomBloc() {
        String nomBloc = "Bloc A";

        // Créer une liste simulée de chambres
        List<Chambre> mockChambres = List.of(
                new Chambre(1L, TypeChambre.SIMPLE),
                new Chambre(2L, TypeChambre.DOUBLE)
        );

        // Simuler le retour du repository
        when(chambreRepository.findChambreByBloc_NomBloc(nomBloc)).thenReturn(mockChambres);

        // Appeler la méthode du service
        List<Chambre> result = chambreService.getChambresParNomBloc(nomBloc);

        // Vérifier que la taille de la liste est correcte
        assertEquals(2, result.size());

        // Vérifier que le contenu est égal à la liste simulée
        assertEquals(mockChambres, result);

        // Vérifier l’appel au repository
        verify(chambreRepository).findChambreByBloc_NomBloc(nomBloc);
    }

    // Test 3 : récupérer toutes les chambres
    @Test
    void testRetrieveAllChambres() {
        // Créer une liste simulée de chambres
        List<Chambre> mockChambres = List.of(
                new Chambre(1L, TypeChambre.SIMPLE),
                new Chambre(2L, TypeChambre.DOUBLE)
        );

        // Simuler le retour de la méthode findAll
        when(chambreRepository.findAll()).thenReturn(mockChambres);

        // Appeler la méthode du service
        List<Chambre> result = chambreService.retrieveAllChambres();

        // Vérifier que le résultat est correct
        assertEquals(2, result.size());
        assertEquals(mockChambres, result);

        // Vérifier que findAll a été appelé
        verify(chambreRepository).findAll();
    }

    // Test 4 : récupérer une chambre qui n’existe pas
    @Test
    void testRetrieveChambre_NotFound() {
        Long idChambre = 1L;

        // Simuler un retour vide (chambre non trouvée)
        when(chambreRepository.findById(idChambre)).thenReturn(Optional.empty());

        // Appeler la méthode du service
        Chambre result = chambreService.retrieveChambre(idChambre);

        // Vérifier que le résultat est null
        assertNull(result);

        // Vérifier que findById a bien été appelé
        verify(chambreRepository).findById(idChambre);
    }

    // Test 5 : capacité négative → déclenchement d’une exception
    @Test
    void testFindAvailableChambresForCapacity_NegativeCapacity() {
        // Vérifie qu'une exception est levée si la capacité est négative
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            chambreService.findAvailableChambresForCapacity(-1, TypeChambre.SIMPLE);
        });

        // Vérifie que le message d'erreur est correct
        assertEquals("La capacité minimale ne peut pas être négative", exception.getMessage());

        // Vérifie que le repository n'a jamais été appelé
        verify(chambreRepository, never()).findChambreByTypeC(any(TypeChambre.class));
    }
}
