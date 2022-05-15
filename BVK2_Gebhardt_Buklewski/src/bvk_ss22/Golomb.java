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
	public static void encodeImage(RasterImage image, int modus, int M, DataOutputStream out) throws IOException {
		int width = image.width;
		int height = image.height;
		BitOutputStream outB = new BitOutputStream(out);
		outB.write(width, 16);
		outB.write(height, 16);
		outB.write(modus, 8);

		String textModus = "";

		if(modus == 0) textModus = "Copy";
		if(modus == 2) textModus = "DPCM";

		System.out.println(M);
		outB.write(M, 8);
		int b = (int) Math.ceil(Math.log(M) / Math.log(2));
		int bound = (int) (Math.pow(2, b) - M);

		for(int pos = 0; pos < image.argb.length; pos++) {
			int q;
			int r;
			int x = image.argb[pos] & 0x000000ff;


			if(modus == 2) {
				//x -= 128;
				if (x > 0) {
					x = x*2;
				}
				else{
					x = ((x*-1)*2)-1;
				}
			}

			// quotienten berechnen
			q = (int) Math.floor(x / M);

			for(x=0; x<q; x++){
				outB.write(1,1);
			}

			outB.write(0, 1);
			// TODO: add 0 at the end. How many bits to write?

			// Rest berechnen
			r = x - q * M;

			// rest case behandlung
			if(r < bound){
				outB.write(r, b - 1);
			}
			if(r >= bound){
				outB.write(r, b);
			}
		}

		outB.close();

		System.out.println("_________________________________________________________________________________________");
		System.out.println("width: " + width + " height: " + height);
		System.out.println("Modus: " + modus + " (" + textModus +")");
		System.out.println("M = " + M);
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
		int b = (int) Math.ceil(Math.log(M) / Math.log(2));

		System.out.println("M: " + M);
		System.out.println("b: " + b);
		int bound = (int) (Math.pow(2, b) - M);
		System.out.println("bound: " + bound);

		String textModus = "";
		if(modus == 0) textModus = "Copy";
		if(modus == 2) textModus = "DPCM";

		RasterImage result = new RasterImage(width, height);
		int[] lastPixel = new int[result.argb.length];

		// Rice Codierung
		if(M == Math.pow(2, b)){
			bound = 0;
		}
		if(modus == 0) { // Copy codiert
			for (int pos = 0; pos < result.argb.length; pos++) {
				// inB lesen bis bit = 0
				boolean notZero = true;
				int qBit;
				int oneBitCounter = 0;
				while (notZero) {
					qBit = inB.read(1);
					if (qBit == 0) {
						break;
					}
					oneBitCounter++;
				}

				// anzahl 1en = q
				int q = oneBitCounter;

				// b - 1 = anzahl zu lesender Bits berechnen
				int readBits = b - 1;

				int r = 0;
				int figure = inB.read(readBits);

				// wenn zahl kleiner als grenze ist zahl auslesen
				if (figure < bound) r = figure;

				// if(zahl >= grenze ein weiteres bit lesen)
				if (figure >= bound) {
					int figureWithNextBit = figure << 1 | inB.read(1);
					r = figureWithNextBit - bound;
				}

				int pixel = q * M + r;

				// clamping
				if (pixel > 255) pixel = 255;
				if (pixel < 0) pixel = 0;

				System.out.print(pixel + ", ");
				result.argb[pos] = (0xff << 24) | (pixel << 16) | (pixel << 8) | pixel;
			}
		}
		int pixelBefore = 128;

		if(modus == 2){ // DPMC horizontal codiert
			for (int pos = 0; pos < result.argb.length; pos++) {
				// inB lesen bis bit = 0
				boolean notZero = true;
				int qBit;
				int oneBitCounter = 0;
				while (notZero) {
					qBit = inB.read(1);
					if (qBit == 0) {
						break;
					}
					oneBitCounter++;
				}

				// anzahl 1en = q
				int q = oneBitCounter;

				// b - 1 = anzahl zu lesender Bits berechnen
				int readBits = b - 1;

				int r = 0;
				int figure = inB.read(readBits);

				// wenn zahl kleiner als grenze ist zahl auslesen
				if (figure < bound) r = figure;

				// if(zahl >= grenze ein weiteres bit lesen)
				if (figure >= bound) {
					int figureWithNextBit = figure << 1 | inB.read(1);
					r = figureWithNextBit - bound;
				}

				int pixel = q * M + r;

				if(pixel % 2 == 0){ // even
					pixel = pixel /2 ;
				} else { // odd
					pixel = ((pixel +1)/2)*-1;
				}

				pixel = pixel + pixelBefore;
				pixelBefore = pixel;


				// clamping
				if (pixel > 255) pixel = 255;
				if (pixel < 0) pixel = 0;

				// System.out.print(pixel + ", ");
				result.argb[pos] = (0xff << 24) | (pixel << 16) | (pixel << 8) | pixel;
			}
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
