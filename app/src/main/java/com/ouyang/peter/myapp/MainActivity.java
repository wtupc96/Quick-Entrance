package com.ouyang.peter.myapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    private static final String[] strs = new String[]{"选课端", "学生端", "缴费", "教务处", "出勤情况", "图书信息查询", "借阅查询", "流量查询", "网络导航"};
    private static String[] urls = new String[]{"https://cas.xjtu.edu.cn/login?service=http%3A%2F%2Fxkfw.xjtu.edu.cn%2Fxsxk%2Flogin.xk", "https://cas.xjtu.edu.cn/login?service=http%3A%2F%2Fssfw.xjtu.edu.cn%2Findex.portal", "https://cas.xjtu.edu.cn/login?service=http://card.xjtu.edu.cn:8050/Account/CASSignIn", "http://dean.xjtu.edu.cn", "http://202.117.1.152:8080/user", "http://202.117.24.14/", "http://202.117.24.14/patroninfo*chx", "http://auth.xjtu.edu.cn/default.aspx", "http://xjtu.so/"};

    private myDialog myDialog = new myDialog();
    private static ListView listView;
    private static WebView webView;
    private static boolean webViewFlag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = (WebView) findViewById(R.id.webView);
        setWebView();

        webView.loadUrl("http://dean.xjtu.edu.cn");

        listView = (ListView) findViewById(R.id.id_lv);
        setListView();

    }

    private void setListView() {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, strs);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                webView.loadUrl(urls[position]);
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

    private void setWebView() {
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
        });


        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100 && !webViewFlag) {
                    myDialog.dismiss();
                    webViewFlag = true;
                } else if(webViewFlag){
                    myDialog.show(getFragmentManager(), "tag");
                    webViewFlag = false;
                }
                super.onProgressChanged(view, newProgress);
            }
        });
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
        switch (item.getItemId()){
            case 0:
                webView.reload();
                return true;
            case 1:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(webView.getUrl())));
                return true;
        }
        return false;
    }

    private void createMenu(Menu menu) {
        MenuItem refresh = menu.add(0,0,0, "刷新");
        refresh.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        MenuItem openInBrowser = menu.add(0,1,1,"在浏览器中打开");
        openInBrowser.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
    }

}
