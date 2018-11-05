package com.binarskugga.examples;

import com.binarskugga.*;
import com.itextpdf.text.*;
import io.nayuki.qrcodegen.*;

import java.util.*;
import java.util.List;

public class App {

	public static void main(String[] args) {
		// This example works with the Avery template #5167
		QRSerialGenerator<UUID> generator = new ExampleQRGenerator();

		generator.setPageSize(new RectangleReadOnly(8.5f, 11f));
		generator.setOutsetMargin(new RectangleReadOnly(5f / 16f, 0.5f));
		generator.setInsetMargin(new RectangleReadOnly(5f / 16f, 0));
		generator.setStickerSize(new RectangleReadOnly(1.75f, 0.5f));

		generator.setLineSize(4);
		generator.setBatchSize(800);
		generator.setEcc(QrCode.Ecc.LOW);

		generator.generate("simple-generator.pdf");


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
	}

}
