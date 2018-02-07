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
        webView.registerHandler("onAjaxRequest", new WVJBWebView.WVJBHandler<JSONObject,String>() {
            @Override
            public void handler(JSONObject data, WVJBWebView.WVJBResponseCallback<String> callback) {
                AjaxHandler.onAjaxRequest(data, callback);
            }
        });
        webView.loadUrl("file:///android_asset/fly.html");
    }

}
