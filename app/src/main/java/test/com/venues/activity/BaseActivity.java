package test.com.venues.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import test.com.venues.R;


public class BaseActivity extends AppCompatActivity {

    private Toolbar toolbar;
    ActionBar mActionBar;
    RelativeLayout mBackArrow;


    protected void setActionBar(String title) {
        // TODO Auto-generated method stub

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mActionBar = getSupportActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        mActionBar.setCustomView(getLayoutInflater().inflate(R.layout.custom_action_bar, null),
                new ActionBar.LayoutParams(
                        ActionBar.LayoutParams.MATCH_PARENT,
                        ActionBar.LayoutParams.MATCH_PARENT,
                        Gravity.CENTER));
       /* TextView mActionBarTitleTV = (TextView) mActionBar.getCustomView().findViewById(R.id.action_bar_title_2);
        mActionBarTitleTV.setText(title);
        mBackArrow = (RelativeLayout) mActionBar.getCustomView().findViewById(R.id.back_arrow_layout);
        mBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                setResult(44, i);
                finish();
                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
            }
        });*/
    }

}
