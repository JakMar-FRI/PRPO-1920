## Opravljanje predmeta
* 50% projekt pri vajah
* 50% kolokviji/izpit
> ### Kolokviji
> v času predavanj (**oba** nad 50% + **povprečje** nad 50%)
>1. kolokvij: 18.11.2019
>2. kolokvij: 20.01.2020

> "*Več znate, več zaslužite*"
> Jurič

# Razvoj IT arhitektur

![](./pics/001.png)
Vrste uporbniškega vmesnika:
*   CLI - uporabniški vmesnik v konzoli
*   GUI - grafični vmesnik, program moramo prej namestiti na končno napravo (Word,...), grafični elementi operacijskega elementa
*   Web - vmesnik se izvaja v browser-ju
    * Server side web development SSWD
        * na client se prenese (večinoma) čisti HTML
        * vsaka sprememba se mora ponovno prenesti s strežnika
    * Client side web development CSWD (SPA - single page application)
        * izvajanje na clientu (JS)

## IT agilnost podjetji
1.  **Doba centralnih računalnikov** 
    * batch/množične obdelave (pripis obresti 1x mesečno, plače 1x mesečno,...), velika količina podatkov 1x v *časovne obdobju*
    * OLTP online transakcije
2.  **Dvo-nivojska arhitektura** - strežnik odjemalec (*fat client* - na klientu je veliko podatkov, na strežniku samo podatki (baza))
3.  **Tro-nivojska arhitektura** (internet) - ločujemo front, backend in podatke
*   premik iz razmišljanja o enotni aplikaciji v aplikacijo kot sestavljeno iz različnih delov
4.  **SOA - server oriented application** - aplikacija je sestavljena iz množice storitev (aplikacijo sestavimo iz "sestavnih delov")
5.  **Mikrostoritve**
    * *Iz katerih sestavnih delov naj bo sestavljena aplikacija?*
        * Spletna trgovina ![](./pics/002.png)
    * Prednosti
        * večja preglednost izvorne kode
        * manjša možnost sesutja celotne aplikacije (bolj robustna)
        * hitrejša (boljša možnost optimizacije)
        * skalabilnost (poganjanje instanc posameznih komponent) - FE se ne skalira, v BE skaliramo podatke z uporabo NoSQL (SQL se ne skalirajo tako dobro zaradi *cap teorema*)
    * Slabosti
        * API - komunikacija med komponentami (ozko grlo)

## IT arhitektura
### Software platforma
Skupek strežnikov storitev, ki jih za poganjanje aplikacije potrebujemo:
*   web server
*   file server
*   database server (DBMS)
*   IAM - identity server (npr. OAuth2)

### Namestitev aplikacije
**Monolitna** namestitev - aplikacijo namestimo v končni sistem v enem kosu
**Mikrostoritvena** namestitev - vsako komponento posebej namestimo (*npr. z uporabo Dockerja*)

> *Űber*\
> Jurič

>### Java EE
>| vrsta | datoteke |
>|---|---|
>|Java SE | JAR |
>|Java EE *zdaj Jakarte EE* | WAR, EAR |


>### Skalabilnost
> ![](./pics/skalabilnost.png)
> Skalabilnost nam pove kaj se bo zgodilo z odzivnim časom, ko povečujemo število aktivnih uporabnikov. Težimo k temu, da je skalabilnost sistema čim boljša.
> ![](./pics/skalabilnost001.png)
>#### Vertikalna skalabilnost
>Povečujemo strojne zmogljivosti aplikacije (CPU, RAM, network)
>#### Horizontalna skalabilnost
>Zmožnost poganjanja večih instanc posamezne komponente (potreno pravilno programiranje)

>### Cap teorem
> C - transakcijska konsistentnost podatkov
> A - razpoložjivost podatkov v konsistentni obliki v vseh vozliščih
> P - particijska enostavnost: enostavnost horizontalne skalabilnosti
> izbereš lahko največ dve od treh\
> ![](./pics/CAP001.png)

> *"Če zaradi tega vržete puško v koruzo in ne govorite z Metko, je dobro, da hodite na ta predavanja"*
> Jurič, 21.10.19

### Vrste SQL baz
#### ACID baze
**A**tomicity, **C**onsistency, **I**solation, **D**urability
(Atomarnost, Konsistentnos, Izolacija, )
Običajno SQL relacijske baze
#### BASE baze
Pomembnejša je razpoložljivost od razpoložjivosti

## Upravljanje z odvisnostmi
Uporabljamo za avtomatizacijo režije:
*   upravljanje z odvisnostmi
*   build cycle - priprava za namestitev aplikacije
Primeri: *Maven*, Gradle.
Project Object Model (POM)

