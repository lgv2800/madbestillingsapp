# Madbestillingsapp

### Lavet af Gruppe B på DTU Diplom E18

### Formål
Formålet med denne app er at indvolvere ældre beboere i bestilling af frokost på plejecentre.

### Backend
Der bruges AWS til hosting af .php-/HTML-side.
Samtidig hoster AWS vores CDN, således at ved opstart af app'en downloader den en .JSON fra AWS, som der derefter bliver genereret fragmenter af.
.JSON-filen beskriver titel og beskrivelse af retten, men har et URL til at hente det korrekte billede fra vores CDN. Det gør app'en fleksibel, da den holder sig opdateret,men det kan også virker langsomt, hvis man er på meget langsomt netværk.
Der benytter Glide til at få importeret billede fra CDN til placeholder-billede, som gemmer sig i /res/mipmap.

Hver ret bliver gemt som SharedPreference, som bliver lavet om til et HashMap, når man starter aktiviteten CartScreen.
Logikken til HashMap'et og generelt logikken, der holder styr på ordrerne findes i Order.class.

![Skærmbilleder fra 21/1 2019](https://imgur.com/a/ZPEFNmM, "Hovedmenu")

Der bliver på nuværende tidspunkt generet to lister, én til visning af retter i ListView, én til bestilling til databasen. Oprindeligt var der kun én liste - men der blev retterne sendt til databasen i det pågældende sprog, som appen var sat til.

Skrevet af Oliver d. 23/1 2019