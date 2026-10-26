package io.github.jihoon135.api.ratification;

import java.io.Serializable;
import java.util.Objects;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class RatificationId implements Serializable {

    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = "iso3", length = 3)
    private String iso3;

    @Column(name = "treaty_code")
    private String treatyCode;

    protected RatificationId() {
    }

    public String getIso3() {
        return iso3;
    }

    public String getTreatyCode() {
        return treatyCode;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof RatificationId other
                && Objects.equals(iso3, other.iso3)
                && Objects.equals(treatyCode, other.treatyCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(iso3, treatyCode);
    }
}