> ## Okolje aplikacije
1.  **Razvojno okolje** (DEV)
2.  **Testno okolje** - testira QA
3.  **User testno okolje** - še eno testno okolje, testirajo končni uporabniki
4.  **Produkcijsko okolje** - okolje, kjer se izvaja aplikacija

# Maven
> ## Verzioniranje
> **a.b.c-okolje** *(npr. 1.0.0)*
> * *a* - major
> * *b* - minor
> * *c* - release
> * *okolje* - SNAPSHOT (testno okolje)

## Prednosti uporabe Maven
*   boljša vidnost in transparentnost razvojnega procesa
*   apliciranje splošno sprejetih dobrih praks (verzioniranje)
*   stndarizacija (enotna struktura projektov)
*   upravljanje z odvisnostmi
*   samodejno generiranje spletne strani in dokumentacije

## POM - Project Object Model
V datoteki **pom.xml** definiramo:
| lastnost | definicija |
|--|--|
|naziv projekta| `<name>` |
|verzija|`<version>`|
|odvisnosti|`<dependencies><dependency>`|
|cilji (goals)||
|vtičniki (plugins)||
|metapodatki||

*   V `pom.xml` datoteki so obvezni podatki `groupID`, `artifactId`, `version` in `modelVersion`.
*   Lahko uporabljamo koncept dedovanja
*   Vsak `pom.xml` deduje od super POM-a


### Primer POM datoteke
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0">
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.demo</groupId>
    <artifactId>projektA</artifactId>

    <!-- privzeto jar (izpuščamo), ostale: war, ejb, rar, ear, pom, custom-->
    <packaging>war</packaging>
    <version>0.0.1-SNAPSHOT</version>

    <dependencies>
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit </artifactId>
            <version>4.8.1</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
</project>
```
*Ustvari artefakt `projektA-0.0.1-SNAPSHOT.war`*

### Dedovanje POM datotek
![](./pics/maven001.jpg)
*Projekti B, C, D dedujejo verzijo, groupId, način pakiranja in vse druge odvisnosti in konfiguracijo vtičnikov, če nimajo sami drugače definirano.*

### Agregacija modulov
![](./pics/maven002.jpg)
*Vsi ukazi nad A se izvdejo tudi nad B, C in D.*

## Maven življenjski cikli
*   **default** - namenjen korakom buildanja in nameščanja, najpomembnejši cilji (izvedejo se tudi vse predhodne faze):
    *   validate
    *   compile (.jar file iz artifactID in verzije)
    *   package - naredil compile in sestavi jar file
*   **clean** - čiščenje za seboj (predhodne builde)
    *   pre-clean
    *   clean
    *   post-clean
*   **site** (oblikovanje dokumentacije)
    *   pre-site
    *   site
    *   post-site
    *   site-deploy

Building iz komandne vrstice:
```cpp
    mvn clean
    //--

    // dobimo .class file
    mvn compile
    //--

    // compile & .jar file
    mvn package
    //--

    mvn run

    //kombinacija dveh ciljev
    mvn cleane package
