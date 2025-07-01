-- 2. Création des tables de référence
CREATE TABLE Roles (
    id_role SERIAL PRIMARY KEY,
    nom_role VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE Profils_Adherent (
    id_profil SERIAL PRIMARY KEY,
    nom_profil VARCHAR(100) NOT NULL UNIQUE,
    quota_emprunts_simultanes INT NOT NULL DEFAULT 3,
    duree_pret_domicile_jours INT NOT NULL DEFAULT 21,
    duree_pret_sur_place_heures INT NOT NULL DEFAULT 3,
    peut_prolonger_pret BOOLEAN NOT NULL DEFAULT TRUE,
    jours_penalite_par_retard INT NOT NULL DEFAULT 1
);

CREATE TABLE Tarifs_Inscription (
    id_tarif SERIAL PRIMARY KEY,
    id_profil INT NOT NULL,
    libelle VARCHAR(100) NOT NULL,
    montant DECIMAL(10, 2) NOT NULL,
    duree_validite_jours INT NOT NULL,
    FOREIGN KEY (id_profil) REFERENCES Profils_Adherent(id_profil)
);

CREATE TABLE Types_Abonnement (
    id_type_abonnement SERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    duree_en_jours INT NOT NULL,
    tarif DECIMAL(10, 2) NOT NULL
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

CREATE TABLE Etats_Exemplaire (
    id_etat SERIAL PRIMARY KEY,
    code_etat VARCHAR(20) NOT NULL UNIQUE,
    libelle VARCHAR(50) NOT NULL,
    peut_etre_emprunte BOOLEAN NOT NULL DEFAULT FALSE
);

-- Maintien de Statuts_Reservation et ajout d'INSERTions
DROP TABLE IF EXISTS Statuts_Reservation CASCADE; -- ATTENTION: Supprime la table et ses dépendances si elle existe.
CREATE TABLE Statuts_Reservation (
    id_statut SERIAL PRIMARY KEY,
    code_statut VARCHAR(50) NOT NULL UNIQUE
);

-- Insertion des statuts courants pour les réservations
INSERT INTO Statuts_Reservation (code_statut) VALUES
('EN_ATTENTE'),          -- Réservation placée, en attente de disponibilité
('PRET_DISPONIBLE'), -- Exemplaire disponible, en attente de retrait par l'adhérent
('RETRAITEE'),           -- Réservation honorée (livre emprunté)
('ANNULEE'),               -- Réservation annulée par l'adhérent ou le système
('EXPIREE');               -- Réservation non retirée dans le délai imparti

-- Maintien de Statuts_Emprunt et ajout d'INSERTions
DROP TABLE IF EXISTS Statuts_Emprunt CASCADE; -- ATTENTION: Supprime la table et ses dépendances si elle existe.
CREATE TABLE Statuts_Emprunt (
    id_statut_emprunt SERIAL PRIMARY KEY,
    code_statut VARCHAR(50) NOT NULL UNIQUE
);

-- Insertion des statuts courants pour les emprunts
INSERT INTO Statuts_Emprunt (code_statut) VALUES
('EN_COURS'),
('RETOURNE'),
('EN_RETARD'),
('PROLONGE'),
('PERDU'),
('ANNULE');


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
    mot_de_passe VARCHAR(255) NOT NULL,
    id_role INT NOT NULL,
    date_creation TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_role) REFERENCES Roles(id_role)
);

-- 4. Création des tables liées aux utilisateurs
CREATE TABLE Adherents (
    id_adherent SERIAL PRIMARY KEY,
    id_utilisateur INT UNIQUE,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    telephone VARCHAR(20),
    date_inscription DATE NOT NULL DEFAULT CURRENT_DATE,
    id_profil INT NOT NULL,
    id_tarif_inscription INT,
    FOREIGN KEY (id_utilisateur) REFERENCES Utilisateurs(id_utilisateur),
    FOREIGN KEY (id_profil) REFERENCES Profils_Adherent(id_profil),
    FOREIGN KEY (id_tarif_inscription) REFERENCES Tarifs_Inscription(id_tarif)
);


CREATE TABLE Statuts_Adherent (
    id_statut_adherent SERIAL PRIMARY KEY,
    code_statut VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE Mvt_Adherent (
    id_mvt_adherent SERIAL PRIMARY KEY,
    id_adherent INT NOT NULL,
    id_statut_nouveau INT NOT NULL,
    date_mouvement TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_adherent) REFERENCES Adherents(id_adherent) ON DELETE CASCADE,
    FOREIGN KEY (id_statut_nouveau) REFERENCES Statuts_Adherent(id_statut_adherent)
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
    quantite INT NOT NULL DEFAULT 1,
    FOREIGN KEY (id_livre) REFERENCES Livres(id_livre) ON DELETE CASCADE,
    FOREIGN KEY (id_etat) REFERENCES Etats_Exemplaire(id_etat)
);

CREATE TABLE Abonnements (
    id_abonnement SERIAL PRIMARY KEY,
    id_adherent INT NOT NULL,
    id_type_abonnement INT NOT NULL,
    date_debut DATE NOT NULL,
    date_fin DATE NOT NULL,
    FOREIGN KEY (id_adherent) REFERENCES Adherents(id_adherent),
    FOREIGN KEY (id_type_abonnement) REFERENCES Types_Abonnement(id_type_abonnement)
);

