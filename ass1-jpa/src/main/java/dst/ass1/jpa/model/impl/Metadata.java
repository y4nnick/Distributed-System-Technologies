package dst.ass1.jpa.model.impl;

import dst.ass1.jpa.model.IMetadata;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by amra.
 */

@Entity
public class Metadata implements IMetadata{

    @Id
    @GeneratedValue
    private long id;

    private String game;

    @ElementCollection
    @OrderColumn
    private List<String> settings = new ArrayList<>();


    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id=id;

    }

    @Override
    public String getGame() {
        return game;
    }

    @Override
    public void setGame(String game) {
        this.game=game;

    }

    @Override
    public List<String> getSettings() {
        return settings;
    }

    @Override
    public void setSettings(List<String> settings) {

        this.settings=settings;

    }

    @Override
    public void addSetting(String setting) {
        settings.add(setting);

    }
}



