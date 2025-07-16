INSERT INTO Profils_Adherent (
    nom_profil, quota_emprunts_simultanes , quota_jours_pret , quota_reservation_livre , quota_prolongement_pret , jours_penalite
) VALUES
    ('Etudiant', 3, 3, 1, 1, 4),
    ('Professeur', 6, 14, 2, 1, 7),
    ('Invite', 1, 14, 2, 1, 7),
    ('Chercheur', 10, 14, 2, 1, 7),
    ('Personnel', 4, 14, 2, 1, 7);



INSERT INTO Type_emprunts (nom_type) VALUES ('À domicile');
INSERT INTO Type_emprunts (nom_type) VALUES ('Sur place');


INSERT INTO Statuts_Reservation (code_statut) VALUES 
('En attente'),     -- La réservation est en attente de traitement
('Validee'),        -- La réservation a été validée
('Annulee'),        -- La réservation a été annulée par l'utilisateur ou le personnel
('Expriree');    -- La réservation a été refusée (par exemple, indisponibilité)


INSERT INTO Statuts_Emprunt (code_statut) VALUES 
('En attente'),     -- La réservation est en attente de traitement
('Validee'),        -- La réservation a été validée
('Annulee'),        -- La réservation a été annulée par l'utilisateur ou le personnel
('Expriree'),
('En cours'),
('Rendu');   







