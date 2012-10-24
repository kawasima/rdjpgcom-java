package net.unit8.jpeg;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

public class RdJpgComTest {
	@Test
	public void testRGB() throws IOException {
		InputStream in = getClass().getClassLoader().getResourceAsStream("RGB.jpg");
		JpegMetaData meta = new RdJpgCom().scanJpegHeader(new DataInputStream(in));
		System.out.println(meta);
	}
	@Test
	public void testCMYK() throws IOException {
		InputStream in = getClass().getClassLoader().getResourceAsStream("CMYK-with-profile.jpg");
		JpegMetaData meta = new RdJpgCom().scanJpegHeader(new DataInputStream(in));
		System.out.println(meta);
		InputStream in2 = getClass().getClassLoader().getResourceAsStream("CMYK-without-profile.jpg");
		JpegMetaData meta2 = new RdJpgCom().scanJpegHeader(new DataInputStream(in2));
		System.out.println(meta2);
	}
}
