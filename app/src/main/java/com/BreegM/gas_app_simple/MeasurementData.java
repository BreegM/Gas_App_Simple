// Version 1.0 - MeasurementData.java
package com.BreegM.gas_app_simple;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MeasurementData {
    private String dateTime;
    private double batteryVoltage;
    private double backgroundGasPercent;
    private double sampleGasPercent;
    private int flowRate;
    private double leakRate;

    public MeasurementData() {
        // Встановлюємо поточну дату і час за замовчуванням
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        this.dateTime = sdf.format(new Date());

        // Початкові значення
        this.batteryVoltage = 0.0;
        this.backgroundGasPercent = 0.0;
        this.sampleGasPercent = 0.0;
        this.flowRate = 0;
        this.leakRate = 0.0;
    }

    // Конструктор для створення об'єкта з отриманих даних
    public MeasurementData(String dataString) {
        try {
            String[] parts = dataString.split(",");
            this.dateTime = parts[0];
            this.batteryVoltage = Double.parseDouble(parts[1]);
            this.backgroundGasPercent = Double.parseDouble(parts[2]);
            this.sampleGasPercent = Double.parseDouble(parts[3]);
            this.flowRate = Integer.parseInt(parts[4]);
            this.leakRate = Double.parseDouble(parts[5]);
        } catch (Exception e) {
            // Встановлюємо значення за замовчуванням у разі помилки
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
            this.dateTime = sdf.format(new Date());
            this.batteryVoltage = 0.0;
            this.backgroundGasPercent = 0.0;
            this.sampleGasPercent = 0.0;
            this.flowRate = 0;
            this.leakRate = 0.0;
        }
    }

    // Геттери та сеттери
    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public double getBatteryVoltage() {
        return batteryVoltage;
    }

    public void setBatteryVoltage(double batteryVoltage) {
        this.batteryVoltage = batteryVoltage;
    }

    public double getBackgroundGasPercent() {
        return backgroundGasPercent;
    }

    public void setBackgroundGasPercent(double backgroundGasPercent) {
        this.backgroundGasPercent = backgroundGasPercent;
    }

    public double getSampleGasPercent() {
        return sampleGasPercent;
    }

    public void setSampleGasPercent(double sampleGasPercent) {
        this.sampleGasPercent = sampleGasPercent;
    }

    public int getFlowRate() {
        return flowRate;
    }

    public void setFlowRate(int flowRate) {
        this.flowRate = flowRate;
    }

    public double getLeakRate() {
        return leakRate;
    }

    public void setLeakRate(double leakRate) {
        this.leakRate = leakRate;
    }

    // Метод для збереження даних у CSV-формат
    public String toCsvString() {
        return String.format(Locale.US, "%s,%.1f,%.2f,%.2f,%d,%.1f",
                dateTime, batteryVoltage, backgroundGasPercent,
                sampleGasPercent, flowRate, leakRate);
    }

    // Метод для перетворення даних у рядок для відображення
    @Override
    public String toString() {
        return "Дата/час: " + dateTime + "\n" +
                "Батарея(V): " + batteryVoltage + "\n" +
                "Фон(%): " + backgroundGasPercent + "\n" +
                "Проба(%): " + sampleGasPercent + "\n" +
                "Потік(л/хв): " + flowRate + "\n" +
                "Витік(л/хв): " + leakRate;
    }
}