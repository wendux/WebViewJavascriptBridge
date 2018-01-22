package wendu.jsbdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.json.JSONObject;

import java.util.HashMap;

import wendu.webviewjavascriptbridge.WVJBWebView;

public class ExampleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);
        final WVJBWebView webView= (WVJBWebView) findViewById(R.id.webview);
        Button testJavascriptHandlerBtn= (Button) findViewById(R.id.testJavascriptHandlerBtn);
        webView.registerHandler("testJavaCallback", new WVJBWebView.WVJBHandler() {
            @Override
            public void handler(Object data, WVJBWebView.WVJBResponseCallback callback) {
                callback.onResult("Response from testJavaCallback");
            }
        });
        testJavascriptHandlerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String,String> data=new HashMap<String, String>(){{
                    put("greetingFromJava", "Hi there, JS!");
                }};
                webView.callHandler("testJavascriptHandler",new JSONObject(data) , new WVJBWebView.WVJBResponseCallback() {
                    @Override
                    public void onResult(Object data) {
                        Log.d("testJavascriptHandler",data.toString());
                    }
                });
            }
        });
        webView.loadUrl("file:///android_asset/exampleApp.html");
    }
}
