package com.balancerator.bluetooth;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

public class ConnectionThread implements Runnable {

    private BluetoothSocket socket;
    private Handler handler;
    private ProcessingThread processingThread;

    @SuppressLint("MissingPermission")
    public ConnectionThread(BluetoothDevice bd, Handler handler) {
        this.handler = handler;
        UUID uuid = bd.getUuids()[0].getUuid();
        try {
                /*
                Get a BluetoothSocket to connect with the given BluetoothDevice.
                Due to Android device varieties,the method below may not work fo different devices.
                You should try using other methods i.e. :
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
                 */
            socket = bd.createInsecureRfcommSocketToServiceRecord(uuid);

        } catch (IOException e) {
            Log.e("TAG", "Socket's create() method failed", e);
        }
    }
    @SuppressLint("MissingPermission")
    @Override
    public void run() {
        // Cancel discovery because it otherwise slows down the connection.
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothAdapter.cancelDiscovery();
        try {
            // Connect to the remote device through the socket. This call blocks
            // until it succeeds or throws an exception.
            socket.connect();
            Log.e("Status", "Device connected");
            handler.obtainMessage(Messages.CONNECTING_STATUS, 1, -1).sendToTarget();
        } catch (IOException connectException) {
            // Unable to connect; close the socket and return.
            try {
                socket.close();
                Log.e("Status", "Cannot connect to device");
                handler.obtainMessage(Messages.CONNECTING_STATUS, -1, -1).sendToTarget();
            } catch (IOException closeException) {
                Log.e("blah", "Could not close the client socket", closeException);
            }
            return;
        }

        // The connection attempt succeeded. Perform work associated with
        // the connection in a separate thread.
        processingThread = new ProcessingThread(socket, handler);
        processingThread.run();
    }

    // Closes the client socket and causes the thread to finish.
    public void cancel() {
        try {
            socket.close();
        } catch (IOException e) {
            Log.e("blah", "Could not close the client socket", e);
        }
    }
}