```

## Struktura Maven projekta
![](./pics/maven003.jpg)

## Prakse pri uporabi Maven
### Slaba praksa: podvajanje odvisnosti
Podvajanje odvisnosti za vsak projekt (*npr. kopiranje v mapo lib*).
### DObra praksa: uporaba binarnega repozitorija
**Repozitorij** je skupna lokacija za vse odvisnosti projektov, prednosti:
*   obstaja samo ena kopija
*   odvisnosti so shranjene izven projekta
*   odvisnosti so definirane v `pom.xml`
Privzeti oddaljen repozitorij je **maven control** (*repo1.maven.org*), uporabljamo lahko tudi druge. **Organizacijski repozitorij** hrani vse artefakte, ki izboljšujejo varnost in hitrost. **Lokalni repozitorij** predstavlja predpomnilnik za artefakte iz oddaljenih repozitorijev.

## Izdelava dokumentacije
Spletno stran z dokumentacijo generiramo z uporabo ukaza `mvn site`, spletne strani se nahajajo v mapi `target/site`.

# JDBC - Java Database Connectivity

Pošiljanje SQL stavkov podatkovni bazi:
*   **Statement** - pošilja navaden SQL, ni dobra praksa, ker je navaden String
*   **Prepared statement** - pošilja navaden SQL, predpošiljanje, lahko si PB pripravi način izvajanja, chekira za vdore
*   **Callable statement** - PL/SQL (ni dobra praksa)

## Koraki pri uporabi JDBC
### Korak 1: Nalaganje gonilnika

### Korak 2: Sestvljanje URL niza za povezavo na bazo

### Korak 3: Vzpostavljanje povezave


### Korak 4: Kreiranje objekta ```Statement```, ```PreparedStatement``` ali ```CallableStatement```

### Korak 5: Izvršitev SQL povpraševanj ali shranjenih procedur

### Korak 6: Obdelava rezultatov

Če ne poznamo strukture tabele, jo pridobimo s klicem metode ```getMetaData()```.

### Korak 7: Zapiranje povezave

> *"Imate prijaznega asistenta letos.... šalim se malo"*\
> Jurič, 21.10.19

## JDBC Connection Pool

Možnosti povezave na podatkovno bazo:
1.  Vsaka mikrostoritev ima svojo povezavo (slaba izkoriščenost povezav)
2.  Koncept bazen povezav - določeno število v naprej prirpavljenih povezav na PB. Vsaka storitev se poveže na connection pool. Storitev ima občutek, da ima svojo povezavo, v resnici bazen sproti določa povezavo, glede na potrebo storitve.

![](./pics/JDBC001.png)
*Connection Pool == **DataSource*** kje se nahaja programerja ne zanima (mikro storitev, PB...)

#### Data Source
**JNDI** ime Java Naming and Directory Interface

**LDAP** ?, JDNI je abstrakcija LDAP

1.  Iz LDAP pridobimo povezavo do Data Sourca (naredimo JDNI lookup, CDX lookup)

> *"Pasvord"*\
> Jurič

> *"Kolegice in kolegi*\
> Jurič

**RDBS**
**TPM**

## Dobre prakse uporabe JDBC
### Ne-mešanje poslovne logike in JDBC

Ne delaj tega
```java
public boolean preveriStanjeUp (...) {
    //JDB koda
    ...
    //

    //poslovna logika
    ...
    //
}
```

Delaj tako:
```java
public boolean preveriStanjeUp (...) {
    //posebej narediš DAO (DataAccessObject)
    getStatus(...);

    //naprej pišemo poslovno logiko
    ...
}
```

V DAO imamo tudi ```getUporabnik()```, kjer dobimo "pravega uporabnika". Za ta namen uporabimo ```Java Zrno (Java Bean)``` s prilagojenimi ```get``` in ```set``` metodami. Takšnemu zrnu pravimo ```DTO (Data Transfer Object)```.

Zakaj?
*   razdelimo odgovornost
*   lahko spreminjamo podatkovno bazo brez spreminjanja poslovne logike

## DAO (Data Access Object)

### DAO vzorec

![](./pics/DAO001.jpg)

### Generiranje baznega DAO
```java
public interface BaseDao {

}
```

> 
> ## Serializacija
> > V eni JVM se objekti pošiljajo kot *pass-by-reference*
> Avomatski postopek pretvarjanja iz stanja objekta v tok podatkov za pošiljanje objekta med različnimi JVM v omrežju. Obstajata dve vrsti serializacije:
> *   binarna serializacija (podpira npr. ciklične grafe)
> *   markup serializacija (JSON ali ??) - pretvarjanje direktno iz in v objekte > (podpira zgolj hierarhične podatkovne modele)
> 
> ```java
> 
> public Razred seriazibilen implements Serializable  {
> 
> }
> 
> ```

# Java Persistence API
Namesto, da za vsak objekt napišemo DAO in DTO, ali lahko napišemo samo DTO?\
![](./pics/003.png)

Za to poskrbi **Objektno-relacijski preslikovalniki (ORM)**, ki obstajajo v večini programskih jeziki. Najbolj poznano je *Hibernate*, v Javi se programski vmesnik imenuje **JPA - Java Persistence Application**.

![](./pics/JPA001.png)

![](./pics/JPA002.jpg)

![](./pics/JPA002.png)

## Anotiranje entitetnih razredov

```@Table``` spreminja privzeto ime tabele (drugače uporabimo ime razreda)\
```@Column``` spreminja privzeto ime stolpca (drugače uporabimo ime spremenljivke)

```java
//koda iz slajda 28
```

### ```@SecondaryTable```

```java

@Entity
@SecondaryTables

//slajd 29

```

### ```@Id```
Označuje atribut, ki definira primarni ključ

### ```@Temporal``` "Kalendar"

### ```@Trannsient```
Spremenljivka, ki je nočemo, da je shranjena v podatkovni bazi.

### ```@Enumerated```

### ```@ElementCollection``` in ```@CollectionTable```

```java

@Entity
public class Oseba {
    @Id
    private String id;
    private String ime;
    private String priimek;
    @ElementCollenction (fetch = FetchType.LAZY) //LAZY ali EAGER
    @CollectionTable (name = "Zaznamek")
    @Column (name = "vrednost")
    private ArrayList <String> zaznamki;
}

