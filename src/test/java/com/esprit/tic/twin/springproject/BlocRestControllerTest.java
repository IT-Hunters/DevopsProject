package com.esprit.tic.twin.springproject;
import com.esprit.tic.twin.springproject.controllers.BlocRestController;
import com.esprit.tic.twin.springproject.entities.Bloc;
import com.esprit.tic.twin.springproject.services.IBlocService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class BlocRestControllerTest {

    @Mock
    private IBlocService blocService;

    @InjectMocks
    private BlocRestController blocRestController;

    private MockMvc mockMvc;
    private Bloc bloc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(blocRestController).build();
        bloc = new Bloc();
        bloc.setIdBloc(1L);
        bloc.setNomBloc("Bloc A");
        bloc.setCapaciteBloc(100L);
    }

    @Test
    void testRetrieveAllBlocs() throws Exception {
        // Arrange
        Bloc bloc2 = new Bloc();
        bloc2.setIdBloc(2L);
        bloc2.setNomBloc("Bloc B");
        bloc2.setCapaciteBloc(200L);
        List<Bloc> blocs = Arrays.asList(bloc, bloc2);
        when(blocService.retrieveAllBlocs()).thenReturn(blocs);

        // Act & Assert
        mockMvc.perform(get("/bloc/retrieve-all-blocs")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idBloc").value(1L))
                .andExpect(jsonPath("$[0].nomBloc").value("Bloc A"))
                .andExpect(jsonPath("$[0].capaciteBloc").value(100L))
                .andExpect(jsonPath("$[1].idBloc").value(2L))
                .andExpect(jsonPath("$[1].nomBloc").value("Bloc B"))
                .andExpect(jsonPath("$[1].capaciteBloc").value(200L));

        verify(blocService, times(1)).retrieveAllBlocs();
    }

    @Test
    void testAddBloc() throws Exception {
        // Arrange
        when(blocService.addBloc(any(Bloc.class))).thenReturn(bloc);

        String blocJson = "{\"nomBloc\":\"Bloc A\",\"capaciteBloc\":100}";

        // Act & Assert
        mockMvc.perform(post("/bloc/add-bloc")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(blocJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idBloc").value(1L))
                .andExpect(jsonPath("$.nomBloc").value("Bloc A"))
                .andExpect(jsonPath("$.capaciteBloc").value(100L));

        verify(blocService, times(1)).addBloc(any(Bloc.class));
    }
}