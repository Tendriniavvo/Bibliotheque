INSERT INTO Profils_Adherent (
    nom_profil, quota_emprunts_simultanes
) VALUES
    ('Étudiant', 3),
    ('Professeur', 6),
    ('Invité', 1),
    ('Chercheur', 10),
    ('Personnel', 4);




INSERT INTO Utilisateurs (email, mot_de_passe_hash)
VALUES ('rakoto@gmail.com', 'rakoto123');


INSERT INTO Adherents (
    id_utilisateur, nom, prenom, date_naissance, id_profil
)
VALUES (
    1, 'Rakoto', 'Lala', '2001-05-15', 1
);


