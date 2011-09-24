/*
 *  Copyright (c) 2006 Triveni Digital, Inc. All Rights Reserved.
 */
/**
 * This package provides utility classes for User
 * Interfaces.
 */
package com.gupta.g13;

import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.image.ImageProducer;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

/**
 * This class provides: To provide a helper mechanism for loading images
 */
public class ImageIconHelper {
	private static Logger log = Logger.getLogger(ImageIconHelper.class.getName());

	private static JFrame tmpFrame = new JFrame();

	private static Map<String, ImageIcon> loadedImages = new TreeMap<String, ImageIcon>();

	/**
	 * Resizies an image using SCALE_SMOOTH (slow)
	 * 
	 * @param src
	 * @param destWidth
	 * @param destHeight
	 * @return
	 */
	public static ImageIcon resize(ImageIcon src, int destWidth, int destHeight) {
		return new ImageIcon(src.getImage().getScaledInstance(destWidth,
				destHeight, Image.SCALE_SMOOTH));
	}

	/**
	 * loads an image and blocks until loading is complete
	 * 
	 * @param name
	 *            the full directory path and name of the image embedded in any
	 *            jar file loaded by the system.  The path should be "/" 
	 *            separated and with ".jpg" or ".gif" or etc.
	 *            
	 * @return fully loaded ImageIcon
	 */
	public static ImageIcon loadEmbeddedImage(String name) {
		log.fine("ImageIconHelper::loadEmbeddedImage(" + name + ")");
		if (loadedImages.get(name) != null) {
			return (ImageIcon) loadedImages.get(name);
		}

		try {
			URL url = ImageIconHelper.class.getResource(name);
			if (url == null) {
				log.warning("Image \"" + name + "\" not found");
				return null;
			}
			Object content = url.getContent();
			if (!(content instanceof ImageProducer)) {
				log.warning("File " + name
						+ " can not load this type of image.");
				return null;
			}
			Image image = Toolkit.getDefaultToolkit().createImage(
					(ImageProducer) content);
			// wait till it is loaded
			MediaTracker tracker;
			try {
				tracker = new MediaTracker(tmpFrame);
				tracker.addImage(image, 0);
				tracker.waitForID(0);
			} catch (InterruptedException e) {
				e.printStackTrace();
				return null;
			}
			ImageIcon imageIcon = new ImageIcon(image);
			loadedImages.put(name, imageIcon);
			return imageIcon;
		} catch (IOException ioe) {
			log.log(Level.WARNING, "Failed to Load Image", ioe);
		}

		return null;
	}

	/**
	 * Loads an embedded image and shrinks it (if required) to the width and
	 * height provided.
	 * 
	 * @param name
	 * @param maxWidth
	 * @param maxHeight
	 * @return
	 */
	public static ImageIcon loadEmbeddedImage(String name, int maxWidth, int maxHeight) {
		if ((name == null) || (maxWidth < 0) || (maxHeight < 0)) {
			return new ImageIcon();
		}
		
		final ImageIcon bigIcon = loadEmbeddedImage(name);
		if (bigIcon == null) {
			return new ImageIcon();
		}
		final double largeSide = (double) Math.max(bigIcon.getIconHeight(), bigIcon
				.getIconWidth());
		final int sWidth = (int) Math.round((double) maxWidth
				* (double) bigIcon.getIconWidth() / largeSide);
		final int sHeight = (int) Math.round((double) maxHeight
				* (double) bigIcon.getIconHeight() / largeSide);

		return new ImageIcon(bigIcon.getImage().getScaledInstance(sWidth,
				sHeight, Image.SCALE_SMOOTH));
	}

}
