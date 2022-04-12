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

public class RLE {
	
	public static void encodeImage(RasterImage image, DataOutputStream out) throws IOException {
		
		// TODO: write RLE data to DataOutputStream
		int width = image.width;
		int height = image.height;
		int numberOfColors = 0;
		int lauflaenge = 0;

		out.writeInt(width);
		out.writeInt(height);

		// LOOP 1
		// iterate over image get number of colors & save color values
		int l = 0;
		ArrayList<Integer> colorsValue = new ArrayList<Integer>();
		for (int x=0; x < image.argb.length; x++) {

				int argbLastColor = image.argb[x-l];
				int argbCurrentColor = image.argb[x];

				if(colorsValue.size() == 0){
					colorsValue.add(argbCurrentColor);
				}

				if(argbLastColor != argbCurrentColor) { // check if difference
					for (int i = 0; i < colorsValue.size(); i++) {
						if(argbCurrentColor != colorsValue.get(i)){ // check if color is already saved
							colorsValue.add(argbCurrentColor);
							numberOfColors++;
							// break;
						}
					}
				}
				else { // if no difference rise lauflänge
					l++;
				}
		}

		System.out.println(numberOfColors + " Colors in this image");

		out.writeInt(numberOfColors); // write number of colors in Stream

		for (int index = 0; index < colorsValue.size(); index++) {
			out.writeInt(colorsValue.get(index)); // write all Colors in Stream
		}
		int[] colors = new int[numberOfColors];

		// LOOP 2
		// iterate over image ReadOut Values
		int currentColor;
		int lastColor;
		int colorIndex = 0;
		for (int x=0; x < image.argb.length; x++) {

				currentColor = image.argb[x]; // get current color from position
				lastColor = image.argb[x - lauflaenge];

				if( lastColor != currentColor) { // check if differ to last known color
					for (int i = 0; i < colors.length; i++) {
						if(colors[i] != currentColor){
							colors[i] = currentColor;
						}
					}
					out.writeByte(colorIndex); // write colorIndex to Stream
					out.writeByte(lauflaenge -1); // write lauflänge to Stream
					colorIndex++;
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
