package test.com.venues.activity;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import test.com.venues.R;
import test.com.venues.gallery.UrlTouchImageView;
import test.com.venues.model.Venues;


public class VenueDetailsActivity extends BaseActivity implements View.OnClickListener {
    private TextView mTVTitle, mTVPhone, mTVTotalRatings, mTVRating, mTVADdress, mTVNameonFB, mTVTwitter;
    private Button mBtnCheckWebsite, mBtnCheckPhotos;
    private String mTitle, mPhone, mTotalRatings, mRatings, mAddress, mNameonFB, mNameonTwitter, mPhotos, mURL, mContacts;
    private Dialog mDialog;
    boolean mIsVerified;
    private ArrayList<Venues> mPhotosList = null;
    private ImageView mIVVerified;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venue_details);
        setUpIDs();
        getDataFromIntent();
        setTheContentToViews();
    }

    private void setTheContentToViews() {
        mTVTitle.setText(mTitle);
        mTVPhone.setText("Phone: " + mPhone);
        mTVTotalRatings.setText("Total ratings: " + mTotalRatings);
        mTVRating.setText("Rating: " + mRatings);
        mTVADdress.setText(mAddress);
        mTVNameonFB.setText("Name on Facebook: " + mNameonFB);
        mTVTwitter.setText("Name on Twitter: " + mNameonTwitter);

        if(mPhotosList != null && mPhotosList.size() > 0) {
            mBtnCheckPhotos.setVisibility(View.VISIBLE);
        }else{
            mBtnCheckPhotos.setVisibility(View.GONE);
        }

        TextView tvVerified = (TextView) findViewById(R.id.tv_verified);
        if(mIsVerified){
            mIVVerified.setBackgroundResource(R.drawable.verified);
            tvVerified.setText("Verified");
        }else{
            mIVVerified.setBackgroundResource(R.drawable.not_verified);
            tvVerified.setText("Not verified");
        }
    }

    private void getDataFromIntent() {
        try {
            Bundle bundle = getIntent().getExtras();
            mTitle = bundle.getString("KEY_TITLE");
            mPhone = bundle.getString("KEY_PHONE");
            mTotalRatings = bundle.getString("KEY_TOTAL_RATINGS");
            mRatings = bundle.getString("KEY_RATING");
            mAddress = bundle.getString("KEY_ADDRES");
            mContacts = bundle.getString("KEY_CONTACT");
            mNameonFB = bundle.getString("KEY_FB");
            mNameonTwitter = bundle.getString("KEY_TWITTER");
            mPhotos = bundle.getString("KEY_PHOTOS");
            mURL = bundle.getString("KEY_URL");
            mIsVerified = bundle.getBoolean("KEY_VERIFIED");


            mPhotosList = null;
            mPhotosList = new ArrayList<>();

            System.out.println("mPhotos " + mPhotos);
            if (!TextUtils.isEmpty(mPhotos) && !mPhotos.equals("null")) {
                JSONArray jsonPhotos = new JSONArray(mPhotos);
                for (int i = 0; i < jsonPhotos.length(); i++) {
                    Venues been = new Venues();
                    JSONObject jsonOBjPhotos = jsonPhotos.getJSONObject(i);
                    been.setPhotoUrl(jsonOBjPhotos.getString("url"));
                    mPhotosList.add(been);
                }

            }else{
                mBtnCheckPhotos.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(mContacts) && !mContacts.equals("null")) {
                JSONArray jsonArrayContacts = new JSONArray(mContacts);
                JSONObject jsonOBjContacts = jsonArrayContacts.getJSONObject(0);
                mPhone = jsonOBjContacts.getString("phone");
                mNameonFB = jsonOBjContacts.getString("facebookName");
                mNameonTwitter = jsonOBjContacts.getString("twitter");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setUpIDs() {

        TextView mActionBarTitleTV = (TextView) findViewById(R.id.action_bar_title_2);
        mActionBarTitleTV.setText("Details");
        mBackArrow = (RelativeLayout) findViewById(R.id.back_arrow_layout);
        mBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                setResult(44, i);
                finish();
            }
        });

        mTVTitle = (TextView) findViewById(R.id.tv_title);
        mTVPhone = (TextView) findViewById(R.id.tv_phone);
        mTVTotalRatings = (TextView) findViewById(R.id.tv_total_ratings);
        mTVRating = (TextView) findViewById(R.id.rating);
        mTVADdress = (TextView) findViewById(R.id.tv_address);
        mTVNameonFB = (TextView) findViewById(R.id.tv_name_on_fb);
        mTVTwitter = (TextView) findViewById(R.id.tv_name_on_twitter);

        mBtnCheckWebsite = (Button) findViewById(R.id.btn_check_website);
        mBtnCheckPhotos = (Button) findViewById(R.id.btn_check_photos);

        mIVVerified = (ImageView) findViewById(R.id.iv_verified_or_not);

        mBtnCheckWebsite.setOnClickListener(this);
        mBtnCheckPhotos.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        try {
            if (view == mBtnCheckPhotos) {
                if(mPhotosList != null && mPhotosList.size() > 0) {
                    mDialog = new Dialog(VenueDetailsActivity.this, R.style.Theme_Dialog);
                    WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
                    lp.dimAmount = 5.0f;
                    mDialog.getWindow().setAttributes(lp);
                    mDialog.getWindow().addFlags(
                            WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                    mDialog.setContentView(R.layout.dialog_big_image);
                    mDialog.setCancelable(true);
                    UrlTouchImageView mDialogHeading = (UrlTouchImageView) mDialog.findViewById(R.id.uriImageview);
                    mDialogHeading.setUrl(mPhotosList.get(0).getPhotoUrl());
                    ImageView imagClose = (ImageView) mDialog.findViewById(R.id.dialog_close_iv);
                    imagClose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mDialog.dismiss();
                        }
                    });
                    mDialog.show();
                }
            } else if (view == mBtnCheckWebsite) {
                if (!TextUtils.isEmpty(mURL) && !mURL.equals("null")) {
                    Intent i = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(mURL));
                    startActivity(i);
                } else {
                    Toast.makeText(getApplicationContext(), "No url found to show details", Toast.LENGTH_SHORT).show();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}