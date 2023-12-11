package com.example.laptopconfig;
public class GPU extends Component{
    public GPU(String manufacturer, String model, double priceWithoutVAT, CompType type) {
        super(manufacturer, model, priceWithoutVAT, type);
    }
    @Override
    public String additionalParameter() {
        return "Price " + String.format("%.2f", getPriceWithVAT()) + " kC";
    }
}
