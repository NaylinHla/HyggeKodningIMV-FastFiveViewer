import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ImageViewerController {

    @FXML
    public ImageView myImageView;
    @FXML
    public Button loadImagesBtn;
    @FXML
    public Button nextBtn;
    @FXML
    public Button previousBtn;



    private List<File> imageFiles = new ArrayList<>();
    private int currentIndex = -1;

    public void initialize() {
        loadImages();
    }

    public void loadImages() {
        File picturesFolder = new File("resources/Pictures");
        if (picturesFolder.isDirectory()) {
            for (File file : picturesFolder.listFiles()) {
                if (file.isFile() && isImageFile(file.getName())) {
                    imageFiles.add(file);
                }
            }
        }
        if (!imageFiles.isEmpty()) {
            currentIndex = 0; // Set current index to the first image
            displayImage();
        }
    }

    private boolean isImageFile(String fileName) {
        return fileName.toLowerCase().endsWith(".jpg") || fileName.toLowerCase().endsWith(".png");
    }

    public void displayImage() {
        if (currentIndex >= 0 && currentIndex < imageFiles.size()) {
            File imageFile = imageFiles.get(currentIndex);
            Image image = new Image(imageFile.toURI().toString());
            myImageView.setImage(image);
        }
    }

    @FXML
    public void nextImage() {
        if (!imageFiles.isEmpty()) {
            currentIndex = (currentIndex + 1) % imageFiles.size();
            displayImage();
        }
    }

    @FXML
    public void previousImage() {
        if (!imageFiles.isEmpty()) {
            currentIndex = (currentIndex - 1 + imageFiles.size()) % imageFiles.size();
            displayImage();
        }
    }




}
