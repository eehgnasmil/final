package ggs.ggs.domain;

import ggs.ggs.dto.FileDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder(toBuilder = true)
public class GoodsReviewFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idx;

    @Column
    private String ofile;
    @Column
    private String sfile;
    @Column
    private String path;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goodsReview", referencedColumnName = "idx")
    private GoodsReview goodsReview;

    public GoodsReviewFile(FileDto fileDto) {
        this.idx = fileDto.getIdx();
        this.ofile = fileDto.getOfile();
        this.sfile = fileDto.getSfile();
        this.path = fileDto.getPath();
        this.goodsReview = fileDto.getGoodsReview();

    }
}
