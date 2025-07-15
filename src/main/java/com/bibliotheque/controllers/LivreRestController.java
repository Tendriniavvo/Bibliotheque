package com.bibliotheque.controllers;

import com.bibliotheque.entities.Livre;
import com.bibliotheque.entities.Exemplaire;
import com.bibliotheque.services.LivreService;
import com.bibliotheque.services.ExemplaireService;
import com.bibliotheque.services.EmpruntService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/livre")
public class LivreRestController {
    @Autowired
    private LivreService livreService;
    @Autowired
    private ExemplaireService exemplaireService;
    @Autowired
    private EmpruntService empruntService;

    @GetMapping("/detail/id={id}")
    public ResponseEntity<?> getLivreDetail(@PathVariable Integer id) {
        Optional<Livre> livreOpt = livreService.findById(id);
        if (livreOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Livre non trouvé"));
        }
        Livre livre = livreOpt.get();
        
        List<Exemplaire> exemplaires = exemplaireService.repository.findByLivreId(id);
        int totalExemplaires = exemplaires.stream().mapToInt(Exemplaire::getQuantite).sum();
        
        long empruntes = 0;
        
        for (Exemplaire ex : exemplaires) {
          
        }
        Map<String, Object> result = new HashMap<>();
        result.put("id", livre.getId());
        result.put("titre", livre.getTitre());
        result.put("isbn", livre.getIsbn());
        result.put("anneePublication", livre.getAnneePublication());
        result.put("resume", livre.getResume());
        result.put("editeur", livre.getEditeur() != null ? livre.getEditeur().getNom() : null);
        result.put("totalExemplaires", totalExemplaires);
        
        result.put("disponibilite", totalExemplaires > 0 ? "Disponible" : "Indisponible");
        return ResponseEntity.ok(result);
    }
}
