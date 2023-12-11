package com.example.laptopconfig;
public class CPU extends Component{
    private final double clockFrequency;
    public CPU(String manufacturer, String model, double priceWithoutVAT, CompType type, double clockFrequency) {
        super(manufacturer, model, priceWithoutVAT, type);
        this.clockFrequency = clockFrequency;
    }

    @Override
    public String additionalParameter() {
        return "Clock Frequency: " + String.format("%.2f", clockFrequency) + " GHz" + " Price " + String.format("%.2f", getPriceWithVAT()) + " kC ";
    }
}
