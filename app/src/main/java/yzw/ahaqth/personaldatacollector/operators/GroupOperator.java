package yzw.ahaqth.personaldatacollector.operators;

import android.text.TextUtils;

import org.json.JSONException;
import org.litepal.LitePal;

import java.util.List;

import yzw.ahaqth.personaldatacollector.modules.AccountRecord;
import yzw.ahaqth.personaldatacollector.modules.RecordGroup;

public class GroupOperator {
    public final static String GROUP_NAME = "czyRCZ8a41E3";
    public final static String SORT_INDEX = "FB5BB48";
    public final static String COLOR_STRING = "xNBSUCju";

    public static List<RecordGroup> findAll(boolean isSorted){
        if(isSorted)
            return LitePal.order("sortIndex").find(RecordGroup.class);
        return LitePal.findAll(RecordGroup.class);
    }

    public static RecordGroup findOne(long id){
        return LitePal.find(RecordGroup.class,id);
    }

    public static RecordGroup findOne(String groupName){
        if(TextUtils.isEmpty(groupName))
            return null;
        return LitePal.where("groupname = ?",groupName).findFirst(RecordGroup.class);
    }

    public static boolean isExist(String groupName){
        if(TextUtils.isEmpty(groupName))
            throw new RuntimeException("Call [ GroupOperator.isExist(String groupName) ] is Error! The groupName is empty!");
        return findOne(groupName) != null;
    }

    public static boolean isExist(RecordGroup recordGroup){
        if(recordGroup == null)
            return false;
        return isExist(recordGroup.getGroupName()) || recordGroup.isSaved();
    }

    public static int dele(RecordGroup recordGroup){
        return recordGroup.delete();
    }

    public static void dele(List<RecordGroup> list){
        for(RecordGroup r:list)
            dele(r);
    }

    public static boolean save(RecordGroup recordGroup){
        if(recordGroup == null)
            throw new RuntimeException("The recordGroup is NULL!");
        if(TextUtils.isEmpty(recordGroup.getGroupName()))
            throw new RuntimeException("The recordGroup has no groupName");
        return recordGroup.save();
    }

    public static void save(List<RecordGroup> recordGroups){
        LitePal.saveAll(recordGroups);
    }

    public static String getGroupName(long id){
        RecordGroup group = findOne(id);
        if(group == null)
            return "未分组";
        return group.getGroupName();
    }

    public static String getGroupName(AccountRecord record){
        return getGroupName(record.getGroupId());
    }

    public static void swapSortIndex(RecordGroup r1,RecordGroup r2){
        if(r1 == null || r2 == null){
            throw new RuntimeException("The parameter is NULL !");
        }
        int preSortIndex = r1.getSortIndex();
        r1.setSortIndex(r2.getSortIndex());
        r2.setSortIndex(preSortIndex);
        save(r1);
        save(r2);
    }

}
