package com.model.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.model.Gallery;
import com.model.MediaObject;

public class GaleriaDAO {

	public GaleriaDAO() {
	}

	public static Gallery getGaleria(PersistenceManager pm, long id) throws Exception {
		return (Gallery) pm.getObjectById(Gallery.class, Long.valueOf(id));
	}

	public static void updateGaleria(PersistenceManager pm, Gallery gallery) throws Exception {
		pm.makePersistent(gallery);
	}

	public static void deleteOrder(PersistenceManager pm, Gallery galeria) {
		pm.deletePersistent(galeria);
	}

	public static Gallery createGallery(PersistenceManager pm, String nombre) throws Exception {
		Gallery galeria = new Gallery(nombre);
		pm.makePersistent(galeria);
		return galeria;
	}

	public static Gallery getGalleryByName(PersistenceManager pm, String nombre)  {
		String query = (new StringBuilder("select from ")).append(Gallery.class.getName()).append(" where name == '")
				.append(nombre).append("'").toString();
		List galerias = (List) pm.newQuery(query).execute();
		if (galerias.size() > 0) {
			return (Gallery) galerias.get(0);
		} else {
			return null;
		}
	}
	
	public static ArrayList getFotosOrdenadas(Gallery galeria) {
		ArrayList<MediaObject> fotos = new ArrayList<>();
		Integer orden = 1;
		if(galeria != null && galeria.getFotos().size() > 0) {
			while(fotos.size() < galeria.getFotos().size()) {
				for(MediaObject foto : galeria.getFotos()) {
					if(foto.getOrden() == orden) {
						fotos.add(foto);
						++orden;
					}
				}
			}
		}
		return fotos;
	}

	
	public static List getGalerias(PersistenceManager pm) throws Exception {
		String query = (new StringBuilder("select from ")).append(Gallery.class.getName())
				.append(" where name != 'Principal'").toString();
		return (List) pm.newQuery(query).execute();
	}

	public static List getAllGalerias(PersistenceManager pm) throws Exception {
		Query query = pm.newQuery(Gallery.class);
		return (List) query.execute();
	}

	public static void upOrderMediaObject(PersistenceManager pm, String galeria, String name, int orderOld,	boolean isPrincipal) {
		Gallery gallery = null;
		
		gallery = getGalleryByName(pm, galeria);
		List<MediaObject> photos;
		if(isPrincipal) {
			photos = gallery.getPrincipales();
		}else {
			photos = gallery.getFotos();
		}
							
					
		if (orderOld > 1 && gallery != null && photos != null) {
			int orderNew = (orderOld - 1);
			for (Iterator<MediaObject> iterator = photos.iterator(); iterator.hasNext();) {
				MediaObject foto = ((MediaObject) iterator.next()); 
				if (!isPrincipal) {
					if (foto.getTitle().equals(name) && foto.getOrden().intValue() == orderNew) {
						foto.setOrden(-1);
					} else if (foto.getTitle().equals(name) && foto.getOrden().intValue() == orderOld) {
						foto.setOrden(orderNew);
					}
				} else {
					if (foto.getOrden().intValue() == orderNew) {
						foto.setOrden(-1);
					} else if (foto.getOrden().intValue() == orderOld) {
						foto.setOrden(orderNew);
					}
				}
				
			}
			for (Iterator<MediaObject> iterator = photos.iterator(); iterator.hasNext();) {
				MediaObject foto = (MediaObject) iterator.next();
				if (!isPrincipal) {
					if (foto.getTitle().equals(name) && foto.getOrden().intValue() == -1) {
						foto.setOrden(orderOld);
					}
				} else {
					if (foto.getOrden().intValue() == -1) {
						foto.setOrden(orderOld);
					}
				}
			}			
		}
		
	}
	
	public static void downOrderMediaObject(PersistenceManager pm, String galeria, String name, int orderOld, boolean isPrincipal)  {
				
		Gallery gallery = GaleriaDAO.getGalleryByName(pm, galeria);
		List<MediaObject> photos;
		if(isPrincipal) {
			photos = gallery.getPrincipales();
		}else {
			photos = gallery.getFotos();
		}
		
		if (orderOld < photos.size() && gallery != null && photos != null) {
			int orderNew = (orderOld + 1);
			for (Iterator<MediaObject> iterator = photos.iterator(); iterator.hasNext();) {
				MediaObject foto = ((MediaObject) iterator.next()); 
				if (!isPrincipal) {
					if (foto.getTitle().equals(name) && foto.getOrden().intValue() == orderNew) {
						foto.setOrden(-1);
					} else if (foto.getTitle().equals(name) && foto.getOrden().intValue() == orderOld) {
						foto.setOrden(orderNew);
					}
				} else {
					if (foto.getOrden().intValue() == orderNew) {
						foto.setOrden(-1);
					} else if (foto.getOrden().intValue() == orderOld) {
						foto.setOrden(orderNew);
					}
				}
				
			}
			for (Iterator<MediaObject> iterator = photos.iterator(); iterator.hasNext();) {
				MediaObject foto = (MediaObject) iterator.next();
				if (!isPrincipal) {
					if (foto.getTitle().equals(name) && foto.getOrden().intValue() == -1) {
						foto.setOrden(orderOld);
					}
				} else {
					if (foto.getOrden().intValue() == -1) {
						foto.setOrden(orderOld);
					}
				}
			}
			
		}
	}

}
