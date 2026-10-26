package io.github.jihoon135.api.treaty;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "treaty")
public class Treaty {

    @Id
    @Column(name = "code")
    private String code;

    @Column(name = "name_en", nullable = false)
    private String nameEn;

    @Column(name = "name_ko")
    private String nameKo;

    @Column(name = "adopted_year")
    private Integer adoptedYear;

    @Column(name = "data_updated_at")
    private LocalDate dataUpdatedAt;

    protected Treaty() {
    }

    public String getCode() {
        return code;
    }

    public String getNameEn() {
        return nameEn;
    }

    public String getNameKo() {
        return nameKo;
    }

    public Integer getAdoptedYear() {
        return adoptedYear;
    }

    public LocalDate getDataUpdatedAt() {
        return dataUpdatedAt;
    }
}
