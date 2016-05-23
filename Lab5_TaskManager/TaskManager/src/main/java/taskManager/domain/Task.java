package taskManager.domain;

import org.hibernate.validator.constraints.NotEmpty;
import taskManager.utils.Utils;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Сергей on 21.03.16.
 */

@Entity
@Table(name = "task", schema = "tu")
public class Task implements  Comparable<Task> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "task_entity_seq_gen")
    @SequenceGenerator(name = "task_entity_seq_gen", sequenceName = "tu.task_id_seq")
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotEmpty
    @Size(min = 2)
    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "contacts")
    private String contacts;

    @Column(name = "date")
    private Date date;

    @Column(name = "highPriority")
    private boolean highPriority;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "userId", referencedColumnName = "id")
    private User user;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "parentId", referencedColumnName = "id")
    private Task parent;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isHighPriority() {
        return highPriority;
    }

    public void setHighPriority(boolean highPriority) {
        this.highPriority = highPriority;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Task getParent() {
        return parent;
    }

    public void setParent(Task parent) {
        this.parent = parent;
    }

    @Override
    public int compareTo(Task o) {
        return Integer.compare(this.id, o.getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;

        if (highPriority != task.highPriority) return false;
        if (contacts != null ? !contacts.equals(task.contacts) : task.contacts != null) return false;
        if (!date.equals(task.date)) return false;
        if (description != null ? !description.equals(task.description) : task.description != null) return false;
        if (!id.equals(task.id)) return false;
        if (!name.equals(task.name)) return false;
        if (parent != null ? !parent.equals(task.parent) : task.parent != null) return false;
        if (!user.equals(task.user)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (contacts != null ? contacts.hashCode() : 0);
        result = 31 * result + date.hashCode();
        result = 31 * result + (highPriority ? 1 : 0);
        result = 31 * result + user.hashCode();
        result = 31 * result + (parent != null ? parent.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        StringBuffer s = new StringBuffer();
        s.append("Task{");
        s.append( (id!= null)?"id=" + id:"null" );
        s.append( (name!= null)?", name='" + name + '\'':", name='null'" );
        s.append( (description!= null)?", description='" + description + '\'': ", description='null'"  );
        s.append( (date != null)? ", date='"+Utils.calendarToStr(calendar, false) + '\'' : ", date='null'"  );
        s.append(", highPriority='" + highPriority + '\'');
        s.append( (parent!= null)? ", parent='" + parent.getName() + '\'' : ", parent='null'");
        s.append( (user!= null)? ", userId='" + user.getName() + '\'' : ", userId='null'");
        s.append('}');
        return s.toString();
    }
}
