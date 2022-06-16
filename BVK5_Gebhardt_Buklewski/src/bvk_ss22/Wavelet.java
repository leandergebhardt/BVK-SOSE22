// BVK Ue1 SS2022 Vorgabe
//
// Copyright (C) 2022 by Klaus Jung
// All rights reserved.
// Date: 2021-03-24

package bvk_ss22;

public class Wavelet {

	public static RasterImage kaskade(RasterImage input, int i){
		double[][] inputArray = convertImageTo2DArray(input);
		inputArray = fitArrayToKaskade(inputArray, i);

		RasterImage resultImage = testConverter(inputArray, i);
		return resultImage;
	}

	public static double[][] lowPassHorizontal (double[][] image){
		double[] h0 = new double[] {-1.0, 2.0, 6.0, 2.0, -1.0};
		double[][] resultImage = new double[image.length][image[0].length];
			// durch x&y iterieren und h0 als filter anwenden; Randbehandlung: spiegeln
			for(int y = 0; y < image.length; y++) {
				for (int x = 0; x < image[0].length; x++) {
					if (x % 2 == 0){
						System.out.println("pixelvalue before: " + image[y][x]);
					//filter anwenden
					int count = 0;
					double value = 0;

					for (int f = -2; f <= 2; f++) {
						int i = f;
						if (x + i < 0) {
							i = i * (-1);
						}
						if (x + i >= image[0].length) {
							i = i * (-1);
						}

						value += image[y][x + i] * h0[count];

						count++;
					}

					value = value / 8.0;

					System.out.println(value);

					resultImage[y][x/2] = value;
				}
			}
			}

		return resultImage;
	}

	public static double[][] lowPassVertical (double[][] image){
		double[] h0 = new double[] {-1.0, 2.0, 6.0, 2.0, -1.0};
		double[][] resultImage = new double[image.length][image[0].length];
		// durch x&y iterieren und h0 als filter anwenden; Randbehandlung: spiegeln
		for(int y = 0; y < image.length; y++) {
			for (int x = 0; x < image[0].length; x++) {
				if (y % 2 == 0){
					System.out.println("pixelvalue before: " + image[y][x]);
					//filter anwenden
					int count = 0;
					double value = 0;

					for (int f = -2; f <= 2; f++) {
						int i = f;
						if (y + i < 0) {
							i = i * (-1);
						}
						if (y + i >= image.length) {
							i = i * (-1);
						}

						value += image[y + i][x] * h0[count];

						count++;
					}

					value = value / 8.0;

					System.out.println(value);

					resultImage[y/2][x] = value;
				}
			}
		}

		return resultImage;
	}

	public static double[][] highPassHorizontal(double[][] image){
		double[] h1 = new double[] {-1.0, 2.0, -1.0};
		double[][] resultImage = new double[image.length][image[0].length];
		// durch x&y iterieren und h0 als filter anwenden; Randbehandlung: spiegeln
		for(int y = 0; y < image.length; y++){
			for(int x = 0; x < image[0].length; x++){
				if (x % 2 == 0) {
					System.out.println("pixelvalue before: " + image[y][x]);
					//filter anwenden
					int count = 0;
					double value = 0;

					for (int f = -1; f <= 1; f++) {
						int i = f;
						if (x + i < 0) {
							i = i * (-1);
						}
						if (x + i >= image[0].length) {
							i = i * (-1);
						}

						value += image[y][x + i] * h1[count];

						count++;
					}

					value = value / 2.0;

					//value += 127;

					System.out.println(value);


					resultImage[y][x/2] = value;
				}
			}
		}

		return resultImage;

	}

	public static double[][] highPassVertical(double[][] image){
		double[] h1 = new double[] {-1.0, 2.0, -1.0};
		double[][] resultImage = new double[image.length][image[0].length];
		// durch x&y iterieren und h0 als filter anwenden; Randbehandlung: spiegeln
		for(int y = 0; y < image.length; y++){
			for(int x = 0; x < image[0].length; x++){
				if (y % 2 == 0) {
					System.out.println("pixelvalue before: " + image[y][x]);
					//filter anwenden
					int count = 0;
					double value = 0;

					for (int f = -1; f <= 1; f++) {
						int i = f;
						if (y + i < 0) {
							i = i * (-1);
						}
						if (y + i >= image.length) {
							i = i * (-1);
						}

						value += image[y + i][x] * h1[count];

						count++;
					}

					value = value / 2.0;


					System.out.println(value);


					resultImage[y/2][x] = value;
				}
			}
		}

		return resultImage;

	}

	public static RasterImage testConverterBegin(RasterImage input){
		double[][] source = convertImageTo2DArray(input);

		double[][] HL = highPassHorizontal(source);
		HL = lowPassVertical(HL);

		double[][] LH = lowPassHorizontal(source);
		LH = highPassVertical(LH);

		double[][] LL = lowPassHorizontal(source);
		LL = lowPassVertical(LL);

		double[][] HH = highPassHorizontal(source);
		HH = highPassVertical(HH);

		// Bilder zuschneiden
		HL = fitArray(HL);
		LH = fitArray(LH);
		LL = fitArray(LL);
		HH = fitArray(HH);
		double[][] result = stitchTogether(LL, LH, HL, HH);

		return convert2DArrayToImage(result);
	}

