package com.esprit.tic.twin.springproject.services;

import com.esprit.tic.twin.springproject.entities.Etudiant;
import com.esprit.tic.twin.springproject.entities.Reservation;
import com.esprit.tic.twin.springproject.entities.Tache;
import com.esprit.tic.twin.springproject.repositories.EtudiantRepository;
import com.esprit.tic.twin.springproject.repositories.ReservationRepository;
import com.esprit.tic.twin.springproject.repositories.TacheRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EtudiantServiceImpl implements IEtudiantService{
    EtudiantRepository etudiantRepository;
    ReservationRepository reservationRepository;
    TacheRepository tacheRepository;
    @Override
    public HashMap<String,Float> calculNouveauMontantInscriptionDesEtudiants(){
        List<Etudiant> listEtuds = etudiantRepository.findAll();
        List<Tache> listTaches = tacheRepository.findAll();
        return listEtuds.stream()
                .collect(Collectors.toMap(
                etud -> etud.getNomEt(),
                        etud -> etud.getMontantInscription() - listTaches.stream()
                                .filter(l -> l.getDateTache().getYear() == Year.now().getValue())
                                .filter(tache -> tache.getEtudiantOrdinaire().equals(etud))
                                .map(l -> l.getDuree() * l.getTarifHoraire())
                                .reduce(0.0F, Float::sum),(old,neww) ->old, HashMap::new));
    }
    @Override
    //@Scheduled(fixedRate = 3000) chaque 3sec
    //@Scheduled(cron="*/60 * * * * *, zone="Europe/Istanbul") place du temps
    //@Scheduled(fixedDelay = 2000)
    //@Scheduled(cron = "* * 9 31 12 *")
    public void updateNouveauMontantInscriptionDesEtudiants(){
        HashMap<String,Float> calcHash = calculNouveauMontantInscriptionDesEtudiants();
        List <Etudiant> liste = etudiantRepository.findAll();
        liste.forEach(
                e-> {
                    calcHash.forEach(
                            (etud,mont) ->{
                                if (etud.equals(e.getNomEt())) e.setMontantInscription(mont);
                            }
                    );
                }
        );
        etudiantRepository.saveAll(liste);
    }
    // Map pour stocker le nombre d'assurances par bénéficiaire (clef : cin, valeur : nombre d'assurances)
   /* private final Map<Integer, Integer> assurances = new ConcurrentHashMap<>();

    // Méthode qui simule l'ajout du nombre d'assurances pour les bénéficiaires
    public void addAssurance(int cin) {
        assurances.merge(cin, 1, Integer::sum);
    }

    // Méthode déclenchée toutes les 60 secondes pour afficher les statistiques
    @Scheduled(fixedRate = 60000)
    public void statistiques() {
        TreeMap<Integer, Integer> sortedAssurances = new TreeMap<>(Collections.reverseOrder()); // TreeMap inversée pour le tri décroissant

        sortedAssurances.putAll(assurances); // Remplir le TreeMap avec les données des assurances

        System.out.println("Statistiques des assurances par bénéficiaire :");

        for (Map.Entry<Integer, Integer> entry : sortedAssurances.entrySet()) {
            System.out.println("Bénéficiaire CIN: " + entry.getKey() + ", Nombre d'assurances: " + entry.getValue());
        }
    }*/
    @Override
    public List<Etudiant> calculTopPerformingStudentsByTaches(int topN) {
        List<Etudiant> listEtudiants = etudiantRepository.findAll();
        List<Tache> listTaches = tacheRepository.findAll();

        return listEtudiants.stream()
                .map(etudiant -> {
                    float totalTaskValue = listTaches.stream()
                            .filter(tache -> tache.getDateTache().getYear() == Year.now().getValue())
                            .filter(tache -> tache.getEtudiantOrdinaire().equals(etudiant))
                            .map(tache -> tache.getDuree() * tache.getTarifHoraire())
                            .reduce(0.0F, Float::sum);
                    etudiant.setMontantInscription(totalTaskValue);
                    return etudiant;
                })
                .sorted((e1, e2) -> Float.compare(e2.getMontantInscription(), e1.getMontantInscription()))
                .limit(topN)
                .collect(Collectors.toList());
    }

    public Etudiant affecterEtudiantAReservation (String nomEt, String prenomEt, String idReservation){
        Etudiant et = etudiantRepository.findEtudiantByNomEtAndPrenomEt(nomEt,prenomEt).get();
        Reservation res = reservationRepository.findById(idReservation).get();
        List <Etudiant> listEt = new ArrayList<>(res.getEtudiants().stream().toList());
        System.out.println("listEt : " + listEt);
        listEt.add(et);
        et.getReservations().add(res);
        System.out.println("listEtAdd : " + listEt);
        res.setEtudiants(listEt.stream().collect(Collectors.toSet()));
        System.out.println("res 2 : " + res);
        etudiantRepository.save(et);
        reservationRepository.save(res);
        return et;
    }
    @Override
    public List<Etudiant> retrieveAllEtudiants() {
        return etudiantRepository.findAll();
    }

    @Override
    public Etudiant addEtudiant(Etudiant e) {
        return etudiantRepository.save(e);
    }

    @Override
    public Etudiant updateEtudiant(Etudiant e) {
        return etudiantRepository.save(e);
    }

    @Override
    public Etudiant retrieveEtudiant(Long idEtudiant) {
        return etudiantRepository.findById(idEtudiant).orElse(null);
    }

    @Override
    public void removeEtudiant(Long idEtudiant) {
        etudiantRepository.deleteById(idEtudiant);
    }
}
