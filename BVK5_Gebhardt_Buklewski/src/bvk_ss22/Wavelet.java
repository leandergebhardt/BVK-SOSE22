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

		int indexh0 = 0;
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

	public static RasterImage testConverter(RasterImage input){
		double[][] test = convertImageTo2DArray(input);
		return convert2DArrayToImage(test);
	}

	public static double[][] convertImageTo2DArray(RasterImage input){
		double[][] result = new double[input.height][input.width];

		for(int y = 0; y < input.height; y++){
			for(int x = 0; x < input.width; x++){
				int pos = y * input.width + x;
				result[y][x] = input.argb[pos];
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
				result.argb[pos] = (int) input[y][x];
			}
		}

		return result;
	}

}