	public static RasterImage testConverter(double[][] input, int i){
		double[][] HL = highPassHorizontal(input);
		HL = lowPassVertical(HL);

		double[][] LH = lowPassHorizontal(input);
		LH = highPassVertical(LH);

		double[][] LL = lowPassHorizontal(input);
		LL = lowPassVertical(LL);

		double[][] HH = highPassHorizontal(input);
		HH = highPassVertical(HH);

		// Bilder zuschneiden
		HL = fitArray(HL);
		LH = fitArray(LH);
		LL = fitArray(LL);
		HH = fitArray(HH);
		double[][] result = stitchTogetherKaskade(LL, LH, HL, HH, i);

		return convert2DArrayToImage(result);
	}

	private static double[][] stitchTogether(double[][] ll, double[][] lh, double[][] hl, double[][] hh) {
		int newHeight = ll.length * 2;
		int newWidth = ll[0].length * 2;
		double[][] result = new double[newHeight][newWidth];

		int offsetY = newHeight / 2;
		int offsetX = newWidth / 2;

		for(int y = 0; y < newHeight; y++){
			for(int x = 0; x < newWidth; x++) {
				// LL Pass oben links
				if(x < newWidth / 2 && y < newHeight / 2 && x < newWidth && y < newHeight){
					result[y][x] = ll[y][x];
				}
				// LH oben rechts
				if(x > newWidth / 2 && y < newHeight / 2 && x < newWidth && y < newHeight){
					result[y][x] = Math.round(lh[y][x -offsetX]*2 + 127);
				}
				// HL unten links
				if(x < newWidth / 2 && y > newHeight / 2 && x < newWidth && y < newHeight){
					result[y][x] = Math.round(hl[y - offsetY][x]*2 + 127);
				}
				// HH unten rechts
				if(x > newWidth / 2 && y > newHeight / 2 && x < newWidth && y < newHeight){
					result[y][x] = Math.round(hh[y - offsetY][x - offsetX]*2 + 127);
				}
			}
		}

		return result;
	}

	private static double[][] stitchTogetherKaskade(double[][] ll, double[][] lh, double[][] hl, double[][] hh, int i) {
		int newHeight = ll.length * 2 * i;
		int newWidth = ll[0].length * 2 * i;
		double[][] result = new double[newHeight][newWidth];

		int offsetY = newHeight / 2 * i;
		int offsetX = newWidth / 2 * i;

		for(int y = 0; y < newHeight; y++){
			for(int x = 0; x < newWidth; x++) {
				// LL Pass oben links
				if(x < newWidth / 2 && y < newHeight / 2 && x < newWidth && y < newHeight){
					result[y][x] = ll[y][x];
				}
				// LH oben rechts
				if(x > newWidth / 2 && y < newHeight / 2 && x < newWidth && y < newHeight){
					result[y][x] = Math.round(lh[y][x -offsetX]*2 + 127);
				}
				// HL unten links
				if(x < newWidth / 2 && y > newHeight / 2 && x < newWidth && y < newHeight){
					result[y][x] = Math.round(hl[y - offsetY][x]*2 + 127);
				}
				// HH unten rechts
				if(x > newWidth / 2 && y > newHeight / 2 && x < newWidth && y < newHeight){
					result[y][x] = Math.round(hh[y - offsetY][x - offsetX]*2 + 127);
				}
			}
		}

		return result;
	}

	public static double[][] convertImageTo2DArray(RasterImage input){
		double[][] result = new double[input.height][input.width];

		for(int y = 0; y < input.height; y++){
			for(int x = 0; x < input.width; x++){
				int pos = y * input.width + x;
				double grey = input.argb[pos] & 0xff;
				result[y][x] = grey;
			}
		}

		return result;
	}

	public static double[][] fitArray(double[][] input){
		int newWidth = input[0].length / 2;
		int newHeight = input.length / 2;

		double[][] fittedArray = new double[newHeight][newWidth];

		for(int y = 0; y < newHeight; y++){
			for(int x = 0; x < newWidth; x++) {
				fittedArray[y][x] = input[y][x];
			}
		}

		return fittedArray;
	}

	public static double[][] fitArrayToKaskade(double[][] input, int i){
		int newWidth = input[0].length / 2 * i;
		int newHeight = input.length / 2 * i;

		double[][] fittedArray = new double[newHeight][newWidth];

		for(int y = 0; y < newHeight; y++){
			for(int x = 0; x < newWidth; x++) {
				fittedArray[y][x] = input[y][x];
			}
		}

		return fittedArray;
	}

	public static RasterImage convert2DArrayToImage(double[][] input){
		int width = input[0].length;
		int height = input.length;
		RasterImage result = new RasterImage(width, height);

		System.out.println("width: " + input[0].length + "px * height: " + input.length + "px");

		for(int y = 0; y < height; y++){
			for(int x = 0; x < width; x++){
				int pos = y * width + x;

				int avg  = (int) Math.round(input[y][x]);
				if(avg < 0){ avg = 0;}
				if(avg > 255){avg = 255;}
				result.argb[pos] = (0xff << 24) | (avg << 16) | (avg << 8) | avg;
			}
		}

		return result;
	}

}
