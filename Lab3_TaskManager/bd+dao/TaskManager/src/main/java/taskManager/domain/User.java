package taskManager.domain;

import taskManager.dao.Identified;

import java.io.Serializable;

/**
 * Created by Сергей on 21.03.16.
 */
public class User implements Identified<Integer> {

    private Integer id = null;
    private String name;
    private String password;


    @Override
    public Integer getId() {
        return id;
    }

    protected void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
