// BVK Ue1 SS2022 Vorgabe
//
// Copyright (C) 2022 by Klaus Jung
// All rights reserved.
// Date: 2022-03-24

package bvk_ss22;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;

public class RLEAppController implements Initializable {
	
	private static final String initialFileName = "dilbert_8.png";
	private static File fileOpenPath = new File(".");

	private RasterImage sourceImage;
	private String sourceFileName;
	
	private RasterImage rleImage;
	private long rleImageFileSize;

    @FXML
    private ImageView sourceImageView;

    @FXML
    private ScrollPane sourceScrollPane;

    @FXML
    private Label sourceInfoLabel;

	@FXML
	private ImageView reprocessedImageView;

	@FXML
	private ScrollPane reprocessedScrollPane;

	@FXML
	private Label reprocessedInfoLabel;

    @FXML
    private ImageView rleImageView;

    @FXML
    private ScrollPane rleScrollPane;

    @FXML
    private Label rleInfoLabel;

    @FXML
    private Label messageLabel;

    @FXML
    private Slider zoomSlider;

    @FXML
    private Label zoomLabel;

	@FXML
	private Slider mSlider;

	@FXML
	private Label mLabel;

	@FXML
	private ChoiceBox<String> myChoiceBox;

	private String[] processTypes = {"Copy", "DPCM"};


	public void getProcessType(ActionEvent event) {
		String process = myChoiceBox.getValue();
		// TODO trigger image processing
	}
    @FXML
    void openImage() {
    	FileChooser fileChooser = new FileChooser();
    	fileChooser.setInitialDirectory(fileOpenPath); 
    	fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Images (*.jpg, *.png, *.gif)", "*.jpeg", "*.jpg", "*.png", "*.gif"));
    	File selectedFile = fileChooser.showOpenDialog(null);
    	if(selectedFile != null) {
        	zoomSlider.setValue(1);
    		zoomChanged();
    		fileOpenPath = selectedFile.getParentFile();
    		loadAndDisplayImage(selectedFile);
    		messageLabel.getScene().getWindow().sizeToScene();;
    	}
    }

	@FXML
	public void initialize() {
		loadAndDisplayImage(new File(initialFileName));		
	}

	@FXML
	void mChanged(){
    	double M = mSlider.getValue();
    	System.out.println(M);
	}

 	@FXML
    void zoomChanged() {
    	double zoomFactor = zoomSlider.getValue();
		zoomLabel.setText(String.format("%.1f", zoomFactor));
    	zoom(sourceImageView, sourceScrollPane, zoomFactor);
    	zoom(rleImageView, rleScrollPane, zoomFactor);
    }
	
	private void loadAndDisplayImage(File file) {
		sourceFileName = file.getName();
		messageLabel.setText("Opened image " + sourceFileName);
		sourceImage = new RasterImage(file);
		sourceImage.setToView(sourceImageView);
		sourceInfoLabel.setText("");
		rleImage = new RasterImage(sourceImage.width, sourceImage.height);
		rleImage.setToView(rleImageView);
		compareImages();
	}
	
	private void compareImages() {
		if(sourceImage.argb.length != rleImage.argb.length || rleImageFileSize == 0) {
			rleInfoLabel.setText("");
			return;
		}
		double mse = rleImage.getMSEfromComparisonTo(sourceImage);
		rleInfoLabel.setText(String.format("MSE = %.1f", mse));
	}
	
	@FXML
	public void saveRLEImage() {
    	FileChooser fileChooser = new FileChooser();
    	fileChooser.setInitialDirectory(fileOpenPath);
    	fileChooser.setInitialFileName(sourceFileName.substring(0, sourceFileName.lastIndexOf('.')) + ".run");
    	fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("RLE Images (*.run)", "*.run"));
    	File selectedFile = fileChooser.showSaveDialog(null);
    	if(selectedFile != null) {
    		try {
    			DataOutputStream ouputStream = new DataOutputStream(new FileOutputStream(selectedFile));
    			long startTime = System.currentTimeMillis();
    			RLE.encodeImage(sourceImage, ouputStream);
    			long time = System.currentTimeMillis() - startTime;
    			messageLabel.setText("Encoding in " + time + " ms");
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    	}
	}
	
	@FXML
	public void openRLEImage() {
    	FileChooser fileChooser = new FileChooser();
    	fileChooser.setInitialDirectory(fileOpenPath);
    	fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("RLE Images (*.run)", "*.run"));
    	File selectedFile = fileChooser.showOpenDialog(null);
    	if(selectedFile != null) {
    		rleImageFileSize = selectedFile.length();
    		try {
    			DataInputStream inputStream = new DataInputStream(new FileInputStream(selectedFile));
    			long startTime = System.currentTimeMillis();
    			rleImage = RLE.decodeImage(inputStream);
    			long time = System.currentTimeMillis() - startTime;
    			messageLabel.setText("Decoding in " + time + " ms");
    			rleImage.setToView(rleImageView);
    			compareImages();
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


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		myChoiceBox.getItems().addAll(processTypes);
		myChoiceBox.setOnAction(this::getProcessType);
	}
}
