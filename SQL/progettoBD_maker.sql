drop database if exists progettobd;
create database progettobd;
use progettobd;



create table dipendente (
	cf char(16) primary key,
    nome char(16) not null,
    cognome char(32) not null,
    ore_lavoro int not null,
    paga_oraria double not null,
    bonus double not null,
    telefono char(12) not null
);

create table sede (
	id int primary key,
    civico int not null,
    via char(32) not null,
    cap int not null,
    citta char(32) not null,
    tipo enum('ufficio', 'cantiere', 'magazzino') not null
);

create table luogo_lavoro (
	dipendente char(16),
    sede int,
    ruolo char(32) not null,
    tipo_sede enum('ufficio', 'cantiere', 'magazzino') not null,
    
    foreign key (dipendente) references dipendente(cf) on delete cascade,
    foreign key (sede) references sede(id) on delete cascade,
    
    primary key (dipendente, sede)
);

create table lotto_materiale (
	id_inventario int primary key,
	data_acquisto date not null,
    costo double not null,
    marca char(32) not null,
    modello char(32) not null,
    quantita_totale int not null
);

create table assegnazione_materiale (
	lotto int,
    sede int,
    quantita int not null,
    
    primary key (lotto, sede),
    
    foreign key (lotto) references lotto_materiale(id_inventario) on delete cascade,
    foreign key (sede) references sede(id) on delete cascade
);

create table macchinario (
	id_inventario int primary key,
	data_acquisto date not null,
    costo double not null,
    marca char(32) not null,
    modello char(32) not null,
    sede int not null,
    
    foreign key (sede) references sede(id)
);

create table veicolo (
	id_inventario int primary key,
	data_acquisto date not null,
    costo double not null,
    marca char(32) not null,
    modello char(32) not null,
    scadenza_assicurazione date not null,
    targa char(10) not null unique,
    sede int not null,
    guidatore char(16),
    utilizzo char(24) not null,
    
    foreign key (sede) references sede(id),
    foreign key (guidatore) references dipendente(cf)
);

create table referente (
	cf char(16) primary key,
    nome char(16) not null,
    cognome char(32) not null,
    telefono char(12) not null,
    cantiere int not null,
    
    foreign key (cantiere) references sede(id) on delete cascade
);

insert into dipendente values
("RSSMRA80A01H501U", "Mario", "Rossi", 140, 10, 0, "393934595232"),
("JOEMMA80A01F839M", "Joe", "Mama", 150, 9, 2.5, "391186002558"),
("LNATNG80A01B963O", "Alan", "Turing", 210, 12, 0, "398014088727"),
("MRAVRD80A01B990Q", "Maria", "Verdi", 140, 10, 0, "396754319098"),
("CLDBGM80A41E463X", "Claudia", "Bergamini", 40, 9.5, 15, "398972792526"),
("LNRNMY31C26L219S", "Leonard", "Nimoy", 100, 10, 0, "396048637464"),
("VTTMNL63A01H224Y", "Emanuele", "Vittorio", 140, 10, 0, "397337646695"),
("GRRGRD92M55A509U", "Gerarda", "Guerriero", 160, 9, 30, "393076501229"),
("ZNJPLA12T21I820U", "Paul", "Zonej", 200, 11, 45, "396453335802"),
("BTTBMN67A01G482M", "Beniamino", "Bottone", 140, 10, 0, "392255383225"),
("BLNFMN94A41H703Y", "Filomena", "Balanzoni", 130, 10.5, 0, "396810031874"),
("WLFVGN80A41G015K", "Virginia", "Woolf", 130, 10.5, 0, "393770249509"),
("LFOTNY02M07A509Z", "Tony", "Loaf", 20, 10, 2000, "397174421993");

insert into sede values
(1, 12, "Via Roma", 83100, "Avellino", 1),
(2, 42, "C.da Pallone", 80125, "Fuorigrotta", 3),
(3, 69, "Piazza di Francia", 10024, "Torino", 2),
(4, 101, "Corso Antartide", 83048, "Montella", 2),
(5, 1, "Via degli Astronauti", 83038, "Montemiletto", 2);

