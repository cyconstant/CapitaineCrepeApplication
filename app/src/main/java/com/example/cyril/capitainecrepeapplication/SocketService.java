package com.example.cyril.capitainecrepeapplication;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketService extends Service {

    public static final String SERVERIP = "10.0.2.2";
    public static final int SERVERPORT = 7777;
    private PrintWriter writer;

    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public BufferedReader getReader() {
        return reader;
    }

    private Socket socket;
    // Binder given to clients
    private final IBinder myBinder = new LocalBinder();


    public SocketService() {
    }

    public class LocalBinder extends Binder {
        public SocketService getService() {
            System.out.println("Localbinder");
            // Return this instance of SocketService so clients can call public methods
            return SocketService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        System.out.println("SocketService.onBind");
        return myBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        socket = null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        System.out.println("SocketService.onStartCommand");
        new StartNetwork().execute();
        return START_STICKY;
    }

    private class StartNetwork extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            System.out.println("StartNetwork.onPreExecute");
        }

        @Override
        protected Boolean doInBackground(Void... v) {
            System.out.println("StartNetwork.doInBackground");
            try {
                Socket socket = new Socket(SERVERIP, SERVERPORT);
                writer = new PrintWriter(socket.getOutputStream(), true);
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                return true;
            } catch (IOException e) {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean b) {
            if (b) {
                System.out.println("Connected to server\n");
                //readMessages = new ReadMessages();
                //readMessages.execute();
            } else {
                System.out.println("Could not connect to server\n");
            }
        }
    }


    public void sendMessage(String message) {
        if (writer != null && !writer.checkError()) {
            System.out.println("SocketService.sendMessage = " + message);
            writer.println(message);
            writer.flush();
        }
    }
}
