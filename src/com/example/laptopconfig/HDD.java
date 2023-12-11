package com.example.laptopconfig;
public class HDD extends Component{
    private final double sizeInGB;
    public HDD(String manufacturer, String model, double priceWithoutVAT, CompType type, double sizeInGB) {
        super(manufacturer, model, priceWithoutVAT, type);
        this.sizeInGB = sizeInGB;
    }
    public double getSizeInGB() {
        return sizeInGB;
    }
    @Override
    public String additionalParameter() {
        return "Size: " + String.format("%.2f", sizeInGB) + " GB"+ " Price " + String.format("%.2f", getPriceWithVAT()) + " kC ";
    }
}
