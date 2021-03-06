package yzw.ahaqth.personaldatacollector.modules;

import android.util.Log;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;
import yzw.ahaqth.personaldatacollector.tools.EncryptAndDecrypt;

public class ImageRecord extends LitePalSupport {
    public String getImageFileName() {
        return EncryptAndDecrypt.decryptFromString(imageFileName);
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = EncryptAndDecrypt.encryptToString(imageFileName);
    }

    public String getPath() {
        return EncryptAndDecrypt.decryptFromString(path);
    }

    public void setPath(String path) {
        this.path = EncryptAndDecrypt.encryptToString(path);
    }

    public String getRawPath(){
        return this.path;
    }

    public String getRawImageFileName(){
        return this.imageFileName;
    }

    public void setRawPath(String path){
        this.path = path;
    }

    public void setRawImageFileName(String imageFileName){
        this.imageFileName = imageFileName;
    }

    private String imageFileName;
    private String path;
    @Column(ignore = true)
    public boolean isDeleted;
}
