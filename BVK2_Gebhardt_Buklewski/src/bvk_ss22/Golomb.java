// BVK Ue1 SS2022 Vorgabe
//
// Copyright (C) 2022 by Klaus Jung
// All rights reserved.
// Date: 2021-03-24

package bvk_ss22;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Golomb {
	
	public static void encodeImage(RasterImage image, int mode, DataOutputStream out) throws IOException {

		int width = image.width;
		int height = image.height;
		BitOutputStream outB = new BitOutputStream(out);
		outB.write(8, mode);

		System.out.println("_________________________________________________________________________________________");
		System.out.println("width: " + width + " height: " + height);
		//System.out.println("Modus: " + modus + " (" + textModus +")");
		//System.out.println("M = " + M);
		// System.out.println();
		System.out.println("_________________________________________________________________________________________");

	}

	public static RasterImage decodeImage(DataInputStream in) throws IOException {
		int width;
		int height;
		int modus;
		int M;
		BitInputStream inB = new BitInputStream(in);

		// read parameters from DataInputStream
		width = inB.read(16);
		height = inB.read(16);
		modus = inB.readByte();
		M = inB.readByte();

		// calculate missing parameters
		double b = Math.ceil(Math.log(M));
		double bound = Math.pow(2, b) - M;

		String textModus = "";
		if(modus == 0) textModus = "Copy";
		if(modus == 2) textModus = "DPCM";

		// inB lesen bis bit = 0
		

		System.out.println("_________________________________________________________________________________________");
		System.out.println("width: " + width + " height: " + height);
		System.out.println("Modus: " + modus + " (" + textModus +")");
		System.out.println("M = " + M);
		// System.out.println();
		System.out.println("_________________________________________________________________________________________");



		// create RasterImage to be returned
		RasterImage image = new RasterImage(width, height);

		return image;
	}

}
