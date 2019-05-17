package com.abhishek.android.apps.whichway;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.pes.androidmaterialcolorpickerdialog.ColorPicker;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import info.hoang8f.widget.FButton;

import static android.graphics.Color.rgb;

public class MainActivity extends AppCompatActivity implements OnClickListener {
    static int sp_i = 0;
    static int tail_count = 0, head_count = 0, cnt = -1;
    ImageView red, green, iv1, iv, iv2;
    InterstitialAd mInterstitialAd;
    TextView hc, tc, sug, h_t;
    FButton flip;
    Typeface tf1, tf2;
    MediaPlayer mp, mp1, mp2;
    SharedPreferences appPreferences;
    boolean isAppInstalled = false;
    ActionBar actionBar;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        red = findViewById(R.id.red);
        green = findViewById(R.id.green);
        red.setVisibility(View.INVISIBLE);
        green.setVisibility(View.INVISIBLE);

        FirebaseAnalytics mFirebaseAnalytics;
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        Bundle params = new Bundle();
        params.putString("AppStarted", "MainActivity");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, params);

        appPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        actionBar = getSupportActionBar();
        int background = rgb(228, 175, 255);
        int actionBar;
        editor = appPreferences.edit();
        isAppInstalled = appPreferences.getBoolean("isAppInstalled", false);
        if (isAppInstalled) {
            background = appPreferences.getInt("Background", 1);
            actionBar = appPreferences.getInt("Actionbar", 1);
        } else {
            actionBar = rgb(0, 0, 0);
            editor.putInt("Actionbar", rgb(0, 0, 0));
            editor.putInt("Background", rgb(228, 175, 255));
            editor.apply();
        }

        SetColor(background, actionBar);
        editor.putBoolean("isAppInstalled", true);
        editor.apply();

        ImageView iv4 = findViewById(R.id.imageView1);
        iv4.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        sp_i++;
                    }
                });

        ImageView io = findViewById(R.id.po_image1);
        io.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Uri uri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    new Extras().rate(uri, view.getContext());
                                }
                            }
                        }, 1500);
                        mp2 = MediaPlayer.create(getApplicationContext(), R.raw.splash);
                        mp2.start();
                    }
                });

        MobileAds.initialize(getApplicationContext(), getString(R.string.admob_app_id));

        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        String font1 = "fonts/3Dumb.ttf";
        String font2 = "fonts/BarkingCatDEMO.otf";
        tf1 = Typeface.createFromAsset(getAssets(), font1);
        tf2 = Typeface.createFromAsset(getAssets(), font2);

        hc = findViewById(R.id.headCount);
        tc = findViewById(R.id.tailCount);

        registerForContextMenu(hc);
        registerForContextMenu(tc);

        hc.setTypeface(tf1);
        tc.setTypeface(tf1);


        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-7383233719473844/1489182019");
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
            }
        });
        requestNewInterstitial();

        sug = findViewById(R.id.lbl_sugg);
        iv = findViewById(R.id.imageView2);
        iv1 = iv4;//findViewById(R.id.imageView1);
        iv2 = findViewById(R.id.imageView);
        h_t = findViewById(R.id.H_T);
        sug.setTypeface(tf2, Typeface.BOLD);

        flip = findViewById(R.id.btn_flip);
        flip.setButtonColor(getResources().getColor(R.color.btnColor));
        flip.setShadowColor(getResources().getColor(R.color.shadow));
        flip.setShadowEnabled(true);
        flip.setShadowHeight(5);
        flip.setCornerRadius(10);

        String font3 = "fonts/VintageOne.ttf";
        flip.setTypeface(Typeface.createFromAsset(getAssets(), font3));
        h_t.setTypeface(Typeface.createFromAsset(getAssets(), font3));

        flip.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_share:
                new Extras().share_app(this);
                break;
            case R.id.menu_about:
                Intent about = new Intent(getBaseContext(), AboutDev.class);
                startActivity(about);
                return true;
            case R.id.Theme:
                final ColorPicker cp = new ColorPicker(MainActivity.this, new Random().nextInt(253) + 1, new Random().nextInt(253) + 1, new Random().nextInt(253) + 1);
                cp.show();
                Button okColor = cp.findViewById(R.id.okColorButton);
                okColor.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int selectedColorRGB, color = 0;
                        Random random = new Random();
                        int choice = random.nextInt(3) + 1;
                        switch (choice) {
                            case 1:
                                color = rgb(cp.getRed(), 0, 0);
                                break;
                            case 2:
                                color = rgb(0, cp.getGreen(), 0);
                                break;
                            case 3:
                                color = rgb(0, 0, cp.getBlue());
                                break;
                        }
                        selectedColorRGB = cp.getColor();
                        SetColor(selectedColorRGB, color);
                        editor.putInt("Actionbar", color);
                        editor.putInt("Background", selectedColorRGB);
                        editor.apply();
                        cp.dismiss();
                    }
                });
                return true;
            case R.id.reset:
                editor.putInt("Actionbar", rgb(0, 0, 0));
                editor.putInt("Background", rgb(228, 175, 255));
                SetColor(rgb(228, 175, 255), rgb(0, 0, 0));
                head_count = 0;
                tail_count = 0;
                hc.setText("Head:" + head_count);
                tc.setText("Tail :" + tail_count);
                editor.apply();
                break;
            case R.id.privacy_policy:
                Intent privacyPolicy = new Intent(MainActivity.this, PrivacyPolicy.class);
                startActivity(privacyPolicy);
                break;
            case R.id.support:
                mInterstitialAd.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }


    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mInterstitialAd.loadAd(adRequest);
    }


    void SetColor(int selectedColorRGB, int color) {
        RelativeLayout l = findViewById(R.id.activity_main);
        l.setBackgroundColor(selectedColorRGB);
        ColorDrawable colorDrawable = new ColorDrawable(color);
        actionBar.setBackgroundDrawable(colorDrawable);
    }

    public void onClick(View view) {

        mp = MediaPlayer.create(this, R.raw.coinflip);
        mp1 = MediaPlayer.create(this, R.raw.coin_tak02);

        if (flip.getText().toString().equals("Try Again")) {
            red.setVisibility(View.INVISIBLE);
            green.setVisibility(View.INVISIBLE);
            sug.setVisibility(View.INVISIBLE);
            h_t.setVisibility(View.INVISIBLE);
        }

        mp.start();
        flip.setVisibility(View.INVISIBLE);
        iv1.setVisibility(View.INVISIBLE);
        iv2.setVisibility(View.INVISIBLE);

        view.setDrawingCacheEnabled(true);
        final Handler myHandler = new Handler();
        (new Thread(new Runnable() {
            public void run() {
                myHandler.post(new Runnable() {
                                   @Override
                                   public void run() {

                                       AnimationSet s;
                                       Animation anim1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.flip_h);
                                       Animation anim2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoomin);
                                       s = new AnimationSet(true);
                                       s.addAnimation(anim1);
                                       s.addAnimation(anim2);
                                       iv.startAnimation(s);
                                   }
                               }
                );
            }
        })).start();


        Timer timer = new Timer();

        TimerTask delayedThreadStartTask = new TimerTask() {
            @Override
            public void run() {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void run() {
                        sug.setVisibility(View.VISIBLE);
                        FindPercent fp = new FindPercent();
                        final int per = fp.percent();
                        iv.setVisibility(View.INVISIBLE);
                        String suggestion = null, hrt = null, color = null;
                        switch (per) {
                            case 0:
                                suggestion = "Master, Good to go";
                                green.setVisibility(View.VISIBLE);
                                head_count++;
                                iv1.setVisibility(View.VISIBLE);
                                hrt = "Head";
                                color = "#03fd53";
                                break;
                            case 1:
                                red.setVisibility(View.VISIBLE);
                                suggestion = "Master, Think of an Alternative";
                                tail_count++;
                                iv2.setVisibility(View.VISIBLE);
                                hrt = "Tail";
                                color = "#d84416";
                                break;
                        }
                        h_t.setTextColor(Color.parseColor(color));
                        h_t.setText(hrt);
                        sug.setText(suggestion);
                        cnt++;
                        h_t.setVisibility(View.VISIBLE);
                        mp1.start();
                        hc.setText("Head:" + head_count);
                        tc.setText("Tail :" + tail_count);

                        flip.setVisibility(View.VISIBLE);
                        flip.setText("Try Again");

                        if (cnt % 3 == 0 && cnt != 0)
                            if (mInterstitialAd.isLoaded()) {
                                mInterstitialAd.show();
                            }
                    }
                });
            }
        };
        timer.schedule(delayedThreadStartTask, 3100);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}