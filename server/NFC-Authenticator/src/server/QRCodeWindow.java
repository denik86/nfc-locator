package server;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Hashtable;

import javax.swing.JPanel;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class QRCodeWindow extends JPanel {
	
		public void paintComponent(Graphics g)
		{
			Graphics2D graphics;
	        int matrixWidth = 120;
	        Hashtable hintMap = new Hashtable();
	        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
	        QRCodeWriter qr = new QRCodeWriter();
	try {
		BitMatrix bm = qr.encode("1DSASDFDS", BarcodeFormat.QR_CODE, 120, 120);
		BufferedImage image = new BufferedImage(120, 120,
		        BufferedImage.TYPE_INT_RGB);
		image.createGraphics();
		graphics = (Graphics2D) image.getGraphics();
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 0, matrixWidth, matrixWidth);
		// Paint and save the image using the ByteMatrix
		graphics.setColor(Color.RED);

		for (int i = 0; i < matrixWidth; i++) {
		    for (int j = 0; j < matrixWidth; j++) {
		        if (bm.get(i, j)) {
		            graphics.fillRect(i, j, 1, 1);
		        }
		    }
		}
		
		g.drawImage(image, 0, 0, this);
	} catch (WriterException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
		}
}

