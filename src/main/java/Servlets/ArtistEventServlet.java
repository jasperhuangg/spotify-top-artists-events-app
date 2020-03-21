package Servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

import Top50.Top50;
import Top50.Top50Artist;

@WebServlet("/ArtistEventServlet")
public class ArtistEventServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		HttpSession session = req.getSession(true);

		String requestedArtist = req.getParameter("requestedartist");
		System.out.println("ra: " + requestedArtist);
//		session.setAttribute("requestedartist", ra);
//		String requestedArtist = session.getAttribute("requestedartist").toString();
//		System.out.println("requested artist: " + requestedArtist);
		Top50 top50 = new Top50();
		List<Top50Artist> top50Artists = top50.getTop50Artists();

		Top50Artist artist = null;
		for (int i = 0; i < top50Artists.size(); i++) {
			if (top50Artists.get(i).getName().equals(requestedArtist)) {
				artist = top50Artists.get(i);
				artist.CreateEvents();
				break;
			}
		}

		String artistJson = new Gson().toJson(artist);

		res.setContentType("application/json");
		res.setCharacterEncoding("UTF-8");
		res.getWriter().write(artistJson);
//
//		session.setAttribute("artist", artist);
//
//		RequestDispatcher dispatch = getServletContext().getRequestDispatcher(req.getContextPath() + "/artist.html");
//		dispatch.forward(req, res);
	}
}
