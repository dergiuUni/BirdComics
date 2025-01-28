DROP DATABASE IF EXISTS BirdComics;
CREATE DATABASE BirdComics;

USE BirdComics;

CREATE TABLE Indirizzo (
    nomeCitta varchar(45) NOT NULL,
    via varchar(45) NOT NULL,
    numeroCivico int NOT NULL,
    cvc varchar(45) NOT NULL,

    PRIMARY KEY (nomeCitta, via, numeroCivico, cvc)
);

CREATE TABLE Autore(
    nome varchar(45) NOT NULL,
    cognome varchar(45) NOT NULL,
    
    PRIMARY KEY(nome, cognome)
);

CREATE TABLE Genere(
    genere varchar(45) NOT NULL,
    PRIMARY KEY (genere)
);

CREATE TABLE Fumetto(
    id INT NOT NULL AUTO_INCREMENT,
    nome varchar(45) NOT NULL,
    descrizione varchar(100) NOT NULL,
    prezzo float NOT NULL,
    active boolean NOT NULL,

    PRIMARY KEY (id)
);

CREATE TABLE Autore_Fumetto(
    nomeAutore varchar(45) NOT NULL,
    cognomeAutore varchar(45) NOT NULL,
    idFumetto INT NOT NULL,

    PRIMARY KEY(nomeAutore, cognomeAutore, idFumetto),
    FOREIGN KEY (nomeAutore, cognomeAutore) REFERENCES Autore (nome, cognome) ON DELETE CASCADE,
    FOREIGN KEY (idFumetto) REFERENCES Fumetto (id) ON DELETE CASCADE
);

CREATE TABLE Genere_Fumetto(
    genere varchar(45) NOT NULL,
    idFumetto INT NOT NULL,

    PRIMARY KEY(genere, idFumetto),
    FOREIGN KEY (Genere) REFERENCES Genere (genere) ON DELETE CASCADE,
    FOREIGN KEY (idFumetto) REFERENCES Fumetto (id) ON DELETE CASCADE
);

CREATE TABLE Scaffali(
    id  INT NOT NULL AUTO_INCREMENT,
    quantita INT NOT NULL,
    quantitaMassima INT NOT NULL,

    idFumetto INT NOT NULL,

    PRIMARY KEY (id),
    FOREIGN KEY (idFumetto) REFERENCES Fumetto (id) ON DELETE CASCADE
);



CREATE TABLE Magazzino(
    nome varchar(45) NOT NULL,
    nomeCitta varchar(45) NOT NULL,
    via varchar(45) NOT NULL,
    numeroCivico int not  NULL,
    cvc varchar(45) not  NULL,

    PRIMARY KEY (nome),
    FOREIGN KEY (nomeCitta, via, numeroCivico, cvc) REFERENCES Indirizzo (nomeCitta, via, numeroCivico, cvc) ON DELETE CASCADE
);

CREATE TABLE MagazzinoScaffali(
    idMagazzino VARCHAR(45) NOT NULL,
    idScaffale INT NOT NULL,
    PRIMARY KEY (idMagazzino, idScaffale),
    FOREIGN KEY (idMagazzino) REFERENCES Magazzino (nome) ON DELETE CASCADE,
    FOREIGN KEY (idScaffale) REFERENCES Scaffali (id) ON DELETE CASCADE
);

CREATE TABLE Fattura(
    id INT NOT NULL AUTO_INCREMENT,
    
    iva INT NOT NULL,
    nomeCittaCliente varchar(45) NOT NULL,
    viaCliente varchar(45) NOT NULL,
    numeroCivicoCliente int not  NULL,
    cvcCliente varchar(45) not  NULL,
    nomeCittaMagazzino varchar(45) NOT NULL,
    viaMagazzino varchar(45) NOT NULL,
    numeroCivicoMagazzino int not  NULL,
    cvcMagazzino varchar(45) not  NULL,
    nomeMagazzino varchar(45) NOT NULL,

    PRIMARY KEY (id)
);

CREATE TABLE Ordine(
    id INT NOT NULL AUTO_INCREMENT,
    idpaypal varchar(45) NOT NULL,
    shipped varchar(45) NOT NULL,
    dataEffettuato date NOT NULL,

    idFattura INT NOT NULL,

    PRIMARY KEY (id),
    FOREIGN KEY (idFattura) REFERENCES Fattura (id) ON DELETE CASCADE
);

