## 重要通知
**由于DSBridge(笔者开发的一个更强大的跨平台JavaScript Bridge)目前已取得大量用户，由于笔者精力有限，为了集中精力打造精品，此项目将不再维护，笔者将把主要精力放到DSBridge的维护与支持上，也欢迎大家使用DSBridge。**：

DSBridge for Android: https://github.com/wendux/DSBridge-Android

DSBridge for iOS: https://github.com/wendux/DSBridge-IOS

WebViewJavascriptBridge
=======================

[![](https://jitpack.io/v/wendux/WebViewJavascriptBridge.svg)](https://jitpack.io/#wendux/WebViewJavascriptBridge)
![language](https://img.shields.io/badge/language-Java-yellow.svg)
[![license](https://img.shields.io/github/license/mashape/apistatus.svg)](https://opensource.org/licenses/mit-license.php)
[![](https://travis-ci.org/wendux/WebViewJavascriptBridge.svg?branch=master)](https://travis-ci.org/wendux/WebViewJavascriptBridge)
![](https://img.shields.io/badge/minSdkVersion-11-yellow.svg)
[![GitHub last commit](https://img.shields.io/github/last-commit/wendux/WebViewJavascriptBridge.svg?color=blue)]()

An  **Android**  bridge for sending messages between Java and JavaScript in WebView. and It is a mirror of [marcuswestin/WebViewJavascriptBridge](https://github.com/marcuswestin/WebViewJavascriptBridge) (object-c) and [Lision/WKWebViewJavascriptBridge](https://github.com/Lision/WKWebViewJavascriptBridge)(swift) which supports IOS platforms.

Introduction
---------------

This Android version project is a mirror of [marcuswestin/WebViewJavascriptBridge](https://github.com/marcuswestin/WebViewJavascriptBridge) (object-c) and [Lision/WKWebViewJavascriptBridge](https://github.com/Lision/WKWebViewJavascriptBridge)(swift), so there are some similarities between the two project , such as API design, native code, and the Javascript code is exactly the same.

## Notice

If you are a new user, I strongly suggest that you use [DSBridge](https://github.com/wendux/DSBridge-Android) instead. [DSBridge](https://github.com/wendux/DSBridge-Android) is a modern cross-platform Javascript bridge, it is more powerful than WebViewJavascriptBridge.

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
WVJBWebView webView = (WVJBWebView) findViewById(R.id.webview);
```

2) Register a handler in Java, and call a JS handler:

```objc
webView.registerHandler("Java Echo", new WVJBWebView.WVJBHandler() {
  @Override
  public void handler(Object data, WVJBWebView.WVJBResponseCallback callback) {
    Log.d("wvjsblog", "Java Echo called with: "+data.toString());
    callback.onResult(data);
  }
});

webView.callHandler("JS Echo", null, new WVJBWebView.WVJBResponseCallback() {
  @Override
  public void onResult(Object data) {
    Log.d("wvjsblog", "Java received response: "+data.toString());
  }
});
```

3) Copy and paste `setupWebViewJavascriptBridge` into your JS:

```javascript
function setupWebViewJavascriptBridge(callback) {
  var bridge = window.WebViewJavascriptBridge || window.WKWebViewJavascriptBridge;
  if (bridge) { return callback(bridge); }
  var callbacks = window.WVJBCallbacks || window.WKWVJBCallbacks;
  if (callbacks) { return callbacks.push(callback); }
  window.WVJBCallbacks = window.WKWVJBCallbacks = [callback];
  if (window.WKWebViewJavascriptBridge) {
    //for https://github.com/Lision/WKWebViewJavascriptBridge
    window.webkit.messageHandlers.iOS_Native_InjectJavascript.postMessage(null);
  } else {
    //for https://github.com/marcuswestin/WebViewJavascriptBridge
    var WVJBIframe = document.createElement('iframe');
    WVJBIframe.style.display = 'none';
    WVJBIframe.src = 'https://__bridge_loaded__';
    document.documentElement.appendChild(WVJBIframe);
    setTimeout(function() { document.documentElement.removeChild(WVJBIframe) }, 0);
	}
}
```

4) Finally, call `setupWebViewJavascriptBridge` and then use the bridge to register handlers and call Java handlers:

```javascript
setupWebViewJavascriptBridge(function(bridge) {

  /* Initialize your app here */

  bridge.registerHandler('JS Echo', function(data, responseCallback) {
    console.log("JS Echo called with:", data);
    responseCallback(data);
  });
  bridge.callHandler('Java Echo', {'key': 'value'}, function responseCallback(responseData) {
    console.log("JS received response:", responseData);
  });
});
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
    callback.onResult(wm.getDefaultDisplay().getHeight());
  }
});

webView.registerHandler("log", new WVJBWebView.WVJBHandler() {
  @Override
  public void handler(Object data, WVJBWebView.WVJBResponseCallback callback) {
    Log.d("wvjsblog", "Log: "+data.toString());
  }
});
```

##### `webview.callHandler(String handlerName, WVJBResponseCallback responseCallback)`

##### `webview.callHandler(String handlerName, Object data,WVJBResponseCallback responseCallback)`

Call the javascript handler called `handlerName`. If a `responseCallback`  is given, the javascript handler can respond.

Example:

```objc
webview.callHandler("showAlert", "Hi from Java to JS!");
webview.callHandler("getCurrentPageUrl", null, new WVJBWebView.WVJBResponseCallback() {
  @Override
    public void onResult(Object data) {
    Log.d("wvjsblog", "Current WVJBWebView page URL is: %@"+data.toString());
  }
});
```

##### `webview.disableJavascriptAlertBoxSafetyTimeout(bool disable)`

UNSAFE. Speed up bridge message passing by disabling the setTimeout safety check. It is only safe to disable this safety check if you do not call any of the javascript popup box functions (alert, confirm, and prompt). If you call any of these functions from the bridged javascript code, the app will hang.

Example:

```java
webview.disableJavascriptAlertBoxSafetyTimeout(true);
```

### Javascript API

##### `bridge.registerHandler("handlerName", function(responseData) { ... })`

Register a handler called `handlerName`. The Java can then call this handler with `webview callHandler("handlerName","Foo")` and `webview.callHandler("handlerName", "Foo", new WVJBWebView.WVJBResponseCallback() {...})`

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

##### `bridge.disableJavascriptAlertBoxSafetyTimeout(...)`

Calling `bridge.disableJavascriptAlertBoxSafetyTimeout(...)` has the same effect as calling `webview disableJavscriptAlertBoxSafetyTimeout(...)` in Java.

Example:

```javascript
//disable
bridge.disableJavascriptAlertBoxSafetyTimeout()
//enable
bridge.disableJavascriptAlertBoxSafetyTimeout(false)
```

## Expansion For Android

In this Android version, I have added a way to judge whether the handler (Javascript and java) exists.

**In Java**

```java
webview.hasJavascriptMethod(String handlerName,  WVJBMethodExistCallback callback)
```

For example:

```java
webView.hasJavascriptMethod("echoHandler", new WVJBWebView.WVJBMethodExistCallback() {
  @Override
  public void onResult(boolean exist) {
    if(exist) {
      Log.d("wvjsblog", "Javascript handler 'echoHandler' exist. ");
    }
  }
});
```

**In Javascript**

```javascript
bridge.hasNativeMethod("handlerName", function responseCallback(responseData){...})
```

For example:

```javascript
bridge.hasNativeMethod('javaEchoToJs', function(exist){
  if(exist){
    console.log("Native method 'javaEchoToJs' exist! ")
  }
})
```

## Compare with DSBridge

[DSBridge](https://github.com/wendux/DSBridge-Android) is  a modern cross-platform JavaScript bridge, through which you can invoke each other's functions synchronously or asynchronously between JavaScript and native applications. On the whole, [DSBridge](https://github.com/wendux/DSBridge-Android) is more powerful than WebViewJavascriptBridge. If you are a new user, I strongly suggest that you use DSBridge instead. More details please rerfer to [https://github.com/wendux/DSBridge-Android](https://github.com/wendux/DSBridge-Android).
