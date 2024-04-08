import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class ImageViewerController {

    @FXML
    public ImageView myImageView;
    @FXML
    public Button loadImagesBtn;
    @FXML
    public Button nextBtn, previousBtn, slideshowBtn;
    public boolean isSlideshowActive = false;
    public Label imageTxt;
    public Label redTxt;
    public Label greenTxt;
    public Label blueTxt;

    private List<File> imageFiles = new ArrayList<>();
    private int currentIndex = -1;
    private Timeline slideshowTimeline;

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

    public void countColors(Image image) {
        // Create a PixelReader to read pixel data from the image
        PixelReader pixelReader = image.getPixelReader();

        // Get the width and height of the image
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();

        // Initialize counters for each color
        int redCount = 0;
        int greenCount = 0;
        int blueCount = 0;

        // Iterate through each pixel in the image
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // Get the color of the pixel at position (x, y)
                Color color = pixelReader.getColor(x, y);

                // Extract RGB components
                double red = color.getRed();
                double green = color.getGreen();
                double blue = color.getBlue();

                // Determine the dominant color channel
                if (red >= green && red >= blue) {
                    redCount++;
                } else if (green >= red && green >= blue) {
                    greenCount++;
                } else {
                    blueCount++;
                }
            }
        }

        // Sets text to the corresponding color
        redTxt.setText("Red pixels: " + redCount);
        greenTxt.setText("Green pixels: " + greenCount);
        blueTxt.setText("Blue pixels: " + blueCount);
    }

    public void displayImage() {
        if (currentIndex >= 0 && currentIndex < imageFiles.size()) {
            File imageFile = imageFiles.get(currentIndex);
            Image image = new Image(imageFile.toURI().toString());
            myImageView.setImage(image);

            // Set text to display the name of the image
            imageTxt.setText(imageFile.getName());

            // Count colors in the image
            countColors(image);
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

    @FXML
    private void slideshowBtn(ActionEvent actionEvent) {
        if (!isSlideshowActive && !imageFiles.isEmpty()) {
            isSlideshowActive = true;
            slideshowTimeline = new Timeline(
                    new KeyFrame(Duration.seconds(2), event -> {
                        nextImage(); // Display a new image every 2 seconds
                    })
            );
            slideshowTimeline.setCycleCount(Timeline.INDEFINITE); // Repeat FOREVER !!
            slideshowTimeline.play();
            slideshowBtn.setText("Stop Slideshow"); // Change button text
        } else {
            stopSlideshow(); // Stop slideshow if it's already active
        }
    }

    private void stopSlideshow() {
        if (slideshowTimeline != null) {
            slideshowTimeline.stop();
            isSlideshowActive = false;
            slideshowBtn.setText("Start Slideshow"); // Change button text
        }
    }
}
