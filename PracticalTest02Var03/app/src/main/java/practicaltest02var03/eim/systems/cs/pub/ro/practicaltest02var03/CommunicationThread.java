package practicaltest02var03.eim.systems.cs.pub.ro.practicaltest02var03;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Set;

import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.ResponseHandler;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.BasicResponseHandler;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

/**
 * Created by student on 19.05.2017.
 */

public class CommunicationThread extends Thread{
    private ServerThread serverThread;
    private Socket socket;

    public CommunicationThread(ServerThread serverThread, Socket socket) {
        this.serverThread = serverThread;
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            BufferedReader reader = Utilities.getReader(socket);
            PrintWriter writer = Utilities.getWriter(socket);
            Log.i(Constants.TAG, "[COMMUNICATION THREAD] Started...");
            String response = "";
            String query = reader.readLine();
            if (serverThread.getData().containsKey(query)) {
                response = serverThread.getData().get(query);
            }
            else {
                HttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet("http://services.aonaware.com/DictService/DictService.asmx/Define?word=" + query);
                ResponseHandler responseHandler = new BasicResponseHandler();
                response = httpClient.execute(httpGet, responseHandler).toString();
            }
            
            writer.println(response);

            socket.close();
            Log.i(Constants.TAG, "[COMMUNICATION THREAD] Ended...");
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
