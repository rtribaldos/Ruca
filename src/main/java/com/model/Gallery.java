package com.model;

import java.util.List;

import javax.jdo.annotations.Element;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

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

}
