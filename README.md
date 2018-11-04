# PrintableQRGenerator
Generates tables of QR codes for automated serial sticker printing. Can be configured with Avery formats provided some mearsurements.


# Simple use
``` java
public class ExampleQRGenerator extends QRSerialGenerator {

	@Override
	protected String nextValue(String previous) {
		return UUID.randomUUID().toString();
	}

}
```
``` java
// This example works with the Avery template #5167
QRSerialGenerator generator = new ExampleQRGenerator();

// All rectangles take values in inches.
// This is the size of the sheet of paper.
generator.setPageSize(new RectangleReadOnly(8.5f, 11f));
// This is the margins around your stickers.
generator.setOutsetMargin(new RectangleReadOnly(5f / 16f, 0.5f));
// This is the margins between your stickers.
generator.setInsetMargin(new RectangleReadOnly(5f / 16f, 0));
// This is the size of a single sticker.
generator.setStickerSize(new RectangleReadOnly(1.75f, 0.5f));

// Amount of stickers on one line.
generator.setLineSize(4);
// This is a prefix value that is concatenated before your value.
generator.setPrefix("TEST");
// This is an arbitrary value. The label to the right of the QR will contain the prefix and the version.
generator.setVersion("v1.3");

// Generate 800 stickers (fitting on exactly 10 pages) in the test.pdf file.
generator.generate("test.pdf", 800);
```

![alt text](https://raw.githubusercontent.com/binarskugga/PrintableQRGenerator/master/result.png)
