package com.example.kodama.models;

public class Plants {

    private String name;
    private String description;
    private String link;
    private String scientificName;

    public Plants() {}

    public Plants (String name, String description, String link, String scientificName) {
        this.name = name;
        this.description = description;
        this.link = link;
        this.scientificName = scientificName;

    }

    public String getName() {
        return name;
    }
    public String getDescription() {return description;}
    public String getLink() {return link;}
    public String getScientificName() {return scientificName;}



    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setScientificName(String scientificName) {
        this.scientificName = scientificName;
    }
}
