use progettobd;

select v.targa, v.scadenza_assicurazione, v.guidatore as cf_guidatore
from Veicolo v
where (YEAR(v.scadenza_assicurazione) > YEAR(CURDATE()) or YEAR(v.scadenza_assicurazione) = YEAR(curdate())) and v.guidatore is not null;

select s.civico, s.via, s.cap, s.citta
from Sede s, Veicolo v
where v.sede = s.id
group by s.id;

select s.civico, s.via, s.cap, s.citta
from Sede s, Macchinario m
where m.sede = s.id
group by s.id;

select SUM(d.paga_oraria * d.ore_lavoro + d.bonus) as totale_stipendi
from Dipendente d;
