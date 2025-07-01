INSERT INTO Profils_Adherent (
    nom_profil, quota_emprunts_simultanes, duree_pret_domicile_jours,
    duree_pret_sur_place_heures, peut_prolonger_pret, jours_penalite_par_retard
) VALUES
    ('Étudiant', 3, 21, 3, TRUE, 1),
    ('Professeur', 6, 30, 4, TRUE, 1),
    ('Invité', 1, 7, 2, FALSE, 2),
    ('Chercheur', 10, 60, 6, TRUE, 1),
    ('Personnel', 4, 15, 3, TRUE, 1);




INSERT INTO Utilisateurs (email, mot_de_passe_hash)
VALUES ('rakoto@gmail.com', 'rakoto123');


INSERT INTO Adherents (
    id_utilisateur, nom, prenom, date_naissance, id_profil
)
VALUES (
    1, 'Rakoto', 'Lala', '2001-05-15', 1
);


