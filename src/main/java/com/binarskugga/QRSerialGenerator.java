package com.binarskugga;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import io.nayuki.qrcodegen.*;
import lombok.*;

import javax.imageio.*;
import java.awt.image.*;
import java.io.*;

public abstract class QRSerialGenerator<T> {

	/**
	 * Default ratio of a PDF document. This is specified in the standard, one user unit is 1/72 of an inch.
	 */
	public static final float UNIT_INCH_RATIO = 72;

	/**
	 * This is the physical measurement for the sheet of paper you are using for printing the codes.
	 */
	@Getter @Setter
	@Builder.Default
	private RectangleReadOnly pageSize = new RectangleReadOnly(8.5f, 11f);

	/**
	 * This is the physical measurement of one single sticker.
	 */
	@Getter @Setter
	private RectangleReadOnly stickerSize;

	/**
	 * This is the physical measurement of the padding around the whole page. Height will be used for bottom
	 * and top while width will be used for left and right.
	 */
	@Getter @Setter
	private RectangleReadOnly outsetMargin;

	/**
	 * This is the physical measurement of the spacing between stickers. Height will be used for bottom
	 * and top while width will be used for left and right.
	 */
	@Getter @Setter
	private RectangleReadOnly insetMargin;

	/**
	 * This is the amount of stickers on a single line.
	 */
	@Getter @Setter
	private int lineSize;


	/**
	 * This is the total amount of stickers to be produced. If the the amount % lineSize is not equals to 0,
	 * the library will create dummy cells to complete the last row.
	 */
	@Getter @Setter
	protected int batchSize;


	/**
	 * Provider for the value to use next. Previous is passed to it for sequential generation.
	 * Collection generators do not use this method.
	 * @param previous The element processed before the current one.
	 * @return Return the element to process next.
	 */
	protected abstract T nextValue(T previous);

	/**
	 * Method to transform your object to a string. This string will be the exact value returned
	 * from scanning the corresponding QR code.
	 * @param current The element to use to create a value
	 * @return The QR code text value.
	 */
	protected String toValue(T current) {
		return current.toString();
	}

	/**
	 * Method to create the label standing to the right of the QR code.
	 * @param current The value to use to generate the label.
	 * @return The string value for the label.
	 */
	protected abstract String toLabel(T current);

	/**
	 * Generate the pdf file with all the codes. Generating large amount of stickers (>10 000) can take a while.
	 * @param file The destination pdf file.
	 */
	public void generate(String file) {
		long time = System.currentTimeMillis();
		boolean needDummies = false;
		int needDummiesAt = 0;

		if(batchSize == 0)
			throw new IllegalStateException("Need at least 1 item.");
		if(batchSize % lineSize > 0) {
			needDummies = true;
			needDummiesAt = batchSize * 2;
			batchSize += lineSize - (batchSize % lineSize);
		}

		T previousValue = null;
		Document document = new Document(new RectangleReadOnly(this.pageSize.getWidth() * UNIT_INCH_RATIO, this.pageSize.getHeight() * UNIT_INCH_RATIO));

		try {
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
			document.setMargins( this.outsetMargin.getWidth() * UNIT_INCH_RATIO, this.outsetMargin.getWidth() * UNIT_INCH_RATIO,
					this.outsetMargin.getHeight() * UNIT_INCH_RATIO, this.outsetMargin.getHeight() * UNIT_INCH_RATIO);
			document.open();

			float spacingWidth = this.insetMargin.getWidth() * UNIT_INCH_RATIO;
			float cellWidth = this.stickerSize.getWidth() * UNIT_INCH_RATIO;

			PdfPTable table = new PdfPTable(this.lineSize * 2);
			table.setTotalWidth(new float[]{cellWidth, spacingWidth, cellWidth, spacingWidth, cellWidth, spacingWidth, cellWidth, 0});
			table.setLockedWidth(true);
			table.setSpacingAfter(0);
			table.setSpacingBefore(0);

			for(int i = 0; i < batchSize * 2; i++){
				PdfPCell cell = new PdfPCell();
				cell.setBorder(0);

				if(i % 2 > 0)  {
					table.addCell(cell);
					continue;
				}

				if(i % 2 == 0 && needDummies && i >= needDummiesAt) {
					table.addCell(cell);
					continue;
				}

				//cell.setBackgroundColor(BaseColor.GRAY);
				cell.setPadding(0);
				cell.setPaddingLeft(this.stickerSize.getWidth() * 0.05f * UNIT_INCH_RATIO);
				cell.setPaddingBottom(this.insetMargin.getHeight() * UNIT_INCH_RATIO);
				cell.setFixedHeight((this.stickerSize.getHeight() + this.insetMargin.getHeight()) * UNIT_INCH_RATIO);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);

				T value = nextValue(previousValue);
				previousValue = value;

				QrCode qr0 = QrCode.encodeText(toValue(value), QrCode.Ecc.HIGH);
				BufferedImage img = qr0.toImage(4, 8);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ImageIO.write(img, "JPG", baos);
				Image pdfQR = Image.getInstance(baos.toByteArray());

				pdfQR.scaleAbsolute(new Rectangle(this.stickerSize.getHeight() * UNIT_INCH_RATIO, this.stickerSize.getHeight() * UNIT_INCH_RATIO));
				Chunk imgChunk = new Chunk(pdfQR, 0, -15);

				Phrase label = new Phrase(toLabel(value), FontFactory.getFont(FontFactory.HELVETICA, 8));

				Paragraph element = new Paragraph();
				element.add(imgChunk);
				element.add(" ");
				element.add(label);
				element.setLeading(this.stickerSize.getHeight() * UNIT_INCH_RATIO);
				element.setAlignment(Element.ALIGN_LEFT);

				cell.addElement(element);
				cell.setUseAscender(true);
				cell.setUseDescender(true);

				table.addCell(cell);
			}
			document.add(table);
		}
		catch(Exception ignored) {}
		finally {
			document.close();
			System.err.println("All " + batchSize + " codes generated in: " + (System.currentTimeMillis() - time) + "ms");
		}
	}

}
