package ggs.ggs.goods;

import ggs.ggs.domain.Goods;
import ggs.ggs.domain.GoodsQnA;
import ggs.ggs.domain.Member;
import ggs.ggs.domain.OrderItem;
import ggs.ggs.dto.CartItemDto;
import ggs.ggs.dto.GoodsQnADto;
import ggs.ggs.dto.ReviewDto;
import ggs.ggs.member.MemberRepository;
import ggs.ggs.order.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Qualifier("goodsCSServiceImpl")
@RequiredArgsConstructor
public class GoodsCSServiceImpl implements GoodsCSService{

    @Autowired
    private final GoodsRepository goodsRepository;

    @Autowired
    private final GoodsQnARepository goodsQnARepository;

    @Autowired
    private final MemberRepository memberRepository;

    @Autowired
    private final OrderItemRepository orderItemRepository;

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
}
