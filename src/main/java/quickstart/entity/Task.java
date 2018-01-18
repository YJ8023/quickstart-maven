package quickstart.entity;

import javax.validation.constraints.NotNull;

/**
 * task
 *
 * @author Administrator
 * @create 2018-01-11
 */

public class Task {

    private Long id;
    private String title;
    private String description;
    private User user;

    @NotNull
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setId(Long id) { this.id = id; }

    public Long getId() { return id; }
}
