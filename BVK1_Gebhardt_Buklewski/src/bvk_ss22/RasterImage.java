// BVK Ue1 SS2022 Vorgabe
//
// Copyright (C) 2022 by Klaus Jung
// All rights reserved.
// Date: 2021-03-24

package bvk_ss22;

import java.io.File;
import java.util.Arrays;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

public class RasterImage {
	
	private static final int gray  = 0xffa0a0a0;

	public int[] argb;	// pixels represented as ARGB values in scanline order
	public int width;	// image width in pixels
	public int height;	// image height in pixels
	
	public RasterImage(int width, int height) {
		// creates an empty RasterImage of given size
		this.width = width;
		this.height = height;
		argb = new int[width * height];
		Arrays.fill(argb, gray);
	}
	
	public RasterImage(File file) {
		// creates an RasterImage by reading the given file
		Image image = null;
		if(file != null && file.exists()) {
			image = new Image(file.toURI().toString());
		}
		if(image != null && image.getPixelReader() != null) {
			width = (int)image.getWidth();
			height = (int)image.getHeight();
			argb = new int[width * height];
			image.getPixelReader().getPixels(0, 0, width, height, PixelFormat.getIntArgbInstance(), argb, 0, width);
		} else {
			// file reading failed: create an empty RasterImage
			this.width = 256;
			this.height = 256;
			argb = new int[width * height];
			Arrays.fill(argb, gray);
		}
	}
	
	public RasterImage(ImageView imageView) {
		// creates a RasterImage from that what is shown in the given ImageView
		Image image = imageView.getImage();
		width = (int)image.getWidth();
		height = (int)image.getHeight();
		argb = new int[width * height];
		image.getPixelReader().getPixels(0, 0, width, height, PixelFormat.getIntArgbInstance(), argb, 0, width);
	}
	
	public void setToView(ImageView imageView) {
		// sets the current argb pixels to be shown in the given ImageView
		if(argb != null) {
			WritableImage wr = new WritableImage(width, height);
			PixelWriter pw = wr.getPixelWriter();
			pw.setPixels(0, 0, width, height, PixelFormat.getIntArgbInstance(), argb, 0, width);
			imageView.setImage(wr);
		}
	}
		
	// image operations
	
	public double getMSEfromComparisonTo(RasterImage image) {
		// TODO: compare images "this" and "image" and return the Mean Square Error
		double error = 0.0;
		double sum = 0.0;
		// Einzelene Farbwerte shiften
		for (int pos = 0; pos < image.argb.length; pos++){
			int rA = (image.argb[pos] >> 16) & 0xff;
			int gA = (image.argb[pos] >> 8) & 0xff;
			int bA = image.argb[pos] & 0xff;

			int rB = (this.argb[pos] >> 16) & 0xff;
			int gB = (this.argb[pos] >> 8) & 0xff;
			int bB = this.argb[pos] & 0xff;

			error += Math.pow(rB-rA,2);
			error += Math.pow(gB-gA,2);
			error += Math.pow(bB-bA,2);

			sum = sum + error * error;
		}

		double numberOfPixel = image.argb.length;
		double mse = numberOfPixel / sum;
		System.out.println("Pixels: " + numberOfPixel + " MSE: " + mse);
		return mse;
	}
	

}
