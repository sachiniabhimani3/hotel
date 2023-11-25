package com.example.hotel.controller;

import com.example.hotel.model.paymentModel;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class paymentController {

    @FXML private TextField bookingIdField;
    @FXML private TextField amountField;
    @FXML private TextField additionalChargesField;
    @FXML private TextField taxesField;
    @FXML private TextField totalAmountField;
    @FXML private DatePicker paymentDatePicker;
    @FXML private ComboBox<String> paymentMethodComboBox;
    @FXML private ComboBox<String> statusComboBox;

    private final paymentModel paymentModel = com.example.hotel.model.paymentModel.getInstance();

    public paymentController() {
        // Constructor logic if needed
    }

    @FXML
    private void initialize() {
        paymentMethodComboBox.getItems().addAll("Cash", "Credit Card", "Bank Transfer");
        statusComboBox.getItems().addAll("Pending", "Paid", "Cancelled");
    }

    @FXML
    protected void handleGenerateBillAction() {
        if (validateFields()) {
            calculateTotalAmount();
            saveBillDetailsToTextFile();
        }
    }

    @FXML
    protected void handleSaveBillDetailsAction() {
        if (validateFields()) {
            calculateTotalAmount();
            savePaymentDetailsToDatabase();
        }
    }

    private void calculateTotalAmount() {
        try {
            double amount = Double.parseDouble(amountField.getText());
            double additionalCharges = Double.parseDouble(additionalChargesField.getText());
            double taxes = Double.parseDouble(taxesField.getText());
            double totalAmount = amount + additionalCharges + taxes;
            totalAmountField.setText(String.format("%.2f", totalAmount));
        } catch (NumberFormatException e) {
            showAlertDialog(AlertType.ERROR, "Input Error", "Please enter valid numbers for amount, charges, and taxes.");
        }
    }

    private boolean validateFields() {
        // Example validation logic
        if (bookingIdField.getText().isEmpty() ||
                amountField.getText().isEmpty() ||
                additionalChargesField.getText().isEmpty() ||
                taxesField.getText().isEmpty() ||
                paymentDatePicker.getValue() == null ||
                paymentMethodComboBox.getValue() == null ||
                statusComboBox.getValue() == null) {
            showAlertDialog(AlertType.ERROR, "Validation Error", "All fields must be filled.");
            return false;
        }
        return true;
    }

    private void savePaymentDetailsToDatabase() {
        try {
            int bookingId = Integer.parseInt(bookingIdField.getText());
            double totalAmount = Double.parseDouble(totalAmountField.getText());
            LocalDate paymentDate = paymentDatePicker.getValue();
            String paymentMethod = paymentMethodComboBox.getValue();
            String status = statusComboBox.getValue();

            if (paymentModel.addPayment(bookingId, totalAmount, paymentDate, paymentMethod, status)) {
                showAlertDialog(AlertType.INFORMATION, "Success", "Payment details saved successfully.");
                clearForm();
            } else {
                showAlertDialog(AlertType.ERROR, "Error", "Failed to save payment details.");
            }
        } catch (NumberFormatException e) {
            showAlertDialog(AlertType.ERROR, "Input Error", "Invalid number format.");
        } catch (Exception e) {
            showAlertDialog(AlertType.ERROR, "Database Error", "An error occurred while saving the payment details.");
        }
    }

    private void saveBillDetailsToTextFile() {
        String fileName = "BillDetails_" + LocalDate.now().format(DateTimeFormatter.ISO_DATE) +"_"+bookingIdField.getText()+ ".txt";
        try (PrintWriter out = new PrintWriter(Files.newBufferedWriter(Paths.get(fileName)))) {
            out.println("Payment Details");
            out.println("Booking ID: " + bookingIdField.getText());
            out.println("Amount: " + amountField.getText());
            out.println("Additional Charges: " + additionalChargesField.getText());
            out.println("Taxes: " + taxesField.getText());
            out.println("Total Amount: " + totalAmountField.getText());
            out.println("Payment Date: " + paymentDatePicker.getValue().format(DateTimeFormatter.ISO_DATE));
            out.println("Payment Method: " + paymentMethodComboBox.getValue());
            out.println("Status: " + statusComboBox.getValue());
            showAlertDialog(AlertType.INFORMATION, "Bill Generated", "The bill details have been saved to " + fileName);
        } catch (IOException e) {
            showAlertDialog(AlertType.ERROR, "File Error", "There was a problem saving the bill details to a text file.");
        }
    }

    private void clearForm() {
        bookingIdField.clear();
        amountField.clear();
        additionalChargesField.clear();
        taxesField.clear();
        totalAmountField.clear();
        paymentDatePicker.setValue(null);
        paymentMethodComboBox.getSelectionModel().clearSelection();
        statusComboBox.getSelectionModel().clearSelection();
    }

    private void showAlertDialog(AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
