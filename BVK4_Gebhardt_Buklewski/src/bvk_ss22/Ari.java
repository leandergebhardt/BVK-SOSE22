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
		double[] a = {0.0, 1.0};
		double[] b = {0.0, 1.0};

		double reducedP0 = reduceAccuracy(p0);

		// Schleife solange noch Symbole zu codieren
		for(int i = 0; i < image.argb.length; i++){

			System.out.println("Pos " + i + ": a = ["+ a[0] + ", " + a[1] + "), b = [" + b[0] + ", " + b[1] + ")");

			// a aktualisieren
			a[0] = reducedP0;
			a[1] = 1 - reducedP0;

			// Pixel lesen
			int pixel = image.argb[i] & 0x000000ff;
			boolean black = pixel == 0;

			// schwarzer Pixel gelesen
			if(black){
				// unteren Abschnitt a

			}
			// weißes Pixel gelesen
			else {
				// oberen Abschnitt a

			}

			// TODO: skalierung der Intervalle


			// Innere Schleife
			while(true){
				// a in obere Hälfte von b
				if(b[1] / 2 <= a[0] && a[1] <= b[1]){
					outB.write(1, 1);
					// obere Abschnitt von b
					b[0] = b[1] / 2;
					b[1] = b [1];
				}
				// a in unterer Hälfte von b
				else if(b[0] <= a[0] && a[1] <= b[1] / 2){
					outB.write(0, 1);
					// unterer Abschnitt von b
					b[0] = b[0];
					b[1] = b[1] / 2;
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

	public static RasterImage decodeImage(DataInputStream in) throws IOException {
		BitInputStream inB = new BitInputStream(in);

		int width = inB.read(16);
		int height = inB.read(16);

		double valueFromFileFormat = inB.read(16);
		double p0 = (double)valueFromFileFormat / 0x4000; // wahrscheinlichkeit schwarz
		System.out.println("p0 = "+ p0);

		RasterImage result = new RasterImage(width, height);

		// Initialisieren der Anfangsintervalle
		double[] a = {0.0, 1.0}; //richtet sich nach wahrscheinlchkeit p0
		double[] b = {0.0, 1.0}; // codierte Bitfolge



		for(int pos = 0; pos < result.argb.length; pos++){
			System.out.println("Pos " + pos + ": a = ["+ a[0] + ", " + a[1] + "), b = [" + b[0] + ", " + b[1] + ")");

			double divisionA = calculateDevision(a, p0);
			double divisionB = calculateDevision(b, 0.5);
			// Innere Schleife
			while(true) {

				// b im oberen Anteil von a
				//if (b[0] >= divisionA && b[1] <= a[1]) {
				if (b[0] >= calculateDevision(a, p0) && a[1] >= b[1]) {
					// Symbol W schreiben
					//System.out.println("oberer Teil");
					int pixel = 255;
					result.argb[pos] = (0xff << 24) | (pixel << 16) | (pixel << 8) | pixel;
					// aktualisiere a
					a[0] = calculateDevision(a, p0);

					break;
				}
				// b im unteren Anteil von a
				//else if (b[0] <= a[1] && b[1] < divisionA) {
				else if (a[0]<= b[0] && b[1] <= calculateDevision(a, p0)) {
					// Symbol S schreiben
					//System.out.println("unterer Teil");
					int pixel = 0;
					result.argb[pos] = (0xff << 24) | (pixel << 16) | (pixel << 8) | pixel;
					// aktualisiere a

					//double irgendwas = (a[1]-a[0])*p0;
					//a[1] = a[0]+ irgendwas;
					a[1] = calculateDevision(a, p0);
					break;
				}
				// aktualisiere b
				int bit = inB.read(1);
				System.out.println("read a bit");
				if(bit == 0){
					b[1] = calculateDevision(b, 0.5);;}
				else{b[0] = calculateDevision(b, 0.5);;}
			}

			scale(a,b);



		}


		System.out.println("");
		System.out.println("_________________________________________________________________________________________");
		System.out.println("width: " + width + " height: " + height);
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

	private static double[] scale(double[] a,double[] b){
		// E1
		if(0.0 <= a[0] && a[1] <= 0.5){
			a[0] = a[0]*2.0;
			a[1] = a[1]*2.0;

			b[0] = b[0]*2.0;
			b[1] = b[1]*2.0;
		}
		// E2
		else if(0.5 <= a[0] && a[1] <= 1.0){
			a[0] = (a[0]-0.5)*2.0;
			a[1] = (a[1]-0.5)*2.0;

			b[0] = (b[0]-0.5)*2.0;
			b[1] = (b[1]-0.5)*2.0;
		}
		// E3
		else if(0.25 <= a[0] && a[1] <= 0.75){
			a[0] = (a[0]-0.25)*2.0;
			a[1] = (a[1]-0.25)*2.0;

			b[0] = (b[0]-0.25)*2.0;
			b[1] = (b[1]-0.25)*2.0;
		}
		return a;
	}

	private static double reduceAccuracy(double value){
		final double div = 0x40000000;
		return Math.round(value * div) / div;
	}

	private static double calculateDevision(double[] a, double p0){
		double result = a[0] + ((a[1]-a[0])*p0);
		//return (a[0] + p0) * (a[1] - a[0]);
		return result;
	}

}