CREATE TABLE Droits_Emprunt_Specifiques (
    id_droit SERIAL PRIMARY KEY,
    id_livre INT NOT NULL,
    id_profil INT NOT NULL,
    emprunt_domicile_autorise BOOLEAN NOT NULL DEFAULT TRUE,
    UNIQUE (id_livre, id_profil),
    FOREIGN KEY (id_livre) REFERENCES Livres(id_livre) ON DELETE CASCADE,
    FOREIGN KEY (id_profil) REFERENCES Profils_Adherent(id_profil) ON DELETE CASCADE
);

-- Table Emprunts sans statut direct
DROP TABLE IF EXISTS Emprunts CASCADE; -- A UTILISER AVEC PRUDENCE !
CREATE TABLE Emprunts (
    id_emprunt SERIAL PRIMARY KEY,
    id_exemplaire INT NOT NULL,
    id_adherent INT NOT NULL,
    date_emprunt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_retour_prevue DATE NOT NULL,
    date_retour_reelle DATE,
    type_emprunt VARCHAR(20) NOT NULL,
    prolongations INT DEFAULT 0,
    FOREIGN KEY (id_exemplaire) REFERENCES Exemplaires(id_exemplaire),
    FOREIGN KEY (id_adherent) REFERENCES Adherents(id_adherent)
);

-- Table Mvt_Emprunt (journal de l'historique des statuts d'emprunt)
DROP TABLE IF EXISTS Mvt_Emprunt CASCADE; -- A UTILISER AVEC PRUDENCE !
CREATE TABLE Mvt_Emprunt (
    id_mvt_emprunt SERIAL PRIMARY KEY,
    id_emprunt INT NOT NULL,
    id_statut_nouveau INT NOT NULL,
    date_mouvement TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_emprunt) REFERENCES Emprunts(id_emprunt) ON DELETE CASCADE,
    FOREIGN KEY (id_statut_nouveau) REFERENCES Statuts_Emprunt(id_statut_emprunt)
);
CREATE INDEX idx_mvt_emprunt_emprunt_date ON Mvt_Emprunt (id_emprunt, date_mouvement DESC, id_mvt_emprunt DESC);


-- MODIFICATION DE LA TABLE Reservations: Suppression de id_statut
-- ATTENTION: Si vous exécutez ce script sur une base de données existante,
-- vous devrez ALTER TABLE Reservations DROP CONSTRAINT reservations_id_statut_fkey; (si le nom est par défaut)
-- Puis ALTER TABLE Reservations DROP COLUMN id_statut;
DROP TABLE IF EXISTS Reservations CASCADE; -- A UTILISER AVEC PRUDENCE ! Supprime la table et ses dépendances.
CREATE TABLE Reservations (
    id_reservation SERIAL PRIMARY KEY,
    id_livre INT NOT NULL,
    id_adherent INT NOT NULL,
    date_demande TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_expiration DATE NOT NULL,
    FOREIGN KEY (id_livre) REFERENCES Livres(id_livre) ON DELETE CASCADE,
    FOREIGN KEY (id_adherent) REFERENCES Adherents(id_adherent) ON DELETE CASCADE
);

-- NOUVELLE TABLE : Mvt_Reservation
-- Enregistre l'historique des changements de statut pour chaque réservation.
-- Le statut actuel d'une réservation sera le 'id_statut_nouveau' de la ligne la plus récente pour cet 'id_reservation'.
CREATE TABLE Mvt_Reservation (
    id_mvt_reservation SERIAL PRIMARY KEY,
    id_reservation INT NOT NULL,
    id_statut_nouveau INT NOT NULL, -- Le statut vers lequel la réservation a transité
    date_mouvement TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_reservation) REFERENCES Reservations(id_reservation) ON DELETE CASCADE,
    FOREIGN KEY (id_statut_nouveau) REFERENCES Statuts_Reservation(id_statut)
);
-- Index pour optimiser la récupération du dernier statut de réservation
CREATE INDEX idx_mvt_reservation_reservation_date ON Mvt_Reservation (id_reservation, date_mouvement DESC, id_mvt_reservation DESC);


CREATE TABLE Penalites (
    id_penalite SERIAL PRIMARY KEY,
    id_emprunt INT NOT NULL,
    id_adherent INT NOT NULL,
    date_debut DATE NOT NULL,
    date_fin DATE NOT NULL,
    raison VARCHAR(255),
    FOREIGN KEY (id_emprunt) REFERENCES Emprunts(id_emprunt),
    FOREIGN KEY (id_adherent) REFERENCES Adherents(id_adherent)
);

CREATE TABLE Paiements_inscription (
    id_paiement SERIAL PRIMARY KEY,
    id_adherent INT NOT NULL,
    id_tarif INT NOT NULL,
    montant DECIMAL(10, 2) NOT NULL,
    date_paiement TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_adherent) REFERENCES Adherents(id_adherent),
    FOREIGN KEY (id_tarif) REFERENCES Tarifs_Inscription(id_tarif)
);