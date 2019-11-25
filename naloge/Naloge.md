# Naloga 1: priprava servleta
## Kreiranje Maven projekta
V izbrane IDE orodju (npr. IntelliJ) kreiramo nov Maven projekt pri katerem določimo:
*   `groupId`, ki predstavlja ime organizacije/ekipe v obliki *si.fri.prpo.jakmar*
*   `artifactId`, ki predstavlja ime projekta, npr. *hello-world*

## Dopolnitev `pom.xml` datoteke
### Ime in opis
```xml
<name>Hello-world</name>
<description>Prvi servlet v Javi</description>
```

### Lastnosti (`properties`)
V znački `properties` definiramo verzije maven kompilerja, znakovno kodiranje in ostale potrebne spremenljivke *(v našem primeru verzijo kumuluza)*.

```xml
<properties>
    <maven.compiler.source>1.8</maven.compiler.source>
    <maven.compiler.target>1.8</maven.compiler.target>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <kumuluzee.version>3.5.0</kumuluzee.version>
</properties>
```

### `dependencyManagement`
```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>com.kumuluz.ee</groupId>
            <artifactId>kumuluzee-bom</artifactId>
            <version>${kumuluzee.version}</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

### `dependencies`
```xml
<dependencies>
    <dependency>
        <groupId>com.kumuluz.ee</groupId>
        <artifactId>kumuluzee-core</artifactId>
    </dependency>
    <dependency>
        <groupId>com.kumuluz.ee</groupId>
        <artifactId>kumuluzee-servlet-jetty</artifactId>
    </dependency>
</dependencies>
```

### `build`
```xml
<build>
    <plugins>
        <plugin>
            <groupId>com.kumuluz.ee</groupId>
            <artifactId>kumuluzee-maven-plugin</artifactId>
            <version>${kumuluzee.version}</version>
            <executions>
                <execution>
                    <id>package</id>
                    <goals>
                        <goal>repackage</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

## `config.yaml`
V mapi **src/main/resources** ustvariš datoteko **config.yaml** z vsebino:
```yaml
kumuluzee:
  name: prvi-servlet
  version: 1.0.0
  env:
    name: dev
```

## Implementacija servleta
### `package`
V mapi **src/main/java** ustvarimo nov javanski paket (npr. *si.fri.prpo.jakmar.servlet*).

### Servlet
Ustvarimo novo javansko datoteko (npr. *JdbcServlet.java*), ki naj se nahaja znotraj ustvarjenega paketa. Razred naj razširja razred `HttpServlet`.

Razred naj vsebuje anotacijo `@WebServlet` z eni argumentom, ki pove relativno lokacijo servleta (*npr. /servlet se nahaja na naslovu localhost:8080/servlet*):
```java
@WebServlet("/servlet")
public class PrviJdbcServlet extends HttpServlet {...}
```

Razred naj ima implementiramo metodo `doGet`
```java
@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // implementacija

    }
```

Importi in package:
```java
package si.fri.prpo.jakmar.servlet;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;
```
> V kolikor IntelliJ ne najde javax.servlet ga prenesite v obliki jar datoteke s pritiskom `alt+shift+enter` na vrstici importa.

#### Implementacija `doGet()` metode

Najprej definiramo tip konteksta in znakovno kodiranje:
```java
resp.setContentType("text/html; charset=UTF-8");
resp.setCharacterEncoding("UTF-8");
```

Besedilo, ki se izpiše v konzoli, definiramo s `System.out`, besedilo v brskalniku pa prek responsa posredujemo servletu:
```java
System.out.println("To piše v konzoli");
resp.getWriter().print("<h1>To piše v brskalniku</h1>");
```

## Poganjanje projekta
Projekt najprej zapakiramov `.jar` datoteko:
```
mvn clean package
```

Nato ga zaganjamo z ukazom:
```
java -jar tagret/<artifactId>-<version>.jar
```

Do vsebine servleta dostopa v brskalniku preko prej vnešenega naslov.
*Npr. @WebServlet("/servlet") najdemo na:*
```
localhost:8080/servlet
```

**Celotna koda prve naloge je objavljena v repozitoriju, v branchu `naloga1`**.

