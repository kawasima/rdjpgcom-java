package net.unit8.jpeg;

public class JpegMetaData {
	public enum Format {
		BASELINE, EXTENDED_SEQUENTIAL, PROGRESSIVE, LOSSLESS,
		DIFFERENTIAL_SEQUENTIAL, DIFFERENTIAL_PROGRESSIVE, DIFFERENTIAL_LOSSLESS, UNKNOWN,
	}

	private int width;
	private int height;
	private int colorComponents;
	private int precision;
	private Format format;
	private boolean arithmeticCoding;
	private String comment;

	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public int getColorComponents() {
		return colorComponents;
	}
	public void setColorComponents(int colorComponents) {
		this.colorComponents = colorComponents;
	}
	public int getPrecision() {
		return precision;
	}
	public void setPrecision(int precision) {
		this.precision = precision;
	}
	public Format getFormat() {
		return format;
	}
	public void setFormat(Format format) {
		this.format = format;
	}
	public boolean isArithmeticCoding() {
		return arithmeticCoding;
	}
	public void setArithmeticCoding(boolean arithmeticCoding) {
		this.arithmeticCoding = arithmeticCoding;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public String toString() {
		return String.format("JPEG image is %dw * %dh, %d color components, %d bits per sample\n"
				+ "JPEG process: %s", width, height, colorComponents, precision, format);
	}
}
