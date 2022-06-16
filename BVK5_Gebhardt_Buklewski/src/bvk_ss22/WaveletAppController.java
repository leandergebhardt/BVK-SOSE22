// BVK Ue1 SS2022 Vorgabe
//
// Copyright (C) 2022 by Klaus Jung
// All rights reserved.
// Date: 2022-03-24

package bvk_ss22;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;

public class WaveletAppController {

	private static final String initialFileName = "./BVK5_Gebhardt_Buklewski/test2.jpg";
	private static File fileOpenPath = new File(".");

	private RasterImage sourceImage;
	private String sourceFileName;
	
	private RasterImage greyScaleImage;

	private RasterImage waveletImage;

	private RasterImage recontructedImage;

	private long decodedImageFileSize;
	private long sourceSize;

	private double p0;

    @FXML
    private ImageView sourceImageView;

	@FXML
	private ImageView processedImageView;

	@FXML
	private ImageView decodedImageView;

    @FXML
    private ScrollPane sourceScrollPane;

	@FXML
	private ScrollPane processedImageScrollPane;

	@FXML
	private ScrollPane decodedScrollPane;

    @FXML
    private Label sourceInfoLabel;

	@FXML
	private Label sourceFileSize;

	@FXML
	private ImageView reprocessedImageView;

	@FXML
	private ScrollPane reprocessedScrollPane;

	@FXML
	private Label reprocessedInfoLabel;

    @FXML
    private Label messageLabel;

    @FXML
    private Label zoomLabel;

	@FXML
	private Label mseLabel;


	@FXML
	private Slider zoomSlider;

	@FXML
	private Slider kSlider;

	@FXML
	private Label kValue;


    @FXML
    void openImage() {
    	FileChooser fileChooser = new FileChooser();
    	fileChooser.setInitialDirectory(fileOpenPath); 
    	fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Images (*.jpg, *.png, *.gif)", "*.jpeg", "*.jpg", "*.png", "*.gif"));
    	File selectedFile = fileChooser.showOpenDialog(null);
    	if(selectedFile != null) {
    		fileOpenPath = selectedFile.getParentFile();
    		loadAndDisplayImage(selectedFile);
    		messageLabel.getScene().getWindow().sizeToScene();;
    	}
    }

	@FXML
	public void initialize() {
		loadAndDisplayImage(new File(initialFileName));
		kChanged();
	}

	@FXML
	void zoomChanged() {
		double zoomFactor = zoomSlider.getValue();
		zoomLabel.setText(String.format("%.1f", zoomFactor));
		zoom(sourceImageView, sourceScrollPane, zoomFactor);
		zoom(processedImageView, processedImageScrollPane, zoomFactor);
		zoom(decodedImageView, decodedScrollPane, zoomFactor);
	}

	@FXML
	void kChanged(){
    	double K = kSlider.getValue();
		int k = (int) Math.floor(K);
		kValue.setText(""+ k +"");
		// TODO: Perform kaskade loop depending on value
		waveletImage = Wavelet.testConverterBegin(greyScaleImage);
		/*for(int i = 1; i <= k; i++){
			waveletImage = Wavelet.kaskade(waveletImage, i);
		}
		 */
		waveletImage.setToView(processedImageView);
	}

	private void setKSlider(int M) {
		kSlider.setValue(M);
		kValue.setText(""+ M +"");
	}
	
	private void loadAndDisplayImage(File file) {
		int K = (int) kSlider.getValue();
		sourceFileName = file.getName();
		sourceSize = file.length();
		sourceSize = (long) Math.ceil(sourceSize / 1000);
		sourceFileSize.setText("" + sourceSize +" KB");
		messageLabel.setText("Opened image " + sourceFileName);
		sourceImage = new RasterImage(file);
		greyScaleImage = new RasterImage(sourceImage.width, sourceImage.height);
		greyScaleImage = Filter.greyScale(sourceImage, greyScaleImage);
		greyScaleImage.setToView(sourceImageView);
		//compareImages();
	}
	
