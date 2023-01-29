drop database if exists progettobd;
create database progettobd;
use progettobd;

#Consulente (CF, Nome, Cognome, Ruolo, Numero di Telefono, IBAN, Retribuzione Mensile, PIVA)
create table Consulente (
	CF char(16) primary key,
    Nome char(32) not null,
    Cognome char(32) not null,
    Ruolo char(32) not null,
    Numero_di_telefono char(12) not null,
    IBAN char(33) not null,
    Retribuzione_mensile double not null,
    PIVA char(11)  not null
);

#Referente(CF, Nome, Cognome, Numero di Telefono)
create table Referente (
	CF char(16) primary key,
    Nome char(32) not null,
    Cognome char(32) not null,
    Numero_di_Telefono char(12) not null
);

#Sede (Indirizzo (Numero Civico, Via, Città, CAP), Tipo)
create table Sede (
	ID int primary key,
	Numero_civico int not null,
    Via char(32) not null,
    Citta char(32) not null,
    CAP int not null,
    Tipo char(20) not null,
    Referente char(16) not null,
    
    foreign key (referente) references referente(cf)
);

#Dipendente (CF, Nome, Cognome, Ruolo, Numero di Telefono, IBAN, Retribuzione, Paga Oraria, Ore, Bonus, Livello CCNL, Luogo di Lavoro)
create table Dipendente (
	CF char(16) primary key,
    Nome char(32) not null,
    Cognome char(32) not null,
    Ruolo char(32) not null,
    Numero_di_telefono char(12) not null,
    IBAN char(33) not null,
    Retribuzione double not null,
    Ore int not null,
    Bonus double not null
);

#Luogo di Lavoro(id sede, cf dipendente)
create table Luogo_di_Lavoro (
	id_sede int,
    cf_dipendente char(16),
    
    primary key (id_sede, cf_dipendente),
    
    foreign key (id_sede) references sede(id),
    foreign key (cf_dipendente) references dipendente(cf)
);

#Fattura (Numero Fattura, Valore, IBAN mittente, IBAN ricevente, Data, Descrizione)
create table Fattura (
	Numero_Fattura int primary key,
    Valore double not null,
    IBAN_mittente char(33) not null,
    IBAN_ricevente char(33),
    Data date not null,
    Descrizione char(128)
);

#Lotto Materiale(Numero Lotto, Nome, Quantità Totale, Descrizione, IBAN Venditore)
create table Lotto_Materiale (
	Numero_Lotto int primary key,
    Nome char(16) not null,
    Quantita_Totale int not null,
    Descrizione char(128),
	IBAN_Venditore char(33) not null
);

#Veicolo(Targa, Scadenza Assicurazione, Guidatore, Modello, IBAN Assicuratore)
create table Veicolo (
	Targa char(7) primary key,
    Scadenza_Assicurazione date not null,
    Guidatore char(16),
	Modello char(32) not null,
    IBAN_Assicuratore char(33) not null
);

#Assegnazione Materiale(Indirizzo Sede, Numero Lotto)
create table Assegnazione_Materiale (
	Id_Sede int not null,
    Numero_Lotto int not null,
    
    primary key (id_sede, numero_lotto),
    
	foreign key (id_sede) references sede(id),
    foreign key (numero_lotto) references lotto_materiale(numero_lotto)
);

#Assegnazione Veicolo(Indirizzo Sede, Targa)
create table Assegnazione_Veicolo (
	id_sede int not null,
    Targa_Veicolo char(7) not null,
    
    primary key (id_sede, targa_veicolo),
    
	foreign key (id_sede) references sede(id),
    foreign key (targa_veicolo) references veicolo(targa)
);