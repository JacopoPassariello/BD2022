select v.targa,v.scadenza_assicurazione, data_acquisto,v.guidatore as cf_guidatore
from Veicolo v
where (YEAR(data_acquisto) > YEAR(GETDATE()) or YEAR(data_acquisto) = YEAR(GETDATE())) and cf_guidatore is not null;

select s.civico, s.via, s.cap, s.citta
from Sede s, Veicolo v
where v.sede = s.id
group by s.id;

select s.civico, s.via, s.cap, s.citta
from Sede s, Macchinario m
where m.sede = s.id
group by s.id;

select SUM(d.paga_oraria * d.ore)
from Dipendente d;