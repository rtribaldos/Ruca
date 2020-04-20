package com.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.annotations.Element;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.model.utils.PhotoComparator;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Gallery {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id; 

	@Persistent
	private String name;

	@Persistent(mappedBy = "gallery")
	@Element(dependent = "true")
	private List<MediaObject> fotos;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<MediaObject> getFotos() {
		return fotos;
	}

	public void setFotos(List<MediaObject> fotos) {
		this.fotos = fotos;
	}

	public Gallery(String name) {
		this.name = name;
	}

	public void addPhoto(MediaObject foto) {
		this.fotos.add(foto);
	}

	public MediaObject getFotoByOrder(Integer orden) {
		MediaObject mediaObject = null;
		try {
			boolean found = false;
			for (int i = 0; !found && getFotos() != null && i < getFotos().size(); i++) {
				if (orden.equals(getFotos().get(i).getOrden())) {
					mediaObject = getFotos().get(i);
					found = true;
				}
			}
			if (!found) {
				getFotoByOrder(orden + 1);
			}
		} catch (Exception e) {
		}
		return mediaObject;
	}

	
	public List<MediaObject> getPrincipales() {
		
		List<MediaObject> photos = new ArrayList<>();
		
		
		//fotos.stream().filter(foto -> foto.isPrincipal().booleanValue()).forEach(System.out::println);
		int ordenNew=1;
		
		for(MediaObject foto : this.fotos) {
			if(foto.isPrincipal()) {
				if(foto.getOrderInGallery() == null) {
					foto.setOrderInGallery(ordenNew);
					foto.setOrden(1);
					++ordenNew;
				}
				photos.add(foto);
			}
		}
		Collections.sort(photos, new PhotoComparator(true));		
		return photos;
	
	}
	
	public List<MediaObject> getFotosVivienda(String vivienda) {
		
		List<MediaObject> photos = new ArrayList<>();
		int ordenNew=2;
		for(MediaObject foto : this.fotos) {
			if(vivienda.toUpperCase().equals(foto.getTitle().toUpperCase())) {
				if(foto.getOrden() == null) {
					foto.setOrden(ordenNew);
					foto.setOrderInGallery(1);
					++ordenNew;
				}
				photos.add(foto);
			}
		}
		Collections.sort(photos, new PhotoComparator(false));		
		return photos;
	
	}
	
}
