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
		// int numberOfColors = 0;
		int lauflaenge = 0;

		out.writeInt(width);
		out.writeInt(height);

		// LOOP 1
		// iterate over image get number of colors & save color values
		int l = 0;
		HashSet<Integer> colorValues = new HashSet<Integer>();

		// 1. set
		// iterate set create map
		// 2. map key = Farbe, value = index

		System.out.println("ARGB size: " + image.argb.length);
		for (int x=0; x < image.argb.length; x++) {

				// int argbLastColor = image.argb[x-l];
				int argbCurrentColor = image.argb[x];

			/*	if(colorValues.size() == 0){
					colorValues.add(argbCurrentColor);
				}
			 */

				if(!colorValues.contains(argbCurrentColor)){
					colorValues.add(argbCurrentColor);
				}

				/*
				if(argbLastColor != argbCurrentColor) { // check if difference
					for (int i = 0; i < colorValues.size(); i++) {
						if(argbCurrentColor != colorValues.get(i)){ // check if color is already saved
							colorValues.add(argbCurrentColor);
							numberOfColors++;
							// break;
						}
					}
				}

				else { // if no difference rise lauflänge
					l++;
				}
				 */
		}

		System.out.println(colorValues.size() + " Colors in this image");

		out.writeInt(colorValues.size()); // write number of colors in Stream

		Iterator<Integer> iter = colorValues.iterator();
		while (iter.hasNext()){
			int next = iter.next();
			System.out.println(next);

			out.writeInt(next);
		}

		/*
		for (int index = 0; index < colorValues.size(); index++) {
			out.writeInt(colorValues.get(index)); // write all Colors in Stream
		}
		 */

		HashMap<Integer, Integer> colors = new HashMap<>();
		// int[] colors = new int[numberOfColors]; // should be Map

		// LOOP 2
		// iterate over image ReadOut Values
		int currentColor;
		int lastColor;
		int colorIndex = 1;
		colors.put(0, image.argb[0]);
		System.out.println();
		for (int x=0; x < image.argb.length; x++) {

				currentColor = image.argb[x]; // get current color from position
				lastColor = image.argb[x - lauflaenge];

				if (lastColor != currentColor) { // check if color switch
					System.out.println("HashMap(" + colorIndex + ", " + currentColor + ") Lauflänge: " + lauflaenge);
					if( colors.containsValue(currentColor)) {
						for (int key : colors.keySet()) {
							if(colors.get(key) == currentColor){
								out.writeByte(key);
								out.writeByte(lauflaenge);
								break;
							}
						}
					}
					colors.put(colorIndex, currentColor);
					out.writeByte(colorIndex); // write colorIndex to Stream
					out.writeByte(lauflaenge -1); // write lauflänge to Stream
					colorIndex++;
					lauflaenge = 0;
				}
				else {
					lauflaenge++;
				}
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

			int index = in.readByte() & 0xff;			//wird farbe und
			int lauflaenge = in.readByte() & 0xff;		//lauflänge gezogen

			if(lauflaenge> 255){						//kommt quasi nie vor

				int overhead = lauflaenge - 255;
				int l = 255;
				for (int i = 0; i <= l; i++) {
					image.argb[x] = colors[index];
				}
				for (int i = 0; i <= overhead; i++) {
					image.argb[x] = colors[index];
				}

			}
			else {
				//lauflänge +1, da lauflänge 0 1 bedeutet
				lauflaenge++;
				//für die definierte lauflänge wird der pixel in der definierten farbe gefärbt und pixelcount +1
				for (int i = 0; i < lauflaenge; i++) {
					image.argb[x] = colors[index];
					x++;
				}
			}
		}

		return image;
	}

}
