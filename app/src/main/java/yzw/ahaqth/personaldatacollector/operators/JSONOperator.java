package yzw.ahaqth.personaldatacollector.operators;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import yzw.ahaqth.personaldatacollector.modules.AccountRecord;
import yzw.ahaqth.personaldatacollector.modules.ImageRecord;
import yzw.ahaqth.personaldatacollector.modules.RecordGroup;
import yzw.ahaqth.personaldatacollector.modules.Setup;
import yzw.ahaqth.personaldatacollector.modules.TextRecord;
import yzw.ahaqth.personaldatacollector.tools.EncryptAndDecrypt;

public class JSONOperator {
    private static final String RECORD_GROUP_CLASS = "UTNwsrwt7E1l";
    private static final String ACCOUNT_RECORD_CLASS = "Vj0debbaJ9Tk";
    private static final String SETUP_CLASS = "6aq1g";
    private static final String HEAD = "12AqU0";
    private static final String BACKUP_TIME = "Tb9kbaJ";
    private static final String USER_ID = "0badeb";

    public static JSONObject recordGroupToJSONObject(RecordGroup recordGroup) throws JSONException {
        JSONObject object = new JSONObject();
        object.put(GroupOperator.GROUP_NAME, recordGroup.getGroupName());
        object.put(GroupOperator.SORT_INDEX, recordGroup.getSortIndex());
        object.put(GroupOperator.COLOR_STRING, recordGroup.getColor());
        return object;
    }

    public static RecordGroup jsonObjectToRecordGroup(JSONObject jsonObject) throws JSONException {
        RecordGroup recordGroup = new RecordGroup();
        recordGroup.setGroupName(jsonObject.getString(GroupOperator.GROUP_NAME));
        recordGroup.setSortIndex(jsonObject.getInt(GroupOperator.SORT_INDEX));
        recordGroup.setColorString(jsonObject.getString(GroupOperator.COLOR_STRING));
        return recordGroup;
    }

