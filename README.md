# New features
Please let me know via an issue if you want a new feature ! You can even make it yourself and make a pull request ! :)

# Simple use
``` java
public class ExampleQRGenerator extends QRSerialGenerator<UUID> {

	@Override
	protected UUID nextValue(UUID previous) {
		return UUID.randomUUID();
	}

	@Override
	protected String toLabel(UUID current) {
		return "UUID v" + current.version();
	}

}
```
``` java
// This example works with the Avery template #5167
QRSerialGenerator<UUID> generator = new ExampleQRGenerator();

// All rectangles take values in inches.
// This is the size of the sheet of paper.
generator.setPageSize(new RectangleReadOnly(8.5f, 11f));
// This is the margins around your page.
generator.setOutsetMargin(new RectangleReadOnly(5f / 16f, 0.5f));
// This is the margins between your stickers.
generator.setInsetMargin(new RectangleReadOnly(5f / 16f, 0));
// This is the size of a single sticker.
generator.setStickerSize(new RectangleReadOnly(1.75f, 0.5f));

// Amount of stickers on one line.
generator.setLineSize(4);

// Generate 800 stickers (fitting on exactly 10 pages) into "simple-generator.pdf".
generator.setBatchSize(800);
generator.generate("simple-generator.pdf");
```

> Click on the image to test scan it.

![Result](https://raw.githubusercontent.com/BinarSkugga/PrintableQRGenerator/master/result.png)

# From a collection
``` java
public class ExampleQRGeneratorCollection extends QRSerialCollectionGenerator<SimplePojo> {

	public ExampleQRGeneratorCollection(List<SimplePojo> items) {
		super(items);
	}

	@Override
	protected String toValue(SimplePojo current) {
		return "SIMPLE-" + current.getId().toString();
	}

	@Override
	protected String toLabel(SimplePojo current) {
		return current.getName() + " " + current.getLastName().charAt(0) + ".";
	}

}
```
``` java
// This example works with the Avery template #5167
List<SimplePojo> items = Arrays.asList(
		new SimplePojo(UUID.randomUUID(), "Charles", "Smith"),
		new SimplePojo(UUID.randomUUID(), "Louis-Philippe", "Potvin"),
		new SimplePojo(UUID.randomUUID(), "Jonathan", "Métras"),
		new SimplePojo(UUID.randomUUID(), "Alexandre", "Marchand"),
		new SimplePojo(UUID.randomUUID(), "Frédéric", "Deschênes")
);
QRSerialCollectionGenerator<SimplePojo> collectionGenerator = new ExampleQRGeneratorCollection(items);

// All rectangles take values in inches.
// This is the size of the sheet of paper.
collectionGenerator.setPageSize(new RectangleReadOnly(8.5f, 11f));
// This is the margins around your page.
collectionGenerator.setOutsetMargin(new RectangleReadOnly(5f / 16f, 0.5f));
// This is the margins between your stickers.
collectionGenerator.setInsetMargin(new RectangleReadOnly(5f / 16f, 0));
// This is the size of a single sticker.
collectionGenerator.setStickerSize(new RectangleReadOnly(1.75f, 0.5f));

// Amount of stickers on one line.
collectionGenerator.setLineSize(4);

// Generate all collection stickers into simple-collection-generator.pdf.
collectionGenerator.generate("simple-collection-generator.pdf");
```

> Click on the image to test scan it.

![Result](https://raw.githubusercontent.com/BinarSkugga/PrintableQRGenerator/master/result-collection.png)
