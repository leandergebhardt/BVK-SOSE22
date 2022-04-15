// BVK Ue1 SS2022 Vorgabe
//
// Copyright (C) 2022 by Klaus Jung
// All rights reserved.
// Date: 2021-03-24

package bvk_ss22;

import com.sun.org.apache.bcel.internal.generic.NEWARRAY;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class RLE {
	
	public static void encodeImage(RasterImage image, DataOutputStream out) throws IOException {
		
		// TODO: write RLE data to DataOutputStream
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

		out.writeInt(colorValues.size()); // write number of colors in Stream

		Iterator<Integer> iter = colorValues.iterator();
		while (iter.hasNext()){
			int next = iter.next();
			System.out.println(next);

			out.writeInt(next); // write color palette in Output Stream
		}

		// int[] colors = new int[numberOfColors]; // should be Map
		HashMap<Integer, Integer> colors = new HashMap<>();

		// LOOP 2
		// iterate over image ReadOut Values
		int currentColor;
		int lastColor;
		int colorIndex = 0;
		System.out.println();
		for (int x=0; x < image.argb.length; x++) {

				currentColor = image.argb[x]; // get current color from position
				lastColor = image.argb[x - lauflaenge];

				if (lastColor != currentColor) { // check if color switch
					if(isNewColor(colors, lastColor)){
						
					}
				}
				else {
					lauflaenge++;
					if(lauflaenge >= 255){
						System.out.println("outStream(" + colorIndex + ") Lauflänge: " + lauflaenge);
						out.writeByte(colorIndex);
						out.writeByte(lauflaenge);
						lauflaenge = 0;
					}
				}
		}
		System.out.println("_________________________________________________________________________________________");
		System.out.println("width: " + image.width);
		System.out.println("height: " + image.height);
		System.out.println("n: " + colorValues.size());
		//Iterator<Integer> iter2 = colorValues.iterator();
		int i = 0;
		for (Iterator<Integer> iter2 = colorValues.iterator(); iter2.hasNext(); i++) {
			int next = iter2.next();
			System.out.println("(index: " + i + ", color: " + next + ")");
		}
		System.out.println("_________________________________________________________________________________________");

	}

	public static boolean isNewColor(HashMap<Integer, Integer> colors, int lastColor) throws IOException {
		if( colors.containsValue(lastColor)) { // color is already saved
			for (int key : colors.keySet()) {
				if(colors.get(key) == lastColor){
					System.out.println("ouStream(" + key + ") Lauflänge: " + lauflaenge);
					out.writeByte(key);
					out.writeByte(lauflaenge);
					lauflaenge = 0;
					return false;
				}
			}
		} else { // color is new
			colors.put(colorIndex, lastColor);
			System.out.println("outStream(" + colorIndex + ") Lauflänge: " + lauflaenge);
			out.writeByte(colorIndex); // write colorIndex to Stream
			out.writeByte(lauflaenge); // write lauflänge to Stream
			colorIndex++;
			lauflaenge = 0;
			return true;
		}
	}

	public static RasterImage decodeImage(DataInputStream in) throws IOException {
		int width;
		int height;
		int numberOfColors;
		int argb;


		// read width and height from DataInputStream
		width = in.readInt();
		height = in.readInt();
		numberOfColors = in.readInt();

		int[] colors = new int[numberOfColors];  //color array

		for(int i=0; i<numberOfColors; i++){
			argb = in.readInt();
			colors[i] = argb;
		}


		// create RasterImage to be returned
		RasterImage image = new RasterImage(width, height);

		// TODO: read remaining RLE data from DataInputStream and reconstruct image

		int x = 0;										//pixel count
		while(in.available()>0){						//solange wie datastream available

			int index = in.readByte() & 0xff;			//farbindex
			int lauflaenge = in.readByte() & 0xff;		//lauflänge gezogen
			System.out.println("lauflänge decode: " + lauflaenge);
			if(lauflaenge> 255){
				while(lauflaenge>255) {
					int l = 255;
					lauflaenge -= 255;
					for (int i = 0; i <= l; i++) {
						image.argb[x] = colors[index];
						x++;
					}
					for (int i = 0; i < lauflaenge; i++) {
						image.argb[x] = colors[index];
						x++;
					}

				}
				/**
				int overhead = lauflaenge - 255;
				int l = 255;
				for (int i = 0; i <= l; i++) {
					image.argb[x] = colors[index];
				}
				for (int i = 0; i <= overhead; i++) {
					image.argb[x] = colors[index];
				}
				**/
			}
			else {
				//lauflänge +1, da lauflänge 0 1 bedeutet
				lauflaenge++;
				//für die definierte lauflänge wird der pixel in der definierten farbe gefärbt und pixelcount +1
				for (int i = 0; i < lauflaenge; i++) {
					if(index == 8) {
						System.out.println("But how");
					}
					image.argb[x] = colors[index];
					x++;
				}
			}
		}

		return image;
	}

}
