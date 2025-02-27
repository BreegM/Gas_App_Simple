// Version 1.0 - BluetoothService.java
package com.BreegM.gas_app_simple;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class BluetoothService {
    private static final String TAG = "BluetoothService";
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); // Стандартний UUID для SPP

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket socket;
    private BluetoothDevice device;
    private InputStream inputStream;
    private OutputStream outputStream;
    private volatile boolean connected = false;
    private Handler handler = new Handler(Looper.getMainLooper());

    // Інтерфейс для зворотного зв'язку
    public interface BluetoothCallback {
        void onConnectionSuccess();
        void onConnectionFailed(String errorMessage);
        void onDataReceived(String data);
        void onDisconnected();
    }

    private BluetoothCallback callback;

    public BluetoothService(BluetoothCallback callback) {
        this.callback = callback;
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public boolean isBluetoothSupported() {
        return bluetoothAdapter != null;
    }

    public boolean isBluetoothEnabled() {
        return bluetoothAdapter != null && bluetoothAdapter.isEnabled();
    }

    public Set<BluetoothDevice> getPairedDevices() {
        if (bluetoothAdapter != null) {
            return bluetoothAdapter.getBondedDevices();
        }
        return null;
    }

    public void connectToDevice(BluetoothDevice device) {
        this.device = device;
        new Thread(this::connect).start();
    }

    private void connect() {
        try {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    Log.e(TAG, "Error closing old socket", e);
                }
            }

            socket = device.createRfcommSocketToServiceRecord(MY_UUID);
            socket.connect();
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            connected = true;

            handler.post(() -> callback.onConnectionSuccess());

            // Запуск потоку для читання даних
            listenForData();
        } catch (IOException e) {
            connected = false;
            Log.e(TAG, "Connection failed", e);
            handler.post(() -> callback.onConnectionFailed(e.getMessage()));
        }
    }

    private void listenForData() {
        new Thread(() -> {
            byte[] buffer = new byte[1024];
            int bytes;

            while (connected) {
                try {
                    if (inputStream.available() > 0) {
                        bytes = inputStream.read(buffer);
                        String data = new String(buffer, 0, bytes);
                        handler.post(() -> callback.onDataReceived(data));
                    }
                    Thread.sleep(100); // Маленька пауза для економії ресурсів
                } catch (IOException | InterruptedException e) {
                    connected = false;
                    Log.e(TAG, "Error reading data", e);
                    handler.post(() -> callback.onDisconnected());
                    break;
                }
            }
        }).start();
    }

    public void sendCommand(String command) {
        if (connected && outputStream != null) {
            new Thread(() -> {
                try {
                    outputStream.write(command.getBytes());
                    outputStream.flush();
                } catch (IOException e) {
                    Log.e(TAG, "Error sending data", e);
                    handler.post(() -> {
                        connected = false;
                        callback.onDisconnected();
                    });
                }
            }).start();
        }
    }

    public void disconnect() {
        connected = false;
        try {
            if (inputStream != null) inputStream.close();
            if (outputStream != null) outputStream.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            Log.e(TAG, "Error closing socket", e);
        }
    }

    public boolean isConnected() {
        return connected;
    }
}