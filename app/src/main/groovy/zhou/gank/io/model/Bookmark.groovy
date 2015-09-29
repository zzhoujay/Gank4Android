package zhou.gank.io.model;

import groovy.transform.CompileStatic

@CompileStatic
public class Bookmark implements Serializable {

    public String url
    public String title
    public Date time

    Bookmark(String url, String title, Date time) {
        this.url = url
        this.title = title
        this.time = time
    }

    Bookmark(String url, String title) {
        this.url = url
        this.title = title
        this.time = new Date()
    }

    Bookmark() {
    }


    @Override
    public String toString() {
        return "Bookmark{" +
                "url='" + url + '\'' +
                ", title='" + title + '\'' +
                ", time=" + time +
                '}';
    }
}