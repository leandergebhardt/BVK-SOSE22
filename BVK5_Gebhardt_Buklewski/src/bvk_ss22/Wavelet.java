// BVK Ue1 SS2022 Vorgabe
//
// Copyright (C) 2022 by Klaus Jung
// All rights reserved.
// Date: 2021-03-24

package bvk_ss22;

public class Wavelet {

	private static double[][] HL;
	private static double[][] LH;
	private static double[][] HH;
	private static double[][] LL;

	public static RasterImage kaskade(RasterImage input, int kaskades){

		double[][] fittedInputArray;
		RasterImage resultImage = null;

		if(kaskades == 1){
			resultImage = testConverterBegin(input);
		}
		if(kaskades == 0){
			resultImage = input;
		}
		else {
			RasterImage firstKaskade = testConverterBegin(input);
			double[][] inputArray = convertImageTo2DArray(firstKaskade);

			for (int i = 1; i < kaskades; i++) {
				fittedInputArray = fitArrayToKaskade(inputArray, i);

				RasterImage secondKaskade = testConverter(fittedInputArray, i);
				double[][] kaskadeArray = convertImageTo2DArray(secondKaskade);
				// Combine kaskades
				resultImage = combineKaskades(inputArray, kaskadeArray, i);
			}
		}
		
		return resultImage;
	}

	private static RasterImage combineKaskades(double[][] inputArray, double[][] secondKaskade, int i) {
		RasterImage result = new RasterImage(inputArray[0].length, inputArray.length);
		int halfWidth = (int) (result.width / Math.pow(2, i));
		int halfHeight = (int) (result.height / Math.pow(2, i));
		//int halfWidth = result.width / 2;
		//int halfHeight = result.height / 2;

		//System.out.println("half Widht = " + halfWidth + " halfHeight = " + halfHeight);

		for(int y = 0; y < result.height; y++) {
			for (int x = 0; x < result.width; x++) {
				if(y < halfHeight && x < halfWidth)
					inputArray[y][x] = secondKaskade[y][x];
			}
		}

		result = convert2DArrayToImage(inputArray);

		return result;
	}

	public static RasterImage testConverterBegin(RasterImage input){
		double[][] source = convertImageTo2DArray(input);

		HL = Filter.highPassHorizontal(source);
		HL = Filter.lowPassVertical(HL);

		LH = Filter.lowPassHorizontal(source);
		LH = Filter.highPassVertical(LH);

		LL = Filter.lowPassHorizontal(source);
		LL = Filter.lowPassVertical(LL);

		HH = Filter.highPassHorizontal(source);
		HH = Filter.highPassVertical(HH);

		// Bilder zuschneiden
		HL = fitArray(HL);
		LH = fitArray(LH);
		LL = fitArray(LL);
		HH = fitArray(HH);
		double[][] result = stitchTogether(LL, LH, HL, HH);

		return convert2DArrayToImage(result);
	}

	public static RasterImage testConverter(double[][] input, int i){
		double[][] HL = Filter.highPassHorizontal(input);
		HL = Filter.lowPassVertical(HL);

		double[][] LH = Filter.lowPassHorizontal(input);
		LH = Filter.highPassVertical(LH);

		double[][] LL = Filter.lowPassHorizontal(input);
		LL = Filter.lowPassVertical(LL);

		double[][] HH = Filter.highPassHorizontal(input);
		HH = Filter.highPassVertical(HH);

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
					result[y][x] = Math.round(lh[y][x -offsetX] * 2 + 127);
				}
				// HL unten links
				if(x < newWidth / 2 && y > newHeight / 2 && x < newWidth && y < newHeight){
					result[y][x] = Math.round(hl[y - offsetY][x] * 2 + 127);
				}
				// HH unten rechts
				if(x > newWidth / 2 && y > newHeight / 2 && x < newWidth && y < newHeight){
					result[y][x] = Math.round(hh[y - offsetY][x - offsetX] * 2 + 127);
				}
			}
		}

		return result;
	}

	private static double[][] stitchTogetherKaskade(double[][] ll, double[][] lh, double[][] hl, double[][] hh, int i) {
		int newHeight = ll.length * 2;
		int newWidth = ll[0].length * 2;
		double[][] result = new double[newHeight][newWidth];

		//System.out.println(i + "nd Kaskade");
		//System.out.println("Kaskade height: " + newHeight +  " Kaskade width: " + newWidth);
		//System.out.println("LL height: " + ll.length +  " LL width: " + ll[0].length);

		int offsetY = ll.length;
		int offsetX = ll[0].length;

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
		int newWidth = (int) (input[0].length / Math.pow(2, i)) * 2;
		int newHeight = (int) (input.length / Math.pow(2, i)) * 2;

		//System.out.println("kaskade height: " + newHeight + " kaskade width: " + newWidth);

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

		//System.out.println("width: " + input[0].length + "px * height: " + input.length + "px");

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

	public static RasterImage cut(double[][] input){
		int height = input.length;
		int width = input[0].length;

		double[][] ll = new double[height / 2][width / 2];
		double[][] hh = new double[height / 2][width / 2];
		double[][] hl = new double[height / 2][width / 2];
		double[][] lh = new double[height / 2][width / 2];

		int offsetX = width / 2;
		int offsetY = height / 2;

		RasterImage result = null;

		for(int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				// LL Pass oben links
				if(x < offsetY && y < offsetX && x < width && y < height){
					ll[y][x] = input[y][x];
				}
				// LH oben rechts
				if(x > offsetX && y < offsetY && x < width && y < height){
					lh[y][x] = input[y][x -offsetX] / 2 - 127;
				}
				// HL unten links
				if(x < offsetX && y > offsetY && x < width && y < height){
					hl[y][x] = input[y - offsetY][x] / 2 - 127;
				}
				// HH unten rechts
				if(x > offsetX && y > offsetY && x < width && y < height){
					hh[y][x] = input[y - offsetY][x - offsetX] / 2 - 127;
				}
			}
		}

		return result;
	}

	public static RasterImage rekonstruct(RasterImage waveletImage) {
		RasterImage resultImage = new RasterImage(waveletImage.width, waveletImage.height);

		double[][]inputArray = convertImageTo2DArray(waveletImage);
		RasterImage result = cut(inputArray);

		return resultImage;
	}

}
