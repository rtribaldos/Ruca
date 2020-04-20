package com.ruca;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model.Gallery;
import com.model.MediaObject;
import com.model.PMF;
import com.model.dao.GaleriaDAO;
import com.ruca.config.LogsManager;

public class RucaServlet extends HttpServlet {

	private static final long serialVersionUID = -3168628741028769870L;

	public RucaServlet() {
		super();
	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		try {
			
			String gallery = req.getParameter("galeria");
			if (gallery == null || gallery.equals("")) {
				gallery = "principal";
			}
			String vivienda = req.getParameter("nombre");
			String antes = req.getParameter("antes");
			String descripcion = req.getParameter("desc");
			boolean encPrincipal = false;
			boolean encObraNueva = false;
			boolean encReformas = false;
			boolean encDecoracion = false;
			boolean encOficinas = false;
			RequestDispatcher dispatcher = null;
			boolean tieneAntes = false;
			
			
			
			if (gallery != null) {
								
				
					//GaleriaDAO galleryDao = new GaleriaDAO();
					PersistenceManager pm = PMF.get().getPersistenceManager();
					Gallery galeria = null;
					List galerias = GaleriaDAO.getAllGalerias(pm);
					for (Iterator<Gallery> iterator = galerias.iterator(); iterator.hasNext();) {
						Gallery item = (Gallery) iterator.next();
						if (item.getName().equals("principal")) {
							encPrincipal = true;
						} else if (item.getName().equals("obraNueva")) {
							encObraNueva = true;
						} else if (item.getName().equals("reformas")) {
							encReformas = true;
						} else if (item.getName().equals("decoracion")) {
							encDecoracion = true;
						} else if (item.getName().equals("oficinas")) {
							encOficinas = true;
						}
					}
	
					if (!encPrincipal) {
						GaleriaDAO.createGallery(pm, "principal");
					}
					if (!encObraNueva) {
						GaleriaDAO.createGallery(pm, "obraNueva");
					}
					if (!encReformas) {
						GaleriaDAO.createGallery(pm, "reformas");
					}
					if (!encDecoracion) {
						GaleriaDAO.createGallery(pm, "decoracion");
					}
					if (!encOficinas) {
						GaleriaDAO.createGallery(pm, "oficinas");
					}
					galeria = GaleriaDAO.getGalleryByName(pm, gallery);
					
					List<MediaObject> photos;
					if (vivienda != null && !vivienda.equals("")) {
						photos = galeria.getFotosVivienda(vivienda);
						for (MediaObject foto : photos) {
							if (!"".equals(foto.getTextAntes())) {
								tieneAntes = true;
							}
						}								
					}else {
						photos = galeria.getPrincipales();
					}
					
					pm.makePersistentAll(photos);
					
					req.setAttribute("galeria", galeria);
					req.setAttribute("fotos", photos);
					if (antes == null) {
						antes = "";
					}
					req.setAttribute("antes", antes);
	
					if (descripcion != null) {
						descripcion = descripcion.replaceAll("\303\241", "&aacute;");
						descripcion = descripcion.replaceAll("\303\251", "&eacute;");
						descripcion = descripcion.replaceAll("\303\255", "&iacute;");
						descripcion = descripcion.replaceAll("\303\263", "&oacute;");
						descripcion = descripcion.replaceAll("\303\272", "&uacute;");
						req.setAttribute("desc", descripcion);
					}
					if (gallery.equals("principal")) {
						dispatcher = req.getRequestDispatcher("/index.jsp");
					} else if (gallery.equals("obraNueva")) {
						if (vivienda == null || vivienda.equals("")) {
							dispatcher = req.getRequestDispatcher("/nueva.jsp");
						} else {
							req.setAttribute("vivienda", vivienda);
							dispatcher = req.getRequestDispatcher("/vivienda.jsp");
						}
					} else if (gallery.equals("decoracion")) {
						if (vivienda == null || vivienda.equals("")) {
							dispatcher = req.getRequestDispatcher("/decoracion.jsp");
						} else {
							req.setAttribute("vivienda", vivienda);
							dispatcher = req.getRequestDispatcher("/decovivienda.jsp");
						}
					} else if (gallery.equals("oficinas")) {
						if (vivienda == null || vivienda.equals("")) {
							dispatcher = req.getRequestDispatcher("/oficinas.jsp");
						} else {
							req.setAttribute("oficina", vivienda);
							dispatcher = req.getRequestDispatcher("/oficina.jsp");
						}
					} else if (gallery.equals("reformas")) {
						if (vivienda == null || vivienda.equals("")) {
							dispatcher = req.getRequestDispatcher("/reforma.jsp");
						} else {
							if (tieneAntes)
								req.setAttribute("tieneAntes", "yes");
							req.setAttribute("vivienda", vivienda);
							dispatcher = req.getRequestDispatcher("/reformado.jsp");
						}
					}
					dispatcher.forward(req, resp);
			}
		} catch (Exception e) {
			LogsManager.showError(e.getMessage(), e);
		}
	}

	

	public void cargaGallery(HttpServletRequest req, HttpServletResponse resp, String galeria) throws Exception {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Gallery gallery = null;
		gallery = GaleriaDAO.getGalleryByName(pm, galeria);
		if (gallery == null) {
			gallery = GaleriaDAO.createGallery(pm, galeria);
		}
		req.setAttribute("galeria", gallery);
	}
	
	
	
	
	
	
	
}
