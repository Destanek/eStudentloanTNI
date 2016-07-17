package th.ac.tni.studentaffairs.estudentloantni.dao;

import java.io.Serializable;

public class DataModelWebContent implements Serializable {
    public String title,link,date;

    public DataModelWebContent(String title,String link, String date) {
        this.title = title;
        this.link = link;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getDate() {
        return date;
    }
}

