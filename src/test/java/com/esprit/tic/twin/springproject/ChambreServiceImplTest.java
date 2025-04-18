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
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChambreServiceImplTest {

    @Mock
    private ChambreRepository chambreRepository;

    @InjectMocks
    private ChambreServiceImpl chambreService;




    // Test 2: nbChambreParTypeEtBloc - Empty list
    @Test
    void testNbChambreParTypeEtBloc_EmptyList() {
        TypeChambre type = TypeChambre.SIMPLE;
        long blocId = 1L;

        when(chambreRepository.findChambreByBloc_IdBlocAndTypeC(blocId, type))
                .thenReturn(List.of());

        long result = chambreService.nbChambreParTypeEtBloc(type, blocId);
        assertEquals(0, result);
        verify(chambreRepository).findChambreByBloc_IdBlocAndTypeC(blocId, type);
    }

   
    // Test 4: retrieveAllChambres - Normal case
    @Test
    void testRetrieveAllChambres() {
        List<Chambre> mockChambres = List.of(
                new Chambre(1L, TypeChambre.SIMPLE),
                new Chambre(2L, TypeChambre.DOUBLE)
        );
        when(chambreRepository.findAll()).thenReturn(mockChambres);

        List<Chambre> result = chambreService.retrieveAllChambres();
        assertEquals(2, result.size());
        assertEquals(mockChambres, result);
        verify(chambreRepository).findAll();
    }
    // Test 14: retrieveChambre - Not found
    @Test
    void testRetrieveChambre_NotFound() {
        Long idChambre = 1L;
        when(chambreRepository.findById(idChambre)).thenReturn(Optional.empty());

        Chambre result = chambreService.retrieveChambre(idChambre);
        assertNull(result);
        verify(chambreRepository).findById(idChambre);
    }
    // Test 17: findAvailableChambresForCapacity - Negative capacity
    @Test
    void testFindAvailableChambresForCapacity_NegativeCapacity() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            chambreService.findAvailableChambresForCapacity(-1, TypeChambre.SIMPLE);
        });
        assertEquals("La capacité minimale ne peut pas être négative", exception.getMessage());
        verify(chambreRepository, never()).findChambreByTypeC(any(TypeChambre.class));
    }
}
