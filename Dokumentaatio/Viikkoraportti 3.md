# Viikkoraportti 3

Tällä viikolla tutkin paljon eri tapoja algoritmin nopeuttamiseen, ja pyrin niistä myös toteuttamaan joitakin. (Kuten transposition table, iterative deepening, principal variation search, aspiration windows, killer heuristic, quiescence search, pondering, null move pruning, etc.)

Tein mahdollisten siirtojen generoimisen valmiiksi. Siitä puuttuu enää linnoittautuminen ja sotilaan ylentyminen. Tein evaluaatiofunktion, joka evaluoi materiaalia ja nappuloiden sijaintia laudalla. Lisään siihen varmaan myöhemmin jotain muuta, kun algoritmi toimii vielä hieman nopeammin. Sain pääalgoritmin tehtyä loppuun, ja se laskee nyt viiden syvyyteen muutamassa sekunnissa. (Kun laitoin algoritmin pelaamaan itseään vastaan niin noin 35 siirron kohdalla se alkoi toistaa samoja siirtoja edes takaisin. Pitää siis varmaan tehdä repetition table, joka laskee toistuvia tilanteita, ja kun on 3 samaa pelattua sijaintia niin peli loppuu tasapeliin.)

Lisäsin pit mutaatiotestit toimimaan JUnit 5 testien kanssa, ja laitoin checkstylen myös. Tein kohtalaisen paljon testejä, lisäilen niitä varmaankin joka viikko lisää.

Seuraavaksi aloitan algoritmin nopeuttamista, ja omien tietorakenteiden tekemistä. Toteutan transposition tablen, joka käyttää HashMappia, ja ajattelin, että ArrayListin sijaan toteuttaisin kaksisuuntaisen LinkedListin, koska se mahdollistaa mahdollisten siirtojen generoimisessa niiden sijoittamisen ensimmäiseksi (O(1) ajassa), tai helposti alkupäähän ilman että tarvitsee koko loppulistaa siirtää oikealle. Pystyn myös LinkedLististä poppaamaan transposition tablesta löytyvän parhaan siirron ensimmäiseksi samalla idealla. Tätä voi myös käyttää HashMapin collisioiden listana. Ja jos ehtii, niin saatan aloittaa UI:n tekemistä.


**Kysymys ohjaajalle**: Miten pit raportin ja checkstylen tulisi olla näkyvillä viikkopalautuksissa? Nyt vain kopioin sen kansion minkä ne teki [Dokumentaatioon](./) sellaisenaan.
Ja kuinka tarkasti tuota checkstylea pitää noudattaa? Kun sitä tutkin, niin se sisältää paljon turhia erroreita, ja aika monen niiden kanssa olen eri mieltä muutenkin. Tämä on kuitenkin minun oma projekti, ja pyrin pitämään sen mahdollisimman selkeänä joka tapauksessa, mutta silti omanlaisena (esim tabs > spaces).


### Tuntikirjanpito
Päivä | Tunnit | Kuvaus
----- | ------ | ------
2.8.2021 | 7h | Tutkin paljon algoritmia nopeuttavia tapoja, ja jatkoin mahdollisten siirtojen generoimista.
3.8.2021 | 4h | Mahdollisten siirtojen generoiminen loppuun.
4.8.2021 | 5h | Alphabeta loppuun, aloitin tekemään transposition tablea, ja tutkin lisää algoritmeista.
5.8.2021 | 5h | Pit ja checkstyle lisääminen. Testien tekemistä.
6.8.2021 | 4h | Tein evaluaatio funktion ja yhdistin sen alphabeta algoritmin kanssa. Tein viikkoraportin.
