/*
 Copyright (c) 2012 Roman Truba

 Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 permit persons to whom the Software is furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all copies or substantial
 portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH
 THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package test.com.venues.gallery;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.Surface.OutOfResourcesException;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import test.com.venues.R;

public class UrlTouchImageView extends RelativeLayout {
    protected LinearLayout mLinearLayout;
    protected ProgressBar mProgressBar;
    protected TouchImageView mImageView;
    protected Context mContext;

    public UrlTouchImageView(Context ctx) {
        super(ctx);
        mContext = ctx;
        init();

    }

    public UrlTouchImageView(Context ctx, AttributeSet attrs) {
        super(ctx, attrs);
        mContext = ctx;
        init();
    }

    public TouchImageView getImageView() {
        return mImageView;
    }

    @SuppressWarnings("deprecation")
    protected void init() {
        mImageView = new TouchImageView(mContext);
        LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
        mImageView.setLayoutParams(params);
        this.addView(mImageView);
        mImageView.setVisibility(GONE);


        mLinearLayout = new LinearLayout(mContext);
        // LayoutParams params1 = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
        params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        mLinearLayout.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        mLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        int rl = (int) (5);
        mLinearLayout.setPadding(rl, rl, rl, rl);
        mLinearLayout.setLayoutParams(params);
        //

		/*Typeface regularfont = Typeface.createFromAsset(mContext.getAssets(),
                "Roboto-Regular.ttf");*/
        TextView mTextView = new TextView(mContext);
        // LayoutParams params2= new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_VERTICAL);
        mTextView.setGravity(Gravity.CENTER_VERTICAL);
        mTextView.setText("Loading");
        mTextView.setPadding(2 * rl, 0, 0, 0);
        //mTextView.setTextSize(mContext.getResources().getDimension(R.dimen.textsize_small_13));
        mTextView.setTextSize(14);
        mTextView.setTextColor(mContext.getResources().getColor(R.color.black));
        //mTextView.setTypeface(regularfont);
        mTextView.setLayoutParams(params);

        mProgressBar = new ProgressBar(mContext, null, android.R.attr.progressBarStyleSmall);
        // params=new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params = new LayoutParams((int) (50), (int) (50));
        params.addRule(RelativeLayout.CENTER_VERTICAL);
        mProgressBar.setIndeterminateDrawable(mContext.getResources().getDrawable(R.drawable.ic_launcher_foreground));
        mProgressBar.setLayoutParams(params);

        mLinearLayout.addView(mProgressBar);
        mLinearLayout.addView(mTextView);


        this.addView(mLinearLayout);
    }

    public void setUrl(String imageUrl) {
        //AQuery androidAQuery=new AQuery(mContext);
        //androidAQuery.id(mImageView).image(imageUrl, true, true, 1000, R.drawable.share);
        new ImageLoadTask().execute(imageUrl);
        //		Picasso.with(mContext).load(imageUrl).into(mImageView);
        //		mImageView.setVisibility(VISIBLE);
        //		mLinearLayout.setVisibility(GONE);
        //		mImageView.setImageBitmap(
        //			decodeSampledBitmapFromResource(getResources(), R.id.ic, 100, 100));
        //		mImageView.setVisibility(VISIBLE);
        //		mLinearLayout.setVisibility(GONE);
		/*androidAQuery.ajax(imageUrl, Bitmap.class,0,new AjaxCallback<Bitmap>(){
			@Override
			public void callback(String url, Bitmap object, AjaxStatus status) {
				super.callback(url, object, status);
				mImageView.setImageBitmap(object);
				//You will get Bitmap from object.
			}
		});*/
    }

    public void setScaleType(ScaleType scaleType) {
        mImageView.setScaleType(scaleType);
    }

    //No caching load
    public class ImageLoadTask extends AsyncTask<String, Integer, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... strings) {
            String url = strings[0];
            Bitmap bm = null;
            try {
                try {
                    try {
                        URL aURL = new URL(url);
                        URLConnection conn = aURL.openConnection();
                        conn.connect();
                        InputStream is = conn.getInputStream();
                        int totalLen = conn.getContentLength();
                        InputStreamWrapper bis = new InputStreamWrapper(is, 10000, totalLen);
                        bis.setProgressListener(new InputStreamWrapper.InputStreamProgressListener() {
                            @Override
                            public void onProgress(float progressValue, long bytesLoaded,
                                                   long bytesTotal) {
                                publishProgress((int) (progressValue * 100));
                            }
                        });
                        //bm = BitmapFactory.decodeStream(bis);

                        //	BitmapFactory.Options options = new BitmapFactory.Options();
                        //bm = BitmapFactory.decodeStream(bis, null, options);
                        //options.inSampleSize = 2;

						/*int scale;
						System.out.println("width "+options.outWidth);
						System.out.println("height "+options.outHeight);
						if (options.outHeight > 2000 || options.outWidth > 2000) {
							scale = (int) Math.pow(2, (int) Math.ceil(Math.log(4000 / 
									(double) Math.max(options.outHeight, options.outWidth)) / Math.log(0.5)));
						}else{
							scale = 1;
						}*/
                        BitmapFactory.Options options2 = new BitmapFactory.Options();
                        options2.inSampleSize = 2;
                        bm = BitmapFactory.decodeStream(is, null, options2);

                        bis.close();
                        is.close();

                    } catch (OutOfResourcesException e) {
                        // TODO: handle exception
                        e.printStackTrace();
                    }
                } catch (RuntimeException e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return bm;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            try {
                try {
                    try {
                        //System.out.println("bitmap "+bitmap);
                        if (bitmap == null) {
                            mImageView.setScaleType(ScaleType.CENTER);
                            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_foreground);
                            mImageView.setImageBitmap(bitmap);
                        } else {
                            mImageView.setScaleType(ScaleType.MATRIX);
                            mImageView.setImageBitmap(bitmap);
                        }
                        mImageView.setVisibility(VISIBLE);
                        mLinearLayout.setVisibility(GONE);
                    } catch (OutOfResourcesException e) {
                        // TODO: handle exception
                        e.printStackTrace();
                    }
                } catch (RuntimeException e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            mProgressBar.setProgress(values[0]);
        }
    }
}
