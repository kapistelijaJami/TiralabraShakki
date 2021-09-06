# Toteutusdokumentti

## Yleisrakenne
Ohjelmalla on Board, joka pitää tiedossa laudan tilan. Tilasta luodaan mahdolliset siirrot (Move), joita voi käyttää laudan päivittämiseen. 

Ohjelmalla on myös AlphaBeta luokka, joka sisältää tekoälyyn tarvittavia metodeja, kuten pääalgoritmit findBestMove ja negamax. FindBestMove käy syvyyksiä iteratiivisesti syvemmälle, jotta aina on parhaat siirrot olemassa, ja ne voi järjestää seuraavan iteraation ensimmäiseksi siirroksi. Tämä nopeuttaa hakua sitä enemmän mitä syvemmälle menee. Negamax käy rekursiivisesti alpha-beta algoritmia läpi, useaa nopeutustapaa käyttäen. Kun päästään haluttuun syvyyteen, aloitetaan quiescence haku, joka hakee niistä haaroista vielä syvemmälle, joissa ei olla niin sanotussa hiljaisessa tilanteessa. Eli on vielä kannattavia syömisiä jäljellä, eli joiden hinta on positiivinen tai 0 (kun syödyn nappulan hinta - syövän nappulan hinta on positiivinen, tai 0). Kun päästään tämän haun loppuun suoritetaan laudan evaluaatio, jossa evaluoidaan molempien pelaajien materiaalia ja nappuloiden sijaintia. Positiivinen evaluaatio on valkoiselle hyvä, ja negatiiviinen mustalle. Tämä antaa arvon lehtisolmulle. Negamax negaatioi tämän evaluaation, sekä alpha ja beta arvot jne, että ne on aina siirtäjän näkökulmasta.

Ohjelmalla on myös graafinen käyttöliittymä, joka pyörii eri säikeessä itse pääalgoritmin kanssa. Se on luokassa Game. Kun pelaaja miettii tai tekee siirron, algoritmi aloittaa pohtimaan taustalla parasta siirtoa eri säikeessä, ja näyttää myös alhaalla sen hetkisen parhaiden siirtojen sarjan. Oikealla näkyy algoritmin löytämä evaluaatio tilanteelle jos parhaat siirrot suorittaa pohdintasyvyyteen asti siitä hetkestä.

## Aika ja tilavaativuudet
Perus minimax algoritmin aikavaativuus on O(h<sup>s</sup>), missä h on keskimääräisten haarojen määrä solmua kohden, ja s on haettava syvyys. Alpha-beta saa puolitettua keskimääräisesti syvyyden, eli toisinsanoen alpha-beta pystyy hakemaan noin kahdesti syvemmälle puussa samassa ajassa, jos paras siirto haetaan ensimmäisenä O(h<sup>s/2</sup>) = O(√h<sup>s</sup>). Itse käytän useaa eri nopeutustapaa lisäksi, jotka karsivat haaroja paljon enemmän. Tästä ei pysty sanomaan tarkkaa aikavaativuutta, koska arvot ovat keskimääräisiä haarojen määrästä ja pelitilanteesta riippuen. Tuuri vaikuttaa haarojen karsintaan paljon, esim pelitilanne on sellaisessa kohdassa että algoritmi sattumalta löysi parhaan siirron jo syvyydellä 1, ja pystyi siksi karsimaan paljon enemmän. Tämä vaihtelee O(1<sup>s</sup>) ja O(h<sup>s</sup>) välillä. Shakin keskimääräinen branching factor on luokkaa 35, keskimäärin tämä tuntuu minulla olevan n. 4-7 O(4<sup>s</sup>). Tässä yksi esimerkki syvyyteen 9 asti. Jos keskimääräisten haarojen määrä on 4, niin 4^9 = 262144, kun testatessa algoritmi kävi 249806 solmua läpi syvyydellä 9.


Depth: 1         Move: Nc3 value: 50    took: 26 ms (max search depth: 1)        nodes: 49\
Depth: 2         Move: Nc3 value: 0     took: 30 ms (max search depth: 2)        nodes: 121\
Depth: 3         Move: Nc3 value: 50    took: 92 ms (max search depth: 4)        nodes: 292\
Depth: 4         Move: Nc3 value: 0     took: 135 ms (max search depth: 6)       nodes: 992\
Depth: 5         Move: Nc3 value: 40    took: 346 ms (max search depth: 9)       nodes: 3860\
Depth: 6         Move: Nc3 value: 0     took: 1174 ms (max search depth: 10)     nodes: 21453\
Depth: 7         Move: Nc3 value: 20    took: 1174 ms (max search depth: 11)     nodes: 31435\
Depth: 8         Move: Nc3 value: 0     took: 4054 ms (max search depth: 14)     nodes: 138424\
Depth: 9         Move: Nc3 value: 20    took: 6764 ms (max search depth: 16)     nodes: 249806\
<br>
Time taken total: 13801.8708 ms
<br><br>

Tilavaativuus algoritmilla on O(h \* s), koska jokaista syvyyttä kohden sen hetkisellä aktiivisella haaralla tarvitsee pitää muistissa niiden lapset. Mutta itsellä on käytössä myös transposition taulu, joka pitää kirjaa käydyistä solmuista, ja niiden arvoista, ja parhaasta siirroista. Tämä ei ole loputtomasti kasvava taulu, solmuja korvataan kun tulee kollisio, mutta se vie tietysti paljon enemmän tilaa kuin pääalgoritmi itsenään, joten en sitä tähän laske, kun on vain algoritmia nopeuttava ominaisuus, eikä siksi vaadittava.


Lisää suorityskykyvertailusta [Testausdokumentissa](Dokumentaatio/Testausdokumentti.md).


## Lähteet:
* https://en.wikipedia.org/wiki/Alpha-beta_pruning
* https://www.uio.no/studier/emner/matnat/ifi/INF4130/h17/undervisningsmateriale/chess-algorithms-theory-and-practice_ver2017.pdf
