WebViewJavascriptBridge
=======================

[![](https://jitpack.io/v/wendux/WebViewJavascriptBridge.svg)](https://jitpack.io/#wendux/WebViewJavascriptBridge)[![license](https://img.shields.io/github/license/mashape/apistatus.svg)](https://opensource.org/licenses/mit-license.php) 


An Android bridge for sending messages between Java and JavaScript in WebView. and **It is a mirror of [marcuswestin/WebViewJavascriptBridge](https://github.com/marcuswestin/WebViewJavascriptBridge) which supports IOS platforms.** 

Introduction
---------------

 This Android version project is a mirror of [marcuswestin/WebViewJavascriptBridge](https://github.com/marcuswestin/WebViewJavascriptBridge) ,  so there are some similarities between the two project , such as  API design, native code, and the Javascript code is exactly the same.

Installation 
------------------------

1. Add the JitPack repository to your build file

   ```java
   allprojects {
     repositories {
      ...
      maven { url 'https://jitpack.io' }
     }
   }
   ```

2. Add the dependency

   ```java
   dependencies {
   	compile 'com.github.wendux:WebViewJavascriptBridge:master-SNAPSHOT'
   }
   ```

Examples
--------

See the `wendu.jsbdemo/` folder. run the `app` project and to see it in action.

To use a WebViewJavascriptBridge in your own project:

Usage
-----

1) Use `WVJBWebView` instead of `WebView`:

```java
import wendu.webviewjavascriptbridge.WVJBWebView
```

...

```java
WVJBWebView webView= (WVJBWebView) findViewById(R.id.webview);
```

2) Register a handler in Java, and call a JS handler:

```objc
webView.registerHandler("ObjC Echo", new WVJBWebView.WVJBHandler() {
  @Override
    public void handler(Object data, WVJBWebView.WVJBResponseCallback callback) {
    Log.d("wvjsblog","Java Echo called with: "+data.toString());
    callback.callback(data);
  }
});

webView.callHandler("JS Echo", null, new WVJBWebView.WVJBResponseCallback() {
  @Override
    public void callback(Object data) {
     Log.d("wvjsblog","Java received response: "+data.toString());
    }
});
```

3) Copy and paste `setupWebViewJavascriptBridge` into your JS:
​	
```javascript
function setupWebViewJavascriptBridge(callback) {
	if (window.WebViewJavascriptBridge) { return callback(WebViewJavascriptBridge); }
	if (window.WVJBCallbacks) { return window.WVJBCallbacks.push(callback); }
	window.WVJBCallbacks = [callback];
	var WVJBIframe = document.createElement('iframe');
	WVJBIframe.style.display = 'none';
	WVJBIframe.src = 'https://__bridge_loaded__';
	document.documentElement.appendChild(WVJBIframe);
	setTimeout(function() { document.documentElement.removeChild(WVJBIframe) }, 0)
}
```

5) Finally, call `setupWebViewJavascriptBridge` and then use the bridge to register handlers and call Java handlers:

```javascript
setupWebViewJavascriptBridge(function(bridge) {
	
	/* Initialize your app here */

	bridge.registerHandler('JS Echo', function(data, responseCallback) {
		console.log("JS Echo called with:", data)
		responseCallback(data)
	})
	bridge.callHandler('Java Echo', {'key':'value'}, function responseCallback(responseData) {
		console.log("JS received response:", responseData)
	})
})
```

API Reference
-------------

### Java API


##### `webview.registerHandler(String handlerName, WVJBHandler handler);`


Register a handler called `handlerName`. The javascript can then call this handler with `WebViewJavascriptBridge.callHandler("handlerName")`.

Example:

```objc
webView.registerHandler("getScreenHeight", new WVJBWebView.WVJBHandler() {
  @Override
    public void handler(Object data, WVJBWebView.WVJBResponseCallback callback) {
    //wm is WindowManager
    callback.callback(wm.getDefaultDisplay().getHeight());
   }
});

webView.registerHandler("log", new WVJBWebView.WVJBHandler() {
  @Override
    public void handler(Object data, WVJBWebView.WVJBResponseCallback callback) {
     Log.d("wvjsblog","Log: "+data.toString());
   }
});
```

##### `webview.callHandler(String handlerName, WVJBResponseCallback responseCallback)`

##### `webview.callHandler(String handlerName, Object data,WVJBResponseCallback responseCallback)`

Call the javascript handler called `handlerName`. If a `responseCallback`  is given, the javascript handler can respond.

Example:

```objc
webview.callHandler("showAlert","Hi from Java to JS!");
webview.callHandler("getCurrentPageUrl", null, new WVJBWebView.WVJBResponseCallback() {
   @Override
    public void callback(Object data) {
     Log.d("wvjsblog","Current WVJBWebView page URL is: %@"+data.toString());
   }
});
                  
```

##### `webview.disableJavscriptAlertBoxSafetyTimeout(bool disable)`

UNSAFE. Speed up bridge message passing by disabling the setTimeout safety check. It is only safe to disable this safety check if you do not call any of the javascript popup box functions (alert, confirm, and prompt). If you call any of these functions from the bridged javascript code, the app will hang.

Example:

	webview.disableJavscriptAlertBoxSafetyTimeout(true);



### Javascript API

##### `bridge.registerHandler("handlerName", function(responseData) { ... })`

Register a handler called `handlerName`. The Java can then call this handler with `webview callHandler("handlerName","Foo")` and `webview.callHandler("handlerName", "Foo",  new WVJBWebView.WVJBResponseCallback() {...})`

Example:

```javascript
bridge.registerHandler("showAlert", function(data) { alert(data) })
bridge.registerHandler("getCurrentPageUrl", function(data, responseCallback) {
	responseCallback(document.location.toString())
})
```


##### `bridge.callHandler("handlerName", data)`
##### `bridge.callHandler("handlerName", data, function responseCallback(responseData) { ... })`

Call an Java handler called `handlerName`. If a `responseCallback` function is given the Java handler can respond.

Example:

```javascript
bridge.callHandler("Log", "Foo")
bridge.callHandler("getScreenHeight", null, function(response) {
	alert('Screen height:' + response)
})
```


##### `bridge.disableJavscriptAlertBoxSafetyTimeout(...)`

Calling `bridge.disableJavscriptAlertBoxSafetyTimeout(...)` has the same effect as calling `webview disableJavscriptAlertBoxSafetyTimeout(...)` in Java.

Example:

```javascript
//disable
bridge.disableJavscriptAlertBoxSafetyTimeout()
//restore 
bridge.disableJavscriptAlertBoxSafetyTimeout(false)
```
