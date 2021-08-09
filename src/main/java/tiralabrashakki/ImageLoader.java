package tiralabrashakki;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

public class ImageLoader {

	public static BufferedImage loadImage(String path) {
		try {
			return ImageIO.read(ImageLoader.class.getResource(path)); //If it doesnt work, might need to build the project
			//return ImageIO.read(getClass().getResource(path)); //works if non-static
			//return ImageIO.read(new File(path)); //doesnt work (has to be static)
		} catch (IOException e) {
			System.out.println("IOException " + e + ", " + path);
		} catch (NullPointerException e) {
			System.out.println("Couldn't get resource. Path was null " + e + ", " + path);
		} catch (IllegalArgumentException e) {
			System.out.println("Couldn't find the resource (resource was null) " + e + ", " + path);
		}

		return null;
	}
	
	/**
	 * Gets a subimage of full sprite sheet image.
	 * @param path Path of full image
	 * @param xGrid n'th subimage in x direction
	 * @param yGrid n'th subimage in y direction
	 * @param xSize tile size x direction
	 * @param ySize tile size y direction
	 * @return 
	 */
	public static BufferedImage getSprite(String path, int xGrid, int yGrid, int xSize, int ySize) {
		BufferedImage image = loadImage(path);

		return getSprite(image, xGrid, yGrid, xSize, ySize);
	}
	
	/**
	 * Gets a subimage of full sprite sheet image.
	 * @param image Image
	 * @param xGrid n'th subimage in x direction
	 * @param yGrid n'th subimage in y direction
	 * @param xSize tile size x direction
	 * @param ySize tile size y direction
	 * @return 
	 */
	public static BufferedImage getSprite(BufferedImage image, int xGrid, int yGrid, int xSize, int ySize) {
		return image.getSubimage(xGrid * xSize, yGrid * ySize, xSize, ySize);
	}
}
