// BVK Ue1 SS2022 Vorgabe
//
// Copyright (C) 2022 by Klaus Jung
// All rights reserved.
// Date: 2021-03-24

package bvk_ss22;

public class Wavelet {

	public static double[][] kaskade(double[][] input){
		double[][] result = new double[input.length][input.length];

		return result;
	}

	public static double[][] convertImageTo2DArray(RasterImage input){
		double[][] result = new double[input.width][input.height];

		return result;
	}

	public static RasterImage convert2DArrayToImage(double[][] input){
		RasterImage result = new RasterImage(input.length, input.length);

		return result;
	}

}
