package com.hgil.game.puzzle;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * An activity that displays the application's license
 * and related documents.
 */
public class LicensePages extends Activity {
    protected static final String CHARSET_UTF_8 = "UTF-8";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = new WebView(this);
        final WebSettings settings = page.getSettings();
        settings.setBuiltInZoomControls(true);
        settings.setTextSize(WebSettings.TextSize.SMALLEST);
        page.setWebViewClient(WEB_CLIENT);
        setContentView(page);
        findViewById(R.id.text_app_footer_license).setVisibility(View.INVISIBLE);
        loadAssetPage(LICENSE_HOME);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && page.canGoBack()) {
            page.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void loadAssetPage(String path) {
        try {
            final SoftReference<String> ref = cache.get(path);
            String data = null == ref ? null : ref.get();
            if (null == data) {
                final byte[] raw = bufferAssetPage(path);
                data = Uri.encode(new String(raw, CHARSET_UTF_8));
                cache.put(path, new SoftReference<String>(data));
            }
            page.loadData(data, contentTypeFor(path) + ";charset=" + CHARSET_UTF_8, null);
        } catch (Exception e) {
            String msg = "Could not load page from asset file '"
                    + path + "'";
            Log.e(LOG_TAG, msg, e);
            alert(msg);
            finish();
        }
    }

    protected byte[] bufferAssetPage(String path)
            throws IOException {
        InputStream in = null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buf = new byte[16384];
        try {
            in = getAssets().open(path);
            for (int read; 0 <= (read = in.read(buf)); )
                out.write(buf, 0, read);
            return out.toByteArray();
        } finally {
            if (null != in)
                try {
                    in.close();
                } catch (Exception ignored) {
                }
        }
    }

    protected String contentTypeFor(String path) {
        final int dotAt = path.lastIndexOf('.');
        final String ext = 0 > dotAt ? null : path.substring(dotAt + 1).toLowerCase();
        final String type = MIME_CONTENT_TYPES.get(ext);
        return null == type ? "text/html" : type;
    }

    protected static final String LICENSE_HOME = "NOTICE.html";
    protected static final Map<String, String> MIME_CONTENT_TYPES;

    static {
        Map<String, String> types = new HashMap<String, String>();
        types.put("html", "text/html");
        types.put("txt", "text/plain");
        MIME_CONTENT_TYPES = Collections.unmodifiableMap(types);
    }

    private final WebViewClient WEB_CLIENT = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            final Uri uri = Uri.parse(url);
            if (uri.isAbsolute() || null != uri.getAuthority()) {
                Intent call = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(call);
            } else {
                final String path = uri.getPath();
                loadAssetPage(path.startsWith("/") ? path.substring(1) : path);
            }
            return true;
        }
    };

    private static final String LOG_TAG = "LicensePages";
    private WebView page;
    private Map<String, SoftReference<String>> cache = new HashMap<String, SoftReference<String>>();
}
