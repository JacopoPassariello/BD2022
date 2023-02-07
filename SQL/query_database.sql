use progettobd;

select v.targa, v.scadenza_assicurazione, v.guidatore as cf_guidatore
from Veicolo v
where (YEAR(v.scadenza_assicurazione) > YEAR(CURDATE()) or YEAR(v.scadenza_assicurazione) = YEAR(curdate())) and v.guidatore is not null;

select s.civico, s.via, s.cap, s.citta, s.tipo
from Sede s, Veicolo v
where v.sede = s.id
group by s.id;

select s.civico, s.via, s.cap, s.citta, s.tipo
from Sede s, Macchinario m
where m.sede = s.id
group by s.id;

select SUM(d.paga_oraria * d.ore_lavoro + d.bonus) as totale_stipendi
from Dipendente d;

select s.civico,s.via,s.cap,s.citta, s.tipo, SUM(d.paga_oraria * d.ore_lavoro + d.bonus) as totale_stipendi
from Dipendente d, luogo_lavoro l, Sede s
where l.dipendente = d.cf and l.sede = s.id
group by l.sede;

select s.civico,s.via,s.cap,s.citta, s.tipo, COUNT(*) as numero_lotti_assegnati
from Sede s,assegnazione_materiale a
where s.id = a.sede
group by s.id
having COUNT(*) >= 5;

select d.ore_lavoro*d.paga_oraria+d.bonus as stipendio, d.cf, d.nome,d.cognome
from Dipendente d
order by stipendio DESC;

select s.civico,s.via,s.cap,s.citta, COUNT(*) as numero_dipendenti
from Sede s, luogo_lavoro l
where s.tipo = 2 and s.id = l.sede
group by s.id
order by numero_dipendenti ASC;

create or replace view v as 
(
	select s.civico,s.via,s.cap,s.citta,s.tipo ,SUM(a.quantita) quantita_totale 
    from Sede s, assegnazione_materiale a
    where s.id = a.sede
    group by s.id
);

select *
from v
where v.quantita_totale = (select MAX(v.quantita_totale) from v);

select d.cf, d.nome, d.cognome
from Dipendente d, luogo_lavoro l
where d.cf = l.dipendente and l.tipo_sede = 1 and d.cf not in 
(
	select d.cf
	from Dipendente d, luogo_lavoro l
    where d.cf = l.dipendente and l.tipo_sede = 2
);

select d.cf,d.nome,d.cognome
from Dipendente d
where not exists
(
	select s.id
    from Sede s
    where not exists
    (
		select *
        from luogo_lavoro l
		where l.sede = s.id and l.dipendente = d.cf
	)
);


