// BVK Ue1 SS2022 Vorgabe
//
// Copyright (C) 2022 by Klaus Jung
// All rights reserved.
// Date: 2021-03-24

package bvk_ss22;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class Golomb {
	
	public static void encodeImage(RasterImage image, DataOutputStream out) throws IOException {

		int width = image.width;
		int height = image.height;
		int lauflaenge = 0;

		out.writeInt(width);
		out.writeInt(height);

		// LOOP 1
		// iterate over image get number of colors & save color values
		int l = 0;
		HashSet<Integer> colorValues = new HashSet<>();

		// 1. set
		// iterate set create map
		// 2. map key = Farbe, value = index

		System.out.println("ARGB size: " + image.argb.length);
		for (int x=0; x < image.argb.length; x++) {

				int argbCurrentColor = image.argb[x];
				colorValues.add(argbCurrentColor);
		}

		System.out.println(colorValues.size() + " Colors in this image");

		out.writeInt(colorValues.size()); // write number of colors in Stream

		HashMap<Integer, Integer> colors = new HashMap<>();

		Iterator<Integer> iter = colorValues.iterator();
		int count=0;
		while (iter.hasNext()){
			int next = iter.next();
			System.out.println(next);
			colors.put(count, next);				//hier werden die Farben mit aufsteigendem count in die hashmap geladen
			count++;
			out.writeInt(next); // write color palette in Output Stream
		}
		
		// LOOP 2
		// iterate over image ReadOut Values
		int currentColor;
		int lastColor;

		System.out.println();
		for (int x=0; x < image.argb.length; x++) {

				currentColor = image.argb[x]; // get current color from position

				if(x==0){lastColor=currentColor;}
				else{lastColor=image.argb[x-1];}

				if (lastColor != currentColor) { // check if color switch
					if(colors.containsValue(lastColor)) { // color is already saved

						for (int key : colors.keySet()) {
							if(colors.get(key) == lastColor){
								System.out.println("ouStream(" + key + ") Lauflänge: " + lauflaenge);
								out.writeByte(key);
								out.writeByte(lauflaenge);
								lauflaenge = 0;

							}
						}

					}
				}
				else {
					if(lauflaenge == 254){
						for (int key : colors.keySet()) {
							if (colors.get(key) == lastColor) {
								System.out.println("ouStream(" + key + ") Lauflänge: " + lauflaenge);
								out.writeByte(key);
								out.writeByte(lauflaenge);
								lauflaenge = 0;

							}
						}
					}
					else {
						lauflaenge++;

					}
				}
		}
		System.out.println("_________________________________________________________________________________________");
		System.out.println("width: " + image.width);
		System.out.println("height: " + image.height);
		System.out.println("n: " + colorValues.size());
		int i = 0;
		for (Iterator<Integer> iter2 = colorValues.iterator(); iter2.hasNext(); i++) {
			int next = iter2.next();
			System.out.println("(n: " + i + " ,color: " + next + ")");
		}
		System.out.println("_________________________________________________________________________________________");

	}

	public static RasterImage decodeImage(DataInputStream in) throws IOException {
		int width;
		int height;
		int modus;
		int M;
		BitInputStream inB = new BitInputStream(in);

		// read width and height from DataInputStream
		width = inB.read(16);
		height = inB.read(16);
		modus = inB.readByte();
		M = inB.readByte();

		String textModus = "";

		if(modus == 0) textModus = "Copy";
		if(modus == 2) textModus = "DPCM";

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
