package forensics;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;

public class MetadataExtractor implements ForensicAnalyzer {

    @Override
    public void analyze() {
        System.out.println("📸 Extracting image metadata...");

        try (InputStream is = getClass()
                .getClassLoader()
                .getResourceAsStream("sample_image.jpg")) {

            if (is == null) {
                System.out.println("❌ sample_image.jpg not found!");
                return;
            }

            BufferedImage image = ImageIO.read(is);
            if (image != null) {
                System.out.println("📏 Width: " + image.getWidth() + " px");
                System.out.println("📐 Height: " + image.getHeight() + " px");
            } else {
                System.out.println("❌ Unable to read image.");
            }

        } catch (Exception e) {
            System.out.println("❌ Error reading image: " + e.getMessage());
        }
    }
}
