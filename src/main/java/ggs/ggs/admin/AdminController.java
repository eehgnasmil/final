package ggs.ggs.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import ggs.ggs.board.BoardService;
import ggs.ggs.domain.Member;
import ggs.ggs.dto.BoardDto;
import ggs.ggs.dto.GoodsDto;
import ggs.ggs.dto.GoodsQnADto;
import ggs.ggs.dto.MemberDto;
import ggs.ggs.dto.ReplyDto;
import ggs.ggs.goods.GoodsCSService;
import ggs.ggs.goods.GoodsService;
import ggs.ggs.member.MemberRepository;
import ggs.ggs.member.MemberService;
import ggs.ggs.service.ReplyService;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    @Autowired
    @Qualifier("adminServiceImpl")
    private final AdminService adminService;

    @Autowired
    @Qualifier("memberServiceImpl")
    private final MemberService memberService;
    private final MemberRepository memberRepository;

    @Autowired
    @Qualifier("boardServiceImpl")
    private final BoardService boardService;

    @Autowired
    @Qualifier("boardReplyServiceImpl")
    private final ReplyService boardReplyService;

    @Autowired
    @Qualifier("goodsServiceImpl")
    private final GoodsService goodsService;
    private String id = null;

    @Autowired
    @Qualifier("goodsCSServiceImpl")
    private final GoodsCSService goodsCSService;

    // 관리자
    @GetMapping("/{title}")
    public String myModify(@PathVariable("title") String title, Model model, Authentication authentication,
            @RequestParam(value = "category", defaultValue = "0") Integer category,
            @RequestParam(value = "sortValue", defaultValue = "0") Integer sortValue,
            @RequestParam(value = "page", defaultValue = "0") int page) {

        // 사용자 id 가져오기(SecurityContextHolder)
        authentication = SecurityContextHolder.getContext().getAuthentication();
        id = authentication.getName();

        switch (title) {

            case "dashboard":
                System.out.println("dddd");
                LocalDate end = LocalDate.now();

                LocalDate start = end.minusMonths(1);
                List<GoodsDto> order = adminService.findByOrderDateRange(start, end);
                List<GoodsDto> like = adminService.findByOrderDateRange(start, end);
                // 페이지 정보 외 페이지로 보낼 데이터
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
                Page<GoodsQnADto> goodsQnADtos = goodsCSService.findbyGoodsQnA(sortValue, page, category);
                model.addAttribute("goodsQnAs", goodsQnADtos);
                model.addAttribute("sortValue", sortValue);
                model.addAttribute("category", category);
                break;

            case "noticeBoard":
                Member member = memberRepository.findByid(id)
                        .orElseThrow(() -> new UsernameNotFoundException("Member not found with ID: " + id));
                String nickname = member.getNick();
                model.addAttribute("nickname", nickname);

                break;
            case "reportBoard":
                List<BoardDto> reportedBoards = boardService.getReportedBoards();
                model.addAttribute("reportedBoards", reportedBoards);
                break;
            case "reportBoardReply":
                List<ReplyDto> reportedReplies = boardReplyService.getReportedReplies();
                System.out.println(reportedReplies + "asdgjhalskdghasdhgas");
                model.addAttribute("reportedReplies", reportedReplies);

                break;
            case "authorization":
                List<MemberDto> members = memberService.getAllMembers();
                model.addAttribute("members", members);
                break;

            default:
                break;
        }

        // 수정

        // 삭제

        // 리플레이스 할 페이지 정보
        model.addAttribute("title", title);

        return "/admin/admin";
    }

    @PostMapping("/deleteGoods")
    public ResponseEntity<String> deleteGoods(@RequestBody List<Integer> goodsItems) {
        System.out.println(goodsItems);
        for (Integer goodsItem : goodsItems) {
            // goodsService.delete(goodsItem);
        }
        return ResponseEntity.ok(goodsItems.size() + "개가 삭제되었습니다.");
    }

    @PostMapping("/orderChart")
    public ResponseEntity<String> orderChart(@RequestParam("startDate") LocalDate start,
            @RequestParam("endDate") LocalDate end) {
        // goodsService.dashBoard
        System.out.println(start);
        System.out.println(end);
        adminService.findByOrderDateRange(start, end);
        return ResponseEntity.ok("개가 삭제되었습니다.");
    }

    @PostMapping("/likeChart")
    public ResponseEntity<String> likeChart(@RequestParam("startDate") LocalDate start,
            @RequestParam("endDate") LocalDate end) {
        // goodsService.dashBoard
        System.out.println(start);
        System.out.println(end);
        adminService.findByLikeDateRange(start, end);
        return ResponseEntity.ok("개가 삭제되었습니다.");
    }

    @PostMapping("/deleteMember/{memberIdx}")
    public String deleteMember(@PathVariable("memberIdx") Integer memberIdx) {
        memberService.deleteMember(memberIdx);
        return "redirect:/admin/authorization";
    }

    @PostMapping("/changeRole/{memberIdx}")
    public String changeRole(@PathVariable("memberIdx") Integer memberIdx, @RequestParam("newRole") String newRole) {
        memberService.changeRole(memberIdx, Member.Role.valueOf(newRole));
        System.out.println(Member.Role.valueOf(newRole) + "aksjgalskdjhgasd");
        return "redirect:/admin/authorization";
    }

    @PostMapping("/deleteBoard/{boardIdx}")
    public String deleteBoard(@PathVariable("boardIdx") Long boardIdx) {
        boardService.AdmindeleteBoard(boardIdx);
        return "redirect:/admin/reportBoard";
    }

    @PostMapping("/punishBoard/{boardIdx}")
    public String punishBoard(@PathVariable("boardIdx") Long boardIdx) {
        boardService.punishBoard(boardIdx);
        return "redirect:/admin/reportBoard";
    }

    @PostMapping("/unpunishBoard/{boardIdx}")
    public String unpunishBoard(@PathVariable("boardIdx") Long boardIdx) {
        boardService.unpunishBoard(boardIdx);
        return "redirect:/admin/reportBoard";
    }

    @PostMapping("/deleteReply/{idx}")
    public String deleteReply(@PathVariable("idx") Long idx) {
        boardReplyService.deleteReply(idx);
        return "redirect:/admin/reportBoardReply";
    }

    @PostMapping("/punishReply/{idx}")
    public String punishReply(@PathVariable("idx") Long idx) {
        boardReplyService.punishReply(idx);
        return "redirect:/admin/reportBoardReply";
    }

    @PostMapping("/unpunishReply/{idx}")
    public String unpunishReply(@PathVariable("idx") Long idx) {
        boardReplyService.unpunishReply(idx);
        return "redirect:/admin/reportBoardReply";
    }

}
