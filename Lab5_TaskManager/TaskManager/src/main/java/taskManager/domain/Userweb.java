package taskManager.domain;

public class Userweb implements Comparable<Userweb> {
    private Integer id ;
    private String name;
    private String password;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    @Override
    public int compareTo(Userweb o) {
        return Integer.compare(this.id, o.getId());
    }
}
