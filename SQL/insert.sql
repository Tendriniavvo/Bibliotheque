INSERT INTO Profils_Adherent (
    nom_profil, quota_emprunts_simultanes
) VALUES
    ('Étudiant', 3),
    ('Professeur', 6),
    ('Invité', 1),
    ('Chercheur', 10),
    ('Personnel', 4);

INSERT INTO Editeurs (id_editeur,nom) VALUES (1 , 'Clarisse');

INSERT INTO Editeurs (id_editeur,nom) VALUES (2, 'St paul');
-- Livres
INSERT INTO Livres (id_livre, titre, isbn, annee_publication, resume, id_editeur)
VALUES 
    (1, '1984', '978-0451524935', 1949, 'Dystopie sur un régime totalitaire', 1),
    (2, 'Le Petit Prince', '978-0156013987', 1943, 'Conte philosophique et poétique', 2);

-- Auteurs
INSERT INTO Auteurs (id_auteur, nom, prenom)
VALUES 
    (1, 'Orwell', 'George'),
    (2, 'Saint-Exupéry', 'Antoine de');

-- Catégories
INSERT INTO Categories (id_categorie, nom)
VALUES 
    (1, 'Science-Fiction'),
    (2, 'Conte');
-- Orwell est l’auteur de 1984
INSERT INTO Livres_Auteurs (id_livre, id_auteur) VALUES (1, 1);

-- Saint-Exupéry est l’auteur du Petit Prince
INSERT INTO Livres_Auteurs (id_livre, id_auteur) VALUES (2, 2);
-- 1984 appartient à la catégorie Science-Fiction
INSERT INTO Livres_Categories (id_livre, id_categorie) VALUES (1, 1);

-- Le Petit Prince appartient à la catégorie Conte
INSERT INTO Livres_Categories (id_livre, id_categorie) VALUES (2, 2);
-- On a 5 exemplaires du livre "1984"
INSERT INTO Exemplaires (id_livre, quantite) VALUES (1, 5);

-- On a 3 exemplaires du livre "Le Petit Prince"
INSERT INTO Exemplaires (id_livre, quantite) VALUES (2, 3);





INSERT INTO Type_emprunts (nom_type) VALUES ('À domicile');
INSERT INTO Type_emprunts (nom_type) VALUES ('Sur place');



INSERT INTO Emprunts (
    id_exemplaire,
    id_adherent,
    id_type_emprunt,
    date_emprunt,
    date_retour_prevue
)
VALUES (
    1,                      -- id_exemplaire
    1,                      -- id_adherent
    1,                      -- id_type_emprunt
    '2025-07-03 10:00:00',  -- date_emprunt
    '2025-07-10 10:00:00'   -- date_retour_prevue
);




INSERT INTO Statuts_Reservation (code_statut) VALUES 
('En attente'),     -- La réservation est en attente de traitement
('Validée'),        -- La réservation a été validée
('Annulée'),        -- La réservation a été annulée par l'utilisateur ou le personnel
('Exprirée');    -- La réservation a été refusée (par exemple, indisponibilité)


INSERT INTO Statuts_Emprunt (code_statut) VALUES 
('En attente'),     -- La réservation est en attente de traitement
('Validée'),        -- La réservation a été validée
('Annulée'),        -- La réservation a été annulée par l'utilisateur ou le personnel
('Exprirée'),
('En cours');   







