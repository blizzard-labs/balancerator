package com.balancerator.bluetooth;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ProcessingThread implements Runnable {

    private BluetoothSocket bs;
    private Handler handler;
    private InputStream inputStream;
    private OutputStream outputStream;
    public ProcessingThread(BluetoothSocket bs, Handler handler) {
        this.bs = bs;
        this.handler = handler;
        try {
            this.inputStream = bs.getInputStream();
            this.outputStream = bs.getOutputStream();
        } catch (IOException e) {
            Log.e("blah", "Unable to get streams", e);
        }
    }

    @Override
    public void run() {
        byte[] buffer = new byte[1024];  // buffer store for the stream
        int bytes = 0; // bytes returned from read()
        // Keep listening to the InputStream until an exception occurs
        while (true) {
            try {
                    /*
                    Read from the InputStream from Arduino until termination character is reached.
                    Then send the whole String message to GUI Handler.
                     */
                buffer[bytes] = (byte) inputStream.read();
                String readMessage;
                if (buffer[bytes] == '\n'){
                    readMessage = new String(buffer,0,bytes);
                    Log.e("Arduino Message",readMessage);
                    handler.obtainMessage(Messages.MESSAGE_READ,readMessage).sendToTarget();
                    bytes = 0;
                } else {
                    bytes++;
                }
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    /* Call this from the main activity to send data to the remote device */
    public void write(String input) {
        byte[] bytes = input.getBytes(); //converts entered String into bytes
        try {
            outputStream.write(bytes);
        } catch (IOException e) {
            Log.e("Send Error","Unable to send message",e);
        }
    }

    /* Call this from the main activity to shutdown the connection */
    public void cancel() {
        try {
            this.bs.close();
        } catch (IOException e) { }
    }
}
