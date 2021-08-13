# Viikkoraportti 4

Tällä viikolla yritin nopeuttaa algoritmia eriaisilla karsintatavoilla, ja aika monen niiden takia haku nopeutui paljon. Lisäsin quiescence haun, joka hakee ainoastaan syömisiä kun lehtisolmuun on päädytty, ja evaluoi laudan vasta kun tilanne on ns. "hiljainen". Tämä taas hidasti hakua huomattavasti, mutta tämän jälkeen algoritmi rupesi tekemään paljon parempia siirtoja. Nyt algoritmia vastaan pystyy pelaamaan noin 6-7 syvyydellä ilman että tarvitsee juurikaan odottaa. Tässä on vielä vähän hiomista, ja saatan toteuttaa vielä muutamia tapoja algoritmin nopeuttamiseen.

Lisäsin käyttöliittymän, jolla pystyy pelaamaan joko yksikseen, tai enteriä painamalla algoritmi tekee siirron sille, jonka vuoro on siirtää. Spacea painamalla konsoliin tulee tulostus algoritmin sanovasta parhaasta siirrosta, mutta sitä ei kuitenkaan automaattisesti suoriteta laudalla. Syvyyttä voi muuttaa numeronäppäimillä (1-9), ja takaisin pääsee siirroissa nuolella vasemmalle.

Aloitin suunnittelemaan suorituskykytestausta, mutta en ole vielä täysin varma miten tuota sovelletaan shakkialgoritmiin. Aloitin myös omia tietorakenteita. Lisäsin perft testausta, joka testaa siirtogeneraattoria (https://www.chessprogramming.org/Perft).

Seuraavaksi jatkan suoritukykytestausta, ja tietorakenteiden tekemistä, sekä teen lisää testejä. Yritän myös hienosäätää haarojen karsimistapoja, ja lisätä pari tapaa, joilla pitäisi olla vielä kohtalaisen iso vaikutus. Teen myös lisää dokumentaatiota.


### Tuntikirjanpito
Päivä | Tunnit | Kuvaus
----- | ------ | ------
7.8.2021 | 4h | Aloitettu tekemään iterative deepeningiä, ja aspiration windowia. Lisäsin linnoittautumisen.
8.8.2021 | 7h | Yhdistin pääalgoritmin min ja max metodit (jotka kutsuivat toisiaan) yhdeksi negamax metodiksi. Tämä helpotti koodin kirjoitusta huomattavasti, kun aloin tekemään lisää haarojen karsintaa. Aloitin tekemään myös käyttöliittymää.
9.8.2021 | 6h | Tein käyttöliittymän loppuun ja lisäsin quiescence haun, sekä muutaman karsintatavan.
10.8.2021 | 4h | Lisäsin FEN notaation lukemisen lautaan perft testausta helpottamaan. Tein Perft luokan, joka laskee lehtisolmuja tietyllä syvyydellä (ilman minkäännäköistä karsintaa).
13.8.2021 | 5h |  Suunnittelin suorituskykytestausta ja muuta dokumentaatiota. Parantelin perft testausta, siivosin koodia, ja tein viikkoraporttia.