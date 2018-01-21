package test.com.venues.Preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;



public class PreferencesManager {

    private static final String KEY_FAVED_ID = "KeyFaved_ID";
    private static PreferencesManager sInstance;
    private static SharedPreferences mPref =  null;
    static String favedid = "";

    private PreferencesManager(Context context) {
        mPref = PreferenceManager.getDefaultSharedPreferences(context);
                //getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized void initializeInstance(Context context) {
        if (sInstance == null) {
            sInstance = new PreferencesManager(context);
        }
    }

    public static synchronized PreferencesManager getInstance() {
        if (sInstance == null) {
            throw new IllegalStateException(PreferencesManager.class.getSimpleName() +
                    " is not initialized, call initializeInstance(..) method first.");
        }
        return sInstance;
    }



    public void setFavedID(String value) {
        mPref.edit()
                .putString(KEY_FAVED_ID, value)
                .commit();
    }

    public String getFavedID() {
        return mPref.getString(KEY_FAVED_ID, favedid);
    }

    public void removeCoupon(String key) {
        mPref.edit()
                .remove(key)
                .commit();
    }

    public boolean clear() {
        return mPref.edit()
                .clear()
                .commit();
    }
}
