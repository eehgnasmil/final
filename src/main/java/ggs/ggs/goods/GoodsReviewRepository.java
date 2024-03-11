package ggs.ggs.goods;

import ggs.ggs.domain.Goods;
import ggs.ggs.domain.GoodsQnA;
import ggs.ggs.domain.GoodsReview;
import ggs.ggs.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoodsReviewRepository extends JpaRepository<GoodsReview,Integer> {
    
    List<GoodsReview> findByGoodsOrderByIdxDesc(Goods goods);

    List<GoodsReview> findByMemberOrderByIdxDesc(Member member);
}
