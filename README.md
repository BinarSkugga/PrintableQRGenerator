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
QRSerialGenerator<UUID> generator = new ExampleQRGenerator();

generator.setPageSize(new RectangleReadOnly(8.5f, 11f));
generator.setOutsetMargin(new RectangleReadOnly(5f / 16f, 0.5f));
generator.setInsetMargin(new RectangleReadOnly(5f / 16f, 0));
generator.setStickerSize(new RectangleReadOnly(1.75f, 0.5f));

generator.setLineSize(4);
generator.setBatchSize(800);
generator.setEcc(QrCode.Ecc.LOW);

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

collectionGenerator.setPageSize(new RectangleReadOnly(8.5f, 11f));
collectionGenerator.setOutsetMargin(new RectangleReadOnly(5f / 16f, 0.5f));
collectionGenerator.setInsetMargin(new RectangleReadOnly(5f / 16f, 0));
collectionGenerator.setStickerSize(new RectangleReadOnly(1.75f, 0.5f));

collectionGenerator.setLineSize(4);
collectionGenerator.setLabelFontSize(10);
collectionGenerator.generate("simple-collection-generator.pdf");
```

> Click on the image to test scan it.

![Result Collection](https://raw.githubusercontent.com/BinarSkugga/PrintableQRGenerator/master/result-collection.png)
