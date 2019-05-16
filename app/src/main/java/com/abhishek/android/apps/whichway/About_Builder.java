package com.abhishek.android.apps.whichway;

import android.app.Activity;
import android.widget.FrameLayout;

import com.vansuita.materialabout.builder.AboutBuilder;

public class About_Builder {

    private Activity activity;

    public About_Builder(Activity activity) {
        this.activity = activity;
    }

    public void loadAbout() {
        final FrameLayout flHolder = activity.findViewById(R.id.aboutA);
        String url = "http://play.google.com/store/apps/details?id=" + activity.getPackageName();
        flHolder.addView(
                AboutBuilder.with(activity)
                        .setAppIcon(R.drawable.logo)
                        .setAppName(R.string.app_name)
                        .setPhoto(R.drawable.profilepic)
                        .setCover(R.mipmap.profile_cover)
                        .setLinksAnimated(true)
                        .setDividerDashGap(13)
                        .setName("Abhishek U Bhat")
                        .setSubTitle("Android Developer")
                        .setLinksColumnsCount(4)
                        .setBrief(R.string.about)
                        .addGooglePlayStoreLink("6398180878838119598")
                        //.addGitHubLink("jrvansuita")
                        //.addBitbucketLink("jrvansuita")
                        .addFacebookLink("abhishek.ubhat?fref=ts")
                        .addTwitterLink("Blackhat_96")
                        .addInstagramLink("abhishek.ubhat/")
                        .addGooglePlusLink("115614625101193369027")
                        //.addYoutubeChannelLink("103588649850838411440")
                        //.addDribbleLink("user")
                        //.addLinkedinLink("abhishek-bhat-a49558126")
                        .addEmailLink("theloneintrovert@gmail.com")
                        .addWhatsappLink("Abhishek U Bhat", "+918197006829")
                        //.addSkypeLink("user")
                        //.addGoogleLink("user")
                        //.addAndroidLink("user")
                        //.addWebsiteLink("site")
                        .addFiveStarsAction(activity.getPackageName())
                        .addMoreFromMeAction("Abhishek U Bhat")
                        //.setVersionAsAppTitle()
                        .addShareAction("Check out this new App ", url)
                        //.addUpdateAction()
                        .setActionsColumnsCount(2)
                        .addFeedbackAction("theloneintrovert@gmail.com")
                        //.addIntroduceAction((Intent) null)
                        //.addHelpAction((Intent) null)
                        //.addChangeLogAction((Intent) null)
                        //.addRemoveAdsAction((Intent) null)
                        //.addDonateAction((Intent) null)
                        .setWrapScrollView(true)
                        .build());
    }
}

