package zhou.gank.io.model;

import org.litepal.crud.DataSupport;

import java.util.Date;

/**
 * Created by zhou on 15-9-29.
 */
public class Bookmark extends DataSupport{

    private String title;
    private String url;
    private Date time;

    public Bookmark(String title, String url, Date time) {
        this.title = title;
        this.url = url;
        this.time = time;
    }

    public Bookmark() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
