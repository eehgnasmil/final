package ggs.ggs.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
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


}