	/*private void compareImages() {
		if(sourceImage.argb.length != binarizedImage.argb.length || decodedImageFileSize == 0) {
			mseLabel.setText("");
			return;
		}
		double mse = decodedImage.getMSEfromComparisonTo(sourceImage);
		mseLabel.setText(String.format("MSE = %.1f", mse));
	}
	
	@FXML
	public void saveCompressedImage() {
		int M = (int) kSlider.getValue();
    	FileChooser fileChooser = new FileChooser();
    	fileChooser.setInitialDirectory(fileOpenPath);
    	fileChooser.setInitialFileName(sourceFileName.substring(0, sourceFileName.lastIndexOf('.')) + "_encode");
    	fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Golomb Images (*.ari)", "*.ari"));
    	File selectedFile = fileChooser.showSaveDialog(null);
    	if(selectedFile != null) {
    		try {
    			DataOutputStream ouputStream = new DataOutputStream(new FileOutputStream(selectedFile));
    			long startTime = System.currentTimeMillis();
				Wavelet.encodeImage(binarizedImage, ouputStream);
    			long time = System.currentTimeMillis() - startTime;
    			messageLabel.setText("Encoding in " + time + " ms");
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    	}
	}
	 */
	
	@FXML
	public void openCompressedImage() {
    	FileChooser fileChooser = new FileChooser();
    	fileChooser.setInitialDirectory(fileOpenPath);
    	fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Arithmetic Images (*.ari)", "*.ari"));
    	File selectedFile = fileChooser.showOpenDialog(null);
    	if(selectedFile != null) {
			decodedImageFileSize = selectedFile.length();
			decodedImageFileSize = (long) Math.ceil(decodedImageFileSize / 1000);
    		try {
    			DataInputStream inputStream = new DataInputStream(new FileInputStream(selectedFile));
    			long startTime = System.currentTimeMillis();
				//decodedImage = Wavelet.decodeImage(inputStream);
    			long time = System.currentTimeMillis() - startTime;
    			messageLabel.setText("Decoding in " + time + " ms");
				//decodedImage.setToView(decodedImageView);
				//sizeLabel.setText("" + decodedImageFileSize + " KB");
    			//compareImages();
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    	}
	}
	
		
	private void zoom(ImageView imageView, ScrollPane scrollPane, double zoomFactor) {
		if(zoomFactor == 1) {
			scrollPane.setPrefWidth(Region.USE_COMPUTED_SIZE);
			scrollPane.setPrefHeight(Region.USE_COMPUTED_SIZE);
			imageView.setFitWidth(0);
			imageView.setFitHeight(0);
		} else {
			double paneWidth = scrollPane.getWidth();
			double paneHeight = scrollPane.getHeight();
			double imgWidth = imageView.getImage().getWidth();
			double imgHeight = imageView.getImage().getHeight();
			double lastZoomFactor = imageView.getFitWidth() <= 0 ? 1 : imageView.getFitWidth() / imgWidth;
			if(scrollPane.getPrefWidth() == Region.USE_COMPUTED_SIZE)
				scrollPane.setPrefWidth(paneWidth);
			if(scrollPane.getPrefHeight() == Region.USE_COMPUTED_SIZE)
				scrollPane.setPrefHeight(paneHeight);
			double scrollX = scrollPane.getHvalue();
			double scrollY = scrollPane.getVvalue();
			double scrollXPix = ((imgWidth * lastZoomFactor - paneWidth) * scrollX + paneWidth/2) / lastZoomFactor;
			double scrollYPix = ((imgHeight * lastZoomFactor - paneHeight) * scrollY + paneHeight/2) / lastZoomFactor;
			imageView.setFitWidth(imgWidth * zoomFactor);
			imageView.setFitHeight(imgHeight * zoomFactor);
			if(imgWidth * zoomFactor > paneWidth)
				scrollX = (scrollXPix * zoomFactor - paneWidth/2) / (imgWidth * zoomFactor - paneWidth);
			if(imgHeight * zoomFactor > paneHeight)
				scrollY = (scrollYPix * zoomFactor - paneHeight/2) / (imgHeight * zoomFactor - paneHeight);
			if(scrollX < 0) scrollX = 0;
			if(scrollX > 1) scrollX = 1;
			if(scrollY < 0) scrollY = 0;
			if(scrollY > 1) scrollY = 1;
			scrollPane.setHvalue(scrollX);
			scrollPane.setVvalue(scrollY);
		}
	}
}
