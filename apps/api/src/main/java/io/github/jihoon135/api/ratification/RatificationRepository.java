package io.github.jihoon135.api.ratification;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RatificationRepository extends JpaRepository<Ratification, RatificationId> {

    @Query("""
            select new io.github.jihoon135.api.ratification.RatificationItem(
                r.id.iso3, r.status, r.dateRatification)
            from Ratification r
            where r.id.treatyCode = :treatyCode
            order by r.id.iso3
            """)
    List<RatificationItem> findItemsByTreatyCode(@Param("treatyCode") String treatyCode);
}
