package org.dromara.nail.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.dromara.nail.domain.NailEmployee;
import org.dromara.nail.domain.NailMember;
import org.dromara.nail.domain.NailOrder;
import org.dromara.nail.domain.vo.NailDashboardVo;
import org.dromara.nail.mapper.NailEmployeeMapper;
import org.dromara.nail.mapper.NailMemberMapper;
import org.dromara.nail.mapper.NailOrderMapper;
import org.dromara.nail.service.INailDashboardService;
import org.dromara.nail.service.INailMemberWalletService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 营业统计
 */
@RequiredArgsConstructor
@Service
public class NailDashboardServiceImpl implements INailDashboardService {

    private final NailMemberMapper memberMapper;
    private final NailEmployeeMapper employeeMapper;
    private final NailOrderMapper orderMapper;
    private final INailMemberWalletService walletService;

    @Override
    public NailDashboardVo overview() {
        NailDashboardVo vo = new NailDashboardVo();
        vo.setMemberCount(memberMapper.selectCount(new LambdaQueryWrapper<NailMember>().eq(NailMember::getStatus, "0")));
        vo.setEmployeeCount(employeeMapper.selectCount(new LambdaQueryWrapper<NailEmployee>().eq(NailEmployee::getStatus, "0")));
        Date todayStart = toDate(LocalDate.now().atStartOfDay());
        Date todayEnd = toDate(LocalDate.now().atTime(LocalTime.MAX));
        Date monthStart = toDate(LocalDate.now().withDayOfMonth(1).atStartOfDay());
        List<NailOrder> todayOrders = orderMapper.selectList(new LambdaQueryWrapper<NailOrder>()
            .eq(NailOrder::getStatus, "1")
            .between(NailOrder::getOrderTime, todayStart, todayEnd));
        vo.setTodayOrderCount((long) todayOrders.size());
        vo.setTodayAmount(sumPay(todayOrders));
        List<NailOrder> monthOrders = orderMapper.selectList(new LambdaQueryWrapper<NailOrder>()
            .eq(NailOrder::getStatus, "1")
            .ge(NailOrder::getOrderTime, monthStart));
        vo.setMonthAmount(sumPay(monthOrders));
        vo.setBirthdayCount((long) walletService.birthdayRemind(7).size());

        LocalDate start = LocalDate.now().minusDays(6);
        List<NailMember> members = memberMapper.selectList(new LambdaQueryWrapper<NailMember>()
            .ge(NailMember::getCreateTime, toDate(start.atStartOfDay())));
        List<NailOrder> weekOrders = orderMapper.selectList(new LambdaQueryWrapper<NailOrder>()
            .eq(NailOrder::getStatus, "1")
            .ge(NailOrder::getOrderTime, toDate(start.atStartOfDay())));
        for (int i = 0; i < 7; i++) {
            LocalDate day = start.plusDays(i);
            long mc = members.stream().filter(m -> m.getCreateTime() != null && sameDay(m.getCreateTime(), day)).count();
            BigDecimal amt = weekOrders.stream()
                .filter(o -> o.getOrderTime() != null && sameDay(o.getOrderTime(), day))
                .map(o -> o.getPayAmount() == null ? BigDecimal.ZERO : o.getPayAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            vo.getMemberTrend().add(new NailDashboardVo.NameValue(day.toString(), BigDecimal.valueOf(mc)));
            vo.getRevenueTrend().add(new NailDashboardVo.NameValue(day.toString(), amt));
        }
        Map<Long, BigDecimal> rank = monthOrders.stream()
            .filter(o -> o.getEmployeeId() != null)
            .collect(Collectors.groupingBy(NailOrder::getEmployeeId,
                Collectors.reducing(BigDecimal.ZERO, o -> nvl(o.getPayAmount()), BigDecimal::add)));
        rank.entrySet().stream()
            .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
            .limit(5)
            .forEach(e -> {
                NailEmployee emp = employeeMapper.selectById(e.getKey());
                String name = emp == null ? String.valueOf(e.getKey()) : emp.getName();
                vo.getEmployeeRank().add(new NailDashboardVo.NameValue(name, e.getValue()));
            });
        return vo;
    }

    private boolean sameDay(Date date, LocalDate day) {
        LocalDate local = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return local.equals(day);
    }

    private Date toDate(LocalDateTime time) {
        return Date.from(time.atZone(ZoneId.systemDefault()).toInstant());
    }

    private BigDecimal sumPay(List<NailOrder> orders) {
        return orders.stream().map(o -> nvl(o.getPayAmount())).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal nvl(BigDecimal val) {
        return val == null ? BigDecimal.ZERO : val;
    }
}
