package com.example.laptopconfig;
import javafx.beans.property.*;
public abstract class Component {
    private final StringProperty manufacturer;
    private final StringProperty model;
    private final DoubleProperty priceWithoutVAT;
    private final DoubleProperty priceWithVAT;
    private final ObjectProperty<CompType> type;

    public Component(String manufacturer, String model, double priceWithoutVAT, CompType type) {
        this.manufacturer = new SimpleStringProperty(manufacturer);
        this.model = new SimpleStringProperty(model);
        this.priceWithoutVAT = new SimpleDoubleProperty(priceWithoutVAT);
        this.priceWithVAT = new SimpleDoubleProperty(priceWithoutVAT * 1.21);
        this.type = new SimpleObjectProperty<>(type);

        this.priceWithoutVAT.addListener((observable, oldValue, newValue) -> {
            this.priceWithVAT.set(newValue.doubleValue() * 1.21);
        });
    }

    // abstract method to be implemented by child classes
    public abstract String additionalParameter();
    public String getManufacturer() {
        return manufacturer.get();
    }
    public StringProperty manufacturerProperty() {
        return manufacturer;
    }
    public void setManufacturer(String manufacturer) {
        this.manufacturer.set(manufacturer);
    }
    public String getModel() {
        return model.get();
    }
    public StringProperty modelProperty() {
        return model;
    }
    public void setModel(String model) {
        this.model.set(model);
    }
    public double getPriceWithoutVAT() {
        return priceWithoutVAT.get();
    }
    public DoubleProperty priceWithoutVATProperty() {
        return priceWithoutVAT;
    }
    public void setPriceWithoutVAT(double priceWithoutVAT) {
        this.priceWithoutVAT.set(priceWithoutVAT);
    }
    @Override
    public String toString() {
        return type.get() + ": " + manufacturer.get() + " " + model.get();
    }
    public double getPriceWithVAT() {
        return priceWithVAT.get();
    }
    public DoubleProperty priceWithVATProperty() {
        return priceWithVAT;
    }
    public void setPriceWithVAT(double priceWithVAT) {
        this.priceWithVAT.set(priceWithVAT);
    }
    public CompType getType() {
        return type.get();
    }
    public ObjectProperty<CompType> typeProperty() {
        return type;
    }
    public void setType(CompType type) {
        this.type.set(type);
    }
}