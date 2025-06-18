package com.booker.g13;

import java.awt.Image;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class ImageIconHelper {
	private static final Logger log = Logger.getLogger(ImageIconHelper.class.getName());

	// Refactoring: ConcurrentHashMap ist eine gute Wahl für thread-sichere Caches.
	private static final Map<String, ImageIcon> loadedImages = new ConcurrentHashMap<>();

	private ImageIconHelper() {
        // Utility-Klassen sollten keinen öffentlichen Konstruktor haben
    }

	public static ImageIcon resize(ImageIcon src, int destWidth, int destHeight) {
		return new ImageIcon(src.getImage().getScaledInstance(destWidth,
				destHeight, Image.SCALE_SMOOTH));
	}

	/**
	 * Refactoring: Lädt ein Bild synchron mit ImageIO.read(), was MediaTracker überflüssig macht.
	 * Dies ist stabiler und moderner.
	 */
	public static ImageIcon loadEmbeddedImage(String name) {
		return loadedImages.computeIfAbsent(name, key -> {
            log.fine("ImageIconHelper::loadEmbeddedImage(" + key + ")");
            try {
                URL url = ImageIconHelper.class.getResource(key);
                if (url == null) {
                    log.warning("Image \"" + key + "\" not found");
                    return null;
                }
                // ImageIO.read blockiert, bis das Bild vollständig geladen ist.
                Image image = ImageIO.read(url);
                if (image == null) {
                    log.warning("File " + key + " could not be loaded as an image.");
                    return null;
                }
                return new ImageIcon(image);
            } catch (IOException ioe) {
                log.log(Level.WARNING, "Failed to Load Image " + key, ioe);
                return null;
            }
        });
	}
	
	public static ImageIcon loadEmbeddedImage(String name, int maxWidth, int maxHeight) {
		if ((name == null) || (maxWidth < 0) || (maxHeight < 0)) {
			// Gibt ein leeres Icon zurück, anstatt NPEs zu riskieren.
			return new ImageIcon();
		}

		final ImageIcon bigIcon = loadEmbeddedImage(name);
		if (bigIcon == null) {
			return new ImageIcon();
		}
		
		// Die Skalierungslogik bleibt erhalten, da sie korrekt ist.
		final double largeSide = Math.max(bigIcon.getIconHeight(), bigIcon.getIconWidth());
		if (largeSide == 0) return bigIcon; // Vermeidung von Division durch Null

		final int sWidth = (int) Math.round((double) maxWidth * bigIcon.getIconWidth() / largeSide);
		final int sHeight = (int) Math.round((double) maxHeight * bigIcon.getIconHeight() / largeSide);

		return new ImageIcon(bigIcon.getImage().getScaledInstance(sWidth, sHeight, Image.SCALE_SMOOTH));
	}
}