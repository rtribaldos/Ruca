package com.model.utils;

import java.util.Comparator;

import com.model.MediaObject;

public class PhotoComparator implements Comparator<MediaObject> {
	
	@Override
	public int compare(MediaObject mo1, MediaObject mo2) {
		if ((mo1 == null || mo2 == null || mo1.getOrden() == null || mo2.getOrden() == null) || 
    			(mo1.getOrden() == mo2.getOrden())) {
            return 0;
        }
		return mo1.getOrden().compareTo(mo2.getOrden());
	}
	
}
	
