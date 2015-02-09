package com.jacksonvillecomedy.broskj.jaxcomedy;

/**
 * Created by Kyle on 12/29/2014.
 */
public class Model {

    public int icon;
    public String title;

    public Model(String title) {
        this(-1, title);
    }

    public Model(int icon, String title) {
        super();
        this.icon = icon;
        this.title = title;
    }
}
