package com.esprit.tic.twin.springproject.repositories;

import com.esprit.tic.twin.springproject.entities.Bloc;
import com.esprit.tic.twin.springproject.entities.Chambre;
import com.esprit.tic.twin.springproject.entities.Reservation;
import com.esprit.tic.twin.springproject.entities.TypeChambre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ChambreRepository extends JpaRepository<Chambre, Long> {
    List<Chambre> findByBlocNomBlocAndTypeC(String s, TypeChambre x);
    List<Chambre> findByReservationsEstValide(Boolean x);
    List<Chambre> findChambreByBloc_NomBloc(String nomBlock);
    Optional<Chambre> findByNumeroChambre(Long numeroChambre);
    @Query("SELECT c FROM Chambre c WHERE c.numeroChambre = :numChambre")
    Optional<Chambre> chercherParNumero(@Param("numChambre") Long numChambre);
   /* @Query("SELECT count() FROM Chambre c JOIN Reservation r WHERE r.estValide=true AND c=:ch")
    int countRes(@Param("ch") Chambre ch);*/
    List<Chambre> findByBlocCapaciteBlocGreaterThanAndBlocNomBloc(Long x, String nom);
    List<Chambre> findChambreByBloc_IdBlocAndAndTypeC( long idBloc, TypeChambre type) ;
    List<Chambre> findChambreByBloc(Bloc bloc) ;
    @Query("select c from Chambre as c join c.reservations as r where extract(year from r.anneeUniversitaire)=:year and r.estValide=true")
    List<Chambre> findChambreByReservations_AnneeUniversitaire_Year(@Param("year") int year);
    @Query("SELECT COUNT(c.typeC) from Chambre c WHERE c.typeC=:TypeC")
    Float getNbrTypeC(@Param("TypeC") TypeChambre x) ;
    @Query("SELECT c FROM Chambre c WHERE c.bloc.nomBloc = :nombloc AND c.typeC = :typechambre")
    List<Chambre> retrieveChambres(@Param("nombloc") String s, @Param("typechambre") TypeChambre x);

    @Query("SELECT c FROM Chambre c JOIN c.reservations r WHERE r.estValide = :estValide")
    List<Chambre> retreiveChambresValide(@Param("estValide") Boolean x);

    @Query("SELECT c FROM Chambre c WHERE c.bloc.capaciteBloc > :capaciteBloc AND c.bloc.nomBloc = :nomBloc")
    List<Chambre> retreiveChambres(@Param("capaciteBloc") Long x, @Param("nomBloc") String nom);

}
