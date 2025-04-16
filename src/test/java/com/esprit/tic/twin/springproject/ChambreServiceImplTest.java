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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChambreServiceImplTest {

    @Mock
    private ChambreRepository chambreRepository;

    @InjectMocks
    private ChambreServiceImpl chambreService;

    // ✅ Test 1 : nbChambreParTypeEtBloc
    @Test
     void testNbChambreParTypeEtBloc() {
        TypeChambre type = TypeChambre.SIMPLE;
        long blocId = 1L;

        List<Chambre> mockChambres = List.of(
                new Chambre(1L, TypeChambre.SIMPLE),
                new Chambre(2L, TypeChambre.SIMPLE)
        );

        when(chambreRepository.findChambreByBloc_IdBlocAndTypeC(blocId, type))
                .thenReturn(mockChambres);

        long result = chambreService.nbChambreParTypeEtBloc(type, blocId);
        assertEquals(2, result);
    }

    // ✅ Test 2 : addChambre
    @Test
     void testAddChambre() {
        Chambre chambre = new Chambre();
        chambre.setTypeC(TypeChambre.DOUBLE);

        when(chambreRepository.save(chambre)).thenReturn(chambre);

        Chambre saved = chambreService.addChambre(chambre);
        assertEquals(TypeChambre.DOUBLE, saved.getTypeC());
    }

    // ✅ Test 3 : pourcentageChambreParTypeChambre
    @Test
 void testPourcentageChambreParTypeChambre() {
        List<Chambre> allChambres = List.of(
                new Chambre(1L, TypeChambre.SIMPLE),
                new Chambre(2L, TypeChambre.SIMPLE),
                new Chambre(3L, TypeChambre.TRIPLE),
                new Chambre(4L, TypeChambre.DOUBLE)
        );

        when(chambreRepository.findAll()).thenReturn(allChambres);
        when(chambreRepository.getNbrTypeC(TypeChambre.SIMPLE)).thenReturn(2f);
        when(chambreRepository.getNbrTypeC(TypeChambre.DOUBLE)).thenReturn(1f);
        when(chambreRepository.getNbrTypeC(TypeChambre.TRIPLE)).thenReturn(1f);

        Map<String, Float> result = chambreService.pourcentageChambreParTypeChambre();

        assertEquals(50f, result.get("SIMPLE"));
        assertEquals(25f, result.get("DOUBLE"));
        assertEquals(25f, result.get("TRIPLE"));
    }
}