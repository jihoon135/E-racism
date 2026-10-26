package io.github.jihoon135.api.country;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "country")
public class Country {

    @Id
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = "iso3", length = 3)
    private String iso3;

    @Column(name = "name_en", nullable = false)
    private String nameEn;

    @Column(name = "name_ko")
    private String nameKo;

    @Column(name = "region")
    private String region;

    protected Country() {
    }

    public String getIso3() {
        return iso3;
    }

    public String getNameEn() {
        return nameEn;
    }

    public String getNameKo() {
        return nameKo;
    }

    public String getRegion() {
        return region;
    }
}
