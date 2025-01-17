package org.example.model;
import java.util.Set;
import org.apache.catalina.User;

public class SpecialCircumstance {
    private Long circumstanceId;
    private Set<SpecialCircumstanceType> types;
    private User user;

    public enum SpecialCircumstanceType {
        MILITARY, PREGNANCY, CHRONIC_ILLNESS, OTHER
    }

    public Long getCircumstanceId() {
        return circumstanceId;
    }

    public void setCircumstanceId(Long circumstanceId) {
        this.circumstanceId = circumstanceId;
    }

    public Set<SpecialCircumstanceType> getTypes() {
        return types;
    }

    public void setTypes(Set<SpecialCircumstanceType> types) {
        this.types = types;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}