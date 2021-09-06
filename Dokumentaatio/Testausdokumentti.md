# Määrittelydokumentti

## Yksikkötestauksen kattavuusraportti
Yksikkötestauksen kattavuus näkyy [Pit raportissa](https://kapistelijajami.github.io/TiralabraShakki/Dokumentaatio/Pit%20raportti/)

## Mitä on testattu, miten tämä tehtiin?
Unit testit testailee koodin metodeita, ja suorituskykytestaus vertailee eri nopeutuskeinoja alpha-beta algoritmille. Jokainen testi käy syvyydet 1:stä 6:een asti, ja tulostaa niihin kuluneet ajat ja solmujen määrät jne. Lopussa kokonaisaika siirron palauttamiselle. Kuten siitä näkee, jokainen parannus alentaa suoritusaikaa ja solmujen määrää, joissain tapauksissa huomattavasti.

## Miten testit voidaan toistaa?
`mvn test`

Suorituskykytestauksen voi suorittaa antamalla ohjelmalle argumentiksi "performance":\
`java -jar ohjelma.jar performance`

# Suorituskykytestaus
Suorituskykytestaus suoritetaan ilman quiescence hakua, jotta tulokset olisivat lähempänä ideaalitapausta. Tulokset voi vaihdella ainoastaan shakkilaudan tilanteesta riippuen, mutta suorituskykytestaukset eivät vaihda laudan tilannetta, joten sen pitäisi olla hyvin samanlainen joka suorituskerralla, tietysti koneen tehoista riippuen. Solmujen määrä laskee myös nopeutustapojen lisääntyessä. Jokaisen testin alussa näkyy millä asetuksilla testi suoritetaan.

### Testaustulokset:

Performance tests, quiescence search off.

Only alpha-beta, depth 6:\
Depth: 1         Move: Nc3 value: 50    took: 20 ms (max search depth: 1)        nodes: 24\
Depth: 2         Move: Nc3 value: 0     took: 50 ms (max search depth: 2)        nodes: 193\
Depth: 3         Move: Nc3 value: 50    took: 252 ms (max search depth: 3)       nodes: 1356\
Depth: 4         Move: Nc3 value: 0     took: 712 ms (max search depth: 5)       nodes: 7256\
Depth: 5         Move: d4 value: 60     took: 3513 ms (max search depth: 6)      nodes: 62148\
Depth: 6         Move: Nc3 value: -20   took: 24663 ms (max search depth: 7)     nodes: 465349

Time taken total: 29215.1985 ms
<br><br>

Alpha-beta with aspiration window, depth 6:\
Depth: 1         Move: Nc3 value: 50    took: 1 ms (max search depth: 1)         nodes: 24\
Depth: 2         Move: Nc3 value: 0     took: 9 ms (max search depth: 2)         nodes: 153\
Depth: 3         Move: Nc3 value: 50    took: 83 ms (max search depth: 3)        nodes: 1354\
Depth: 4         Move: Nc3 value: 0     took: 298 ms (max search depth: 5)       nodes: 4928\
Depth: 5         Move: Nc3 value: 60    took: 4195 ms (max search depth: 6)      nodes: 59545\
Depth: 6         Move: Nc3 value: -20   took: 17077 ms (max search depth: 7)     nodes: 294483

Time taken total: 21666.5685 ms
<br><br>

Alpha-beta, aspiration window, sort hash move, depth 6:\
Depth: 1         Move: Nc3 value: 50    took: 1 ms (max search depth: 1)         nodes: 21\
Depth: 2         Move: Nc3 value: 0     took: 4 ms (max search depth: 2)         nodes: 60\
Depth: 3         Move: Nc3 value: 50    took: 26 ms (max search depth: 3)        nodes: 559\
Depth: 4         Move: Nc3 value: 0     took: 104 ms (max search depth: 4)       nodes: 1618\
Depth: 5         Move: Nc3 value: 60    took: 886 ms (max search depth: 6)       nodes: 18161\
Depth: 6         Move: Nc3 value: -20   took: 2599 ms (max search depth: 7)      nodes: 34758

Time taken total: 3623.4832 ms
<br><br>

Alpha-beta, aspiration window, sort hash move, null move pruning, depth 6:\
Depth: 1         Move: Nc3 value: 50    took: 1 ms (max search depth: 1)         nodes: 22\
Depth: 2         Move: Nc3 value: 0     took: 4 ms (max search depth: 2)         nodes: 69\
Depth: 3         Move: Nc3 value: 50    took: 8 ms (max search depth: 3)         nodes: 143\
Depth: 4         Move: Nc3 value: 0     took: 34 ms (max search depth: 4)        nodes: 672\
Depth: 5         Move: Nc3 value: 40    took: 81 ms (max search depth: 5)        nodes: 1636\
Depth: 6         Move: Nc3 value: -20   took: 891 ms (max search depth: 7)       nodes: 20377

Time taken total: 1020.6143 ms
<br><br>

Alpha-beta, aspiration window, sort hash move, null move pruning, late move reduction, depth 6:\
Depth: 1         Move: Nc3 value: 50    took: 1 ms (max search depth: 1)         nodes: 22\
Depth: 2         Move: Nc3 value: 0     took: 5 ms (max search depth: 2)         nodes: 69\
Depth: 3         Move: Nc3 value: 50    took: 7 ms (max search depth: 3)         nodes: 143\
Depth: 4         Move: Nc3 value: 0     took: 33 ms (max search depth: 4)        nodes: 660\
Depth: 5         Move: Nc3 value: 40    took: 94 ms (max search depth: 5)        nodes: 1955\
Depth: 6         Move: Nc3 value: -20   took: 827 ms (max search depth: 7)       nodes: 19336

Time taken total: 969.1364 ms
