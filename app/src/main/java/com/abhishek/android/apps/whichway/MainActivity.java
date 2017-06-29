package com.abhishek.android.apps.whichway;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.pes.androidmaterialcolorpickerdialog.ColorPicker;
import com.startapp.android.publish.ads.splash.SplashConfig;
import com.startapp.android.publish.adsCommon.StartAppAd;
import com.startapp.android.publish.adsCommon.StartAppSDK;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static android.graphics.Color.rgb;

public class MainActivity extends AppCompatActivity implements OnClickListener {
    ImageView mRed, mGreen, mImageview_1, mImageview, mImageview_2; //Imageviews
    InterstitialAd mInterstitialAd;
    TextView mHeadCount, mTailCount, mSuggestion, mHeadOrTail;
    Button bFlip;
    static int sp_i = 0;
    Typeface tf1, tf2;
    static int tail_count = 0, head_count = 0, cnt = -1;
    PopupWindow pwindo;
    MediaPlayer mp, mp1, mp2;
    SharedPreferences appPreferences;
    boolean isAppInstalled = false;
    ActionBar actionBar;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRed = (ImageView) findViewById(R.id.red);
        mGreen = (ImageView) findViewById(R.id.green);
        mRed.setVisibility(View.INVISIBLE);
        mGreen.setVisibility(View.INVISIBLE);