    public static List<RecordGroup> jsonArrayToRecordGroupList(JSONArray jsonArray) throws JSONException {
        List<RecordGroup> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            list.add(jsonObjectToRecordGroup(jsonArray.getJSONObject(i)));
        }
        return list;
    }

    public static JSONArray recordGroupListToJSON(List<RecordGroup> list) throws JSONException {
        JSONArray jsonArray = new JSONArray();
        for (RecordGroup recordGroup : list) {
            jsonArray.put(recordGroupToJSONObject(recordGroup));
        }
        return jsonArray;
    }

    public static JSONObject setupToJSONObject(Setup setup) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(SetupOperator.KEY, setup.getRawKey());
        jsonObject.put(SetupOperator.VALUE, setup.getRawValue());
        return jsonObject;
    }

    public static Setup jsonObjectToSetup(JSONObject jsonObject) throws JSONException {
        Setup setup = new Setup();
        setup.setRawKey(jsonObject.getString(SetupOperator.KEY));
        setup.setRawValue(jsonObject.getString(SetupOperator.VALUE));
        return setup;
    }

    public static JSONArray setupListToJSON(List<Setup> list) throws JSONException {
        JSONArray array = new JSONArray();
        for (Setup setup : list) {
            array.put(setupToJSONObject(setup));
        }
        return array;
    }

    public static List<Setup> jsonArrayToSetupList(JSONArray jsonArray) throws JSONException {
        List<Setup> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            list.add(jsonObjectToSetup(jsonArray.getJSONObject(i)));
        }
        return list;
    }

    public static JSONObject textRecordToJSONObject(TextRecord textRecord) throws JSONException {
        JSONObject object = new JSONObject();
        object.put(TextRecord.KEY, textRecord.getRawKey());
        object.put(TextRecord.CONTENT, textRecord.getRawContent());
        return object;
    }

    public static TextRecord jsonObjectToTextRecord(JSONObject jsonObject) throws JSONException {
        TextRecord record = new TextRecord();
        record.setRawKey(jsonObject.getString(TextRecord.KEY));
        record.setRawContent(jsonObject.getString(TextRecord.CONTENT));
        return record;
    }

    public static JSONArray textRecordListToJSONArray(List<TextRecord> list) throws JSONException {
        JSONArray array = new JSONArray();
        for (TextRecord textRecord : list) {
            array.put(textRecordToJSONObject(textRecord));
        }
        return array;
    }

    public static List<TextRecord> jsonArrayToTextRecordList(JSONArray jsonArray) throws JSONException {
        List<TextRecord> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            list.add(jsonObjectToTextRecord(jsonArray.getJSONObject(i)));
        }
        return list;
    }

    public static JSONObject imageRecordToJSONObject(ImageRecord imageRecord) throws JSONException {
        JSONObject object = new JSONObject();
        object.put(ImageOperator.FILE_NAME, imageRecord.getRawImageFileName());
        object.put(ImageOperator.FILE_PATH, imageRecord.getRawPath());
        return object;
    }

    public static ImageRecord jsonObjectToImageRecord(JSONObject jsonObject) throws JSONException {
        ImageRecord imageRecord = new ImageRecord();
        imageRecord.setRawImageFileName(jsonObject.getString(ImageOperator.FILE_NAME));
        imageRecord.setRawPath(jsonObject.getString(ImageOperator.FILE_PATH));
        return imageRecord;
    }

    public static JSONArray imageRecordListToJSONArray(List<ImageRecord> list) throws JSONException {
        JSONArray array = new JSONArray();
        for (ImageRecord imageRecord : list) {
            array.put(imageRecordToJSONObject(imageRecord));
        }
        return array;
    }

    public static List<ImageRecord> jsonArrayToImageRecordList(JSONArray jsonArray) throws JSONException {
        List<ImageRecord> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            list.add(jsonObjectToImageRecord(jsonArray.getJSONObject(i)));
        }
        return list;
    }

    public static JSONObject accountRecordToJSONObject(AccountRecord accountRecord) throws JSONException {
        JSONObject object = new JSONObject();
        object.put(RecordOperator.RECORD_NAME, accountRecord.getRawRecordName());
        object.put(RecordOperator.ACCOUNT_NAME, accountRecord.getRawAccountName());
        object.put(RecordOperator.ACCOUNT_PWD, accountRecord.getRawAccountPWD());
        object.put(RecordOperator.DESCRIBE, accountRecord.getRawDescribe());
        object.put(RecordOperator.GROUP_ID, accountRecord.getGroupId());
        object.put(RecordOperator.SORT_INDEX, accountRecord.getSortIndex());
        object.put(RecordOperator.RECORD_TIME, accountRecord.getRecordTime());
        object.put(RecordOperator.MODIFY_TIME, accountRecord.getModifyTime());
        object.put(RecordOperator.IS_DELETED, accountRecord.isDeleted());
        object.put(RecordOperator.DELE_TIME, accountRecord.getDeleTime());
        object.put(RecordOperator.TEXT_RECORD_LIST, textRecordListToJSONArray(accountRecord.getTextRecords()));
        object.put(RecordOperator.IMAGE_RECORD_LIST, imageRecordListToJSONArray(accountRecord.getImageRecords()));
        return object;
    }

    public static AccountRecord jsonObjectToAccountRecord(JSONObject jsonObject) throws JSONException {
        AccountRecord accountRecord = new AccountRecord();
        accountRecord.setRawRecordName(jsonObject.getString(RecordOperator.RECORD_NAME));
        accountRecord.setRawAccountName(jsonObject.getString(RecordOperator.ACCOUNT_NAME));
        accountRecord.setRawAccountPWD(jsonObject.getString(RecordOperator.ACCOUNT_PWD));
        accountRecord.setRawDescribe(jsonObject.getString(RecordOperator.DESCRIBE));
        accountRecord.setGroupId(jsonObject.getLong(RecordOperator.GROUP_ID));
        accountRecord.setSortIndex(jsonObject.getInt(RecordOperator.SORT_INDEX));
        accountRecord.setRecordTime(jsonObject.getLong(RecordOperator.RECORD_TIME));
        accountRecord.setModifyTime(jsonObject.getLong(RecordOperator.MODIFY_TIME));
        accountRecord.setDeleted(jsonObject.getBoolean(RecordOperator.IS_DELETED));
        accountRecord.setDeleTime(jsonObject.getLong(RecordOperator.DELE_TIME));
        accountRecord.getTextRecords().addAll(jsonArrayToTextRecordList(jsonObject.getJSONArray(RecordOperator.TEXT_RECORD_LIST)));
        accountRecord.getImageRecords().addAll(jsonArrayToImageRecordList(jsonObject.getJSONArray(RecordOperator.IMAGE_RECORD_LIST)));
        return accountRecord;
    }

    public static List<AccountRecord> jsonArrayToAccountRecordList(JSONArray jsonArray) throws JSONException {
        List<AccountRecord> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            list.add(jsonObjectToAccountRecord(jsonArray.getJSONObject(i)));
        }
        return list;
    }

    public static JSONArray accountRecordListToJSON(List<AccountRecord> list) throws JSONException {
        JSONArray array = new JSONArray();
        for (AccountRecord accountRecord : list) {
            array.put(accountRecordToJSONObject(accountRecord));
        }
        return array;
    }

    public static String getBackupJsonString() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(HEAD, EncryptAndDecrypt.generateBackupFileSeedString(SetupOperator.getSeed()));
        jsonObject.put(BACKUP_TIME,System.currentTimeMillis());
        jsonObject.put(USER_ID,SetupOperator.getPhoneId());
        jsonObject.put(SETUP_CLASS, setupListToJSON(SetupOperator.findAll()));
        jsonObject.put(RECORD_GROUP_CLASS, recordGroupListToJSON(GroupOperator.findAll(false)));
        jsonObject.put(ACCOUNT_RECORD_CLASS, accountRecordListToJSON(RecordOperator.findAll()));
        return jsonObject.toString();
    }

    public static List<Setup> getSetupList(String jsonString) throws JSONException{
        JSONObject object = new JSONObject(jsonString);
        return jsonArrayToSetupList(object.getJSONArray(SETUP_CLASS));
    }

    public static List<RecordGroup> getRecordGroupList(String jsonString) throws JSONException{
        JSONObject object = new JSONObject(jsonString);
        return jsonArrayToRecordGroupList(object.getJSONArray(RECORD_GROUP_CLASS));
    }

    public static List<AccountRecord> getAccountRecordList(String jsonString) throws JSONException{
        JSONObject object = new JSONObject(jsonString);
        return jsonArrayToAccountRecordList(object.getJSONArray(ACCOUNT_RECORD_CLASS));
    }

    public static String getSeed(String jsonString) throws JSONException {
        JSONObject object = new JSONObject(jsonString);
        return object.getString(HEAD);
    }

    public static long getBackupTime(String jsonString) throws JSONException{
        JSONObject object = new JSONObject(jsonString);
        return object.getLong(BACKUP_TIME);
    }

    public static String getPhoneId(String jsonString) throws JSONException{
        JSONObject object = new JSONObject(jsonString);
        return object.getString(USER_ID);
    }

    public static String getJsonPWD(String jsonString) throws JSONException {
        JSONObject object = new JSONObject(jsonString);
        List<Setup> list = jsonArrayToSetupList(object.getJSONArray(SETUP_CLASS));
        return SetupOperator.getRawPassWordInSetupList(list);
    }

}
