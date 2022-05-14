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
	public static int M;

	public static int getM() {
		return M;
	}

	public void setM(int M) {
		this.M = M;
	}
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
		BitInputStream inB = new BitInputStream(in);

		// read parameters from DataInputStream
		width = inB.read(16);
		height = inB.read(16);
		modus = inB.read(8);
		M = inB.read(8);

		// calculate missing parameters
		int b = (int) Math.ceil(Math.log(M));
		int bound = (int) (Math.pow(2, b) - M);

		String textModus = "";
		if(modus == 0) textModus = "Copy";
		if(modus == 2) textModus = "DPCM";

		RasterImage result = new RasterImage(width, height);

		for(int pos= 0; pos < result.argb.length; pos++) {
			// inB lesen bis bit = 0
			boolean notZero = true;
			int qBits = 0;
			int oneBitCounter = 0;
			while (notZero) {
				qBits = inB.read(1);
				if (qBits == 0) {
					notZero = false;
				}
				oneBitCounter++;
			}

			// anzahl 1en = q
			int q = oneBitCounter;

			// b - 1 = anzahl weitere Bits lesen
			int readBits = b - 1;

			// if(zahl < bound read all calculated bits)
			int num = inB.read(readBits);
			if (num < bound) {

			}

			// if(zahl >= bound -> read one more bit)
			else if (num >= bound) {
				num += inB.read(1);
			}

			int pixel = q * M + num;
			
			// clamping
			if(pixel > 255) pixel = 255;
			if(pixel < 0) pixel = 0;

			System.out.print(pixel + ", ");
			result.argb[pos] = pixel;
		}


		System.out.println("");
		System.out.println("_________________________________________________________________________________________");
		System.out.println("width: " + width + " height: " + height);
		System.out.println("Modus: " + modus + " (" + textModus +")");
		System.out.println("M = " + M);
		// System.out.println();
		System.out.println("_________________________________________________________________________________________");

		return result;
	}

}
