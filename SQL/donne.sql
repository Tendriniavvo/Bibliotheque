INSERT INTO Profils_Adherent (
    nom_profil, quota_emprunts_simultanes , quota_jours_pret , quota_reservation_livre , quota_prolongement_pret , jours_penalite
) VALUES
    ('Etudiant', 2, 7, 1, 3, 10),
    ('Enseigant', 3, 9, 2, 5, 9),
    ('Professeur', 4, 12, 3, 7, 8);



INSERT INTO Utilisateurs (email, mot_de_passe_hash)
VALUES ('ETU001@gmail.com', 'ETU001'),
        ('ETU002@gmail.com', 'ETU002'),
        ('ETU003@gmail.com', 'ETU003'),
        ('ENS001@gmail.com', 'ENS001'),
        ('ENS002@gmail.com', 'ENS002'),
        ('ENS003@gmail.com', 'ENS003'),
        ('PROF001@gmail.com', 'PROF001'),
        ('PROF002@gmail.com', 'PROF002');



INSERT INTO Adherents (id_utilisateur, nom, prenom, date_naissance, date_inscription, id_profil)
VALUES (1, 'ETU001', 'Amine Bensaïd', '2000-05-12', '2024-01-10', 1),
        (2, 'ETU002', 'Sarah El Khattabi', '2000-05-12', '2024-01-10', 1),
        (3, 'ETU003', 'Youssef Moujahid', '2000-05-12', '2024-01-10', 1),
        (4, 'ENS001', 'Nadia Benali', '2000-05-12', '2024-01-10', 2),
        (5, 'ENS002', 'Karim Haddadi', '2000-05-12', '2024-01-10', 2),
        (6, 'ENS003', 'Salima Touhami', '2000-05-12', '2024-01-10', 2),
        (7, 'PROF001', 'Rachid El Mansouri', '2000-05-12', '2024-01-10', 3),
        (8, 'PROF002', 'Amina Zerouali', '2000-05-12', '2024-01-10', 3);

INSERT INTO Type_emprunts (nom_type) VALUES ('À domicile');
INSERT INTO Type_emprunts (nom_type) VALUES ('Sur place');



INSERT INTO Statuts_Reservation (code_statut) VALUES 
('En attente'),   
('Validee'),     
('Annulee'),    
('Expriree');   


INSERT INTO Statuts_Emprunt (code_statut) VALUES 
('En attente'),   
('Validee'),       
('Annulee'),     
('Expriree'),
('En cours'),
('Rendu'),
('Prolonge');   


INSERT INTO Statuts_Prolongement (code_statut) VALUES 
('En attente'),  
('Validee');


INSERT INTO Auteurs(nom) VALUES ('Victor Hugo');
INSERT INTO Auteurs(nom) VALUES ('Albert Camus');
INSERT INTO Auteurs(nom) VALUES ('J.K. Rowling');



INSERT INTO Categories(nom) VALUES ('Littérature classique');
INSERT INTO Categories(nom) VALUES ('Philosophie');
INSERT INTO Categories(nom) VALUES ('Jeunesse / Fantastique');



INSERT INTO Livres( titre, isbn, id_editeur) VALUES ('Les Misérables', '9782070409189', NULL);
INSERT INTO Livres( titre, isbn, id_editeur) VALUES ( 'L''Étranger', '9782070360022', NULL);
INSERT INTO Livres(titre, isbn, id_editeur) VALUES ( 'Harry Potter à l''école des sorciers', '9782070643026', NULL);


INSERT INTO Exemplaires(id_livre, quantite) VALUES (1, 3);
INSERT INTO Exemplaires(id_livre, quantite) VALUES (2, 2);
INSERT INTO Exemplaires(id_livre, quantite) VALUES (3, 1);


INSERT INTO Livres_Auteurs(id_livre, id_auteur) VALUES (1, 1);
INSERT INTO Livres_Auteurs(id_livre, id_auteur) VALUES (2, 2);
INSERT INTO Livres_Auteurs(id_livre, id_auteur) VALUES (3, 3);



INSERT INTO Livres_Categories(id_livre, id_categorie) VALUES (1, 1);
INSERT INTO Livres_Categories(id_livre, id_categorie) VALUES (2, 2);
INSERT INTO Livres_Categories(id_livre, id_categorie) VALUES (3, 3);


INSERT INTO Jours_Feries (date_ferie, description) VALUES ('2025-07-13', 'Jour férié spécial');
INSERT INTO Jours_Feries (date_ferie, description) VALUES ('2025-07-20', 'Jour férié spécial');
INSERT INTO Jours_Feries (date_ferie, description) VALUES ('2025-07-27', 'Jour férié spécial');
INSERT INTO Jours_Feries (date_ferie, description) VALUES ('2025-08-03', 'Jour férié spécial');
INSERT INTO Jours_Feries (date_ferie, description) VALUES ('2025-08-10', 'Jour férié spécial');
INSERT INTO Jours_Feries (date_ferie, description) VALUES ('2025-08-17', 'Jour férié spécial');
INSERT INTO Jours_Feries (date_ferie, description) VALUES ('2025-07-26', 'Jour férié spécial');
INSERT INTO Jours_Feries (date_ferie, description) VALUES ('2025-07-19', 'Jour férié spécial');

