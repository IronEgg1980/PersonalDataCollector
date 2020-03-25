package yzw.ahaqth.personaldatacollector.modules;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;
import yzw.ahaqth.personaldatacollector.tools.EncryptAndDecrypt;

public class ImageRecord extends LitePalSupport {
    public String getImageFileName() {
        return EncryptAndDecrypt.decrypt(imageFileName);
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = EncryptAndDecrypt.encrypt(imageFileName);
    }

    public String getPath() {
        return EncryptAndDecrypt.decrypt(path);
    }

    public void setPath(String path) {
        this.path = EncryptAndDecrypt.encrypt(path);
    }

    private String imageFileName;
    private String path;
    @Column(ignore = true)
    public boolean isDeleted;
}
