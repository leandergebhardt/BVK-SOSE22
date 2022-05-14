// BVK Ue1 SS2022 Vorgabe
//
// Copyright (C) 2022 by Klaus Jung
// All rights reserved.
// Date: 2021-03-24

package bvk_ss22;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
		modus = inB.read(8);
		M = inB.read(8);

		// calculate missing parameters
		int b = (int) Math.ceil(Math.log(M));
		double bound = Math.pow(2, b) - M;

		String textModus = "";
		if(modus == 0) textModus = "Copy";
		if(modus == 2) textModus = "DPCM";

		String result = Integer.toBinaryString(6);
		System.out.println(result);
		Pattern pattern = Pattern.compile("1");
		Matcher m = pattern.matcher(result);

		int count = 0;
		if(m.find()) count++;
		System.out.println("Found: " + count + " 1` in 110");

		/*
		// inB lesen bis bit = 0
			boolean zeroFound = false;
			int qBits = 0;
			while(zeroFound) {
				qBits = inB.read(1);
				if(qBits == 0) zeroFound = true;
			}
			String result = Integer.toBinaryString(qBits);
			Pattern pattern = Pattern.compile("1");
			Matcher m = pattern.matcher(result);

			int count = 0;
			if(m.find()) count++;

		// anzahl 1en = q
			int q = count;

		// b - 1 = anzahl weitere Bits lesen
			int readMoreBits = b - 1;

		 */
		// if(zahl < bound r = 3 bit)
		// if(zahl >= bound (r = 4 bit - bound))

		System.out.println("_________________________________________________________________________________________");
		System.out.println("width: " + width + " height: " + height);
		System.out.println("Modus: " + modus + " (" + textModus +")");
		System.out.println("M = " + M);
		// System.out.println();
		System.out.println("_________________________________________________________________________________________");



		// create RasterImage to be eturned
		RasterImage image = new RasterImage(width, height);

		return image;
	}

}