```

## Relacije med entitetami


## Dedovanje

## Struktura Java Persistence APIja
![](./pics/jpa003.png)

> *"Kolegice in kolegi, tako mimogrede"*
> **Design paterns**
> Najboljše rešitve nekih tipičnih problemov s katerimi se srečamo pri programiranju.
>
>Zbirka *design paternov* **GoF** - *Gang of Four*. [Mogoče ta](https://en.wikipedia.org/wiki/Gang_of_Four), [verjetno ta](https://en.wikipedia.org/wiki/Design_Patterns).

> **UML CLASS DIAGRAM**
>
> ![](./pics/UML001.png)
> spremenljivke in metode lahko izpustimo

# Nivo poslovne logike in CDI

## CDI - Contexts and Dependancy Injection
1. Zagotavlja kontekst izvajanja - komponente imajo določen življenski cikel in iteracije glede na jasno.definirane in razširljive kontekste
![](./pics/CDI001.png)
2. Vstavljanje odvisnosti omogoča vstavljanje referenc na posamezne komponente znotraj aplikacije

### CDI zrna
So razredi, ki jih instancira, upravlja in vstavlje CDI vsebnik.

## CDI container
CDI vsebnik skrbi za:
*   življenski cikel 
    * vsebnik sam ustvarja nove instance razreda (določamo samo scope - ```reguest, session, application```)
    ![](./pics/JDC001.jpg)
*   vstavljanje odvisnosti

![](./pics/CDI002.png)

> *"Kaj bluzi Jurič"*\
> Jurič, 4.11.2019

## Interceptor
Metoda, ki omogoča, da se pred ali po izvedbi neke metode izvedemo še neko drugo kodo (metodo) avtomatsko.

```java
@MojPrestreznik
public void nekaDrugaMetoda (int id) {
    //preden se izvede nekaDrugaMetoda se izvede metoda MojPrestreznik
}
```
```MojPrestreznik()``` je definiran v CDI zrnu.

## CRUD ukazi
**C** - create, **R** - read, **U** - update, **D** - delete ukazi

# Loggiranje v Javi

## Knjižnice, ki podpirajo loggiranje
*   JUL (Java Util Logger) - že vključena
*   LOG4J 1 in 2
*   SLF4J

> *Pol so prišli malo boljši časi, manj stari časi.*
> Jurišič, 11.11

# REST

## Zgodovinski razvoj vmesnikov
*   binarni
    *   RPC
    *   CORBA
    *   RMI
*   text
    *   SOAP (XML), opišemo z WSDL
    *   REST (JSON, XML, ali katerikoli MIME), opis ni potreben (ponavadi Swegen/Open API)
*   gRPC (spet binarni)

![](./pics/rest01.png)

## API in HTTP metode

```java
vrniSeznamArtiklov();
izvediPlacilo();
dodajArtikel();
posodobiArtikel();
```

Za dostop do metod uporabimo ```HTTP``` metode:

|HTTP metoda|URL||
|--|:--:|--|
|```GET```|```/razmerje```|pridobi seznam razmerij|
|```GET```|```/razmerje/345```|pridobi razmerje z *DI 345*|
|```POST```|```/razmerje```|ustvari novo razmerje|
|```PUT```|```/razmerje/345```|posodobi razmerje z *ID 345*|
|```DELETE```|```/razmerje/345```|izbriše razmerje z *ID 345*|

*Vire oblikujemo enostavno in učinkovito (grobo zrnato, samostalniki v množini)*
Ustvarjamo lahko nove pod vire ```GET artikel/345/akcija```


| Vir zbirke | Vir instance |
|--|--|
|/razmerja|/razmerja/*id_razmerja*|

> *"A se še kaj spomnite slovenščine iz srednje šole? Samostalnik, pridevnik,... Šalim se, saj vem da se"*
> Jurič

### ```GET``` za branje vira

#### ```GET``` za branje določenega vira

#### `GET` na viru

|Tip|URL|
|--|--|
|Ostranjevanje|`/artikli?start=0;offset=0`|
|Filtracija (iskanje)|`artikli?q='...'`, `artikli?where=vrednost:gte:512`|
|Sort|`artikli?sort='...'`, `artikli?order=naziv ASC, prioriteta DESC`|


### ```POST``` za ustvarjanje

### ```PUT`` za posodabljanje

### ```DELETE``` za brisanje

> ### *Medklic*: minor/major verzije in kompatibilnost za nazaj
> **Minor** verzije (1.0, 1.1, 1.2) so kompatibilne za nazaj
> **Major** verzije (1.x, 2.x) niso kompatibilne za nazaj - različne major verzije imajo svoj url ```api.url/v1/...```, ```api.url/v2/...```

## Fornat sporočil

### JSON

#### JSON header
 Header si zamislimo sami (pri vseh API klicih naj bo *bolj ali manj* enak). V njem definiramo podatke kot so vsi artikli, preneseni artikli, *offset*...
![](./pics/rest02.png)

