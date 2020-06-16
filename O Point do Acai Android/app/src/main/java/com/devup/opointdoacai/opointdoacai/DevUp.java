package com.devup.opointdoacai.opointdoacai;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;
import com.vansuita.materialabout.builder.AboutBuilder;
import com.vansuita.materialabout.views.AboutView;

public class DevUp extends AppCompatActivity {

    private RelativeLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev_up);

        AboutView view = AboutBuilder.with(this)
                .setPhoto(R.drawable.devup)
                .setCover(R.drawable.cover)
                .setIconColor(Color.parseColor("#646464"))
                .setName("Dev. Up Aplicativos")
                .setSubTitle("Luis C. Lima - Mobile Developer")
                .setBrief("Desenvolvimento de Aplicações Mobile e Web. Você imagina, nós tornamos realidade.")
                .setAppIcon(R.mipmap.ic_launcher)
                .setAppName(R.string.app_name)
                .addGooglePlayStoreLink("8002078663318221363")
                .addGitHubLink("devuptechnology")
                .addBitbucketLink("devuptechnology")
                .addFacebookLink("devuptechnology")
                .addTwitterLink("devuptechnology")
                .addInstagramLink("devuptechnology")
                .addGooglePlusLink("+devuptechnology")
                .addYoutubeChannelLink("devuptechnology")
                .addDribbbleLink("luisoberhofer")
                .addLinkedInLink("luis-carlos-lima-dos-santos-developer")
                .addEmailLink("luiscarlos_lima@outlook.com.br")
                .addWhatsappLink("Luis Carlos", "+5519974147236")
                .addSkypeLink("devuptechnology")
                .addGoogleLink("devuptechnology")
                .addAndroidLink("devuptechnology")
                .addWebsiteLink("devuptechnology")
                .addFiveStarsAction()
                .addMoreFromMeAction("Vansuita")
                .setVersionNameAsAppSubTitle()
                .addShareAction(R.string.app_name)
                .addUpdateAction()
                .setActionsColumnsCount(2)
                .addFeedbackAction("luiscarlos_lima@outlook.com.br")
                .addPrivacyPolicyAction("https://opointdoacai.home.blog/politica-de-privacidade/")
                .addIntroduceAction((Intent) null)
                .addHelpAction((Intent) null)
                .addChangeLogAction((Intent) null)
                .addRemoveAdsAction((Intent) null)
                .addDonateAction((Intent) null)
                .setWrapScrollView(true)
                .setShowAsCard(true)
                .build();

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);

        addContentView(view, params);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
