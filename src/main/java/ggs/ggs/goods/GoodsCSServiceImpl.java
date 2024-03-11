package ggs.ggs.goods;

import ggs.ggs.domain.*;
import ggs.ggs.dto.*;
import ggs.ggs.member.MemberRepository;
import ggs.ggs.order.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Qualifier("goodsCSServiceImpl")
@RequiredArgsConstructor
public class GoodsCSServiceImpl implements GoodsCSService{

    @Autowired
    private final GoodsRepository goodsRepository;

    @Autowired
    private final GoodsQnARepository goodsQnARepository;

    @Autowired
    private final GoodsReviewRepository goodsReviewRepository;

    @Autowired
    private final MemberRepository memberRepository;

    @Autowired
    private final OrderItemRepository orderItemRepository;

    @Autowired
    @Qualifier("reviewFileServiceImpl")
    private final ReviewFileServiceImpl fileService;

    @Override
    public void save(GoodsQnADto goodsQnADto) {

        Goods goods = goodsRepository.findById(goodsQnADto.getGoodsIdx()).get();
        Member member = memberRepository.findByid(goodsQnADto.getMemberId()).get();
        GoodsQnA goodsQnA = new GoodsQnA(goodsQnADto,goods,member);
        goodsQnARepository.save(goodsQnA);
    }

    @Override
    public Page<GoodsQnADto> findbyGoodsQnA(String id, int sortValue, int page, Integer category) {
        Member member = memberRepository.findByid(id).get();
        Pageable pageable = PageRequest.of(page,10);
        Page<GoodsQnA> goodsQnAs = goodsQnARepository.findByCategoryAndStateAndMember(member,sortValue, category, pageable);
        return goodsQnAs.map(goodsQnA -> {
            GoodsQnADto goodsQnADto = new GoodsQnADto(goodsQnA);
            return goodsQnADto;
        });
    }

    @Override
    public ReviewDto findbyOrderItem(Integer idx) {
        OrderItem orderItem = orderItemRepository.findById(idx).orElseThrow();
        CartItemDto cartItemDto = new CartItemDto(orderItem);
        ReviewDto reviewDto = new ReviewDto(cartItemDto);
        return reviewDto;
    }

	@Override
	public Page<GoodsQnADto> findbyGoodsQnA(int sortValue, int page, Integer category) {
		Pageable pageable = PageRequest.of(page,10);
		Page<GoodsQnA> goodsQnAs = goodsQnARepository.findAllByCategoryAndState(sortValue, category, pageable);
		 return goodsQnAs.map(goodsQnA -> {
	            GoodsQnADto goodsQnADto = new GoodsQnADto(goodsQnA);
	            return goodsQnADto;
	        });
	}

    @Override
    public void saveAnswer(GoodsQnADto goodsQnADto) {
        GoodsQnA goodsQna = goodsQnARepository.findById(goodsQnADto.getIdx()).orElse(null);
        goodsQna.saveAnswer(goodsQnADto);
        goodsQnARepository.save(goodsQna);
    }

    @Override
    public void save(ReviewDto reviewDto) throws IOException {
        List<GoodsReviewFile> goodsReviewFiles = new ArrayList<>();

        Goods goods = goodsRepository.findById(reviewDto.getGoodsIdx()).orElse(null);
        Member member = memberRepository.findByid(reviewDto.getMemberId()).orElse(null);
        memberRepository.updateMemberPoint(member, member.getPoint()+100);
        OrderItem orderItem = orderItemRepository.findById(reviewDto.getOrderItemIdx()).orElse(null);
        orderItem.setState(1);
        GoodsReview goodsReview = new GoodsReview(reviewDto, goods,member, orderItem);

        for (FileDto fileDto : reviewDto.getFileDtos()) {
            if (!(fileDto.getFile().isEmpty())) {
                FileDto nfileDto = fileService.insert(fileDto.getFile());
                nfileDto.setGoodsReview(goodsReview);
                GoodsReviewFile goodsReviewFile = new GoodsReviewFile(nfileDto);
                goodsReviewFiles.add(goodsReviewFile);
            }
        }
        goodsReview.getGoodsReviewFiles().addAll(goodsReviewFiles);
        goodsReviewRepository.save(goodsReview);
    }

    @Override
    public List<ReviewDto> findAll(GoodsDto goodsdto) {
        Goods goods = goodsRepository.findById(goodsdto.getIdx()).orElseThrow(null);
        List<GoodsReview> goodsReviews = goodsReviewRepository.findByGoodsOrderByIdxDesc(goods);
        List<ReviewDto> reviewDtos = new ArrayList<>();
        for(GoodsReview goodsReview: goodsReviews){
            ReviewDto reviewDto = new ReviewDto(goodsReview);
            reviewDtos.add(reviewDto);
            System.out.println(reviewDto);
        }
        return reviewDtos;
    }

    @Override
    public List<ReviewDto> findAll(String id) {
        Member member = memberRepository.findByid(id).orElseThrow();
        List<GoodsReview> goodsReviews = goodsReviewRepository.findByMemberOrderByIdxDesc(member);
        List<ReviewDto> reviewDtos = new ArrayList<>();
        for(GoodsReview goodsReview: goodsReviews){
            ReviewDto reviewDto = new ReviewDto(goodsReview);
            reviewDtos.add(reviewDto);
        }
        return reviewDtos;
    }
}
