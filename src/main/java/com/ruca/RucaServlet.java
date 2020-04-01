package com.ruca;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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
import com.model.utils.PhotoComparator;
import com.ruca.config.LogsManager;
import javax.jdo.*;

public class RucaServlet extends HttpServlet {

	private static final long serialVersionUID = -3168628741028769870L;

	public RucaServlet() {
		super();
	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		try {
			String borraFileName = req.getParameter("borrar");
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
			
			String subirOrden = req.getParameter("subirOrden");
			String bajarOrden = req.getParameter("bajarOrden");
			String ordenActual = req.getParameter("ordenActual");
			boolean isFotoPrincipal = "true".equalsIgnoreCase(req.getParameter("principal"));
			
			if (gallery != null) {
				if (borraFileName != null && !borraFileName.equals("")) {
					borra(req, resp, gallery, borraFileName);
				} else if (subirOrden != null && !subirOrden.equals("")) {
					subirOrden(req, resp, gallery, subirOrden, ordenActual, isFotoPrincipal);
				} else if (bajarOrden != null && !bajarOrden.equals("")) {
					bajarOrden(req, resp, gallery, bajarOrden, ordenActual);
				} else {
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

					List<MediaObject> photos = GaleriaDAO.getFotosOrdenadas(galeria);
					if (photos != null && photos.size() > 0) {
						for (MediaObject foto : photos) {
							if (vivienda != null && !vivienda.equals("")) {
								if (vivienda.equals(foto.getTitle())) {
									if (!"".equals(foto.getTextAntes())) {
										tieneAntes = true;
									}
								}
							}
						}								
					}
					
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
			}
		} catch (Exception e) {
			LogsManager.showError(e.getMessage(), e);
		}
	}

	public void borra(HttpServletRequest req, HttpServletResponse resp, String galeria, String filename)
			throws IOException {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Gallery gallery = GaleriaDAO.getGalleryByName(pm, galeria);
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
		resp.sendRedirect((new StringBuilder("/upload?galeria=")).append(galeria).toString());
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
	
	public void bajarOrden(HttpServletRequest req, HttpServletResponse resp, String galeria, String filename, 
			String ordenActual) throws IOException {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Integer currentOrden = ordenActual != null ? Integer.valueOf(ordenActual) : null;
			Gallery gallery = GaleriaDAO.getGalleryByName(pm, galeria);
			if (currentOrden != null && gallery != null && gallery.getFotos() != null && currentOrden < gallery.getFotos().size()) {
				for (Iterator<MediaObject> iterator = gallery.getFotos().iterator(); iterator.hasNext();) {
					MediaObject foto = (MediaObject) iterator.next();
					if (foto.getOrden().equals(currentOrden + 1)) {
						foto.setOrden(currentOrden);
					}					
					if (foto.getFilename().equals(filename)) {
						foto.setOrden(currentOrden + 1);
					}
				}
			}
		} catch (Exception e) {
			LogsManager.showError(e.getMessage(), e);
		}
		resp.sendRedirect((new StringBuilder("/upload?galeria=")).append(galeria).append("&ordenar=no").toString());
	}
	
}
