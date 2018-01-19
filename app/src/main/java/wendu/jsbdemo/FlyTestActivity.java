package wendu.jsbdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.json.JSONObject;

import wendu.webviewjavascriptbridge.WVJBWebView;

public class FlyTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fly_test);
        final WVJBWebView webView = (WVJBWebView) findViewById(R.id.webview);
        webView.registerHandler("onAjaxRequest", new WVJBWebView.WVJBHandler() {
            @Override
            public void handler(Object data, WVJBWebView.WVJBResponseCallback callback) {
                AjaxHandler.onAjaxRequest((JSONObject) data, callback);
            }
        });
        webView.loadUrl("file:///android_asset/fly.html");
    }

}
