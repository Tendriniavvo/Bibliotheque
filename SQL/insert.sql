INSERT INTO Profils_Adherent (
    nom_profil, quota_emprunts_simultanes
) VALUES
    ('Étudiant', 3),
    ('Professeur', 6),
    ('Invité', 1),
    ('Chercheur', 10),
    ('Personnel', 4);



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







