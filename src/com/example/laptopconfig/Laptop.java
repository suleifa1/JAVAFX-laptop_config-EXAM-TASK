package com.example.laptopconfig;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Laptop {
    private StringProperty name;
    private StringProperty color;
    private ObservableList<RAM> ram;
    private ObjectProperty<CPU> cpu;
    private ObjectProperty<GPU> gpu;

    private ObservableList<HDD> hdd;
    @Override
    public String toString() {
        return "Laptop{" +
                "name=" + name.get() +
                ", color=" + color.get() +
                ", ram=" + ram +
                ", cpu=" + cpu.get() +
                ", gpu=" + gpu.get() +
                ", hdd=" + hdd +
                '}';
    }
    public Laptop(String name, String color, ObservableList<RAM> ram, CPU cpu, GPU gpu, ObservableList<HDD> hdd) {
        this.name = new SimpleStringProperty(name);
        this.color = new SimpleStringProperty(color);
        this.ram = ram;
        this.cpu = new SimpleObjectProperty<>(cpu);
        this.gpu = new SimpleObjectProperty<>(gpu);
        this.hdd = hdd;
    }
    public String getName() {
        return name.get();
    }
    public StringProperty nameProperty() {
        return name;
    }
    public void setName(String name) {
        this.name.set(name);
    }
    public String getColor() {
        return color.get();
    }
    public StringProperty colorProperty() {
        return color;
    }
    public void setColor(String color) {
        this.color.set(color);
    }
    public ObservableList<RAM> getRam() {
        return ram;
    }
    public void setRam(ObservableList<RAM> ram) {
        this.ram = ram;
    }
    public CPU getCpu() {
        return cpu.get();
    }
    public ObjectProperty<CPU> cpuProperty() {
        return cpu;
    }
    public void setCpu(CPU cpu) {
        this.cpu.set(cpu);
    }
    public GPU getGpu() {
        return gpu.get();
    }
    public ObjectProperty<GPU> gpuProperty() {
        return gpu;
    }
    public ObservableList<HDD> getHdd() {
        return hdd;
    }
    public void setHdd(ObservableList<HDD> hdd) {
        this.hdd = hdd;
    }
    public void setGpu(GPU gpu) {
        this.gpu.set(gpu);
    }




}
