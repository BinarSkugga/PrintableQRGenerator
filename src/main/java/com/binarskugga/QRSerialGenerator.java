package com.binarskugga;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import io.nayuki.qrcodegen.*;
import lombok.*;

import javax.imageio.*;
import java.awt.image.*;
import java.io.*;

public abstract class QRSerialGenerator {

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
	private String prefix;

	@Getter @Setter
	private String version;

	@Getter @Setter
	private String labelFormat = "{1}-{2}";

	protected String nextValue(String previous) {
		return null;
	}

	public void generate(String file, int amount) {
		long time = System.currentTimeMillis();
		String previousValue = null;
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

			for(int i = 0; i < amount * 2; i++){
				PdfPCell cell = new PdfPCell();
				cell.setBorder(0);
				//cell.setBackgroundColor(BaseColor.GRAY);
				cell.setPadding(0);
				cell.setPaddingLeft(this.stickerSize.getWidth() * 0.05f * UNIT_INCH_RATIO);
				cell.setPaddingBottom(this.insetMargin.getHeight() * UNIT_INCH_RATIO);
				cell.setFixedHeight((this.stickerSize.getHeight() + this.insetMargin.getHeight()) * UNIT_INCH_RATIO);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);

				if(i % 2 > 0)  {
					table.addCell(cell);
					continue;
				}

				String value = nextValue(previousValue);
				previousValue = new StringBuffer(value).toString();

				QrCode qr0 = QrCode.encodeText(this.prefix + "-" + value, QrCode.Ecc.HIGH);
				BufferedImage img = qr0.toImage(4, 8);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ImageIO.write(img, "JPG", baos);
				Image pdfQR = Image.getInstance(baos.toByteArray());

				pdfQR.scaleAbsolute(new Rectangle(this.stickerSize.getHeight() * UNIT_INCH_RATIO, this.stickerSize.getHeight() * UNIT_INCH_RATIO));
				Chunk imgChunk = new Chunk(pdfQR, 0, -15);

				labelFormat = labelFormat.replaceAll("\\{0}", value)
						.replaceAll("\\{1}", this.prefix)
						.replaceAll("\\{2}", this.version);

				Phrase label = new Phrase(labelFormat, FontFactory.getFont(FontFactory.HELVETICA, 8));

				Paragraph element = new Paragraph();
				element.add(imgChunk);
				element.add("    ");
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
			System.err.println("All " + amount + " codes generated in: " + (System.currentTimeMillis() - time) + "ms");
		}
	}

}
