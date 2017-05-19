package practicaltest02var03.eim.systems.cs.pub.ro.practicaltest02var03;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PracticalTest02Var03MainActivity extends AppCompatActivity {


    private ServerThread serverThread = null;
    private int serverPort = -1;

    private class MyButtonClickListener implements Button.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.start_server_button:
                    if (serverThread != null) {
                        Toast.makeText(getApplicationContext(), "Server is already running!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String port = ((EditText)findViewById(R.id.server_port_edit_text)).getText().toString();
                    serverPort = Integer.parseInt(port);
                    serverThread = new ServerThread(serverPort);
                    serverThread.startServer();

                    break;
                case  R.id.start_client_button:
                    if (serverThread == null || !serverThread.isAlive() || serverPort == -1) {
                        Toast.makeText(getApplicationContext(), "There is no server to connect to!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String query = ((EditText)findViewById(R.id.client_custom_data_edit_text)).getText().toString();
                    if (query.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Word can't be null!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    TextView resultTextView = (TextView)findViewById(R.id.result_text_view);
                    resultTextView.setText("");
                    new ClientThread("localhost", serverPort, query, resultTextView).start();

                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test02_var03_main);

        MyButtonClickListener listener = new MyButtonClickListener();
        ((Button) findViewById(R.id.start_server_button)).setOnClickListener(listener);
        ((Button) findViewById(R.id.start_client_button)).setOnClickListener(listener);
    }

    @Override
    protected void onDestroy() {
        if (serverThread != null) {
            serverThread.stopServer();
            serverThread = null;
            serverPort = -1;
        }

        super.onDestroy();
    }


}