        FirebaseAnalytics mFirebaseAnalytics;
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        Bundle params = new Bundle();
        params.putString("App_started", "MainActivity");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, params);

        StartAppSDK.init(this, "200770809", true);
        StartAppAd.showSplash(this, savedInstanceState,
                new SplashConfig()
                        .setTheme(SplashConfig.Theme.BLAZE)
                        .setAppName("Flip A Coin")
                        .setLogo(R.drawable.logo)
                        .setOrientation(SplashConfig.Orientation.PORTRAIT)
        );


        appPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        actionBar = getSupportActionBar();
        int background = rgb(228, 175, 255);
        int actionbr;
        editor = appPreferences.edit();
        isAppInstalled = appPreferences.getBoolean("isAppInstalled", false);
        if (isAppInstalled) {
            background = appPreferences.getInt("Background", 1);
            actionbr = appPreferences.getInt("Actionbar", 1);
        } else {
            actionbr = rgb(0, 0, 0);
            editor.putInt("Actionbar", rgb(0, 0, 0));
            editor.putInt("Background", rgb(228, 175, 255));
            editor.apply();
        }

        SetColor(background, actionbr);
        editor.putBoolean("isAppInstalled", true);
        editor.apply();

        ImageView iv4 = (ImageView) findViewById(R.id.imageView1);
        iv4.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        sp_i++;
                    }
                });

        ImageView io = (ImageView) findViewById(R.id.po_image1);
        io.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Uri uri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
                                new Extras().rate(uri, view.getContext());
                            }
                        }, 1500);
                        mp2 = MediaPlayer.create(getApplicationContext(), R.raw.splash);
                        mp2.start();
                    }
                });

        iv4.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (sp_i == 3) {
                    LayoutInflater inflater = (LayoutInflater) v.getContext()
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View layout = inflater.inflate(R.layout.screen_popup,
                            (ViewGroup) findViewById(R.id.popup_element));
                    Display display = getWindowManager().getDefaultDisplay();
                    int width = display.getWidth() * 90 / 100;
                    int height = display.getHeight() * 65 / 100;
                    pwindo = new PopupWindow(layout, width, height, true);
                    pwindo.showAtLocation(layout, Gravity.CENTER, 0, 0);
                    Button btnClosePopup = (Button) layout.findViewById(R.id.btn_close_popup);
                    btnClosePopup.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            pwindo.dismiss();
                        }
                    });
                }
                return true;
            }
        });

        MobileAds.initialize(getApplicationContext(), "ca-app-pub-7383233719473844~7535715614");

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        String font1 = "fonts/3Dumb.ttf";
        String font2 = "fonts/BarkingCatDEMO.otf";
        tf1 = Typeface.createFromAsset(getAssets(), font1);
        tf2 = Typeface.createFromAsset(getAssets(), font2);

        mHeadCount = (TextView) findViewById(R.id.headCount);
        mTailCount = (TextView) findViewById(R.id.tailCount);

        registerForContextMenu(mHeadCount);
        registerForContextMenu(mTailCount);

        mHeadCount.setTypeface(tf1);
        mTailCount.setTypeface(tf1);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-7383233719473844/1489182019");
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
            }
        });
        requestNewInterstitial();

        mSuggestion = (TextView) findViewById(R.id.lbl_sugg);
        mImageview = (ImageView) findViewById(R.id.imageView2);
        mImageview_1 = (ImageView) findViewById(R.id.imageView1);
        mImageview_2 = (ImageView) findViewById(R.id.imageView);
        mHeadOrTail = (TextView) findViewById(R.id.H_T);
        mSuggestion.setTypeface(tf2, Typeface.BOLD);

        bFlip = (Button) findViewById(R.id.btn_flip);
        String font3 = "fonts/VintageOne.ttf";
        bFlip.setTypeface(Typeface.createFromAsset(getAssets(), font3));
        mHeadOrTail.setTypeface(Typeface.createFromAsset(getAssets(), font3));
        bFlip.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

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
                Button okColor = (Button) cp.findViewById(R.id.okColorButton);
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
                mHeadCount.setText("Head:" + head_count);
                mTailCount.setText("Tail :" + tail_count);
                editor.apply();
                break;
            case R.id.support:
                mInterstitialAd.show();
                StartAppAd.showAd(this);
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
        RelativeLayout l = (RelativeLayout) findViewById(R.id.activity_main);
        l.setBackgroundColor(selectedColorRGB);
        ColorDrawable colorDrawable = new ColorDrawable(color);
        actionBar.setBackgroundDrawable(colorDrawable);
    }

    public void onClick(View view) {

        mp = MediaPlayer.create(this, R.raw.coinflip);
        mp1 = MediaPlayer.create(this, R.raw.coin_tak02);

        if (bFlip.getText().toString().equals("Try Again")) {
            mRed.setVisibility(View.INVISIBLE);
            mGreen.setVisibility(View.INVISIBLE);
            mSuggestion.setVisibility(View.INVISIBLE);
            mHeadOrTail.setVisibility(View.INVISIBLE);
        }

        mp.start();
        bFlip.setVisibility(View.INVISIBLE);
        mImageview_1.setVisibility(View.INVISIBLE);
        mImageview_2.setVisibility(View.INVISIBLE);

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
                                       mImageview.startAnimation(s);
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
                    @Override
                    public void run() {
                        mSuggestion.setVisibility(View.VISIBLE);
                        FindPercent fp = new FindPercent();
                        final int per = fp.percent();
                        mImageview.setVisibility(View.INVISIBLE);
                        String suggestion = null, hrt = null, color = null;
                        switch (per) {
                            case 0:
                                suggestion = "Master, Good to go";
                                mGreen.setVisibility(View.VISIBLE);
                                head_count++;
                                mImageview_1.setVisibility(View.VISIBLE);
                                hrt = "Head";
                                color = "#03fd53";
                                break;
                            case 1:
                                mRed.setVisibility(View.VISIBLE);
                                suggestion = "Master, Think of an Alternative";
                                tail_count++;
                                mImageview_2.setVisibility(View.VISIBLE);
                                hrt = "Tail";
                                color = "#d84416";
                                break;
                        }
                        mHeadOrTail.setTextColor(Color.parseColor(color));
                        mHeadOrTail.setText(hrt);
                        mSuggestion.setText(suggestion);
                        cnt++;
                        mHeadOrTail.setVisibility(View.VISIBLE);
                        mp1.start();
                        mHeadCount.setText("Head:" + head_count);
                        mTailCount.setText("Tail :" + tail_count);

                        bFlip.setVisibility(View.VISIBLE);
                        bFlip.setText("Try Again");

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
        StartAppAd.onBackPressed(this);
        super.onBackPressed();
    }
}