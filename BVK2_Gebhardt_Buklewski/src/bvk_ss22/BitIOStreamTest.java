// BVK Ue2 Unit Test
//
// Copyright (C) 2019 by Klaus Jung
// All rights reserved.
// Date: 20198-05-13

package bvk_ss22;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BitIOStreamTest {
	
	int[] values = { 0b11011, 0b1001100, 0b01, 0b00010101101011010110, 0b101 };
	int[] bits   = {       5,         7,    2,                     20,     3 };
	int[][] streams = {
			{ 0b11011000 },
			{ 0b11011100, 0b11000000 },
			{ 0b11011100, 0b11000100, },
			{ 0b11011100, 0b11000100, 0b01010110, 0b10110101, 0b10000000 },
			{ 0b11011100, 0b11000100, 0b01010110, 0b10110101, 0b10101000 },			
	};

	public void setUp() throws Exception {
		
	}
	
	@Test
	public void testBitOutputStream() throws Exception {
		for(int i = 1; i <= values.length; i++) {
			testBitOutputStream(i);
		}
	}
	
	private void testBitOutputStream(int num) throws Exception {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		BitOutputStream bos = new BitOutputStream(os);
		
		for(int i = 0; i < num; i++) {
			bos.write(values[i], bits[i]);
		}
		bos.close();
		byte[] bytes = os.toByteArray();
		//printOutputStream(bytes);
		
		assertEquals(Float.parseFloat("Writing " + num + " values: OutputStream size mismatch:"), streams[num-1].length, bytes.length);
		
		for(int i = 0; i < streams[num-1].length; i++) {
			assertEquals(Float.parseFloat("Writing " + num + " values: Value " + i + " mismatch:"), streams[num-1][i], ((int)bytes[i]) & 0xff);
		}
		
	}
	
	@Test
	public void testBitInputStream() throws Exception {
		for(int i = 1; i <= values.length; i++) {
			testBitInputStream(i);
		}
	}

	private void testBitInputStream(int num) throws Exception {
		byte[] data = new byte[streams[num-1].length];
		for(int i = 0; i < data.length; i++)
			data[i] = (byte)streams[num-1][i];
		int[] readValues = new int[num];

		ByteArrayOutputStream is = new ByteArrayOutputStream(data);
		BitInputStream bis = new BitInputStream(is);
		
		for(int i = 0; i < num; i++) {
			readValues[i] = bis.read(bits[i]);
		}
		bis.close();
		
		for(int i = 0; i < num; i++) {
			assertEquals(Float.parseFloat("Reading " + num + " values: Value " + i + " mismatch:"), values[i], readValues[i]);
		}
		
	}
	
	
	public void printOutputStream(byte[] bytes) {
		for(int value : bytes) {
			System.out.print(String.format("0x%02x, ", ((int)value) & 0xff));
		}
		System.out.println("");
	}

}
