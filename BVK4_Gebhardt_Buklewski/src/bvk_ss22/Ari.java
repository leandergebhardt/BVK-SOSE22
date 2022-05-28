// BVK Ue1 SS2022 Vorgabe
//
// Copyright (C) 2022 by Klaus Jung
// All rights reserved.
// Date: 2021-03-24

package bvk_ss22;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Ari {

	public static void encodeImage(RasterImage image, DataOutputStream out) throws IOException {
		int width = image.width;
		int height = image.height;
		BitOutputStream outB = new BitOutputStream(out);
		outB.write(width, 16);
		outB.write(height, 16);

		double p0 = generateP0(image);

		int valueForFileFormat = (int)Math.round(p0 * 0x4000);
		// double p0 = (double)valueForFileFormat / 0x4000;
		outB.write(valueForFileFormat, 16);

		// Intervalle initialisieren
		double[] a = {0, 1};
		double[] b = {0, 1};
		a[0] = p0;
		a[1] = 1 -p0;

		System.out.println("b[0]= "+ b[0] + " b[1]= " + b[1]);

		double reducedP0 = reduceAccuracy(p0);

		// Schleife solange noch Symbole zu codieren
		for(int i = 0; i < image.argb.length; i++){
			int pixel = image.argb[i] & 0x000000ff;
			boolean black = pixel == 0;

			// a aktualisieren
			// skalierung der Intervalle


			// Innere Schleife
			while(true){
				// a in obere Hälfte von b
				if(b[1] / 2 <= a[0] && a[1] <= b[1]){
					outB.write(1, 1);
					// TODO: aktualisiere b
				}
				// a in unterer Hälfte von b
				else if(b[0] <= a[0] && a[1] <= b[1] / 2){
					outB.write(0, 1);
					// TODO: aktualisiere b
				}
				else {
					break;
				}

			}
		}

		outB.close();

		System.out.println("_________________________________________________________________________________________");
		System.out.println("Finished Encoding");
		System.out.println("width: " + width + " height: " + height);
		//System.out.println("Modus: " + modus );
		System.out.println(out.size() / 1000 + " KB");
		System.out.println("_________________________________________________________________________________________");

	}

	public static RasterImage decodeImage(DataInputStream in, double p0) throws IOException {
		BitInputStream inB = new BitInputStream(in);

		int width = inB.read(16);
		int height = inB.read(16);

		RasterImage result = new RasterImage(width, height);

		int valueForFileFormat = (int)Math.round(p0 * 0x4000);

		double[] intervallSeen = new double[2];
		double[] intervallGenerated = new double[2];
		intervallSeen[0] = p0;
		intervallSeen[1] = 1 -p0;

		System.out.println("");
		System.out.println("_________________________________________________________________________________________");
		System.out.println("width: " + width + " height: " + height);
		//System.out.println("Modus: ");
		//System.out.println("M = " + M);
		// System.out.println();
		System.out.println("_________________________________________________________________________________________");

		return result;
	}

	public static double generateP0(RasterImage input){
		double p0;

		int nBlack = 0;

		for (int i = 0; i < input.argb.length; i++){
			int pixel = input.argb[i] & 0x000000ff;
			if(pixel == 0)
				nBlack++;
		}
		p0 = (double)nBlack / input.argb.length;
		System.out.println("p0 = " + p0);

		return p0;
	}

	private static double[] scale(double[] a, double[] b){
		// E1
		if(a[0] <= 0 && a[1] <= 0.5){

		}
		// E2
		else if(a[0] <= 0.5 && a[1] <= 1){

		}
		// E3
		else if(a[0] <= 0.25 && a[0] <= 0.75){

		}
		return a;
	}

	private static double reduceAccuracy(double value){
		final double div = 0x40000000;
		return Math.round(value * div) / div;
	}

}
