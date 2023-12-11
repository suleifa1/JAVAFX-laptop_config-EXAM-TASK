package com.example.laptopconfig;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

import java.net.URL;
import java.util.*;
public class HelloController implements Initializable {
    @FXML
    private ColorPicker colorLaptop;
    @FXML
    Label averagePrice;
    @FXML
    private ListView<CPU> cpuLV;
    @FXML
    private Button editComponent;
    @FXML
    private ListView<GPU> gpuLV;
    @FXML
    private ListView<HDD> hddLV;
    @FXML
    private TextField laptopNameTF;
    @FXML
    private TextArea laptopProperties;
    @FXML
    private ListView<Component> overViewLV;
    @FXML
    private ListView<RAM> ramLV;
    @FXML
    private Tab hddTab;
    @FXML
    private Tab cpuTab;
    @FXML
    private Tab ramTab;
    @FXML
    private Tab gpuTab;
    @FXML
    private Label ramAveragePriceLabel;
    @FXML
    private Label hddAveragePriceLabel;
    @FXML
    private Label cpuAveragePriceLabel;
    @FXML
    private Label gpuAveragePriceLabel;
    private double calculateAveragePrice(ObservableList<? extends Component> componentList) {
        double totalPrice = componentList.stream()
                .mapToDouble(Component::getPriceWithVAT)
                .sum();
        int componentCount = componentList.size();
        return componentCount > 0 ? totalPrice / componentCount : 0.0;
    }
    private double calculateAveragePricePerGB(ObservableList<? extends HDD> componentList) {
        double totalPrice = componentList.stream()
                .mapToDouble(HDD::getPriceWithVAT)
                .sum();
        double totalGB = componentList.stream()
                .mapToDouble(HDD::getSizeInGB)
                .sum();
        return totalGB > 0 ? totalPrice / totalGB : 0.0;
    }
    private void updateAveragePrices() {
        double ramAveragePrice = calculateAveragePrice(ramList);
        double hddAveragePrice = calculateAveragePricePerGB(hddList);
        double gpuAveragePrice = calculateAveragePrice(gpuList);
        double cpuAveragePrice = calculateAveragePrice(cpuList);
        ramAveragePriceLabel.setText(String.format("Average Price: %.2f", ramAveragePrice));
        hddAveragePriceLabel.setText(String.format("Average Price(per GB): %.2f", hddAveragePrice));
        gpuAveragePriceLabel.setText(String.format("Average Price: %.2f", gpuAveragePrice));
        cpuAveragePriceLabel.setText(String.format("Average Price: %.2f", cpuAveragePrice));
    }
    private ObservableList<RAM> ramList;
    private ObservableList<HDD> hddList;
    private ObservableList<GPU> gpuList;
    private ObservableList<CPU> cpuList;
    private Optional<RAM> showRamInputDialog() {
        // Создаем новый диалог
        Dialog<RAM> dialog = new Dialog<>();
        dialog.setTitle("New RAM Component");
        TextField manufacturerTF = new TextField();
        manufacturerTF.setPromptText("Manufacturer");
        TextField modelTF = new TextField();
        modelTF.setPromptText("Model");
        TextField priceTF = new TextField();
        priceTF.setPromptText("Price");
        priceTF.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d{0,7}([\\,]\\d{0,2})?")) {
                priceTF.setText(oldValue);
            }
        });
        TextField sizeInGBTf = new TextField();
        sizeInGBTf.setPromptText("Size in GB");
        sizeInGBTf.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d{0,7}([\\,]\\d{0,2})?")) {
                sizeInGBTf.setText(oldValue);
            }
        });
        GridPane grid = new GridPane();
        grid.add(new Label("Manufacturer:"), 0, 0);
        grid.add(manufacturerTF, 1, 0);
        grid.add(new Label("Model:"), 0, 1);
        grid.add(modelTF, 1, 1);
        grid.add(new Label("Price:"), 0, 2);
        grid.add(priceTF, 1, 2);
        grid.add(new Label("Size in GB:"), 0, 3);
        grid.add(sizeInGBTf, 1, 3);
        dialog.getDialogPane().setContent(grid);
        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                String manufacturer = manufacturerTF.getText();
                String model = modelTF.getText();
                double price;
                double sizeInGB;
                try {
                    price = Double.parseDouble(priceTF.getText().replace(",", "."));
                    sizeInGB = Double.parseDouble(sizeInGBTf.getText().replace(",", "."));
                } catch (NumberFormatException e) {
                    return null;
                }
                return new RAM(manufacturer, model, price, CompType.RAM, sizeInGB);
            }
            return null;
        });
        return dialog.showAndWait();
    }
    private Optional<String> showComponentTypeDialog() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Choose Component Type");
        // Create ComboBox and add Component types
        ComboBox<String> componentTypeCB = new ComboBox<>();
        componentTypeCB.getItems().addAll("RAM", "HDD", "CPU", "GPU");
        dialog.getDialogPane().setContent(componentTypeCB);
        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                return componentTypeCB.getSelectionModel().getSelectedItem();
            }
            return null;
        });
        return dialog.showAndWait();
    }
    Comparator<RAM> ramComparator = Comparator.comparingDouble(RAM::getPriceWithVAT).reversed();
    Comparator<HDD> hddComparator = Comparator.comparingDouble(HDD::getPriceWithVAT).reversed();
    Comparator<CPU> cpuComparator = Comparator.comparingDouble(CPU::getPriceWithVAT).reversed();
    Comparator<GPU> gpuComparator = Comparator.comparingDouble(GPU::getPriceWithVAT).reversed();
    @FXML
    void addComponentToLV() {
        Optional<String> result = showComponentTypeDialog();
        result.ifPresent(componentType -> {
            switch (componentType) {
                case "RAM":
                    Optional<RAM> ram = showRamInputDialog();
                    ram.ifPresent(value -> ramList.add(value));
                    FXCollections.sort(ramList, ramComparator);
                    break;
                case "HDD":
                    break;
                case "CPU":
                    break;
                case "GPU":
                    break;
                default:
                    break;
            }
        });
    }
    private ObservableList<Component> overViewList = FXCollections.observableArrayList();
    @FXML
    void addComponentToOV(ActionEvent event) {
        if (ramTab.isSelected()) {
            RAM selectedRAM = ramLV.getSelectionModel().getSelectedItem();
            if (selectedRAM != null) {
                int count = 0;
                for (Component comp : overViewList) {
                    if (comp.getType() == CompType.RAM) {
                        count++;
                    }
                }
                if (count > 3) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation Dialog");
                    alert.setHeaderText("You are about to add a RAM component.");
                    alert.setContentText("Are you ok with this?");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK) {
                        overViewList.add(selectedRAM);
                    }
                } else {
                    overViewList.add(selectedRAM);
                }
            }
        } else if (cpuTab.isSelected()) {
            CPU selectedCPU = cpuLV.getSelectionModel().getSelectedItem();
            if (selectedCPU != null) {
                int count = 0;
                for (Component comp : overViewList) {
                    if (comp.getType() == CompType.CPU) {
                        count++;
                    }
                }
                if (count > 0) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation Dialog");
                    alert.setHeaderText("You are about to replace a CPU component.");
                    alert.setContentText("Are you ok with this?");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK) {
                        for (Component comp : overViewList) {
                            if (comp.getType() == CompType.CPU) {
                                int i = overViewList.indexOf(comp);
                                overViewList.remove(comp);
                                overViewList.add(i, selectedCPU);
                                break;
                            }
                        }
                    }
                } else {
                    overViewList.add(selectedCPU);
                }
            }
        } else if (gpuTab.isSelected()) {
            GPU selectedGPU = gpuLV.getSelectionModel().getSelectedItem();
            if (selectedGPU != null && !overViewList.contains(selectedGPU)) {
                overViewList.add(selectedGPU);
            }
        } else if (hddTab.isSelected()) {
            HDD selectedHDD = hddLV.getSelectionModel().getSelectedItem();
            if (selectedHDD != null && !overViewList.contains(selectedHDD)) {
                overViewList.add(selectedHDD);
            }
        }
    }
    @FXML
    void deleteComponent(ActionEvent event) {
        if (ramTab.isSelected()) {
            RAM selectedRAM = ramLV.getSelectionModel().getSelectedItem();
            if (selectedRAM != null) {
                ramList.remove(selectedRAM);
            }
        } else if (cpuTab.isSelected()) {
            CPU selectedCPU = cpuLV.getSelectionModel().getSelectedItem();
            if (selectedCPU != null) {
                cpuList.remove(selectedCPU);
            }
        } else if (gpuTab.isSelected()) {
            GPU selectedGPU = gpuLV.getSelectionModel().getSelectedItem();
            if (selectedGPU != null) {
                gpuList.remove(selectedGPU);
            }
        } else if (hddTab.isSelected()) {
            HDD selectedHDD = hddLV.getSelectionModel().getSelectedItem();
            if (selectedHDD != null) {
                hddList.remove(selectedHDD);
            }
        }
        updateAveragePrices();
    }
    void loadRamList() {
        ramList = FXCollections.observableArrayList();
        RAM ram1 = new RAM("Kingston", "HyperX", 100.0, CompType.RAM, 8);
        RAM ram2 = new RAM("Kingston", "HyperX Fury", 200.0, CompType.RAM, 8);
        ramLV.setCellFactory(new Callback<>() {
            @Override
            public ListCell<RAM> call(ListView<RAM> ramListView) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(RAM ram, boolean empty) {
                        super.updateItem(ram, empty);
                        if (ram == null || empty) {
                            setText(null);
                        } else {
                            setText(ram.getManufacturer() + " " + ram.getModel() + " " + ram.additionalParameter());
                        }
                    }
                };
            }
        });
        ramList.addAll(ram1, ram2);
        ramLV.setItems(ramList);
        FXCollections.sort(ramList, ramComparator);
    }
    void loadHDDList() {
        hddList = FXCollections.observableArrayList();
        hddLV.setCellFactory(new Callback<>() {
            @Override
            public ListCell<HDD> call(ListView<HDD> ramListView) {
                return new ListCell<HDD>() {
                    @Override
                    protected void updateItem(HDD ram, boolean empty) {
                        super.updateItem(ram, empty);
                        if (ram == null || empty) {
                            setText(null);
                        } else {
                            setText(ram.getManufacturer() + " " + ram.getModel() + " " + ram.additionalParameter());
                        }
                    }
                };
            }
        });
        HDD hdd = new HDD("Manufacturer1", "HDD", 100.0, CompType.HDD, 500);
        HDD ssd = new HDD("Manufacturer2", "SSD", 200.0, CompType.SSD, 250);
        hddList.addAll(hdd, ssd);
        hddLV.setItems(hddList);
        FXCollections.sort(hddList, hddComparator);
    }
    void loadGPUList() {
        gpuList = FXCollections.observableArrayList();
        gpuLV.setCellFactory(new Callback<>() {
            @Override
            public ListCell<GPU> call(ListView<GPU> ramListView) {
                return new ListCell<GPU>() {
                    @Override
                    protected void updateItem(GPU ram, boolean empty) {
                        super.updateItem(ram, empty);
                        if (ram == null || empty) {
                            setText(null);
                        } else {
                            setText(ram.getManufacturer() + " " + ram.getModel() + " " + ram.additionalParameter());
                        }
                    }
                };
            }
        });
        GPU gpu1 = new GPU("Manufacturer1", "Radeon 1", 100.0, CompType.GPU);
        GPU gpu2 = new GPU("Manufacturer2", "Radeon 2", 200.0, CompType.GPU);
        gpuList.addAll(gpu2, gpu1);
        gpuLV.setItems(gpuList);
        FXCollections.sort(gpuList, gpuComparator);
    }
    void loadCpuList() {
        cpuList = FXCollections.observableArrayList();
        cpuLV.setCellFactory(new Callback<>() {
            @Override
            public ListCell<CPU> call(ListView<CPU> ramListView) {
                return new ListCell<CPU>() {
                    @Override
                    protected void updateItem(CPU ram, boolean empty) {
                        super.updateItem(ram, empty);
                        if (ram == null || empty) {
                            setText(null);
                        } else {
                            setText(ram.getManufacturer() + " " + ram.getModel() + " " + ram.additionalParameter());
                        }
                    }
                };
            }
        });
        CPU cpu1 = new CPU("Manufacturer1", "Intel", 100.0, CompType.CPU, 2000);
        CPU cpu2 = new CPU("Manufacturer2", "AMD", 200.0, CompType.CPU, 2000);
        cpuList.addAll(cpu1, cpu2);
        cpuLV.setItems(cpuList);
        FXCollections.sort(cpuList, cpuComparator);
    }
    private double calculateTotalPrice() {
        double totalPrice = 0.0;
        for (RAM ram : laptop.getRam()) {
            totalPrice += ram.getPriceWithVAT();
        }
        for (HDD hdd : laptop.getHdd()) {
            totalPrice += hdd.getPriceWithVAT();
        }
        CPU cpu = laptop.getCpu();
        if (cpu != null) {
            totalPrice += cpu.getPriceWithVAT();
        }
        GPU gpu = laptop.getGpu();
        if (gpu != null) {
            totalPrice += gpu.getPriceWithVAT();
        }
        return totalPrice;
    }
    private String ramInfo() {
        StringBuilder builder = new StringBuilder();
        for (RAM ram : laptop.getRam()) {
            builder.append(ram.getSizeInGB()).append("GB, ");
        }
        if (builder.length() > 0) {
            builder.setLength(builder.length() - 2);
        }
        return "RAM: " + builder;
    }
    private String hddInfo() {
        StringBuilder builder = new StringBuilder();
        for (HDD hdd : laptop.getHdd()) {
            builder.append(hdd.getSizeInGB()).append("GB (").append(hdd.getType()).append("), ");
        }
        if (builder.length() > 0) {
            builder.setLength(builder.length() - 2);
        }
        return "HDD: " + builder;
    }
    private String cpuInfo() {
        CPU cpu = laptop.getCpu();
        return cpu != null ? "CPU: " + cpu.getManufacturer() + " " + cpu.getModel() : "";
    }
    private String gpuInfo() {
        GPU gpu = laptop.getGpu();
        return gpu != null ? "GPU: " + gpu.getManufacturer() + " " + gpu.getModel() : "";
    }
    private void updateLaptopProperties() {
        laptopProperties.setText(
                laptop.getName() + "\n" +
                        laptop.getColor() + "\n" +
                        cpuInfo() + "\n" +
                        gpuInfo() + "\n" +
                        ramInfo() + "\n" +
                        hddInfo() + "\n" +
                        "Total Price: " + String.format("%.2f", calculateTotalPrice()) + "kC"
        );
    }
    Laptop laptop;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        laptop = new Laptop(null, null, FXCollections.observableArrayList(), null, null, FXCollections.observableArrayList());
        loadRamList();
        loadCpuList();
        loadGPUList();
        loadHDDList();

        editComponent.setOnAction(e -> {
            overViewList.clear();
            laptopProperties.clear();
            laptop = new Laptop(null, null, FXCollections.observableArrayList(), null, null, FXCollections.observableArrayList());
        });
        updateAveragePrices();
        laptopNameTF.textProperty().addListener((observableValue, s, t1) -> laptop.setName(s));
        colorLaptop.valueProperty().addListener((observableValue, color, t1) -> laptop.setColor(String.valueOf(color)));
        overViewLV.setCellFactory(new Callback<>() {
            @Override
            public ListCell<Component> call(ListView<Component> componentListView) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(Component component, boolean empty) {
                        super.updateItem(component, empty);
                        if (component == null || empty) {
                            setText(null);
                        } else {
                            setText(component.getManufacturer() + " " + component.getModel());
                        }
                    }
                };
            }
        });
        overViewLV.setItems(overViewList);
        overViewList.addListener((ListChangeListener<Component>) change -> {
            laptop.getRam().clear();
            laptop.getHdd().clear();
            for (Component component : overViewList) {
                if (component instanceof RAM) {
                    laptop.getRam().add((RAM) component);
                } else if (component instanceof HDD) {
                    laptop.getHdd().add((HDD) component);
                } else if (component instanceof GPU) {
                    laptop.setGpu((GPU) component);
                } else if (component instanceof CPU) {
                    laptop.setCpu((CPU) component);
                }
            }
            updateLaptopProperties();
        });
        laptop.getRam().addListener((ListChangeListener<RAM>) change -> updateLaptopProperties());
        laptop.getHdd().addListener((ListChangeListener<HDD>) change -> updateLaptopProperties());
        laptop.nameProperty().addListener((observable, oldValue, newValue) -> updateLaptopProperties());
        laptop.colorProperty().addListener((observable, oldValue, newValue) -> updateLaptopProperties());
        laptop.cpuProperty().addListener((observable, oldValue, newValue) -> updateLaptopProperties());
        laptop.gpuProperty().addListener((observable, oldValue, newValue) -> updateLaptopProperties());
        overViewLV.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Component selectedComponent = overViewLV.getSelectionModel().getSelectedItem();
                if (selectedComponent != null) {
                    Dialog<Component> dialog = new Dialog<>();
                    dialog.setTitle("Edit Component");
                    // Create dialog content
                    GridPane grid = new GridPane();
                    grid.setHgap(10);
                    grid.setVgap(10);
                    TextField manufacturerField = new TextField(selectedComponent.getManufacturer());
                    TextField modelField = new TextField(selectedComponent.getModel());
                    TextField priceField = new TextField(String.format("%.2f", selectedComponent.getPriceWithoutVAT()));
                    // Price field input validation
                    priceField.textProperty().addListener((observable, oldValue, newValue) -> {
                        if (!newValue.matches("\\d{0,7}([\\,]\\d{0,2})?")) {
                            priceField.setText(oldValue);
                        }
                    });
                    grid.add(new Label("Manufacturer:"), 0, 0);
                    grid.add(manufacturerField, 1, 0);
                    grid.add(new Label("Model:"), 0, 1);
                    grid.add(modelField, 1, 1);
                    grid.add(new Label("Price (Without VAT):"), 0, 2);
                    grid.add(priceField, 1, 2);
                    dialog.getDialogPane().setContent(grid);
                    // Add button to dialog
                    ButtonType buttonTypeOk = new ButtonType("Submit", ButtonBar.ButtonData.OK_DONE);
                    dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
                    dialog.setResultConverter(b -> {
                        if (b == buttonTypeOk) {
                            selectedComponent.setManufacturer(manufacturerField.getText());
                            selectedComponent.setModel(modelField.getText());
                            String priceWithComma = priceField.getText();
                            String priceWithDot = priceWithComma.replace(',', '.');
                            selectedComponent.setPriceWithoutVAT(Double.parseDouble(priceWithDot));
                            System.out.println(selectedComponent.getPriceWithVAT());
                            return selectedComponent;
                        }
                        return null;
                    });
                    Optional<Component> result = dialog.showAndWait();
                    result.ifPresent(newComponent -> {
                        overViewList.set(overViewList.indexOf(selectedComponent), newComponent);
                        if (newComponent.getType() == CompType.HDD) {
                            FXCollections.sort(hddList, hddComparator);
                        } else if (newComponent.getType() == CompType.CPU) {
                            FXCollections.sort(cpuList, cpuComparator);
                        } else if (newComponent.getType() == CompType.GPU) {
                            FXCollections.sort(gpuList, gpuComparator);
                        } else {
                            FXCollections.sort(ramList, ramComparator);
                        }
                        cpuLV.refresh();
                        gpuLV.refresh();
                        ramLV.refresh();
                        hddLV.refresh();
                        updateAveragePrices();
                    });
                }
            }
        });
        overViewLV.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.DELETE) {
                Component selectedComponent = overViewLV.getSelectionModel().getSelectedItem();
                if (selectedComponent != null) {
                    overViewList.remove(selectedComponent);
                    if (selectedComponent.getType() == CompType.RAM) {
                        laptop.getRam().remove((RAM) selectedComponent);
                    } else if (selectedComponent.getType() == CompType.HDD) {
                        laptop.getHdd().remove((HDD) selectedComponent);
                        FXCollections.sort(hddList, hddComparator);
                    } else if (selectedComponent.getType() == CompType.CPU) {
                        laptop.setCpu(null);
                        FXCollections.sort(cpuList, cpuComparator);
                    } else if (selectedComponent.getType() == CompType.GPU) {
                        laptop.setGpu(null);
                        FXCollections.sort(gpuList, gpuComparator);
                    }
                    updateAveragePrices();
                    updateLaptopProperties();
                }
            }
        });
    }
}
