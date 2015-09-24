package zhou.gank.io.model

import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString;

@ToString
@EqualsAndHashCode
@CompileStatic
class Gank implements Serializable{

    public String who
    public String desc
    public String type
    public String url
    public String objectId
    public boolean used
    public Date publishedAt
    public Date createdAt
    public Date updatedAt

    Gank(String who, String desc, String type, String url, String objectId, boolean used, Date publishedAt, Date createdAt, Date updatedAt) {
        this.who = who
        this.desc = desc
        this.type = type
        this.url = url
        this.objectId = objectId
        this.used = used
        this.publishedAt = publishedAt
        this.createdAt = createdAt
        this.updatedAt = updatedAt
    }

    Gank() {
    }


    @Override
    public String toString() {
        return "Gank{" +
                "who='" + who + '\'' +
                ", desc='" + desc + '\'' +
                ", type='" + type + '\'' +
                ", url='" + url + '\'' +
                ", objectId='" + objectId + '\'' +
                ", used=" + used +
                ", publishedAt=" + publishedAt +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}