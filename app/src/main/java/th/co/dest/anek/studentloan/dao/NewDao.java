package th.co.dest.anek.studentloan.dao;

import java.io.Serializable;

public class NewDao implements Serializable {
    public String message,link,date,type;

    public NewDao() {
    }

    public NewDao(String message, String link, String date, String type) {
        this.message = message;
        this.link = link;
        this.date = date;
        this.type = type;
    }

    public String getTitle() {
        return message;
    }

    public String getLink() {
        return link;
    }

    public String getDate() {
        return date;
    }
}

