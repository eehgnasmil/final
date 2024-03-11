package ggs.ggs.domain;

import ggs.ggs.dto.ReviewDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder(toBuilder = true)
public class GoodsReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idx;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime created_date;

    @Column
    private Integer star; // 1~5

    @Column
    private String content; // 1~5

    @Column
    private String color;

    @Column
    private String size;

    @OneToMany(mappedBy = "goodsReview", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GoodsReviewFile> goodsReviewFiles;

    @OneToOne
    @JoinColumn(name="orderItem" , referencedColumnName = "idx")
    private  OrderItem orderItem;

    @ManyToOne
    @JoinColumn(name="member",referencedColumnName = "idx" )
    private Member member;

    @ManyToOne
    @JoinColumn(name="goods",referencedColumnName = "idx" )
    private Goods goods;


    public GoodsReview(ReviewDto reviewDto, Goods goods, Member member, OrderItem orderItem) {
        this.star = reviewDto.getStar();
        this.color = orderItem.getColor();
        this.size = orderItem.getSize();
        this.content = reviewDto.getContent();
        this.goods = goods;
        this.member = member;
        this.orderItem = orderItem;
        this.goodsReviewFiles = new ArrayList<>();
    }
}
