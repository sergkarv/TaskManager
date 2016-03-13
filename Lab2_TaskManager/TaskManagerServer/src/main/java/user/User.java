/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package user;

import java.io.Serializable;
import java.util.Calendar;

/**
 *
 * @author Сергей
 */
public class User implements Serializable{
    private long id;
    private String name;
    
    public User() {
        this.id = Calendar.getInstance().getTimeInMillis();
    }

    public User(String name) {
        this.id = Calendar.getInstance().getTimeInMillis();
        this.name = name;
    }
    
    

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    
    

    public long getId() {
        return id;
    }
    
    @Override
    public int hashCode() {
        int t = (int)id ^ (int)(id >> 32); 
        return t;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final User other = (User) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.getName();
    }
    
}
