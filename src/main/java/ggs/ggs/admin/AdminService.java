package ggs.ggs.admin;

import ggs.ggs.dto.GoodsDto;

import java.time.LocalDate;
import java.util.List;

public interface AdminService{
    List<GoodsDto> findByOrderDateRange(LocalDate start, LocalDate end);

    List<GoodsDto> findByLikeDateRange(LocalDate start, LocalDate end);
}
