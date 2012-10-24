package net.unit8.jpeg;

import java.io.DataInputStream;
import java.io.IOException;

import net.unit8.jpeg.JpegMetaData.Format;

public class RdJpgCom {
	@SuppressWarnings("unused")
	private static final int M_APP0  = 0xE0;
	private static final int M_APP12 = 0xEC;
	private static final int M_COM   = 0xFE;
	private static final int M_EOI   = 0xD9;
	private static final int M_SOF0  = 0xC0;
	private static final int M_SOF1  = 0xC1;
	private static final int M_SOF10 = 0xCA;
	private static final int M_SOF11 = 0xCB;
	private static final int M_SOF13 = 0xCD;
	private static final int M_SOF14 = 0xCE;
	private static final int M_SOF15 = 0xCF;
	private static final int M_SOF2  = 0xC2;
	private static final int M_SOF3  = 0xC3;
	private static final int M_SOF5  = 0xC5;
	private static final int M_SOF6  = 0xC6;
	private static final int M_SOF7  = 0xC7;
	private static final int M_SOF9  = 0xC9;
	private static final int M_SOI   = 0xD8;
	private static final int M_SOS   = 0xDA;

	public JpegMetaData scanJpegHeader(DataInputStream jpegStream) throws IOException {
		JpegMetaData meta = new JpegMetaData();
		if (firstMarker(jpegStream) != M_SOI) {
			throw new RdJpgComException("Expected SOI marker first");
		}
		while(true) {
			int marker = nextMarker(jpegStream);
			switch (marker) {
			case M_SOF0:	/* Baseline */
			case M_SOF1:	/* Extended sequential, Huffman */
			case M_SOF2:	/* Progressive, Huffman */
			case M_SOF3:	/* Losless, Huffman */
			case M_SOF5:	/* Differential sequential, Huffman */
			case M_SOF6:	/* Differential progressive, Huffman */
			case M_SOF7:	/* Differential lossless, Huffman */
			case M_SOF9:	/* Extended sequential, arithmetic */
			case M_SOF10:	/* Progressive, arithmetic */
			case M_SOF11:	/* Lossless, arithmetic */
			case M_SOF13:	/* Differential sequential, arithmetic */
			case M_SOF14:	/* Differential progressive, arithmetic */
			case M_SOF15:	/* Differential lossless, arithmetic */
				processSOFn(meta, jpegStream, marker);
				break;
			case M_SOS:
				return meta; // Skip read body
			case M_EOI:
				return meta;
			case M_COM:
				processCOM(meta, jpegStream);
				break;
			case M_APP12:

			}
		}
	}

	private void processCOM(JpegMetaData meta, DataInputStream jpegStream) throws IOException {
		int length = jpegStream.readUnsignedShort();
		if (length < 2)
			throw new RdJpgComException("Erroneous JPEG marker length");
		length -= 2;

		byte[] buf = new byte[length];
		if (jpegStream.read(buf) > 0) {
			meta.setComment(new String(buf));
		}
	}

	private void processSOFn(JpegMetaData meta, DataInputStream jpegStream, int marker) throws IOException {
		int length = jpegStream.readUnsignedShort();
		meta.setPrecision(jpegStream.readUnsignedByte());
		meta.setHeight(jpegStream.readUnsignedShort());
		meta.setWidth(jpegStream.readUnsignedShort());
		meta.setColorComponents(jpegStream.readUnsignedByte());

		switch (marker) {
		case M_SOF0:  meta.setFormat(Format.BASELINE); break;
		case M_SOF1:  meta.setFormat(Format.EXTENDED_SEQUENTIAL); break;
		case M_SOF2:  meta.setFormat(Format.PROGRESSIVE); break;
		case M_SOF3:  meta.setFormat(Format.LOSSLESS); break;
		case M_SOF5:  meta.setFormat(Format.DIFFERENTIAL_SEQUENTIAL); break;
		case M_SOF6:  meta.setFormat(Format.DIFFERENTIAL_PROGRESSIVE); break;
		case M_SOF7:  meta.setFormat(Format.DIFFERENTIAL_LOSSLESS); break;
		case M_SOF9:  meta.setFormat(Format.EXTENDED_SEQUENTIAL); meta.setArithmeticCoding(true); break;
		case M_SOF10: meta.setFormat(Format.PROGRESSIVE); meta.setArithmeticCoding(true); break;
		case M_SOF11: meta.setFormat(Format.LOSSLESS); meta.setArithmeticCoding(true); break;
		case M_SOF13: meta.setFormat(Format.DIFFERENTIAL_SEQUENTIAL); meta.setArithmeticCoding(true); break;
		case M_SOF14: meta.setFormat(Format.DIFFERENTIAL_PROGRESSIVE); meta.setArithmeticCoding(true); break;
		case M_SOF15: meta.setFormat(Format.DIFFERENTIAL_LOSSLESS); meta.setArithmeticCoding(true); break;
		default:      meta.setFormat(Format.UNKNOWN); break;
		}

		if (length != 8 + meta.getColorComponents() * 3)
			throw new RdJpgComException("Bogus SOF marker length");

		for (int ci = 0; ci < meta.getColorComponents(); ci ++) {
			jpegStream.readByte();	/* Component ID code */
			jpegStream.readByte();	/* H, V sampling factors */
			jpegStream.readByte();	/* Quantization table number */
		}
	}

	private int firstMarker(DataInputStream jpegStream) throws IOException {
		int c1 = jpegStream.readUnsignedByte();
		int c2 = jpegStream.readUnsignedByte();
		if (c1 != 0xFF || c2 != M_SOI)
			throw new RdJpgComException("Not a JPEG file");
		return c2;
	}

	private int nextMarker(DataInputStream jpegStream) throws IOException {
		int discardedBytes = 0;
		int c = jpegStream.readUnsignedByte();
		while (c != 0xFF) {
			discardedBytes++;
			c = jpegStream.readUnsignedByte();
		}
		do {
			c = jpegStream.readUnsignedByte();
		} while (c == 0xFF);

		if (discardedBytes != 0) {
			// warning
		}
		return c;
	}
}
