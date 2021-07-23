# Määrittelydokumentti

Teen shakki tekoälyn joka käyttää minimaxia alpha-beta pruningilla. Teen jonkunlaisen iterative deepening algoritmin, joka etenevästi hakee syvemmältä puusta, jotta aina olisi joku siirto valmiina pelattavaksi, ja tämän syvyyden voi myös jollain tavalla järjestää jotta alpha-beta pystyisi prunamaan enemmän seuraavalle syvyydelle. Tämä saattaa tarvita jonkun järjestämisalgoritmin myös (kuten merge-sort).

Alussa teen varmaan konsoliversion, jossa siirtoja tehdään numeroilla, ja myöhemmin teen laudan, jossa siirtoja tehdään hiirellä.

Ohjelmassa pystyy myös valitsemaan alussa pelaako tekoäly itseään vastaan vai pelaako ihminen sitä vastaan.

Alpha-beta algoritmin aikavaativuus on O(h<sup>s/2</sup>), missä h on keskimääräisten haarojen määrä solmua kohden, ja s on haettava syvyys. Tilavaativuus on O(h \* s). Jos tarvitsee tehdä järjestämisalgoritmia, niin sen aikavaativuus riippuu algoritmista, mutta se on merge-sortille O(n log n) ja tilavaativuus on O(n).

### Lähteet:
* https://en.wikipedia.org/wiki/Alpha-beta_pruning

Opinto-ohjelma: Tietojenkäsittelytieteen kandidaatti (TKT)
Projektin kieli: suomi