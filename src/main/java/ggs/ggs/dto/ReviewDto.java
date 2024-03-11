package ggs.ggs.dto;

import ggs.ggs.domain.GoodsReview;
import ggs.ggs.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDto {
    private Integer idx;
    private Integer orderItemIdx;
    private Integer goodsIdx;
    private String memberId;
    private CartItemDto cartItemDto;
    private LocalDateTime createDate;
    private String content;
    private Integer star; // 1~5
    private List<FileDto> fileDtos;


    public ReviewDto(CartItemDto cartItemDto) {
        this.cartItemDto = cartItemDto;
        this.orderItemIdx = cartItemDto.getIdx();
    }

    public ReviewDto(GoodsReview review) {
        this.idx = review.getIdx();
        this.goodsIdx = review.getGoods().getIdx();
        this.memberId = review.getMember().getName();
        this.cartItemDto = new CartItemDto(review.getOrderItem());
        this.createDate = review.getCreated_date();
        this.content = review.getContent();
        this.star = review.getStar();
        this.fileDtos = review.getGoodsReviewFiles().stream()
                .map(file -> new FileDto(file))
                .collect(Collectors.toList());
    }
}
