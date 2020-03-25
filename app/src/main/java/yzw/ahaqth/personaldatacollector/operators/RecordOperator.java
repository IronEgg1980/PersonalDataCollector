package yzw.ahaqth.personaldatacollector.operators;

import org.json.JSONException;
import org.litepal.LitePal;

import java.util.List;

import yzw.ahaqth.personaldatacollector.modules.AccountRecord;
import yzw.ahaqth.personaldatacollector.modules.ImageRecord;
import yzw.ahaqth.personaldatacollector.tools.EncryptAndDecrypt;
import yzw.ahaqth.personaldatacollector.tools.ToolUtils;

public final class RecordOperator {
    public final static String RECORD_NAME = "gXlKAIS";
    public final static String ACCOUNT_NAME = "E9C3h";
    public final static String ACCOUNT_PWD = "inz8svDgD";
    public final static String DESCRIBE = "N2he4Iw5tm";
    public final static String GROUP_ID = "0mpeJ";
    public final static String SORT_INDEX = "Bs95RvQ";
    public final static String RECORD_TIME = "HkcVUsj";
    public final static String MODIFY_TIME = "zRK7BsaB";
    public final static String IS_DELETED = "3hm7R4h";
    public final static String DELE_TIME = "DkdsE";
    public final static String TEXT_RECORD_LIST = "Ydfh1n2z";
    public final static String IMAGE_RECORD_LIST = "kvnJpA008v";

    public static List<AccountRecord> findAll() {
        return LitePal.findAll(AccountRecord.class, true);
    }

    public static List<AccountRecord> findAll(long recordGroupId) {
        if(recordGroupId < 0)
            return findAllNotDeleted();
        return LitePal.where("isDeleted = 0 and groupId = ?", String.valueOf(recordGroupId))
                .find(AccountRecord.class, true);
    }

    public static List<AccountRecord> findAllNotDeleted() {
        return LitePal.where("isDeleted = 0")
                .find(AccountRecord.class, true);
    }

    public static List<AccountRecord> findAllDeleted() {
        return LitePal.order("deletime desc")
                .where("isDeleted = 1")
                .find(AccountRecord.class, true);
    }

    public static void clearOldDeletedRecord() {
        for (AccountRecord record : findAllDeleted()) {
            long deleTime = record.getDeleTime();
            long diffDay = (System.currentTimeMillis() - deleTime) / ToolUtils.ONE_DAY_MILLES;
            if (diffDay > 29) {
                clear(record);
            }
        }
    }

    public static AccountRecord findOne(long id) {
        return LitePal.find(AccountRecord.class, id, true);
    }

    public static void saveAll(List<AccountRecord> list) {
        LitePal.saveAll(list);
    }

    public static void save(AccountRecord record) {
        record.save();
    }

    public static boolean isExist(String recordName) {
        return LitePal.isExist(AccountRecord.class, "recordname = ?", EncryptAndDecrypt.encrypt(recordName));
    }

    public static void deleAll(List<AccountRecord> list) {
        for (AccountRecord accountRecord : list) {
            dele(accountRecord);
        }
    }

    public static void dele(AccountRecord accountRecord) {
        accountRecord.setDeleted(true);
        accountRecord.setDeleTime(System.currentTimeMillis());
        accountRecord.save();
    }

    public static void clear(AccountRecord accountRecord) {
        for (ImageRecord imageRecord : accountRecord.getImageRecords()) {
            ImageOperator.deleImageFile(imageRecord);
        }
        accountRecord.delete();
    }

    public static void clearAll(List<AccountRecord> list) {
        for (AccountRecord accountRecord : list) {
            clear(accountRecord);
        }
    }

    public static void clearAll() {
        List<AccountRecord> list = findAll();
        clearAll(list);
    }

    public static void resumeOne(AccountRecord accountRecord) {
        accountRecord.setDeleted(false);
        accountRecord.setDeleTime(1000);
        accountRecord.save();
    }

    public static void resumeAll(List<AccountRecord> list) {
        for (AccountRecord accountRecord : list) {
            resumeOne(accountRecord);
        }
    }
}
