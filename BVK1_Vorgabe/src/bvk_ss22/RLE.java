// BVK Ue1 SS2022 Vorgabe
//
// Copyright (C) 2022 by Klaus Jung
// All rights reserved.
// Date: 2021-03-24

package bvk_ss22;

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
		
		// TODO: read width and height from DataInputStream
//		width = ...;
//		height = ...;
		
		// create RasterImage to be returned
		RasterImage image = new RasterImage(width, height);

		// TODO: read remaining RLE data from DataInputStream and reconstruct image

		return image;
	}

}
