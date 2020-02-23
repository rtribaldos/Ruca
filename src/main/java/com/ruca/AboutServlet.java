package com.ruca;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

// Referenced classes of package com.ruca:
//            AdminServlet

public class AboutServlet extends HttpServlet {

	private static final long serialVersionUID = -6419198060064774759L;

	public AboutServlet() {
		super();
	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp) {
		try {
			UserService userService = UserServiceFactory.getUserService();
			User user = userService.getCurrentUser();
			String authURL = userService.createLogoutURL("/");
			req.setAttribute("authURL", authURL);
			req.setAttribute("user", user);
			String op = req.getParameter("op");
			System.out.println((new StringBuilder("Op:")).append(op).toString());
			RequestDispatcher dispatcher = req.getRequestDispatcher("reformas.jsp");
			if (op != null && op.equals("reforma")) {
				dispatcher = req.getRequestDispatcher("reformando.jsp");
			}
			dispatcher.forward(req, resp);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
