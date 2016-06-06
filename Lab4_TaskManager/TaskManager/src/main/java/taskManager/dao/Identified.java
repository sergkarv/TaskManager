package taskManager.dao;

import java.io.Serializable;

/**
 * Interface id of object
 */
public interface Identified<PK extends Serializable> {

    /** Returns id of object */
    public PK getId();
}
