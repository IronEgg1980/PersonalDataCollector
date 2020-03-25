package yzw.ahaqth.personaldatacollector.operators;

import android.text.TextUtils;

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

public class JSONOperator {
    private static final String RECORD_GROUP_CLASS = "UTNwsrwt7E1l" ;
    private static final String ACCOUNT_RECORD_CLASS = "Vj0debbaJ9Tk";
    private static final String SETUP_CLASS = "6aq1g";
    private static final String HEAD = "12AqU0";

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

    public static List<RecordGroup> jsonArrayToRecordGroupList(String jsonArrayString) throws JSONException {
        if(TextUtils.isEmpty(jsonArrayString))
            throw new JSONException(" Custom Exception : the jsonArrayString is empty ! ");
        List<RecordGroup> list = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(jsonArrayString);
        for(int i = 0;i<jsonArray.length();i++){
            list.add(jsonObjectToRecordGroup(jsonArray.getJSONObject(i)));
        }
        return list;
    }

    public static JSONArray recordListGroupToJSON(List<RecordGroup> list) throws JSONException {
        JSONArray jsonArray = new JSONArray();
        for (RecordGroup recordGroup : list) {
            jsonArray.put(recordGroupToJSONObject(recordGroup));
        }
        return jsonArray;
    }

    public static JSONObject setupToJSONObject(Setup setup) throws JSONException{
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(SetupOperator.KEY,setup.getKey());
        jsonObject.put(SetupOperator.VALUE,setup.getValue());
        return jsonObject;
    }

    public static Setup jsonObjectToSetup(JSONObject jsonObject) throws JSONException{
        return new Setup(jsonObject.getString(SetupOperator.KEY),jsonObject.getString(SetupOperator.VALUE));
    }

    public static JSONArray setupListToJSON(List<Setup> list) throws JSONException{
        JSONArray array = new JSONArray();
        for(Setup setup : list){
            array.put(setupToJSONObject(setup));
        }
        return array;
    }

    public static List<Setup> jsonArrayToSetupList(String jsonArrayString) throws JSONException{
        if(TextUtils.isEmpty(jsonArrayString))
            throw new JSONException(" Custom Exception : the jsonArrayString is empty ! ");
        List<Setup> list = new ArrayList<>();

        JSONArray jsonArray = new JSONArray(jsonArrayString);
        for(int i = 0;i<jsonArray.length();i++){
            list.add(jsonObjectToSetup(jsonArray.getJSONObject(i)));
        }
        return list;
    }

    public static JSONObject textRecordToJSONObject(TextRecord textRecord) throws JSONException{
        JSONObject object = new JSONObject();
        object.put(TextRecord.KEY,textRecord.getKey());
        object.put(TextRecord.CONTENT,textRecord.getContent());
        return object;
    }

    public static TextRecord jsonObjectToTextRecord(JSONObject jsonObject) throws JSONException{
        TextRecord record = new TextRecord();
        record.setKey(jsonObject.getString(TextRecord.KEY));
        record.setContent(jsonObject.getString(TextRecord.CONTENT));
        return record;
    }

    public static JSONArray textRecordListToJSONArray(List<TextRecord> list) throws JSONException{
        JSONArray array = new JSONArray();
        for(TextRecord textRecord : list){
            array.put(textRecordToJSONObject(textRecord));
        }
        return array;
    }

    public static List<TextRecord> jsonArrayToTextRecordList(JSONArray jsonArray) throws JSONException{
        List<TextRecord> list = new ArrayList<>();
        for(int i = 0;i<jsonArray.length();i++){
            list.add(jsonObjectToTextRecord(jsonArray.getJSONObject(i)));
        }
        return list;
    }

    public static JSONObject imageRecordToJSONObject(ImageRecord imageRecord) throws JSONException{
        JSONObject object = new JSONObject();
        object.put(ImageOperator.FILE_NAME,imageRecord.getImageFileName());
        object.put(ImageOperator.FILE_PATH,imageRecord.getPath());
        return object;
    }

    public static ImageRecord jsonObjectToImageRecord(JSONObject jsonObject) throws JSONException{
        ImageRecord imageRecord = new ImageRecord();
        imageRecord.setImageFileName(jsonObject.getString(ImageOperator.FILE_NAME));
        imageRecord.setPath(jsonObject.getString(ImageOperator.FILE_PATH));
        return imageRecord;
    }

    public static JSONArray imageRecordListToJSONArray(List<ImageRecord> list) throws JSONException{
        JSONArray array = new JSONArray();
        for(ImageRecord imageRecord : list){
            array.put(imageRecordToJSONObject(imageRecord));
        }
        return array;
    }

    public static List<ImageRecord> jsonArrayToImageRecordList(JSONArray jsonArray) throws JSONException{
        List<ImageRecord> list = new ArrayList<>();
        for(int i = 0;i<jsonArray.length();i++){
            list.add(jsonObjectToImageRecord(jsonArray.getJSONObject(i)));
        }
        return list;
    }

    public static JSONObject accountRecordToJSONObject(AccountRecord accountRecord) throws JSONException{
        JSONObject object = new JSONObject();
        object.put(RecordOperator.RECORD_NAME,accountRecord.getRecordName());
        object.put(RecordOperator.ACCOUNT_NAME,accountRecord.getAccountName());
        object.put(RecordOperator.ACCOUNT_PWD,accountRecord.getAccountPWD());
        object.put(RecordOperator.DESCRIBE,accountRecord.getDescribe());
        object.put(RecordOperator.GROUP_ID,accountRecord.getGroupId());
        object.put(RecordOperator.SORT_INDEX,accountRecord.getSortIndex());
        object.put(RecordOperator.RECORD_TIME,accountRecord.getRecordTime());
        object.put(RecordOperator.MODIFY_TIME,accountRecord.getModifyTime());
        object.put(RecordOperator.IS_DELETED,accountRecord.isDeleted());
        object.put(RecordOperator.DELE_TIME,accountRecord.getDeleTime());
        object.put(RecordOperator.TEXT_RECORD_LIST,textRecordListToJSONArray(accountRecord.getTextRecords()));
        object.put(RecordOperator.IMAGE_RECORD_LIST,imageRecordListToJSONArray(accountRecord.getImageRecords()));
        return object;
    }

    public static AccountRecord jsonObjectToAccountRecord(JSONObject jsonObject) throws JSONException{
        AccountRecord accountRecord = new AccountRecord();
        accountRecord.setRecordName(jsonObject.getString(RecordOperator.RECORD_NAME));
        accountRecord.setAccountName(jsonObject.getString(RecordOperator.ACCOUNT_NAME));
        accountRecord.setAccountPWD(jsonObject.getString(RecordOperator.ACCOUNT_PWD));
        accountRecord.setDescribe(jsonObject.getString(RecordOperator.DESCRIBE));
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

    public static List<AccountRecord> jsonArrayToAccountRecordList(String jsonArrayString) throws JSONException{
        if(TextUtils.isEmpty(jsonArrayString))
            throw new JSONException(" Custom Exception : the jsonArrayString is empty ! ");
        List<AccountRecord> list = new ArrayList<>();

        JSONArray jsonArray = new JSONArray(jsonArrayString);
        for(int i = 0;i<jsonArray.length();i++){
            list.add(jsonObjectToAccountRecord(jsonArray.getJSONObject(i)));
        }
        return list;
    }

    public static JSONArray accountRecordListToJSON(List<AccountRecord> list) throws JSONException{
        JSONArray array = new JSONArray();
        for(AccountRecord accountRecord : list){
            array.put(accountRecordToJSONObject(accountRecord));
        }
        return array;
    }
}
