SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

CREATE TABLE IF NOT EXISTS participant (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    type_participant VARCHAR(31),
    CONSTRAINT chk_email_format CHECK (email LIKE '%@gmail.com')
);

CREATE TABLE IF NOT EXISTS organisateur (
    id INT PRIMARY KEY,
    CONSTRAINT fk_organisateur_participant FOREIGN KEY (id)
        REFERENCES participant(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS evenement (
    id_Event BIGINT AUTO_INCREMENT PRIMARY KEY,
    name_Event VARCHAR(255) NOT NULL,
    Date DATETIME,
    Lieu VARCHAR(255),
    CapaciteMax INT,
    type_event VARCHAR(31),
    organisateur_id INT,
    CONSTRAINT fk_evenement_organisateur FOREIGN KEY (organisateur_id)
        REFERENCES organisateur(id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS concert (
    id_Event BIGINT PRIMARY KEY,
    artiste VARCHAR(255),
    genre_musical VARCHAR(255),
    CONSTRAINT fk_concert_evenement FOREIGN KEY (id_Event)
        REFERENCES evenement(id_Event) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS conference (
    id_Event BIGINT PRIMARY KEY,
    theme VARCHAR(255),
    CONSTRAINT fk_conference_evenement FOREIGN KEY (id_Event)
        REFERENCES evenement(id_Event) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS intervenant (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    specialite VARCHAR(255) NOT NULL,
    CONSTRAINT chk_intervenant_email CHECK (email LIKE '%@gmail.com')
);

CREATE TABLE IF NOT EXISTS evenement_participant (
    evenement_id BIGINT,
    participant_id INT,
    PRIMARY KEY (evenement_id, participant_id),
    FOREIGN KEY (evenement_id) REFERENCES evenement(id_Event) ON DELETE CASCADE,
    FOREIGN KEY (participant_id) REFERENCES participant(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS conference_intervenants (
    conference_id BIGINT,
    intervenant_id BIGINT,
    PRIMARY KEY (conference_id, intervenant_id),
    FOREIGN KEY (conference_id) REFERENCES conference(id_Event) ON DELETE CASCADE,
    FOREIGN KEY (intervenant_id) REFERENCES intervenant(id) ON DELETE CASCADE
);

SET FOREIGN_KEY_CHECKS = 1;
