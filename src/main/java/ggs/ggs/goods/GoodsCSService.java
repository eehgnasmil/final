package ggs.ggs.goods;

import ggs.ggs.dto.GoodsDto;
import ggs.ggs.dto.GoodsQnADto;
import ggs.ggs.dto.ReviewDto;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.util.List;


public interface GoodsCSService {
    void save(GoodsQnADto goodsQnADto);

    Page<GoodsQnADto> findbyGoodsQnA(String id, int sortValue, int page, Integer category);

    ReviewDto findbyOrderItem(Integer idx);

	Page<GoodsQnADto> findbyGoodsQnA(int sortValue, int page, Integer category);

    void saveAnswer(GoodsQnADto goodsQnADto);

    void save(ReviewDto reviewDto) throws IOException;

    List<ReviewDto> findAll(GoodsDto goods);

    List<ReviewDto> findAll(String id);
}
