package si.fri.prpo.jakmar.servlet;

import si.fri.prpo.jakmar.jdbc.BaseDao;
import si.fri.prpo.jakmar.jdbc.Entiteta;
import si.fri.prpo.jakmar.jdbc.Uporabnik;
import si.fri.prpo.jakmar.jdbc.UporabnikDaoImpl;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/servlet")
public class JdbcServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html; charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");

        System.out.println("To piše v konzoli");
        resp.getWriter().print("<h1>To piše v brskalniku</h1>");

        BaseDao uporabnikDao = new UporabnikDaoImpl();

        vsiUporabniki(uporabnikDao, resp);

        Uporabnik u = new Uporabnik("Kekec", "Peter", "pero");
        uporabnikDao.vstavi(u);
        vsiUporabniki(uporabnikDao, resp);
    }

    private void vsiUporabniki (BaseDao uporabnikDao, HttpServletResponse resp) throws IOException {
        System.out.printf("\n\nSeznam vseh uporabnikov:\n");
        resp.getWriter().printf("\n\nSeznam vseh uporabnikov:\n");

        List<Entiteta> vsiUporabniki = uporabnikDao.vrniVse();
        for (int i = 0; i < vsiUporabniki.size(); i++) {
            Uporabnik iti = (Uporabnik) vsiUporabniki.get(i);
            System.out.printf("%s\t%s\t%s\n", iti.getIme(), iti.getPriimek(), iti.getUporabniskoIme());

            resp.getWriter().printf("%s\t%s\t%s\n", iti.getIme(), iti.getPriimek(), iti.getUporabniskoIme());
        }
    }
}
