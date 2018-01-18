package xtend.net;

/**
 * Created by eliezer on 12/17/17.
 */

public class MultiPartFile {

    private byte[] file;
    private String name;
    private String mediaType;

    public MultiPartFile(byte[] file, String name, String mediaType) {
        this.file = file;
        this.name = name;
        this.mediaType = mediaType;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }
}