# Naloga 2: dostop do baze z JDBC
## Priprava Docker vsebnika
### Instalacija Docker-ja
["Prava instalacija"](https://docs.docker.com/docker-for-windows/) - potrebna PRO verzija Windowsev, omogočen Hyper-v

["Legacy verzija"](https://docs.docker.com/toolbox/toolbox_install_windows/) - ni potrebna PRO verzija Windowsev, ustvari se Linux virtualka

### Priprava vsebnika
Docker vsebnik s postgresql bazo poženemo z ukazom:
```
docker run -d --name <ime vsebnika> -e POSTGRES_USER=<ime uporabnika> -e POSTGRES_PASSWORD=<geslo za dostop> -e POSTGRES_DB=<ime baze> -p <port za dostop> postgres:12
```

Privzeto za naš projekt:
```
docker run -d --name jdbc-baza -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=uporabniki -p 5432:5432 postgres:12
```
Privzeti uporabnik je `postgres`.

### Drugi uporabni Docker ukazi
|ukaz||
|---|---|
|`docker ps`|prikaže vse vsebnike, ki so trenutno zagnani|
|`docker ps -a`|prikaže vse vsebnike v sistemu (zagnane in ne zagnane)|
|`docker start <ime vsebnika>`|zažene vsebnik|
|`docker stop <ime vsebnika>`|ustavi vsebnik|
|`docker rm <ime vsebnika>`|odstrani vsebnik (vsebnik ne sme biti zagnan)|

## Ustvarjanje baze
### Povezovanje na bazo
Na bazo se povežemo z enim izmed orodji (IntelliJ npr.). Podatki za povezavo so:
```
host: localhost
port: 5432
user: postgres
password: postgres
database: uporabniki
URL: jdbc:postgresql://localhost:5432/uporabniki
```

### Ustvarjanje sheme, entitetnega tipa in entitete
```sql
create scheme uporabniki;
```

```sql
create table uporabniki.Uporabnik
(
	ime varchar(20) not null,
	priimek varchar(50) not null,
	uporabnisko_ime varchar(20) not null,
	id_uporabnika serial
		constraint Uporabnik_pk
			primary key
);

create unique index Uporabnik_uporabnisko_ime_uindex
	on uporabniki.Uporabnik (uporabnisko_ime);
```

```sql
insert into uporabniki.uporabnik(ime, priimek, uporabnisko_ime) VALUES ('Pinko', 'Palinko', 'pinkop');
```

## Konfiguracija dokumenta
### `config.yaml`
`config.yaml`:
```yaml
kumuluzee:
  name: <ime servleta:prvi-servlet>
  version: 1.0.0
  env:
    name: dev
  datasources:
    - jndi-name: jdbc/podatkovnaBaza
      connection-url: jdbc:postgresql://localhost:<port:5432>/<ime baze:uporabniki>
      username: <uporabnik:postgres>
      password: <geslo:postgres>
      max-pool-size: 20
```
### `pom.xml`
V pom datoteko dodamo (med `<dependencies>`):
```xml
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <version>${postgresql.version}</version>
</dependency>
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <version>${postgresql.version}</version>
</dependency>
```

Med spremelnljivke `<properties>`dodamo verzijo postgresa:
```xml
<postgresql.version>42.0.0</postgresql.version>
```

## Implementacija knjižnice za delo z entitetami s pomočjo JDBC
### Abstraktni razred `Entiteta`
Abstraktni razred Entiteta ustvarite v novem paketu, razred implementira vmesni *java.io.Serializable*.

Razred naj vsebuje enoličen identifikator za entiteto `Integer id` ter getter in setter metodi.

```java
package si.fri.prpo.jakmar.jdbc;

import java.io.Serializable;

public abstract class Entiteta implements Serializable {
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
```

### `Uporabnik.java`
Ustvarite nov javanski razred, katerega ime je identično imenu entitetnega tipa v podatkovni bazi (*npr. Uporabnik.java*).

Razred najrazširja razred Entiteta ter ima enaka polja kot jih ima tabela v podatkovni bazi. Metoda ima definirane tudi getter-je in setter-je.

```java
package si.fri.prpo.jakmar.jdbc;

public class Uporabnik extends Entiteta {
    private String ime;
    private String priimek;
    private String uporabniskoIme;

    public Uporabnik(String ime, String priimek, String uporabniskoIme) {
        this.ime = ime;
        this.priimek = priimek;
        this.uporabniskoIme = uporabniskoIme;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }
    ...
```

### Vzorec DAO
Najprej kreiramo vmesnik `BaseDao`, ta definira osnovne operacije nad entitetami, ki omogočajo branje in pisanje v bazo.
```java
package si.fri.prpo.jakmar.jdbc;

import java.sql.Connection;
import java.util.List;

public interface BaseDao {
    Connection getConnection();
    
    Entiteta vrni(int id);
    
    void vstavi(Entiteta ent);
    
    void odstrani(int id);
    
    void posodobi(Entiteta ent);
    
    List<Entiteta> vrniVse();
}
```

Sledi implementacija vmesnika v razredu `NasaEntitetaDaoImpl`, ki vsebuje JDBC kodo. Razred naj bo pripravljen po vzorcu `Singleton` (instanca se ustvari samo enkrat).
```java
private static UporabnikDaoImpl instance;

    private static UporabnikDaoImpl getInstance() {
        if (instance == null)
            instance = new UporabnikDaoImpl();
        return instance;
    }
```

Pridobivanje povezave na podatkovno bazo:
```java
@Override
    public Connection getConnection() {
        try {
            InitialContext intiCtx = new InitialContext();
            DataSource ds = (DataSource) intiCtx.lookup("jdbc/podatkovnaBaza");
            return ds.getConnection();
        } catch (Exception e) {
            log.severe("Na morem se povezati: " + e.getMessage());
        }
        return null;
    }
```

Pridobivanje entitete iz baze poteka v devh korakih:
```java
@Override
    public Entiteta vrni(int id) {
        PreparedStatement ps = null;
        try {
            if (connection == null)
                connection = getConnection();

            String sql = "SELECT * FROM uporabniki WHERE id = ?";
            ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next())
                return getUporabnik(rs);
            else
                log.info("Uporabnik ne obstaja");
        } catch (SQLException e) {
            log.severe(e.toString());
        } finally {
            if (ps != null)
                try{
                    ps.close();
                } catch (SQLException e) {
                    log.severe(e.toString());
                }
        }
        return null;
    }

    private Entiteta getUporabnik(ResultSet rs) throws SQLException {
        String ime = rs.getString("ime");
        String priimek = rs.getString("priimek");
        String uporabniskoIme = rs.getString("uporabnisko_ime");
        Uporabnik u = new Uporabnik(ime, priimek, uporabniskoIme);
        return u;
    }
```

Vstavljanje v bazo:
```java
@Override
    public void vstavi(Entiteta ent) {
        PreparedStatement ps = null;
        Uporabnik u = (Uporabnik) ent;
        try {
            if (connection == null)
                connection = getConnection();

            String sql = "INSERT INTO uporabniki (ime, priimek, username) VALUES (?, ?, ?)";
            ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, u.getIme());
            ps.setString(2, u.getPriimek());
            ps.setString(3, u.getUporabniskoIme());

            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()) {
                int id = rs.getInt(1);
                u.setId(id);
            }
        } catch (SQLException e) {
            log.severe(e.toString());
        } finally {
            if (ps != null)
                try{
                    ps.close();
                } catch (SQLException e) {
                    log.severe(e.toString());
                }
        }
    }
```

Odstranjevanje:
```java
@Override
    public void odstrani(int id){
        PreparedStatement ps = null;

        try{
            if (connection == null)
                connection = getConnection();

            String sql = "DELETE FROM uporabniki WHERE id = ?";
            ps = connection.prepareStatement(sql);
            ps.setInt(1, id);

            ps.executeUpdate();
        } catch (SQLException e) {
            log.severe(e.toString());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    log.severe(e.toString());
                }
            }
        }
    }
```

Posodabljanje:
```java
@Override
    public void posodobi(Entiteta ent){
        PreparedStatement ps = null;
        Uporabnik u = (Uporabnik) ent;

        try{
            if (connection == null)
                connection = getConnection();
            String sql = "UPDATE uporabniki SET ime = ?, priimek = ?, username = ? WHERE id = ?";
            ps = connection.prepareStatement(sql);
            ps.setString(1, u.getIme());
            ps.setString(2,u.getPriimek());
            ps.setString(3, u.getUporabniskoIme());
            ps.setInt(4, u.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            log.severe(e.toString());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    log.severe(e.toString());
                }
            }
        }
    }
```

Vračanje vseh entitet:
```java
@Override
    public List<Entiteta> vrniVse(){
        List<Entiteta> entitete = new ArrayList<Entiteta>();
        Statement st = null;

        try{
            if(connection == null){
                connection = getConnection();
            }

            st = connection.createStatement();
            String sql = "SELECT * FROM uporabniki";
            ResultSet rs = st.executeQuery(sql);

            while(rs.next()){
                entitete.add(getUporabnikFromRS(rs));
            }
        } catch (SQLException e){
            log.severe(e.toString());
        } finally {
            if(st == null){
                try{
                    st.close();
                } catch (Exception e){
                    log.severe(e.toString());
                }
            }
        }
        return entitete;
    }
}
```

## Izpis uporabnikov v servletu
