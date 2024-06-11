package pl.wipb.beershop.models.utils;

import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseModel<ID> {
    public abstract ID getId();
    public abstract void setId(ID id);
}
