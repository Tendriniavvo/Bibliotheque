-- 2. Création des tables de référence

CREATE TABLE Profils_Adherent (
    id_profil SERIAL PRIMARY KEY,
    nom_profil VARCHAR(100) NOT NULL UNIQUE,
    quota_emprunts_simultanes INT NOT NULL DEFAULT 3
);


CREATE TABLE Auteurs (
    id_auteur SERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100)
);

CREATE TABLE Editeurs (
    id_editeur SERIAL PRIMARY KEY,
    nom VARCHAR(150) NOT NULL UNIQUE
);

CREATE TABLE Categories (
    id_categorie SERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE Jours_Feries (
    date_ferie DATE PRIMARY KEY,
    description VARCHAR(255)
);



CREATE TABLE Statuts_Reservation (
    id_statut SERIAL PRIMARY KEY,
    code_statut VARCHAR(20) NOT NULL UNIQUE
);

-- 3. Création des tables principales
CREATE TABLE Livres (
    id_livre SERIAL PRIMARY KEY,
    titre VARCHAR(255) NOT NULL,
    isbn VARCHAR(20) UNIQUE,
    annee_publication INT,
    resume TEXT,
    id_editeur INT,
    FOREIGN KEY (id_editeur) REFERENCES Editeurs(id_editeur)
);

CREATE TABLE Utilisateurs (
    id_utilisateur SERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    mot_de_passe_hash VARCHAR(255) NOT NULL,
    date_creation TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 4. Création des tables liées aux utilisateurs
CREATE TABLE Adherents (
    id_adherent SERIAL PRIMARY KEY,
    id_utilisateur INT UNIQUE,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    date_naissance  DATE NOT NULL,
    date_inscription DATE NOT NULL DEFAULT CURRENT_DATE,
    id_profil INT NOT NULL,
    FOREIGN KEY (id_utilisateur) REFERENCES Utilisateurs(id_utilisateur),
    FOREIGN KEY (id_profil) REFERENCES Profils_Adherent(id_profil)
);

CREATE TABLE Bibliothecaires (
    id_bibliothecaire SERIAL PRIMARY KEY,
    id_utilisateur INT UNIQUE NOT NULL,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    FOREIGN KEY (id_utilisateur) REFERENCES Utilisateurs(id_utilisateur)
);

-- 5. Création des tables de liaison
CREATE TABLE Livres_Auteurs (
    id_livre INT NOT NULL,
    id_auteur INT NOT NULL,
    PRIMARY KEY (id_livre, id_auteur),
    FOREIGN KEY (id_livre) REFERENCES Livres(id_livre) ON DELETE CASCADE,
    FOREIGN KEY (id_auteur) REFERENCES Auteurs(id_auteur) ON DELETE CASCADE
);

CREATE TABLE Livres_Categories (
    id_livre INT NOT NULL,
    id_categorie INT NOT NULL,
    PRIMARY KEY (id_livre, id_categorie),
    FOREIGN KEY (id_livre) REFERENCES Livres(id_livre) ON DELETE CASCADE,
    FOREIGN KEY (id_categorie) REFERENCES Categories(id_categorie) ON DELETE CASCADE
);

-- 6. Création des tables de transactions
CREATE TABLE Exemplaires (
    id_exemplaire SERIAL PRIMARY KEY,
    id_livre INT NOT NULL,
    quantite INT NOT NULL,
    FOREIGN KEY (id_livre) REFERENCES Livres(id_livre) ON DELETE CASCADE
);

CREATE TABLE Abonnements (
    id_abonnement SERIAL PRIMARY KEY,
    id_adherent INT NOT NULL,
    date_debut DATE NOT NULL,
    date_fin DATE NOT NULL,
    FOREIGN KEY (id_adherent) REFERENCES Adherents(id_adherent)
);

CREATE TABLE Droits_Emprunt_Specifiques (
    id_droit SERIAL PRIMARY KEY,
    id_livre INT NOT NULL,
    id_profil INT NOT NULL,
    age INT ,
    emprunt_surplace_autorise BOOLEAN NOT NULL DEFAULT TRUE,
    emprunt_domicile_autorise BOOLEAN NOT NULL DEFAULT TRUE,
    UNIQUE (id_livre, id_profil),
    FOREIGN KEY (id_livre) REFERENCES Livres(id_livre) ON DELETE CASCADE,
    FOREIGN KEY (id_profil) REFERENCES Profils_Adherent(id_profil) ON DELETE CASCADE
);


CREATE TABLE Type_emprunts(
    id_type_emprunt SERIAL PRIMARY KEY,
    nom_type VARCHAR(50)
);

CREATE TABLE Emprunts (
    id_emprunt SERIAL PRIMARY KEY,
    id_exemplaire INT NOT NULL,
    id_adherent INT NOT NULL,
    id_type_emprunt INT NOT NULL,
    date_emprunt TIMESTAMP NOT NULL,
    date_retour_prevue TIMESTAMP NOT NULL,
    FOREIGN KEY (id_type_emprunt) REFERENCES Type_emprunts(id_type_emprunt),
    FOREIGN KEY (id_exemplaire) REFERENCES Exemplaires(id_exemplaire),
    FOREIGN KEY (id_adherent) REFERENCES Adherents(id_adherent)
);


CREATE TABLE Reservations (
    id_reservation SERIAL PRIMARY KEY,
    id_livre INT NOT NULL,
    id_adherent INT NOT NULL,
    id_statut INT NOT NULL DEFAULT 1,
    date_demande TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_expiration DATE NOT NULL,
    FOREIGN KEY (id_livre) REFERENCES Livres(id_livre) ON DELETE CASCADE,
    FOREIGN KEY (id_adherent) REFERENCES Adherents(id_adherent) ON DELETE CASCADE,
    FOREIGN KEY (id_statut) REFERENCES Statuts_Reservation(id_statut)
);

CREATE TABLE Mvt_Reservation (
    id_mvt_reservation SERIAL PRIMARY KEY,
    id_reservation INT NOT NULL,
    id_statut_nouveau INT NOT NULL, -- Le statut vers lequel la réservation a transité
    date_mouvement TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_reservation) REFERENCES Reservations(id_reservation) ON DELETE CASCADE,
    FOREIGN KEY (id_statut_nouveau) REFERENCES Statuts_Reservation(id_statut)
);



CREATE TABLE Penalites (
    id_penalite SERIAL PRIMARY KEY,
    id_emprunt INT NOT NULL,
    id_adherent INT NOT NULL,
    date_debut DATE NOT NULL,
    jour INT NOT NULL,
    raison VARCHAR(255),
    FOREIGN KEY (id_emprunt) REFERENCES Emprunts(id_emprunt),
    FOREIGN KEY (id_adherent) REFERENCES Adherents(id_adherent)
);

