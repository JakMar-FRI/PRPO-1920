package si.fri.prpo.jakmar.jdbc;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class UporabnikDaoImpl implements BaseDao {
    private static UporabnikDaoImpl instance;

    private static UporabnikDaoImpl getInstance() {
        if (instance == null)
            instance = new UporabnikDaoImpl();
        return instance;
    }

    private Connection connection;
    private Logger log = Logger.getLogger(String.valueOf((instance)));

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
                entitete.add(getUporabnik(rs));
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
