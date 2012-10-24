rdjpgcom-java
=============

Tiny library for reading JPEG header.

## Usage

```java
  	InputStream in = new FileInputStream("CMYK.jpg");
		JpegMetaData meta = new RdJpgCom().scanJpegHeader(new DataInputStream(in));
    switch (meta.getColorComponents()) {
    case 3: // RGB color space
    case 4: // CMYK color space
    default: // Unknown color space
    }
```