// BVK Ue1 SS2022 Vorgabe
//
// Copyright (C) 2022 by Klaus Jung
// All rights reserved.
// Date: 2021-03-24

package bvk_ss22;

public class Wavelet {

	public static double[][] kaskade(double[][] input){
		double[][] result = new double[input.length][input.length];

		double[] h0 = new double[] {-1.0, 2.0, 6.0, 2.0, -1.0};
		double[] h1 = new double[] {-1.0, 2.0, -1.0};

		double[] g0 = new double[] {1.0, 2.0, 1.0};
		double[] g1 = new double[] {-1.0, -2.0, 6.0, -2.0, -1.0};

		int indexh0 = 0;		//wofür sind diese berechnungen?
		for(double element : h0){
			h0[indexh0] = element * 0.125;
			indexh0++;
		}

		int indexh1 = 0;
		for(double element : h1){
			h1[indexh1] = element * 0.5;
			indexh1++;
		}
		int indexg0 = 0;
		for(double element : g0){
			g0[indexg0] = element * 0.5;
			indexg0++;
		}

		int indexg1 = 0;
		for(double element : g1){
			g1[indexg1] = element * 0.125;
			indexg1++;
		}


		return result;
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
				if (x % 2 == 0){
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

					resultImage[y][x/2] = value;
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
				if (y % 2 == 0) {
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


					resultImage[y/2][x] = value;
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

	public static RasterImage testConverter(RasterImage input){
		double[][] test = convertImageTo2DArray(input);
		test = highPassHorizontal(test);
		test = lowPassVertical(test);
		return convert2DArrayToImage(test);
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

	public static RasterImage convert2DArrayToImage(double[][] input){
		RasterImage result = new RasterImage(input[0].length, input.length);

		System.out.println("width: " + input[0].length + "px * height: " + input.length + "px");

		for(int y = 0; y < input.length; y++){
			for(int x = 0; x < input[0].length; x++){
				int pos = y * input[0].length + x;

				int avg  = (int) Math.round(input[y][x]*2 + 127);
				if(avg < 0){ avg = 0;}
				if(avg > 255){avg = 255;}
				result.argb[pos] = (0xff << 24) | (avg << 16) | (avg << 8) | avg;
			}
		}

		return result;
	}

}
