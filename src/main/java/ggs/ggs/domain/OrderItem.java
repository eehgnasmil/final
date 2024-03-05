package ggs.ggs.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder(toBuilder = true)
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orders")
    private Order order;

    @Column String name;

    @Column
    private Integer cnt;

    @Column
    private String color;

    @Column
    private String size;

    @Column
    private Integer state; // 리뷰 상태 0 리뷰x 1 리뷰o

    @ManyToOne(fetch = FetchType.LAZY )
    @JoinColumn(name = "goods", referencedColumnName = "idx")
    private Goods goods;

    @OneToOne(mappedBy = "orderItem" , cascade = CascadeType.REMOVE)
    private  GoodsReview goodsReview;

    @CreatedDate
    private LocalDateTime created_date;

    public void setGoodsToNull() {
        this.goods = null;
    }

    public OrderItem(Order order, CartItem cartItem) {
        this.name = cartItem.getGoodsOption().getGoods().getName();
        this.goods = cartItem.getGoodsOption().getGoods();
        this.color = cartItem.getGoodsOption().getColor();
        this.size = cartItem.getGoodsOption().getSize();
        this.cnt = cartItem.getCnt();
        this.order = order;
        this.state = 0;

    }


}