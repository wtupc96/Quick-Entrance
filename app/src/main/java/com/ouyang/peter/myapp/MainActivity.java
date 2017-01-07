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
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {
    // 用于确认由form传回信息的类型
    private static final int UNI_VERIFIED = 2131492957;
    private static final int LIBRARY = 2131492958;
    private static final int FLOW = 2131492959;

    // 用于确认由form表单传回信息的标识（根据上述三条）
    private static final String USERNAME_FOR_UNI = "usernameForUni";
    private static final String PASSWORD_FOR_UNI = "passwordForUni";
    private static final String USERNAME_FOR_LIBRARY = "usernameForLibrary";
    private static final String PASSWORD_FOR_LIBRARY = "passwordForLibrary";
    private static final String USERNAME_FOR_FLOW = "usernameForFlow";
    private static final String PASSWORD_FOR_FLOW = "passwordForFlow";
    //表单id信息
    private static final String UNI_USERNAME_ID = "username";
    private static final String UNI_PASSWORD_ID = "password";
    private static final String LIBRARY_USERNAME_ID = "code";
    private static final String LIBRARY_PASSWORD_ID = "pin";
    private static final String FLOW_USERNAME_ID = "TB_userName";
    private static final String FLOW_PASSWORD_ID = "TB_password";

    // 存储类别信息
    private static String usernameString;
    private static String passwordString;
    // 存储表单id信息
    private static String usernameId;
    private static String passwordId;

    // 由form表单传回的结果号
    private static final int REQ_CODE_0 = 0;
    // 存储文件的名称
    private static final String SETTING_INFOS = "Setting_infos";

    // 抽屉中的提示信息
    private static final String[] strs = new String[]{"选课端", "学生端", "缴费", "教务处", "出勤情况", "图书信息查询", "借阅查询", "流量查询", "网络导航"};
    // 对应的网址
    private static String[] urls = new String[]{"https://cas.xjtu.edu.cn/login?service=http%3A%2F%2Fxkfw.xjtu.edu.cn%2Fxsxk%2Flogin.xk", "https://cas.xjtu.edu.cn/login?service=http%3A%2F%2Fssfw.xjtu.edu.cn%2Findex.portal", "https://cas.xjtu.edu.cn/login?service=http://card.xjtu.edu.cn:8050/Account/CASSignIn", "http://dean.xjtu.edu.cn", "http://202.117.1.152:8080/user", "http://202.117.24.14/", "http://202.117.24.14/patroninfo*chx", "http://auth.xjtu.edu.cn/default.aspx", "http://xjtu.so/"};
    // 应用初始化为教务处网址
    private static String url = urls[3];
    // 标识当前webView装载的页面的下标，用户load对应Javascript
    private static int positionUrl = 3;

    // 进度弹框对象
    private myDialog myDialog = new myDialog();
    // 界面控件对象
    private static ListView listView;
    private static WebView webView;
    // 标识webView装载的Progress
    private static boolean webViewFlag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 设置webView相关属性
        setWebView();
        // 设置抽屉选项相关属性
        setListView();
    }

    private void setListView() {
        // 获取ListView对象
        listView = (ListView) findViewById(R.id.id_lv);

        // 配置适配器
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, strs);
        listView.setAdapter(arrayAdapter);

        // 点击listView时页面装载相应网页
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                webView.loadUrl(url = urls[positionUrl = position]);
            }
        });
    }

    private void setWebView() {
        // 获取webView控件对象
        webView = (WebView) findViewById(R.id.webView);

        // 获取webView设置
        WebSettings webSettings = webView.getSettings();

        // 设置为从本地读取网页否则从网络抓取
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        // 设置为允许应用缓存
        webSettings.setAppCacheEnabled(true);
        // 设置为网页允许缩放
        webSettings.setBuiltInZoomControls(true);
        // 允许Javascript打开窗口
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        // 允许读取地理位置
        webSettings.setGeolocationEnabled(true);
        // 允许页面的Javascript
        webSettings.setJavaScriptEnabled(true);
        // 允许页面自动加载图片
        webSettings.setLoadsImagesAutomatically(true);
        // 允许页面存储DOM树
        webSettings.setDomStorageEnabled(true);
        // 允许页面保存表单信息
        webSettings.setSaveFormData(true);
        // 支持多窗口
        webSettings.setSupportMultipleWindows(true);

        //解决了显示错位问题
        webSettings.setUseWideViewPort(true);

        // 允许缩放
        webSettings.setSupportZoom(true);

        webView.setWebViewClient(new WebViewClient() {
            // 页面跳转时保证在当前webView内跳转
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            // 页面加载完成时加载相应自动填充表单的Javascript信息
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                SharedPreferences sharedPreferences = getSharedPreferences(SETTING_INFOS, 0);
                switch (positionUrl) {
                    // 统一身份认证
                    case 0:
                    case 1:
                    case 2:
                    case 4:
                        usernameString = USERNAME_FOR_UNI;
                        passwordString = PASSWORD_FOR_UNI;
                        usernameId = UNI_USERNAME_ID;
                        passwordId = UNI_PASSWORD_ID;
                        break;
                    // 图书借阅
                    case 6:
                        usernameString = USERNAME_FOR_LIBRARY;
                        passwordString = PASSWORD_FOR_LIBRARY;
                        usernameId = LIBRARY_USERNAME_ID;
                        passwordId = LIBRARY_PASSWORD_ID;
                        break;
                    // 流量查询
                    case 7:
                        usernameString = USERNAME_FOR_FLOW;
                        passwordString = PASSWORD_FOR_FLOW;
                        usernameId = FLOW_USERNAME_ID;
                        passwordId = FLOW_PASSWORD_ID;
                        break;

                }
                view.loadUrl("JavaScript: {" +
                        "document.getElementById('" + usernameId + "').value = '" + sharedPreferences.getString(usernameString, "") + "';" +
                        "document.getElementById('" + passwordId + "').value = '" + sharedPreferences.getString(passwordString, "") + "';" +
                        "var frms = document.getElementsById('fm1');" +
                        "frms[0].submit(); };");

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

        webView.loadUrl(url);
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
                            usernameString = USERNAME_FOR_UNI;
                            passwordString = PASSWORD_FOR_UNI;
                            break;
                        case LIBRARY:
                            usernameString = USERNAME_FOR_LIBRARY;
                            passwordString = PASSWORD_FOR_LIBRARY;
                            break;
                        case FLOW:
                            usernameString = USERNAME_FOR_FLOW;
                            passwordString = PASSWORD_FOR_FLOW;
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
