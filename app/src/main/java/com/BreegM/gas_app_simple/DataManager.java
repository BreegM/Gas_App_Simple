// Version 1.0 - DataManager.java
package com.BreegM.gas_app_simple;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DataManager {
    private static final String TAG = "DataManager";
    private static final String FILE_NAME_PREFIX = "LMDData_";
    private static final String CSV_HEADER = "DateTime,BatteryVoltage,BackgroundGas,SampleGas,FlowRate,LeakRate";

    private Context context;
    private List<MeasurementData> measurements;

    public DataManager(Context context) {
        this.context = context;
        this.measurements = new ArrayList<>();
    }

    public void addMeasurement(MeasurementData measurement) {
        measurements.add(measurement);
    }

    public List<MeasurementData> getMeasurements() {
        return measurements;
    }

    public void clearMeasurements() {
        measurements.clear();
    }

    // Метод для збереження всіх вимірювань у CSV-файл
    public boolean saveMeasurementsToFile() {
        if (measurements.isEmpty()) {
            return false;
        }

        // Створюємо ім'я файлу з поточною датою і часом
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        String fileName = FILE_NAME_PREFIX + sdf.format(new Date()) + ".csv";

        try {
            File file = new File(context.getExternalFilesDir(null), fileName);
            FileWriter writer = new FileWriter(file);

            // Записуємо заголовок
            writer.append(CSV_HEADER);
            writer.append("\n");

            // Записуємо всі вимірювання
            for (MeasurementData measurement : measurements) {
                writer.append(measurement.toCsvString());
                writer.append("\n");
            }

            writer.flush();
            writer.close();

            Log.d(TAG, "Saved measurements to file: " + file.getAbsolutePath());
            return true;
        } catch (IOException e) {
            Log.e(TAG, "Error saving measurements to file", e);
            return false;
        }
    }

    // Метод для збереження одного вимірювання у CSV-файл
    public boolean saveSingleMeasurementToFile(MeasurementData measurement) {
        if (measurement == null) {
            return false;
        }

        // Створюємо ім'я файлу з поточною датою і часом
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        String fileName = FILE_NAME_PREFIX + sdf.format(new Date()) + ".csv";

        try {
            File file = new File(context.getExternalFilesDir(null), fileName);
            FileWriter writer = new FileWriter(file);

            // Записуємо заголовок
            writer.append(CSV_HEADER);
            writer.append("\n");

            // Записуємо вимірювання
            writer.append(measurement.toCsvString());
            writer.append("\n");

            writer.flush();
            writer.close();

            Log.d(TAG, "Saved measurement to file: " + file.getAbsolutePath());
            return true;
        } catch (IOException e) {
            Log.e(TAG, "Error saving measurement to file", e);
            return false;
        }
    }
}