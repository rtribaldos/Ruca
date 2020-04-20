package com.ruca;

import com.google.appengine.api.blobstore.*;
import com.google.appengine.api.users.*;
import com.model.Gallery;
import com.model.MediaObject;
import com.model.PMF;
import com.model.dao.GaleriaDAO;
import com.ruca.config.LogsManager;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;
import javax.jdo.*;
import javax.servlet.http.*;

public class UploadPost extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 100079094770751968L;
	private BlobstoreService blobstoreService;

	public UploadPost() {
		blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		User user;
		BlobKey blobKey;
		String contentType;
		Long size;
		Date creation;
		String fileName;
		String op;
		String title;
		String description;
		Boolean bPrincipal;
		UserService userService = UserServiceFactory.getUserService();
		user = userService.getCurrentUser();
		
		Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(req);

		if (blobs.keySet().isEmpty()) {
			resp.sendRedirect((new StringBuilder("/?error=")).append(URLEncoder.encode("No file uploaded", "UTF-8")).toString());
			return;
		}

		Iterator<?> names = blobs.keySet().iterator();
		String blobName = (String) names.next();
		blobKey = blobs.get(blobName).get(0);
		if (user == null) {
			blobstoreService.delete(new BlobKey[] { blobKey });
			resp.sendRedirect((new StringBuilder("/?error=")).append(URLEncoder.encode("Must be logged in to upload", "UTF-8")).toString());
			return;
		}
		BlobInfoFactory blobInfoFactory = new BlobInfoFactory();
		BlobInfo blobInfo = blobInfoFactory.loadBlobInfo(blobKey);
		contentType = blobInfo.getContentType();
		size = blobInfo.getSize();
		creation = blobInfo.getCreation();
		fileName = blobInfo.getFilename();
		op = req.getParameter("op");
		title = req.getParameter("title");
		description = req.getParameter("description");
		String sPrincipal = req.getParameter("principal");
		bPrincipal = false;
		if (sPrincipal != null && sPrincipal.equalsIgnoreCase("yes")) {
			bPrincipal = true;
		}
		String anterior = req.getParameter("anterior");
		if (anterior == null) {
			anterior = "";
		}
		
		PersistenceManager pm;
		Transaction tx;
		pm = PMF.get().getPersistenceManager();
		tx = pm.currentTransaction();
		tx.begin();

		Gallery gallery = null;
		try {
			gallery = GaleriaDAO.getGalleryByName(pm, op);
			if (gallery == null) {
				gallery = GaleriaDAO.createGallery(pm, op);
			}
		} catch (Exception e) {
			LogsManager.showError(e.getMessage(), e);
		}
		
		Integer orden = 1;
		Integer ordenEnGaleria = 1;
		if (gallery != null) {
			if(bPrincipal) {
				ordenEnGaleria = getLastOrder(gallery.getPrincipales());
			}else {
				orden = getLastOrder(gallery.getFotos());
			}
		}
		
		MediaObject mediaObj = new MediaObject(user, blobKey, creation, contentType, fileName, size, title, description,
				true, gallery, bPrincipal, anterior, "", orden, ordenEnGaleria);		
		gallery.addPhoto(mediaObj);
		
		tx.commit();
		pm.close();

		String redireccion="/upload?galeria=" + op;
		
		if(!bPrincipal) {
			redireccion += "&detalle=" + title;
		}
		
		resp.sendRedirect(redireccion);
		
	}
	
	private Integer getLastOrder(List<MediaObject> photos) {
		Integer order = 0;
		if (photos != null && photos.isEmpty()) {
			order = 1;
		} else if (photos != null && photos.size() == 1) {
			order =  photos.get(0).getOrden() + 1;
		} else if (photos != null && photos.size() > 1) {
			for (MediaObject photo : photos) {
				if (photo.getOrden() > order) {
					order = photo.getOrden();
				}
			}
			order++;
		}		
		return order;
	}
}