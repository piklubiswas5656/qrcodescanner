package qr.code.scanner.android.digital;

import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;

public class Qrcoderesult extends AppCompatActivity {
    private String result;
    private Button copy, open, share;
    private TextView resulttext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcoderesult);
        init();
        onclick();

        if (!IsValidUrl(result)) {
            open.setVisibility(View.GONE);
        }
    }

    private void init() {
        result = getIntent().getStringExtra(Constant.qrresult);
        copy = findViewById(R.id.copy);
        open = findViewById(R.id.open);
        share = findViewById(R.id.share);
        resulttext = findViewById(R.id.qrresultText);
        resulttext.setText(result);
    }

    private void onclick() {
        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("textcopy", result);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getApplicationContext(), "Copy", Toast.LENGTH_SHORT).show();
            }
        });
        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(result));
                startActivity(intent);
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Share
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, result);
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, "Share by");
                startActivity(shareIntent);
            }
        });
    }


    private boolean IsValidUrl(String urlString) {
        try {
            URL url = new URL(urlString);
            return URLUtil.isValidUrl(urlString) && Patterns.WEB_URL.matcher(urlString).matches();
        } catch (MalformedURLException ignored) {
        }
        return false;
    }
}