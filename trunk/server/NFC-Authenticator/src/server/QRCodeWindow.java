package server;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Hashtable;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class QRCodeWindow extends JPanel {
	
	private static final long serialVersionUID = 1L;
	private String s;

	public QRCodeWindow(String string)
	{
		s = string;
	}

	public void paintComponent(Graphics g)
	{
		Graphics2D graphics;
		int matrixWidth = 240;
		Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<EncodeHintType, ErrorCorrectionLevel>();
		hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
		QRCodeWriter qr = new QRCodeWriter();
		BitMatrix bm = null;
		try {
			bm = qr.encode(s, BarcodeFormat.QR_CODE, matrixWidth, matrixWidth);
		} catch (WriterException e) {
			e.printStackTrace();
		}

		BufferedImage image = new BufferedImage(240, 240, BufferedImage.TYPE_INT_RGB);
		image.createGraphics();
		graphics = (Graphics2D) image.getGraphics();
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 0, matrixWidth, matrixWidth);
		

		graphics.setColor(Color.BLACK);

		for (int i = 0; i < matrixWidth; i++) {
			for (int j = 0; j < matrixWidth; j++) {
				if (bm.get(i, j)) {
					graphics.fillRect(i, j, 1, 1);
				}
			}
		}
		g.drawImage(image, 10, 50, this);
	}

}
