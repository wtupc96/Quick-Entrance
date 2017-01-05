package com.ouyang.peter.myapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {
    private static final int UNI_VERIFIED = 2131492957;
    private static final int LIBRARY = 2131492958;
    private static final int FLOW = 2131492959;

    private static final String USERNAMEFORUNI = "usernameForUni";
    private static final String PASSWORDFORUNI = "passwordForUni";
    private static final String USERNAMEFORLIBRARY = "usernameForLibrary";
    private static final String PASSWORDFORLIBRARY = "passwordForLibrary";
    private static final String USERNAMEFORFLOW = "usernameForFlow";
    private static final String PASSWORDFORFLOW = "passwordForFlow";

    private static final int REQ_CODE_0 = 0;
    private static final String SETTING_INFOS = "Setting_infos";

    private static final String[] strs = new String[]{"选课端", "学生端", "缴费", "教务处", "出勤情况", "图书信息查询", "借阅查询", "流量查询", "网络导航"};
    private static String[] urls = new String[]{"https://cas.xjtu.edu.cn/login?service=http%3A%2F%2Fxkfw.xjtu.edu.cn%2Fxsxk%2Flogin.xk", "https://cas.xjtu.edu.cn/login?service=http%3A%2F%2Fssfw.xjtu.edu.cn%2Findex.portal", "https://cas.xjtu.edu.cn/login?service=http://card.xjtu.edu.cn:8050/Account/CASSignIn", "http://dean.xjtu.edu.cn", "http://202.117.1.152:8080/user", "http://202.117.24.14/", "http://202.117.24.14/patroninfo*chx", "http://auth.xjtu.edu.cn/default.aspx", "http://xjtu.so/"};
    private static String url = urls[3];
    private static int positionUrl = 3;

    private myDialog myDialog = new myDialog();
    private static ListView listView;
    private static WebView webView;
    private static boolean webViewFlag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setWebView();

        webView.loadUrl(url);

        setListView();

    }

    private void setListView() {
        listView = (ListView) findViewById(R.id.id_lv);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, strs);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                webView.loadUrl(url = urls[positionUrl = position]);
            }
        });
    }

    private void setWebView() {

        webView = (WebView) findViewById(R.id.webView);

        WebSettings webSettings = webView.getSettings();

        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webSettings.setAppCacheEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setDisplayZoomControls(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setGeolocationEnabled(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setSaveFormData(true);
        webSettings.setSupportMultipleWindows(true);

        //解决了显示错位问题
        webSettings.setUseWideViewPort(true);

        webSettings.setSupportZoom(true);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                SharedPreferences sharedPreferences = getSharedPreferences(SETTING_INFOS, 0);
                switch (positionUrl) {
                    case 0:
                    case 1:
                    case 2:
                    case 4:
                        view.loadUrl("JavaScript: {" +
                                "document.getElementById('username').value = '" + sharedPreferences.getString(USERNAMEFORUNI, "") + "';" +
                                "document.getElementById('password').value = '" + sharedPreferences.getString(PASSWORDFORUNI, "") + "';" +
                                "var frms = document.getElementsById('fm1');" +
                                "frms[0].submit(); };");
                        break;
                    case 6:
                        view.loadUrl("JavaScript: {" +
                                "document.getElementById('code').value = '" + sharedPreferences.getString(USERNAMEFORLIBRARY, "") + "';" +
                                "document.getElementById('pin').value = '" + sharedPreferences.getString(PASSWORDFORLIBRARY, "") + "';" +
                                "var frms = document.getElementsById('fm1');" +
                                "frms[0].submit(); };");
                        break;
                    case 7:
                        view.loadUrl("JavaScript: {" +
                                "document.getElementById('TB_userName').value = '" + sharedPreferences.getString(USERNAMEFORFLOW, "") + "';" +
                                "document.getElementById('TB_password').value = '" + sharedPreferences.getString(PASSWORDFORFLOW, "") + "';" +
                                "var frms = document.getElementsById('fm1');" +
                                "frms[0].submit(); };");
                        break;

                }

            }
        });


        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100 && !webViewFlag) {
                    myDialog.dismiss();
                    webViewFlag = true;
                } else if (webViewFlag) {
                    myDialog.show(getFragmentManager(), "tag");
                    webViewFlag = false;
                }
                super.onProgressChanged(view, newProgress);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && webView.canGoBack())
            webView.goBack();
        else
            super.onKeyDown(keyCode, event);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            switch (resultCode) {
                case REQ_CODE_0:
                    Bundle bundle = data.getBundleExtra("userInform");
                    int type = bundle.getInt("type");
                    String usernameString = "";
                    String passwordString = "";
                    switch (type) {
                        case UNI_VERIFIED:
                            usernameString = USERNAMEFORUNI;
                            passwordString = PASSWORDFORUNI;
                            break;
                        case LIBRARY:
                            usernameString = USERNAMEFORLIBRARY;
                            passwordString = PASSWORDFORLIBRARY;
                            break;
                        case FLOW:
                            usernameString = USERNAMEFORFLOW;
                            passwordString = PASSWORDFORFLOW;
                            break;
                        default:
                            try {
                                throw new Exception("No responding type");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                    }
                    SharedPreferences sharedPreferences = getSharedPreferences(SETTING_INFOS, 0);
                    sharedPreferences.edit()
                            .putString(usernameString, bundle.getString(usernameString))
                            .putString(passwordString, bundle.getString(passwordString))
                            .commit();
                    return;
                default:
                    return;
            }
        } catch (Exception e) {
            Log.e("ExceptionOccurred", e.toString());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        createMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return menuChoice(item);
    }

    private boolean menuChoice(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                webView.reload();
                return true;
            case 1:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(webView.getUrl())));
                return true;
            case 2:
                startActivityForResult(new Intent("com.peter.ouyang.formInformationActivity"), REQ_CODE_0);
                return true;
        }
        return false;
    }

    private void createMenu(Menu menu) {
        MenuItem refresh = menu.add(0, 0, 0, "刷新");
        refresh.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

        MenuItem openInBrowser = menu.add(0, 1, 1, "在浏览器中打开");
        openInBrowser.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

        MenuItem addInform = menu.add(1, 2, 2, "管理信息");
        addInform.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
    }
}
