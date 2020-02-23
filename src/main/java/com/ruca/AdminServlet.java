package com.ruca;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.model.Gallery;
import com.model.PMF;
import com.ruca.config.LogsManager;

public class AdminServlet extends HttpServlet {
	
	private static final long serialVersionUID = 5205089470446376084L;

	public AdminServlet() {
		super();
	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp) {
		try {
			UserService userService = UserServiceFactory.getUserService();
			User user = userService.getCurrentUser();
			String authURL = userService.createLogoutURL("/");
			req.setAttribute("authURL", authURL);
			req.setAttribute("user", user);
			RequestDispatcher dispatcher = null;
			String galeria = (String) req.getAttribute("galeria");
			if (galeria == null) {
				galeria = req.getParameter("galeria");
			}
			String destino = "admin.jsp";
			if (galeria != null && !galeria.trim().isEmpty()) {
				if (galeria.equals("0")) {
					destino = "galeria.jsp";
				} else {
					//PersistenceManager pm = PMF.get().getPersistenceManager();
					Gallery gallery = new Gallery(galeria);
					PMF.get().getPersistenceManager().makePersistent(gallery);
					req.setAttribute("galeria", galeria);
				}
			}
			dispatcher = req.getRequestDispatcher(destino);
			dispatcher.forward(req, resp);
		} catch (Exception e) {
			LogsManager.showError(e.getMessage(), e);
		}
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		try {
			UserService userService = UserServiceFactory.getUserService();
			User user = userService.getCurrentUser();
			String authURL = userService.createLogoutURL("/");
			req.setAttribute("authURL", authURL);
			req.setAttribute("user", user);
			String galeria = req.getParameter("galeria");
			if (galeria != null && !galeria.trim().isEmpty()) {
				//PersistenceManager pm = PMF.get().getPersistenceManager();
				Gallery gallery = new Gallery(galeria);
				PMF.get().getPersistenceManager().makePersistent(gallery);
				req.setAttribute("galeria", galeria);
			}
			RequestDispatcher dispatcher = req.getRequestDispatcher("panel.jsp");
			dispatcher.forward(req, resp);
		} catch (Exception e) {
			LogsManager.showError(e.getMessage(), e);
		}
	}

}
