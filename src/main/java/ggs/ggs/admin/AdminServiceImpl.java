package ggs.ggs.admin;

import ggs.ggs.domain.Goods;
import ggs.ggs.dto.GoodsDto;
import ggs.ggs.goods.GoodsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    @Autowired
    private final GoodsRepository goodsRepository;
    @Override
    public List<GoodsDto> findByOrderDateRange(LocalDate start, LocalDate end) {
        LocalDateTime startDateTime = start.atStartOfDay();
        LocalDateTime endDateTime = end.atTime(23, 59, 59);

        List<Object[]> byOrderDateRange = goodsRepository.findByOrderDateRange(startDateTime, endDateTime);
        return byOrderDateRange.stream()
                .map(arr -> {
                    Goods goods = (Goods) arr[0];
                    Long orderCount = (Long) arr[1];
                    return new GoodsDto(goods,orderCount.intValue());
                })
                .collect(Collectors.toList());

    }

    @Override
    public List<GoodsDto> findByLikeDateRange(LocalDate start, LocalDate end) {
        LocalDateTime startDateTime = start.atStartOfDay();
        LocalDateTime endDateTime = end.atTime(23, 59, 59);
        List<Object[]> byLikeDateRange = goodsRepository.findByOrderDateRange(startDateTime, endDateTime);
        return byLikeDateRange.stream()
                .map(arr -> {
                    Goods goods = (Goods) arr[0];
                    Long orderCount = (Long) arr[1];
                    return new GoodsDto(goods,orderCount.intValue());
                })
                .collect(Collectors.toList());
    }
}
