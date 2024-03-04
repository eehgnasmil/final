package ggs.ggs.admin;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import ggs.ggs.board.BoardService;
import ggs.ggs.dto.GoodsDto;
import ggs.ggs.dto.GoodsQnADto;
import ggs.ggs.goods.GoodsCSService;
import ggs.ggs.goods.GoodsService;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    @Autowired
    @Qualifier("adminServiceImpl")
    private final AdminService adminService;

    @Autowired
    @Qualifier("boardServiceImpl")
    private final BoardService boardService;

    @Autowired
    @Qualifier("goodsServiceImpl")
    private final GoodsService goodsService;
    private String id = null;
    
    @Autowired
    @Qualifier("goodsCSServiceImpl")
    private final GoodsCSService goodsCSService;
    
    //관리자
    @GetMapping("/{title}")
    public String myModify(@PathVariable("title") String title, Model model, Authentication authentication,
    		@RequestParam(value = "category", defaultValue = "0") Integer category
            , @RequestParam(value = "sortValue", defaultValue = "0") Integer sortValue
            , @RequestParam(value = "page", defaultValue = "0") int page) {

        // 사용자 id 가져오기(SecurityContextHolder)
        authentication = SecurityContextHolder.getContext().getAuthentication();
        id = authentication.getName();
       
        switch (title) {

            case "dashboard":
                System.out.println("dddd");
                LocalDate end = LocalDate.now();

                LocalDate start = end.minusMonths(1);
                List<GoodsDto> order = adminService.findByOrderDateRange(start,end);
                List<GoodsDto> like =adminService.findByOrderDateRange(start,end);
                //페이지 정보 외 페이지로 보낼 데이터
                model.addAttribute("title", title);
                model.addAttribute("order", order);
                model.addAttribute("like", like);

                break;

            case "goodsForm":
                model.addAttribute("goods", new GoodsDto());
                break;

            case "goodsList":
            	Page<GoodsDto> goods = goodsService.findAll(sortValue, page, category);
            	model.addAttribute("goods", goods);
            	model.addAttribute("sortValue", sortValue);
                model.addAttribute("category", category);
                break;
                
            case "goodsCs":
                Page<GoodsQnADto> goodsQnADtos = goodsCSService.findbyGoodsQnA(sortValue, page,category);
                model.addAttribute("goodsQnAs", goodsQnADtos);
                model.addAttribute("sortValue", sortValue);
                model.addAttribute("category", category);
            	break;

            case "myBoard":

                break;

            default:
                break;
        }

        //수정


        //삭제

        //리플레이스 할 페이지 정보
        model.addAttribute("title", title);

        return "/admin/admin";
    }

    @PostMapping("/deleteGoods")
    public ResponseEntity<String> deleteGoods(@RequestBody List<Integer> goodsItems) {
        System.out.println(goodsItems);
        for(Integer goodsItem:goodsItems){
//            goodsService.delete(goodsItem);
        }
        return ResponseEntity.ok(goodsItems.size() + "개가 삭제되었습니다.");
    }

    @PostMapping("/orderChart")
    public ResponseEntity<String> orderChart(@RequestParam("startDate") LocalDate start, @RequestParam("endDate") LocalDate end) {
//        goodsService.dashBoard
        System.out.println(start);
        System.out.println(end);
        adminService.findByOrderDateRange(start,end);
        return ResponseEntity.ok("개가 삭제되었습니다.");
    }

    @PostMapping("/likeChart")
    public ResponseEntity<String> likeChart(@RequestParam("startDate") LocalDate start, @RequestParam("endDate") LocalDate end) {
//        goodsService.dashBoard
        System.out.println(start);
        System.out.println(end);
        adminService.findByLikeDateRange(start,end);
        return ResponseEntity.ok("개가 삭제되었습니다.");
    }

}