CREATE TABLE Ordine_Fumetto(
    idOrdine INT NOT NULL,
    idFumetto INT NOT NULL,

    PRIMARY KEY (idOrdine, idFumetto),
    FOREIGN KEY (idOrdine) REFERENCES Ordine (id) ON DELETE CASCADE,
    FOREIGN KEY (idFumetto) REFERENCES Fumetto (id) ON DELETE CASCADE
);

CREATE TABLE Ordine_Magazzino(
    idOrdine INT NOT NULL,
    nomeMagazzino varchar(45) NOT NULL, 

    PRIMARY KEY (idOrdine, nomeMagazzino),
    FOREIGN KEY (idOrdine) REFERENCES Ordine (id) ON DELETE CASCADE,
    FOREIGN KEY (nomeMagazzino) REFERENCES Magazzino (nome) ON DELETE CASCADE
);
CREATE TABLE Utente(
    email varchar(45) NOT NULL,
    pass varchar(45) NOT NULL,
    nome varchar(45) NOT NULL,
    cognome varchar(45) NOT NULL,
    telefono varchar(11) NOT NULL,
    nomeCitta varchar(45) NOT NULL,
    via varchar(45) NOT NULL,
    numeroCivico int not  NULL,
    cvc varchar(45) not  NULL,
    dataNascita date,
    
    PRIMARY KEY (email),
    FOREIGN KEY (nomeCitta, via, numeroCivico, cvc) REFERENCES Indirizzo (nomeCitta, via, numeroCivico, cvc) ON DELETE CASCADE
);

CREATE TABLE CarrelloCliente(
    id VARCHAR(45) NOT NULL,
    idFumetto INT NOT NULL,
    quantita INT NOT NULL,

    PRIMARY KEY (id, idFumetto),
    FOREIGN KEY (idFumetto) REFERENCES Fumetto (id) ON DELETE CASCADE,
    FOREIGN KEY (id) REFERENCES Utente (email) ON DELETE CASCADE
);



CREATE TABLE Utente_Ordine (
    emailUtente VARCHAR(45) NOT NULL,
    idOrdine INT NOT NULL,
    PRIMARY KEY (emailUtente, idOrdine),
    FOREIGN KEY (emailUtente) REFERENCES Utente (email) ON DELETE CASCADE,
    FOREIGN KEY (idOrdine) REFERENCES Ordine (id) ON DELETE CASCADE
);

CREATE TABLE Utente_Ruolo (
    idRuolo ENUM('GestoreGenerale', 'GestoreMagazzino', 'RisorseUmane', 'GestoreCatalogo', 'Magazziniere', 'Spedizioniere', 'Assistenza', 'Finanza', 'Cliente') NOT NULL default  'Cliente',
    emailUtente VARCHAR(45) NOT NULL,

    PRIMARY KEY (idRuolo, emailUtente),
    FOREIGN KEY (emailUtente) REFERENCES Utente (email) ON DELETE CASCADE
);





-- Inserimento nella tabella Indirizzo
INSERT INTO Indirizzo (nomeCitta, via, numeroCivico, cvc) VALUES 
    ('Napoli', 'Via Toledo', 22, '4321'),
    ('Torino', 'Via Po', 30, '8765'),
    ('Bologna', 'Via Indipendenza', 15, '1357'),
    ('Firenze', 'Piazza del Duomo', 5, '2468'),
    ('Roma', 'Via della Conciliazione', 10, '1234'),
    ('Milano', 'Corso Buenos Aires', 15, '5678');

-- Inserimento nella tabella Autore
INSERT INTO Autore (nome, cognome) VALUES 
    ('Geoff', 'Johns'),
    ('Mark', 'Millar'),
    ('Grant', 'Morrison'),
    ('Brian', 'Bendis'),
    ('Ed', 'Brubaker'),
    ('Alan', 'Moore'),
    ('Frank', 'Miller');

-- Inserimento nella tabella Genere
INSERT INTO Genere (genere) VALUES
    ('Fantasy'),
    ('Avventura'),
    ('Guerra'),
    ('Commedia'),
    ('Drammatico'),
    ('Storico'),
    ('Supereroi'),
    ('Fantascienza'),
    ('Horror');

