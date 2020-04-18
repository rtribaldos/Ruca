package com.ruca;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;
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
import com.model.Gallery;
import com.model.MediaObject;
import com.model.PMF;
import com.model.dao.GaleriaDAO;
import com.model.utils.PhotoComparator;
import com.ruca.config.LogsManager;


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
		
		String borraFileName = req.getParameter("borrar");
		String subirOrden = req.getParameter("subirOrden");
		String bajarOrden = req.getParameter("bajarOrden");
		String ordenActual = req.getParameter("ordenActual");
					
		boolean isFotoPrincipal = "true".equalsIgnoreCase(req.getParameter("principal")) || 
				"y".equalsIgnoreCase(req.getParameter("principal"));
		
		
		if (user.getEmail().equalsIgnoreCase("raultribaldos@gmail.com")	|| user.getEmail().equalsIgnoreCase("gracialafamilia@gmail.com")
				|| user.getEmail().equalsIgnoreCase("alexei_lescaylle@yahoo.es")) {
			
			
			String galeria = req.getParameter("galeria");
			String viviendaDetalle = req.getParameter("detalle"); 
			
			///Carga urls upload
			String authURL = userService.createLogoutURL("/");
			String uploadURL = blobstoreService.createUploadUrl("/post");
			req.setAttribute("uploadURL", uploadURL);
			req.setAttribute("authURL", authURL);
			req.setAttribute("user", user);
			req.setAttribute("user", user);
			
			//
			PersistenceManager pm = PMF.get().getPersistenceManager();
			Gallery gallery = GaleriaDAO.getGalleryByName(pm, galeria);
			req.setAttribute("galeria", gallery);
			
			if (gallery != null) {
				
				try {					
									
					if (borraFileName != null && !borraFileName.equals("")) {
						borra(req, resp, gallery, borraFileName);
					} else if (subirOrden != null && !subirOrden.equals("")) {
						subirOrden(req, resp, galeria, subirOrden, ordenActual, isFotoPrincipal);
					} else if (bajarOrden != null && !bajarOrden.equals("")) {
						bajarOrden(req, resp, galeria, bajarOrden, ordenActual, isFotoPrincipal);
					} 
										
					if(viviendaDetalle != null && !"".equalsIgnoreCase(viviendaDetalle)) {
						
						List<MediaObject> fotos = gallery.getFotosVivienda(viviendaDetalle);
						/*List<MediaObject> fotos = new ArrayList<>();
						for(int i=0; i < fotosOrdenadas.size(); i++) {
							 if(fotosOrdenadas.get(i).getTitle().equals(viviendaDetalle)) {
								 fotos.add(fotosOrdenadas.get(i));
							 }
						}*/						 
						req.setAttribute("fotos", fotos); 
						req.setAttribute("vivienda", viviendaDetalle);
						dispatcher = req.getRequestDispatcher("WEB-INF/templates/detalle.jsp");
						
					}else {
						
						cargaGallery(req, resp, galeria);
						dispatcher = req.getRequestDispatcher("WEB-INF/templates/viviendas.jsp");
						
					}
										
				} catch (Exception e) {
					LogsManager.showError(e.getMessage(), e);
				}
			} else {
				
				Gallery principal = null;
				try {
					principal = GaleriaDAO.getGalleryByName(pm, "principal");
					if (principal != null && principal.getFotos() != null && principal.getFotos().size() > 0) {
						Collections.sort(principal.getFotos(), new PhotoComparator(true));
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
			Collections.sort(gallery.getFotos(), new PhotoComparator(true));
		}				
		req.setAttribute("galeria", gallery);
	}
	
	
	
	
	
	public void subirOrden(HttpServletRequest req, HttpServletResponse resp, String galeria, String title, String ordenActual, 
			boolean isPrincipal) throws IOException {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Transaction tx = pm.currentTransaction();
			tx.begin();
			GaleriaDAO.upOrderMediaObject(pm, galeria, title, Integer.parseInt(ordenActual), isPrincipal);
			tx.commit();
		} catch (Exception e) {
			LogsManager.showError(e.getMessage(), e);
		}finally {
			pm.close();
		}
		resp.sendRedirect((new StringBuilder("/upload?galeria=")).append(galeria).append("&ordenar=no").toString());
	}
	
	public void bajarOrden(HttpServletRequest req, HttpServletResponse resp, String galeria, String title, 
			String ordenActual, boolean isPrincipal) throws IOException {
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Transaction tx = pm.currentTransaction();
			tx.begin();
			GaleriaDAO.downOrderMediaObject(pm, galeria, title, Integer.parseInt(ordenActual), isPrincipal);
			tx.commit();
		} catch (Exception e) {
			LogsManager.showError(e.getMessage(), e);
		}finally {
			pm.close();
		}
		resp.sendRedirect((new StringBuilder("/upload?galeria=")).append(galeria).append("&ordenar=no").toString());
	}
	
	public void borra(HttpServletRequest req, HttpServletResponse resp, Gallery gallery, String filename)
			throws IOException {
		
		try {			
			MediaObject fotoBorrar = null;
			for (Iterator<MediaObject> iterator = gallery.getFotos().iterator(); iterator.hasNext();) {
				MediaObject foto = (MediaObject) iterator.next();
				if (foto.getFilename().equals(filename)) {
					fotoBorrar = foto;
				}
			}

			if (fotoBorrar != null) {
				gallery.getFotos().remove(fotoBorrar);
			}
		} catch (Exception e) {
			LogsManager.showError(e.getMessage(), e);
		}
		resp.sendRedirect((new StringBuilder("/upload?galeria=")).append(gallery.getName()).toString());
	}
	
}
