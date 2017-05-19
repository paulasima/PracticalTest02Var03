package practicaltest02var03.eim.systems.cs.pub.ro.practicaltest02var03;

import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

/**
 * Created by student on 19.05.2017.
 */

public class ServerThread extends Thread{
    private boolean isRunning;
    private int serverPort;
    private ServerSocket serverSocket;
    private HashMap<String, String> data = null;

    public ServerThread(int serverPort) {
        this.isRunning = false;
        this.serverPort = serverPort;
        this.data = new HashMap<>();
    }

    public synchronized void setData(String word, String definition) {
        this.data.put(word, definition);
    }

    public synchronized HashMap<String, String> getData() {
        return data;
    }

    public void startServer() {
        Log.i(Constants.TAG, "startServer() was invoked");
        isRunning = true;
        start();
    }

    public void stopServer() {
        Log.i(Constants.TAG, "stopServer() was invoked");
        isRunning = false;
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(serverPort);
            while (isRunning) {
                Socket socket = serverSocket.accept();
                if (socket != null) {
                    Log.i(Constants.TAG, "[SERVER THREAD] A connection request was received from " + socket.getInetAddress() + ":" + socket.getLocalPort());
                    new CommunicationThread(this, socket).start();
                }
            }
        } catch (IOException e) {
            Log.e (Constants.TAG, "[SERVER THREAD] Caught exception: " + e.getMessage());
        } finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
