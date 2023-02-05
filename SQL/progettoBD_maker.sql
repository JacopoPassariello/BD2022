drop database if exists progettobd;
create database progettobd;
use progettobd;



create table dipendente (
	cf char(16) primary key,
    nome char(16) not null,
    cognome char(32) not null,
    ore_di_lavoro int not null,
    paga_oraria double not null,
    bonus double,
    telefono char(12) not null
);

create table sede (
	id int primary key,
    civico int not null,
    via char(32) not null,
    cap int not null,
    città char(32) not null,
    tipo enum('magazzino', 'ufficio', 'cantiere') not null
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