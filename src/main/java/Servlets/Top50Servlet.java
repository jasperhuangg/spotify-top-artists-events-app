package Servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import PersonalizedTop.UserTopArtists;
import Top50.Top50Artist;

@WebServlet("/Top50Servlet")
public class Top50Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
//		HttpSession session = req.getSession(true);
		String OAuthCode = req.getParameter("oauthcode");
//		System.out.println(OAuthCode);
		UserTopArtists uta = new UserTopArtists();
		uta.setOAuthCode(OAuthCode);
		uta.SetAccessToken();

		List<Top50Artist> top50Artists = uta.GetUsersTopArtists();

//		Top50 top50 = new Top50();
//		List<Top50Artist> top50Artists = top50.getTop50Artists();

		// original code
		String top50ArtistsJson = new Gson().toJson(top50Artists);
		res.setContentType("application/json");
		res.setCharacterEncoding("UTF-8");
		res.getWriter().write(top50ArtistsJson);

//		session.setAttribute("top50list", top50Artists);
//
//		RequestDispatcher dispatch = getServletContext().getRequestDispatcher(req.getContextPath() + "/index.jsp");
//		dispatch.forward(req, res);
	}

}
