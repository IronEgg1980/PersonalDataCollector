package yzw.ahaqth.personaldatacollector.modules;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import yzw.ahaqth.personaldatacollector.tools.EncryptAndDecrypt;

public class TextRecord extends LitePalSupport {
    public final static String KEY = "fqektdpFQIb";
    public final static String CONTENT = "mzR4z37";

    private String key;
    private String content;
    @Column(ignore = true)
    public boolean isDeleted;

    public String getKey() {
        return EncryptAndDecrypt.decryptFromString(key);
    }

    public void setKey(String key) {
        this.key = EncryptAndDecrypt.encryptToString(key);
    }

    public String getContent() {
        return EncryptAndDecrypt.decryptFromString(content);
    }

    public void setContent(String content) {
        this.content = EncryptAndDecrypt.encryptToString(content);
    }

    public String getRawKey(){
        return this.key;
    }

    public void setRawKey(String key){
        this.key = key;
    }

    public String getRawContent(){
        return this.content;
    }

    public void setRawContent(String content){
        this.content = content;
    }
}
