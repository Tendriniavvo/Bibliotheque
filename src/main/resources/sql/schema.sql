CREATE TABLE categorie (
                           id SERIAL PRIMARY KEY,
                           nom VARCHAR(100) NOT NULL
);

CREATE TABLE film (
                      id SERIAL PRIMARY KEY,
                      titre VARCHAR(200) NOT NULL,
                      annee_sortie INT
);

CREATE TABLE film_categorie (
                                film_id INT REFERENCES film(id) ON DELETE CASCADE,
                                categorie_id INT REFERENCES categorie(id) ON DELETE CASCADE,
                                PRIMARY KEY (film_id, categorie_id)
);