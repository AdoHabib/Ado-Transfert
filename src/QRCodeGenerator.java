import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class QRCodeGenerator {

    public static void main(String[] args) {
        String contenuto = "Je t'aime ma dulcinée";
        String percorsoFile = "qrcode.png";
        int larghezza = 300;
        int altezza = 300;
    
        QRCodeGenerator.generaQRCode(contenuto, percorsoFile, larghezza, altezza);
    }

    public static void generaQRCode(String contenuto, String percorsoFile, int larghezza, int altezza) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        try {
            // Genera la matrice del QR Code
            BitMatrix bitMatrix = qrCodeWriter.encode(contenuto, BarcodeFormat.QR_CODE, larghezza, altezza);

            // Salva il QR Code come immagine PNG
            Path path = FileSystems.getDefault().getPath(percorsoFile);
            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);

            System.out.println("QR Code generato con successo: " + percorsoFile);
        } catch (WriterException | IOException e) {
            System.out.println("Errore durante la generazione del QR Code: " + e.getMessage());
        }
    }
}