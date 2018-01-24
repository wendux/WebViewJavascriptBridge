package wendu.jsbdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import wendu.webviewjavascriptbridge.WVJBWebView;

import static android.widget.Toast.LENGTH_SHORT;

public class EchoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_echo);
        final WVJBWebView webView= (WVJBWebView) findViewById(R.id.webview);
        final EditText editText= (EditText) findViewById(R.id.edit);
        Button  echoHandlerBtn= (Button) findViewById(R.id.echoHandlerBtn);
        Button jsRcvResponseTestBtn= (Button) findViewById(R.id.jsRcvResponseTestBtn);
        webView.registerHandler("javaEchoToJs", new WVJBWebView.WVJBHandler() {
            @Override
            public void handler(Object data, WVJBWebView.WVJBResponseCallback callback) {
                Toast.makeText(EchoActivity.this,data.toString(),LENGTH_SHORT).show();
                Log.d("wvjsblog",data.toString());
                callback.onResult(data);
            }
        });

        echoHandlerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.callHandler("echoHandler", editText.getText(), new WVJBWebView.WVJBResponseCallback() {
                    @Override
                    public void onResult(Object data) {
                        Toast.makeText(EchoActivity.this,data.toString(),LENGTH_SHORT).show();
                    }
                });
            }
        });

        jsRcvResponseTestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.callHandler("jsRcvResponseTest", "", new WVJBWebView.WVJBResponseCallback() {
                    @Override
                    public void onResult(Object data) {
                        Toast.makeText(EchoActivity.this,data.toString(),LENGTH_SHORT).show();
                    }
                });
            }
        });

        webView.hasJavascriptMethod("echoHandler", new WVJBWebView.WVJBMethodExistCallback() {
            @Override
            public void onResult(boolean exist) {
                if(exist) {
                    Log.d("wvjsblog", "Javascript handler 'echoHandler' exist. ");
                }
            }
        });

        webView.loadUrl("file:///android_asset/echo.html");

    }
}
