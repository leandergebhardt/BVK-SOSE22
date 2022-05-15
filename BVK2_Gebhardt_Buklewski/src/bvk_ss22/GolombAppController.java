// BVK Ue1 SS2022 Vorgabe
//
// Copyright (C) 2022 by Klaus Jung
// All rights reserved.
// Date: 2022-03-24

package bvk_ss22;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;

import java.io.*;

public class GolombAppController {

	private static final String initialFileName = "./BVK2_Gebhardt_Buklewski/ara_klein.png";
	private static File fileOpenPath = new File(".");

	private RasterImage sourceImage;
	private String sourceFileName;
	
	private RasterImage processedImage;

	private RasterImage golombImage;
	private RasterImage greyScaleImage;
	private long golombImageFileSize;

    @FXML
    private ImageView sourceImageView;

	@FXML
	private ImageView processedImageView;

	@FXML
	private ImageView golombImageView;

    @FXML
    private ScrollPane sourceScrollPane;

	@FXML
	private ScrollPane processedImageScrollPane;

	@FXML
	private ScrollPane golombScrollPane;

    @FXML
    private Label sourceInfoLabel;

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
	private Label sizeLabel;

	@FXML
	private Slider zoomSlider;

	@FXML
	private Slider mSlider;

	@FXML
	private Label mValue;

	@FXML
	private ChoiceBox<String> myChoiceBox;

	private String[] processTypes = {"Copy", "DPCM horizontal"};

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
		myChoiceBox.getItems().addAll(processTypes);
		myChoiceBox.setValue(processTypes[0]);
		myChoiceBox.setOnAction(this::getProcessType);
		String processType = myChoiceBox.getValue();
		if(processType == "Copy") {
			greyScaleImage = Filter.greyScale(sourceImage, greyScaleImage);
			processedImage = Filter.copy(greyScaleImage, processedImage);

			processedImage.setToView(processedImageView);
		}
	}

	@FXML
	void zoomChanged() {
		double zoomFactor = zoomSlider.getValue();
		zoomLabel.setText(String.format("%.1f", zoomFactor));
		zoom(sourceImageView, sourceScrollPane, zoomFactor);
		zoom(processedImageView, processedImageScrollPane, zoomFactor);
		zoom(golombImageView, golombScrollPane, zoomFactor);
	}

	private void getProcessType(ActionEvent actionEvent) {
		String processType = myChoiceBox.getValue();

		if(processType == "Copy") {
			messageLabel.setText("Copy");
			greyScaleImage = Filter.greyScale(sourceImage, greyScaleImage);
			processedImage = Filter.copy(greyScaleImage, processedImage);
			processedImage.setToView(processedImageView);
		}
		if(processType == "DPCM horizontal") {
			messageLabel.setText("Switched to DPCM");
			greyScaleImage = Filter.greyScale(sourceImage, greyScaleImage);
			processedImage = Filter.dpcm(greyScaleImage, processedImage);
			processedImage.setToView(processedImageView);
		}
		// Todo set Labels for MSE and File Size
	}

	@FXML
	void mChanged(){
    	double M = mSlider.getValue();
		int m = (int) Math.floor(M);
		mValue.setText(""+ m +"");
	}

	private void setMSlider(int M) {
		mSlider.setValue(M);
		mValue.setText(""+ M +"");
	}
	
	private void loadAndDisplayImage(File file) {
		sourceFileName = file.getName();
		messageLabel.setText("Opened image " + sourceFileName);
		sourceImage = new RasterImage(file);
		greyScaleImage = Filter.greyScale(sourceImage, sourceImage);
		greyScaleImage.setToView(sourceImageView);
		sourceInfoLabel.setText(sourceFileName);
		processedImage = new RasterImage(sourceImage.width, sourceImage.height);
		processedImage.setToView(processedImageView);
		compareImages();
	}
	
	private void compareImages() {
		if(sourceImage.argb.length != processedImage.argb.length || golombImageFileSize == 0) {
			mseLabel.setText("");
			return;
		}
		double mse = processedImage.getMSEfromComparisonTo(sourceImage);
		mseLabel.setText(String.format("MSE = %.1f", mse));
	}
	
	@FXML
	public void saveGolombImage() {
    	FileChooser fileChooser = new FileChooser();
    	fileChooser.setInitialDirectory(fileOpenPath);
    	fileChooser.setInitialFileName(sourceFileName.substring(0, sourceFileName.lastIndexOf('.')) + ".run");
    	fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Golomb Images (*.gol)", "*.gol"));
    	File selectedFile = fileChooser.showSaveDialog(null);
    	if(selectedFile != null) {
    		try {
    			DataOutputStream ouputStream = new DataOutputStream(new FileOutputStream(selectedFile));
    			long startTime = System.currentTimeMillis();
				String processType = myChoiceBox.getValue();
				if(processType == "Copy") Golomb.encodeImage(processedImage, 0,ouputStream);
				if(processType == "DPCM horizontal") Golomb.encodeImage(processedImage, 2, ouputStream);
    			long time = System.currentTimeMillis() - startTime;
    			messageLabel.setText("Encoding in " + time + " ms");
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    	}
	}
	
	@FXML
	public void openGolombImage() {
    	FileChooser fileChooser = new FileChooser();
    	fileChooser.setInitialDirectory(fileOpenPath);
    	fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Golomb Images (*.gol)", "*.gol"));
    	File selectedFile = fileChooser.showOpenDialog(null);
    	if(selectedFile != null) {
    		golombImageFileSize = selectedFile.length();
			golombImageFileSize = golombImageFileSize / 1000;
    		try {
    			DataInputStream inputStream = new DataInputStream(new FileInputStream(selectedFile));
    			long startTime = System.currentTimeMillis();
				//golombImage = new RasterImage(sourceImage.width, sourceImage.height);
    			golombImage = Golomb.decodeImage(inputStream);
    			long time = System.currentTimeMillis() - startTime;
    			messageLabel.setText("Decoding in " + time + " ms");
    			golombImage.setToView(golombImageView);
				setMSlider(Golomb.getM());
				sizeLabel.setText("" + golombImageFileSize + " KB");
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
