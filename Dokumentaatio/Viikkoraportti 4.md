# Viikkoraportti 4

Tällä viikolla yritin nopeuttaa algoritmia eriaisilla karsintatavoilla, ja aika monen niiden takia haku nopeutui paljon. Lisäsin quiescence haun, joka hakee ainoastaan syömisiä kun lehtisolmuun on päädytty, ja evaluoi laudan vasta kun tilanne on ns. "hiljainen". Tämä taas hidasti hakua huomattavasti, mutta tämän jälkeen algoritmi rupesi tekemään paljon parempia siirtoja. Nyt algoritmia vastaan pystyy pelaamaan noin 6-7 syvyydellä ilman että tarvitsee juurikaan odottaa. Tässä on vielä vähän hiomista, ja saatan toteuttaa vielä muutamia tapoja algoritmin nopeuttamiseen.

Lisäsin käyttöliittymän, jolla pystyy pelaamaan joko yksikseen, tai enteriä painamalla algoritmi tekee siirron sille, jonka vuoro on siirtää. Spacea painamalla konsoliin tulee tulostus algoritmin sanovasta parhaasta siirrosta, mutta sitä ei kuitenkaan automaattisesti suoriteta laudalla. Syvyyttä voi muuttaa numeronäppäimillä (1-9), ja takaisin pääsee siirroissa nuolella vasemmalle.

Aloitin suunnittelemaan suorituskykytestausta, mutta en ole vielä täysin varma miten tuota sovelletaan shakkialgoritmiin. Aloitin myös omia tietorakenteita. Lisäsin perft testausta, joka testaa siirtogeneraattoria (https://www.chessprogramming.org/Perft).




### Tuntikirjanpito
Päivä | Tunnit | Kuvaus
----- | ------ | ------
.8.2021 |  | 