-- Inserimento nella tabella Fumetto
INSERT INTO Fumetto (nome, descrizione, prezzo, active) VALUES 
    ('Green Lantern', 'Il supereroe con un anello che dà poteri illimitati', 12.99, TRUE),
    ('Kick-Ass', 'Un ragazzo normale che decide di diventare un supereroe', 14.99, TRUE),
    ('Batman: Arkham Asylum', 'Le avventure di Batman contro i suoi nemici più pericolosi', 17.99, TRUE),
    ('Daredevil', 'Un avvocato cieco che combatte il crimine', 18.99, TRUE),
    ('Captain America', 'Un soldato super-soldato durante la Seconda Guerra Mondiale', 16.99, TRUE),
    ('X-Men', 'Un gruppo di supereroi mutanti che combattono per l uguaglianza', 13.99, TRUE),
    ('Watchmen', 'Fumetto di supereroi con tematiche adulte', 19.99, TRUE),
    ('The Dark Knight Returns', 'Racconto epico di Batman', 15.99, TRUE);

-- Inserimento nella tabella Autore_Fumetto
INSERT INTO Autore_Fumetto (nomeAutore, cognomeAutore, idFumetto) VALUES 
    ('Geoff', 'Johns', 1),
    ('Mark', 'Millar', 2),
    ('Grant', 'Morrison', 3),
    ('Brian', 'Bendis', 4),
    ('Ed', 'Brubaker', 5),
    ('Geoff', 'Johns', 6),
    ('Alan', 'Moore', 1),
    ('Frank', 'Miller', 2);

-- Inserimento nella tabella Genere_Fumetto
INSERT INTO Genere_Fumetto (genere, idFumetto) VALUES
    ('Fantasy', 1),
    ('Avventura', 2),
    ('Guerra', 3),
    ('Commedia', 4),
    ('Drammatico', 5),
    ('Supereroi', 6),
    ('Supereroi', 1),
    ('Supereroi', 2),
    ('Fantascienza', 1);

-- Inserimento nella tabella Scaffali
INSERT INTO Scaffali (quantita, quantitaMassima, idFumetto) VALUES 
    (10, 30, 1),
    (15, 50, 2),
    (20, 60, 3),
    (25, 80, 4),
    (30, 100, 5),
    (35, 120, 6),
    (5, 20, 1),
    (10, 50, 2);

-- Inserimento nella tabella Magazzino
INSERT INTO Magazzino (nome, nomeCitta, via, numeroCivico, cvc) VALUES 
    ('Magazzino Napoli', 'Napoli', 'Via Toledo', 22, '4321'),
    ('Magazzino Bologna', 'Bologna', 'Via Indipendenza', 15, '1357');
    
INSERT INTO MagazzinoScaffali(idMagazzino, idScaffale) VALUES
    ('Magazzino Napoli',  1),
    ('Magazzino Napoli',  2),
    ('Magazzino Napoli',  3),
    ('Magazzino Napoli',  4),
    ('Magazzino Napoli',  5),
    ('Magazzino Bologna', 6),
    ('Magazzino Bologna', 7),
    ('Magazzino Bologna', 8);



-- Inserimento nella tabella Fattura
INSERT INTO Fattura (iva, nomeCittaCliente, viaCliente, numeroCivicoCliente, cvcCliente, nomeCittaMagazzino, viaMagazzino, numeroCivicoMagazzino, cvcMagazzino, nomeMagazzino) VALUES 
    (22, 'Roma', 'Via della Conciliazione', 10, '1234', 'Roma', 'Via della Conciliazione', 10, '1234', 'Magazzino Centrale'),
    (10, 'Milano', 'Via Po', 12, '8765', 'Milano', 'Via Vittorio Emanuele', 45, '7854', 'Magazzino Nord'),
    (22, 'Roma', 'Via della Conciliazione', 10, '1234', 'Roma', 'Via della Conciliazione', 10, '1234', 'Magazzino Centrale');

