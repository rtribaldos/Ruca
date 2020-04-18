package com.model.utils;

import java.util.Comparator;

import com.model.MediaObject;

public class PhotoComparator implements Comparator<MediaObject> {
	
	boolean principal; 	
	
	public PhotoComparator(boolean principal) {
		super();
		this.principal = principal;
	}

	@Override
	public int compare(MediaObject mo1, MediaObject mo2) {
		
		if(this.principal) {
			return comparePrincipal(mo1, mo2);
		}
		
		if ((mo1 == null || mo2 == null || mo1.getOrden() == null || mo2.getOrden() == null) || 
    			(mo1.getOrden() == mo2.getOrden())) {
            return 0;
        }
		return mo1.getOrden().compareTo(mo2.getOrden());
	}
	
	public int comparePrincipal(MediaObject mo1, MediaObject mo2) {
		if ((mo1 == null || mo2 == null || mo1.getOrderInGallery() == null || mo2.getOrderInGallery() == null) || 
    			(mo1.getOrderInGallery() == mo2.getOrderInGallery())) {
            return 0;
        }
		return mo1.getOrderInGallery().compareTo(mo2.getOrderInGallery());
	}
	
}
	
