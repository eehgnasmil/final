package ggs.ggs.goods;

import ggs.ggs.domain.GoodsQnA;
import ggs.ggs.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GoodsQnARepository extends JpaRepository<GoodsQnA,Integer> {
    
    @Query("SELECT g FROM GoodsQnA g WHERE (:category=0 OR g.category = :category) AND (:state=0 OR g.state = :state)")
    Page<GoodsQnA> findAllByCategoryAndState(@Param("state") int sortValue, @Param("category") Integer category, Pageable pageable);

    @Query("SELECT g FROM GoodsQnA g " +
            "WHERE g.member = :member AND (:category = 0 OR g.category = :category) AND (:state = 0 OR g.state = :state) ORDER BY g.idx DESC")
    Page<GoodsQnA> findByCategoryAndStateAndMember(@Param("member") Member member, @Param("state") int sortValue, @Param("category") Integer category, Pageable pageable);

}
