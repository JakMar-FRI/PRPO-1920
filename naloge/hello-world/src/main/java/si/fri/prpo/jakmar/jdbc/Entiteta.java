package si.fri.prpo.jakmar.jdbc;

import java.io.Serializable;

public abstract class Entiteta implements Serializable {
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
