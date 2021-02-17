package app.sharma.searchview2;

public class Places {

    public String name, link, description;

    public Places() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return link;
    }

    public void setImage(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Places(String name, String link, String description) {
        this.name = name;
        this.link = link;
        this.description = description;
    }
}
