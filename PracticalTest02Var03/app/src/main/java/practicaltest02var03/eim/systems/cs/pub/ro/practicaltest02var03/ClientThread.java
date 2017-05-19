package practicaltest02var03.eim.systems.cs.pub.ro.practicaltest02var03;

import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by student on 19.05.2017.
 */

public class ClientThread extends Thread{

    private String address;
    private int port;
    private String query;
    private Socket socket;
    private TextView resultTextView;

    public ClientThread(String address, int port, String query, TextView resultTextView) {
        this.address = address;
        this.port = port;
        this.query = query;
        this.resultTextView = resultTextView;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(address, port);
            BufferedReader reader = Utilities.getReader(socket);
            PrintWriter writer = Utilities.getWriter(socket);
            Log.i(Constants.TAG, "[CLIENT THREAD] Started...");

            writer.println(query);

            String fromServer = reader.readLine();
            final String result = fromServer.replace("|", "\n");
            if (result != null) {
                resultTextView.post(new Runnable() {
                    @Override
                    public void run() {
                        resultTextView.setText(result);
                    }
                });
            }

            socket.close();
            Log.i(Constants.TAG, "[CLIENT THREAD] Ended...");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
