package ggs.ggs.goods;

import ggs.ggs.dto.GoodsDto;
import ggs.ggs.dto.GoodsQnADto;
import ggs.ggs.dto.ReviewDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@RequestMapping("/goodsCS")
@RequiredArgsConstructor
public class GoodsCSController {
    @Autowired
    @Qualifier("goodsServiceImpl")
    private final GoodsService goodsService;
    @Autowired
    private final GoodsCSService goodsCSService;
    @GetMapping("/goodsQuestion/{idx}")

    public String goodsQnAForm(@PathVariable("idx") Integer idx, Model model, Authentication authentication){
        String sid = authentication.getName();

        GoodsDto goodsDto = goodsService.getGoods(idx);

        model.addAttribute("goods",goodsDto);
        GoodsQnADto goodsQnADto = new GoodsQnADto();
        goodsQnADto.setGoodsIdx(idx);
        goodsQnADto.setMemberId(sid);
        model.addAttribute("question",goodsQnADto);

        return "/goods/questionForm";
    }

    @PostMapping("/goodsQusetion")
    public String GoodsQnAList(@ModelAttribute GoodsQnADto goodsQnADto,Model model,  Authentication authentication){
        String sid = authentication.getName();
        goodsQnADto.setMemberId(sid);
        goodsCSService.save(goodsQnADto);
        return "redirect:/mypage/myGoodsQnAList";
    }

    @ResponseBody
    @PostMapping("/goodsAnswer")
    public String goodsAnswer(@RequestParam("idx")Integer idx,@RequestParam("answer")String answer){
        GoodsQnADto goodsQnADto = new GoodsQnADto(idx,answer);
        goodsCSService.saveAnswer(goodsQnADto);
        return "답변이 등록되었습니다.";
    }

    @GetMapping("/goodsReview/{idx}")
    public String goodsReviewForm(@PathVariable("idx") Integer idx, Model model, Authentication authentication){
        String sid = authentication.getName();
        System.out.println(idx);
        ReviewDto reviewDto = goodsCSService.findbyOrderItem(idx);
        GoodsDto goodsDto = goodsService.getGoods(reviewDto.getCartItemDto().getGoodsDto().getIdx());
        reviewDto.setGoodsIdx(goodsDto.getIdx());
        model.addAttribute("goods",goodsDto);
        model.addAttribute("review", reviewDto);

        return "/goods/reviewForm";
    }

    @PostMapping("/goodsReview")
    public String  GoodsReviewList(@ModelAttribute ReviewDto reviewDto,Model model,  Authentication authentication) throws IOException {
        String sid = authentication.getName();
        reviewDto.setMemberId(sid);
        goodsCSService.save(reviewDto);

        return "redirect:/mypage/myGoodsReviewList";
    }


}