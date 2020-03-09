package com.model;

import java.io.IOException;
import java.util.Collections;

import javax.jdo.PersistenceManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.model.dao.GaleriaDAO;
import com.model.utils.PhotoComparator;
import com.ruca.config.LogsManager;

// Referenced classes of package com.model:
//            PMF, Gallery

public class Upload extends HttpServlet {

	private static final long serialVersionUID = -595062092925430080L;
	
	private BlobstoreService blobstoreService;

	public Upload() {
		blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		RequestDispatcher dispatcher = null;
		if (user.getEmail().equalsIgnoreCase("raultribaldos@gmail.com")
				|| user.getEmail().equalsIgnoreCase("gracialafamilia@gmail.com")
				|| user.getEmail().equalsIgnoreCase("alexei_lescaylle@yahoo.es")) {
			String authURL = userService.createLogoutURL("/");
			String uploadURL = blobstoreService.createUploadUrl("/post");
			req.setAttribute("uploadURL", uploadURL);
			req.setAttribute("authURL", authURL);
			req.setAttribute("user", user);
			String gallery = req.getParameter("galeria");
			if (gallery != null && (gallery.equals("obraNueva") || gallery.equals("decoracion")
					|| gallery.equals("oficinas") || gallery.equals("reformas"))) {
				try {
					cargaGallery(req, resp, gallery);
					if (gallery.equals("obraNueva")) {
						dispatcher = req.getRequestDispatcher("WEB-INF/templates/obraNueva.jsp");
					} else if (gallery.equals("oficinas")) {
						dispatcher = req.getRequestDispatcher("WEB-INF/templates/office.jsp");
					} else if (gallery.equals("reformas")) {
						dispatcher = req.getRequestDispatcher("WEB-INF/templates/ref.jsp");
					} else {
						dispatcher = req.getRequestDispatcher("WEB-INF/templates/deco.jsp");
					}
				} catch (Exception e) {
					LogsManager.showError(e.getMessage(), e);
				}
			} else {
				PersistenceManager pm = PMF.get().getPersistenceManager();
				Gallery principal = null;
				try {
					principal = GaleriaDAO.getGalleryByName(pm, "principal");
					if (principal != null && principal.getFotos() != null && principal.getFotos().size() > 0) {
						Collections.sort(principal.getFotos(), new PhotoComparator());
					}
				} catch (Exception e1) {
					LogsManager.showError(e1.getMessage(), e1);
				}
				req.setAttribute("principal", principal);
				dispatcher = req.getRequestDispatcher("WEB-INF/templates/admin.jsp");
			}
		} else {
			dispatcher = req.getRequestDispatcher("/index.jsp");
		}
		dispatcher.forward(req, resp);
	}

	public void cargaGallery(HttpServletRequest req, HttpServletResponse resp, String galeria) throws Exception {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Gallery gallery = GaleriaDAO.getGalleryByName(pm, galeria);
		if (gallery != null && gallery.getFotos() != null && gallery.getFotos().size() > 0) {
			Collections.sort(gallery.getFotos(), new PhotoComparator());
		}				
		req.setAttribute("galeria", gallery);
	}
}
