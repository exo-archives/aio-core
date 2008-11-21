package org.exoplatform.services.document.image;

import java.awt.image.BufferedImage;

/**
 * Author : Davide Dalle Carbonare davide.dallecarbonare@eng.it
 */

public interface ImageProcessingService {

  public BufferedImage createCroppedImage(BufferedImage img, int chosenWidth, int chosenHeight);

  public BufferedImage createBoundImage(BufferedImage img,
                                        int chosenWidth,
                                        int chosenHeight,
                                        String bgColor);

  public BufferedImage createScaledImage(BufferedImage img, double factor);

}
