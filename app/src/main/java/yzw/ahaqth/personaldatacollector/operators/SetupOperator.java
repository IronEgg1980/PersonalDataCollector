package yzw.ahaqth.personaldatacollector.operators;

import android.text.TextUtils;
import android.util.Log;

import org.litepal.LitePal;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;

import yzw.ahaqth.personaldatacollector.modules.Setup;
import yzw.ahaqth.personaldatacollector.tools.EncryptAndDecrypt;

public final class SetupOperator {
    private static String TAG = "殷宗旺";
    private static String MY_PHONEID = "";
    private static final String ID = "MHUYOkVc";
    private static final String LAST_VERSION = "ce2UqiXEtG";
    private static final String LAST_BACKUP_TIME = "SEZx18BiR";
    private static final String PWD = "HeUbhrCtdiu";
    private static final String GESTURE_PWD = "jfZJhU9oP";
    private static final String USER_NAME = "RneodSSWTR";
    private static final String INPUTPWDMODE = "uDKXrwEOdY7";
    private static final String SEED = "PTsYzb";


    public final static String KEY = "IXj8caaQHmUI";
    public final static String VALUE = "2VulCdJH3b";

    // 获取唯一ID，恢复出厂设置会改变
    private static String getUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replace("-", "");
    }

    private static <T> T getSetupValue(String key, T defaultValue) {
        Setup appSetup = findOne(key);
        if (appSetup == null) {
            appSetup = new Setup(key, String.valueOf(defaultValue));
            appSetup.save();
            return defaultValue;
        }
        String value = appSetup.getValue();
        if (defaultValue instanceof Integer)
            return (T) Integer.valueOf(value);
        if (defaultValue instanceof Long)
            return (T) Long.valueOf(value);
        if (defaultValue instanceof Boolean)
            return (T) Boolean.valueOf(value);
        if (defaultValue instanceof Float)
            return (T) Float.valueOf(value);
        return (T) value;
    }

    private static void saveSetupValue(String key, Object value) {
        String stringValue = String.valueOf(value);
        Setup appSetup = findOne(key);
        if (appSetup == null) {
            appSetup = new Setup(key, stringValue);
        } else {
            appSetup.setValue(stringValue);
        }
        appSetup.save();
    }

    public static int getCount() {
        return LitePal.count(Setup.class);
    }

    public static Setup findOne(String key) {
        return LitePal.where("key = ?", EncryptAndDecrypt.encryptToString(key)).findFirst(Setup.class);
    }

    public static List<Setup> findAll() {
        return LitePal.findAll(Setup.class);
    }

    public static void saveAll(List<Setup> list) {
        LitePal.saveAll(list);
    }

    public static String getPhoneId() {
        if (TextUtils.isEmpty(MY_PHONEID)) {
            MY_PHONEID = getSetupValue(ID, getUUID());
        }
        return MY_PHONEID;
    }

    public static void setPhoneId(String id) {
        saveSetupValue(ID, id);
    }

    public static long getLastAppVersion() {
        return getSetupValue(LAST_VERSION, 0L);
    }

    public static void setLastAppVersion(long version) {
        saveSetupValue(LAST_VERSION, version);
    }

    public static String getUserName() {
        return getSetupValue(USER_NAME, "");
    }

    public static void saveUserName(String username) {
        saveSetupValue(USER_NAME, username);
    }

    public static String getPassWord() {
        return getSetupValue(PWD, "");
    }

    public static String getRawPassWordInSetupList(List<Setup> list) {
        for (Setup setup : list) {
            if (PWD.equals(setup.getRawKey())) {
                return setup.getRawValue();
            }
        }
        return "";
    }

    public static String getRawPassWord() {
        Setup setup = findOne(PWD);
        return setup == null ? "" : setup.getRawValue();
    }

    public static void savePassWord(String pwd) {
        saveSetupValue(PWD, pwd);
    }

    public static String getGesturePassWord() {
        return getSetupValue(GESTURE_PWD, "");
    }

    public static void saveGesturePassWord(String gesturePWD) {
        saveSetupValue(GESTURE_PWD, gesturePWD);
    }

    public static void setInputPassWordMode(int mode) {
        saveSetupValue(INPUTPWDMODE, mode);
    }

    public static int getInputPassWordMode() {
        // 1 - 输入密码；2 - 手势密码
        return getSetupValue(INPUTPWDMODE, 1);
    }

    public static long getLastBackupTime(){
        return getSetupValue(LAST_BACKUP_TIME,-1L);
    }

    public static void setBackupTime(long time){
        saveSetupValue(LAST_BACKUP_TIME,time);
    }

    public static String getSeed() throws NullPointerException {
        Setup appSetup = LitePal.where("key = ?", SEED).findFirst(Setup.class);
        if (appSetup == null) {
            return "";
        }
        return appSetup.getRawValue();
    }

    public static boolean initialSeed() {
        try {
            Setup appSetup = LitePal.where("key = ?", SEED).findFirst(Setup.class);
            if (appSetup == null) {
                appSetup = new Setup();
                appSetup.setRawKey(SEED);
                appSetup.setRawValue(EncryptAndDecrypt.getRawKey("yDxRXJxA"));
                appSetup.save();
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            Log.d(TAG, "initialSeed: " + e.getMessage());
            return false;
        }
        return true;
    }
}
