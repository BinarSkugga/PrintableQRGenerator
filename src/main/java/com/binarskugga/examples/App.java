package com.binarskugga.examples;

import com.binarskugga.*;
import com.itextpdf.text.*;

import java.util.*;
import java.util.List;

public class App {

	public static void main(String[] args) {
		// This example works with the Avery template #5167
		QRSerialGenerator<UUID> generator = new ExampleQRGenerator();

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

		// Generate 800 stickers (fitting on exactly 10 pages) into "simple-generator.pdf".
		generator.setBatchSize(800);
		//generator.generate("simple-generator.pdf");


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
		// This is the margins around your stickers.
		collectionGenerator.setOutsetMargin(new RectangleReadOnly(5f / 16f, 0.5f));
		// This is the margins between your stickers.
		collectionGenerator.setInsetMargin(new RectangleReadOnly(5f / 16f, 0));
		// This is the size of a single sticker.
		collectionGenerator.setStickerSize(new RectangleReadOnly(1.75f, 0.5f));

		// Amount of stickers on one line.
		collectionGenerator.setLineSize(4);

		// Generate all collection stickers into simple-collection-generator.pdf.
		collectionGenerator.generate("simple-collection-generator.pdf");
	}

}