insert into lotto_materiale values
(0, "2023-01-01", 2000, "Vileda", "Scopa", 1000),
(1, "2023-01-01", 3000, "Mondo Pulizia", "Detergente Lava-Pavimenti", 500),
(2, "2023-01-01", 4000, "Vileda", "Panno Microfibra", 3000),
(3, "2023-01-01", 2500, "Vileda", "Mocio", 750),
(4, "2023-01-01", 900, "Carrelloni", "Carrello", 30);

insert into macchinario values
(5, "2023-01-01", 200, "Karcher", "Idropulitrice", 3),
(6, "2023-01-01", 150, "Bosch", "Idropulitrice", 4),
(7, "2023-01-01", 2000, "Lavor", "Lavapavimenti", 4),
(8, "2023-01-01", 300, "Karcher", "Aspirapolvere", 5),
(9, "2023-01-01", 200, "Bosch", "Aspirapolvere", 2);

insert into veicolo values
(10, "2021-01-01", 1000, "FIAT", "Panda Torpedo", "2022-12-31", "AB420EM", 1, "LNATNG80A01B963O", "Auto aziendale"),
(11, "2021-01-01", 20000, "Citroen", "Jumper", "2024-01-01", "GM069LL", 2, "ZNJPLA12T21I820U", "Trasporto Materiali"),
(12, "2021-01-01", 3000, "Ford", "Ka", "2023-11-09", "PP313PP", 5, null, "Auto Aziendale");

insert into referente values
("GNPFTR80A01F205G", "Giampiero", "Fatturoni", "398653149266", 3),
("VCIGBT80A01F839U", "Gianbattista", "Vico", "399747710145", 4),
("PNAGPP99T31A662K", "Giuseppe", "Pane", "399435638991", 5);

insert into luogo_lavoro values
("RSSMRA80A01H501U", 3, "Capo Cantiere", 2),
("JOEMMA80A01F839M", 2, "Capo Magazzino", 3),
("LNATNG80A01B963O", 1, "Capo Ufficio", 1),
("MRAVRD80A01B990Q", 4, "Capo Cantiere", 2),
("CLDBGM80A41E463X", 5, "Capo Cantiere", 2),
("LNRNMY31C26L219S", 1, "Impiegato", 1),
("VTTMNL63A01H224Y", 5, "Fravecatore", 2),
("GRRGRD92M55A509U", 4, "Addetto Pulizie", 2),
("BTTBMN67A01G482M", 1, "Segretario", 1),
("BLNFMN94A41H703Y", 1, "Magazziniere", 3),
("BLNFMN94A41H703Y", 3, "Addetto Pulizie", 2),
("WLFVGN80A41G015K", 3, "Addetto Pulizie", 2),
("WLFVGN80A41G015K", 5, "Supervisore", 2),
("WLFVGN80A41G015K", 2, "Magazziniere", 3),
("ZNJPLA12T21I820U", 1, "Front Office", 1),
("ZNJPLA12T21I820U", 2, "Applicatore di adesivi", 3),
("ZNJPLA12T21I820U", 3, "Idraulica", 2),
("ZNJPLA12T21I820U", 4, "Idraulica", 2),
("ZNJPLA12T21I820U", 5, "Idraulica", 2),
("LFOTNY02M07A509Z", 1, "Business Manager", 1),
("LFOTNY02M07A509Z", 2, "Logistic Development Officer", 3),
("LFOTNY02M07A509Z", 3, "Quality Assurance", 2),
("LFOTNY02M07A509Z", 4, "Quality Assurance", 2),
("LFOTNY02M07A509Z", 5, "Quality Assurance", 2);

insert into assegnazione_materiale values
(0, 2, 300),
(0, 3, 80),
(0, 4, 500),
(0, 1, 20),
(0, 5, 100),
(1, 2, 140),
(1, 3, 100),
(1, 4, 250),
(1, 1, 10),
(2, 2, 2200),
(2, 3, 200),
(2, 4, 400),
(2, 5, 150),
(2, 1, 50),
(3, 2, 200),
(3, 3, 150),
(3, 4, 150),
(3, 5, 140),
(3, 1, 10),
(4, 3, 10),
(4, 4, 12),
(4, 5, 6),
(4, 1, 2);