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

public class RLE {
	
	public static void encodeImage(RasterImage image, DataOutputStream out) throws IOException {
		
		// TODO: write RLE data to DataOutputStream

	}

	public static RasterImage decodeImage(DataInputStream in) throws IOException {
		int width = 10;
		int height = 10;

		int numberOfColors;
		int argb;


		// TODO: read width and height from DataInputStream
//		width = ...;
//		height = ...;
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
