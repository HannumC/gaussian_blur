//Imports
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;




public class Gaussian_blur {

	// input arguments as: radius sigma (i.e: Gaussian_blur 5 2.5)
	public static void main(String[] args) {
		int radius = Integer.parseInt(args[0]);
		double sigma = Double.parseDouble(args[1]);

		BufferedImage img = null;
		try {
		// import the image as a BufferedImage
		img = ImageIO.read(new File("Gaussian_Blur.jpg"));
			
		// use calcKernel to generate a filter based on the radius and sigma values
		double kernel[][] = calcKernel(radius, sigma);

		File outputFile = new File("image.jpg");

		// generate the blured image using the kernel and saves as outputFile
		blur(img, kernel, radius, outputFile);
		}
		catch (IOException e) {
            System.out.println("Error: " + e);
        }
	}

	// takes input "startImg" as type BufferedImage, "kernel" as a 2D array of type double, "radius" as type int and "outputFile" as type File
	// saves blured image as outputFile
	public static void blur(BufferedImage startImg, double[][] kernel, int radius, File outputFile){
		int width = startImg.getWidth();
		int height = startImg.getHeight();

		BufferedImage bluredImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		double kernelVal = 0.0;
		int colors = 0, red = 0, green = 0, blue = 0;
		double bRed = 0.0, bGreen = 0.0, bBlue = 0.0;
		int blurPixels = 0;
		try{

		// loop through each pixel excluding those within 1 radius of the edge
		for (int x = radius; x < width - radius; x++) {
			for (int y = radius; y < height - radius; y++) {
				bRed = 0.0;
				bGreen = 0.0;
				bBlue = 0.0;

				// loop through kernel values and apply kernel value to each color channel 
				for (int xKernel = -radius; xKernel <= radius; xKernel++) {
					for (int yKernel = -radius; yKernel <= radius; yKernel++) {
						kernelVal = kernel[xKernel + radius][yKernel + radius];

						// convert color values to range 0 - 255
						colors = startImg.getRGB(x + xKernel, y + yKernel);
						blue = (colors & 0xff);
						green = ((colors & 0xff00) >> 8);
						red = ((colors & 0xff0000) >> 16);

						bRed += (red * kernelVal);
						bGreen += (green * kernelVal);
						bBlue += (blue * kernelVal);
					}
				}

				// recombine color channels
				blurPixels = ((int)bRed & 0xFF) << 16
                        | ((int)bGreen & 0xFF) << 8
                        | ((int)bBlue & 0xFF);

                // set pixel at position (x,y)
				bluredImg.setRGB(x, y, blurPixels);
			}
		}

		// save blured image as image.jpg
		ImageIO.write(bluredImg, "jpg", outputFile);

		}
		catch (IOException e) {
            System.out.println("Error: " + e);
        }
	}

	// calculate the kernel values based on the radius and sigma values
	// takes input "radius" as type int and "sigma" as type double
	// returns kernel as a 2D array of type double
	public static double[][] calcKernel(int radius, double sigma){
		int kernelSize = (2 * radius) + 1;
		double kernel[][] = new double[kernelSize][kernelSize];
		int expNumerator = 0;
		double expDenominator = 0;
		double exp = 0.0;
		double ePower = 0.0;
		double kernelVal = 0.0;
		double sum = 0.0;


		// calculate the value of each kernel based on sigma value
		for (int x = -radius; x <= radius; x++) {
			for (int y = -radius; y <= radius; y++) {
				
				expNumerator = (x * x) + (y * y);
				expDenominator = 2.0 * (sigma * sigma);
				exp = -1 * (expNumerator / expDenominator);

				ePower = Math.pow(Math.E, exp);

				kernelVal = ePower / (2.0 * Math.PI * (sigma * sigma));

				kernel[x + radius][y + radius] = kernelVal;
				sum += kernelVal;
			}
		}

		// normalize kernel so all values add up to 1
		for (int x = 0; x < kernelSize; x++) {
			for (int y = 0; y < kernelSize; y++) {
				kernel[x][y] = kernel[x][y] / sum;

			}
		}

		return kernel;
	}

}