-- Inserimento nella tabella Ordine
INSERT INTO Ordine (idpaypal, shipped, dataEffettuato, idFattura) VALUES 
    ('PAYPAL112233', 'No', '2025-02-20', 1),
    ('PAYPAL445566', 'Yes', '2025-02-22', 2),
    ('PAYPAL778899', 'Yes', '2025-02-23', 1),
    ('PAYPAL998877', 'No', '2025-02-24', 2),
    ('PAYPAL123456789', 'No', '2025-01-20', 1),
    ('PAYPAL987654321', 'Yes', '2025-01-22', 1);


-- Inserimento nella tabella Ordine_OrdineFumetto
INSERT INTO Ordine_Fumetto (idOrdine, idFumetto) VALUES 
    (1, 1),
    (1, 2),
    (2, 3),
    (3, 4),
    (4, 5),
    (2, 2);

-- Inserimento nella tabella Ordine_Magazzino
INSERT INTO Ordine_Magazzino (idOrdine, nomeMagazzino) VALUES 
    (1, 'Magazzino Napoli'),
    (2, 'Magazzino Napoli'),
    (3, 'Magazzino Napoli'),
    (4, 'Magazzino Napoli'),
    (1, 'Magazzino Bologna'),
    (2, 'Magazzino Bologna');
    
    INSERT INTO Utente (email, pass, nome, cognome, telefono, nomeCitta, via, numeroCivico, cvc) VALUES 
    ('cliente@example.com', 'silviapass456', 'Silvia', 'Rossi', '6789012345', 'Torino', 'Via Po', 30, '8765');
INSERT INTO Utente (email, pass, nome, cognome, telefono, nomeCitta, via, numeroCivico, cvc) VALUES 
    ('generale@BirdComics.com', 'paolopass789', 'Paolo', 'Ferrari', '7890123456', 'Torino', 'Via Po', 30, '8765'),
    ('magazzino@BirdComics.com', 'martapass012', 'Marta', 'Bianchi', '8901234567', 'Torino', 'Via Po', 30, '8765'),
    ('hr@BirdComics.com', 'luigipass345', 'Luigi', 'Verdi', '9012345678', 'Torino', 'Via Po', 30, '8765'),
    ('catalogo@BirdComics.com', 'francescapass678', 'Francesca', 'Galli', '1234098765', 'Torino', 'Via Po', 30, '8765'),
    ('magazziniere@BirdComics.com', 'giorgiopass234', 'Giorgio', 'Moretti', '2345109876', 'Torino', 'Via Po', 30, '8765'),
    ('spedizioniere@BirdComics.com', 'elisapass567', 'Elisa', 'Conti', '3456210987', 'Torino', 'Via Po', 30, '8765'),
    ('assistenza@BirdComics.com', 'claudiopass890', 'Claudio', 'Ricci', '4567321098', 'Torino', 'Via Po', 30, '8765'),
    ('finanza@BirdComics.com', 'serenapass345', 'Serena', 'Marini', '5678432109', 'Torino', 'Via Po', 30, '8765');

    

-- Inserimento nella tabella CarrelloCliente
INSERT INTO CarrelloCliente (id, idFumetto, quantita) VALUES 
    ("cliente@example.com", 1, 5),
    ("cliente@example.com", 2, 3),
    ("cliente@example.com", 3, 3);


-- Inserimento nella tabella Utente

-- Inserimento nella tabella Utente_Ruolo
INSERT INTO Utente_Ruolo (idRuolo, emailUtente) VALUES 
    ('Cliente', 'cliente@example.com'),
    ('GestoreGenerale', 'generale@BirdComics.com'),
    ('GestoreMagazzino', 'magazzino@BirdComics.com'),
    ('RisorseUmane', 'hr@BirdComics.com'),
    ('GestoreCatalogo', 'catalogo@BirdComics.com'),
    ('Magazziniere', 'magazziniere@BirdComics.com'),
    ('Spedizioniere', 'spedizioniere@BirdComics.com'),
    ('Assistenza', 'assistenza@BirdComics.com'),
    ('Finanza', 'finanza@BirdComics.com');

    


INSERT INTO Utente_Ordine (emailUtente, idOrdine) VALUES
    ('cliente@example.com', 1),
    ('cliente@example.com', 2),
    ('cliente@example.com', 3),
    ('cliente@example.com', 4),
    ('cliente@example.com', 5);