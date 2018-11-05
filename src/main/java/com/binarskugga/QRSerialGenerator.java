package com.binarskugga;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import io.nayuki.qrcodegen.*;
import lombok.*;

import javax.imageio.*;
import java.awt.image.*;
import java.io.*;

public abstract class QRSerialGenerator<T> {

	public static final float UNIT_INCH_RATIO = 72;

	@Getter @Setter
	@Builder.Default
	private RectangleReadOnly pageSize = new RectangleReadOnly(8.5f, 11f);

	@Getter @Setter
	private RectangleReadOnly stickerSize;

	@Getter @Setter
	private RectangleReadOnly outsetMargin;

	@Getter @Setter
	private RectangleReadOnly insetMargin;

	@Getter @Setter
	private int lineSize;

	@Getter @Setter
	protected int batchSize;

	protected abstract T nextValue(T previous);
	protected abstract String toLabel(T current);

	protected String toValue(T current) {
		return current.toString();
	}

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
