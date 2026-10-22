package org.dromara.nail.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.satoken.utils.LoginHelper;
import org.dromara.nail.domain.NailCommission;
import org.dromara.nail.domain.NailEmployee;
import org.dromara.nail.domain.NailEmployeePerformance;
import org.dromara.nail.domain.NailMember;
import org.dromara.nail.domain.NailMemberBalanceLog;
import org.dromara.nail.domain.NailMemberLevel;
import org.dromara.nail.domain.NailMemberPointLog;
import org.dromara.nail.domain.NailOrder;
import org.dromara.nail.domain.NailOrderItem;
import org.dromara.nail.domain.NailServiceItem;
import org.dromara.nail.domain.bo.NailCheckoutBo;
import org.dromara.nail.domain.vo.NailEmployeePerformanceRankVo;
import org.dromara.nail.domain.vo.NailEmployeeVo;
import org.dromara.nail.domain.vo.NailOrderVo;
import org.dromara.nail.domain.vo.NailPerformanceMemberCheckVo;
import org.dromara.nail.domain.vo.NailServiceItemVo;
import org.dromara.nail.helper.NailDataScopeHelper;
import org.dromara.nail.mapper.NailCommissionMapper;
import org.dromara.nail.mapper.NailEmployeeMapper;
import org.dromara.nail.mapper.NailEmployeePerformanceMapper;
import org.dromara.nail.mapper.NailMemberBalanceLogMapper;
import org.dromara.nail.mapper.NailMemberLevelMapper;
import org.dromara.nail.mapper.NailMemberMapper;
import org.dromara.nail.mapper.NailMemberPointLogMapper;
import org.dromara.nail.mapper.NailOrderItemMapper;
import org.dromara.nail.mapper.NailOrderMapper;
import org.dromara.nail.mapper.NailServiceItemMapper;
import org.dromara.nail.service.INailOrderCheckoutService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 收银结算
 */
@RequiredArgsConstructor
@Service
public class NailOrderCheckoutServiceImpl implements INailOrderCheckoutService {

    private final NailServiceItemMapper itemMapper;
    private final NailMemberMapper memberMapper;
    private final NailMemberLevelMapper levelMapper;
    private final NailMemberBalanceLogMapper balanceLogMapper;
    private final NailMemberPointLogMapper pointLogMapper;
    private final NailOrderMapper orderMapper;
    private final NailOrderItemMapper orderItemMapper;
    private final NailEmployeeMapper employeeMapper;
    private final NailCommissionMapper commissionMapper;
    private final NailEmployeePerformanceMapper performanceMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public NailOrderVo checkout(NailCheckoutBo bo) {
        NailServiceItem item = itemMapper.selectById(bo.getItemId());
        if (item == null || !"0".equals(item.getStatus())) {
            throw new ServiceException("服务项目不可用");
        }
        int qty = bo.getQuantity() == null || bo.getQuantity() < 1 ? 1 : bo.getQuantity();
        NailMember member = resolveMember(bo);
        BigDecimal unit = nvl(item.getPrice());
        if (member != null && item.getMemberPrice() != null) {
            unit = item.getMemberPrice();
        }
        BigDecimal total = bo.getAmount() != null && bo.getAmount().compareTo(BigDecimal.ZERO) > 0
            ? bo.getAmount().setScale(2, RoundingMode.HALF_UP)
            : unit.multiply(BigDecimal.valueOf(qty)).setScale(2, RoundingMode.HALF_UP);
        BigDecimal discount = nvl(bo.getDiscountAmount());
        BigDecimal pay = total.subtract(discount);
        if (pay.compareTo(BigDecimal.ZERO) < 0) {
            pay = BigDecimal.ZERO;
        }
        String payType = StringUtils.blankToDefault(bo.getPayType(), "cash");
        if (member != null && "balance".equals(payType)) {
            BigDecimal before = nvl(member.getBalance());
            if (before.compareTo(pay) < 0) {
                throw new ServiceException("会员余额不足");
            }
            BigDecimal after = before.subtract(pay);
            member.setBalance(after);
            NailMemberBalanceLog log = new NailMemberBalanceLog();
            log.setMemberId(member.getId());
            log.setChangeType("consume");
            log.setAmount(pay.negate());
            log.setBeforeBalance(before);
            log.setAfterBalance(after);
            log.setRemark("收银消费");
            balanceLogMapper.insert(log);
        }
        NailOrder order = new NailOrder();
        order.setOrderNo("DD" + System.currentTimeMillis());
        order.setMemberId(member == null ? null : member.getId());
        order.setEmployeeId(bo.getEmployeeId());
        order.setCustomerName(StringUtils.blankToDefault(bo.getCustomerName(), member == null ? null : member.getName()));
        order.setCustomerPhone(StringUtils.blankToDefault(bo.getCustomerPhone(), member == null ? null : member.getPhone()));
        order.setStoreId(bo.getStoreId());
        NailDataScopeHelper.fillStoreId(order);
        order.setOrderTime(new Date());
        order.setPayType(payType);
        order.setTotalAmount(total);
        order.setDiscountAmount(discount);
        order.setPayAmount(pay);
        int points = pay.setScale(0, RoundingMode.DOWN).intValue();
        order.setPointAmount(points);
        order.setPraise(StringUtils.blankToDefault(bo.getPraise(), "0"));
        order.setPraiseImages("1".equals(order.getPraise()) ? bo.getPraiseImages() : null);
        order.setStatus("1");
        order.setRemark(bo.getRemark());
        orderMapper.insert(order);

        NailOrderItem orderItem = new NailOrderItem();
        orderItem.setOrderId(order.getId());
        orderItem.setItemId(item.getId());
        orderItem.setItemName(item.getItemName());
        orderItem.setQuantity(qty);
        orderItem.setPrice(unit);
        orderItem.setAmount(total);
        orderItemMapper.insert(orderItem);

        if (member != null) {
            member.setTotalConsume(nvl(member.getTotalConsume()).add(pay));
            member.setConsumeTimes((member.getConsumeTimes() == null ? 0 : member.getConsumeTimes()) + 1);
            member.setLastVisitTime(new Date());
            if (points > 0) {
                int beforePoint = member.getPoint() == null ? 0 : member.getPoint();
                int afterPoint = beforePoint + points;
                member.setPoint(afterPoint);
                NailMemberPointLog pointLog = new NailMemberPointLog();
                pointLog.setMemberId(member.getId());
                pointLog.setChangeType("consume");
                pointLog.setPoint(points);
                pointLog.setBeforePoint(beforePoint);
                pointLog.setAfterPoint(afterPoint);
                pointLog.setBizNo(order.getOrderNo());
                pointLog.setRemark("消费赠送积分");
                pointLogMapper.insert(pointLog);
            }
            memberMapper.updateById(member);
        }

        if (bo.getEmployeeId() != null && pay.compareTo(BigDecimal.ZERO) > 0) {
            NailEmployee employee = employeeMapper.selectById(bo.getEmployeeId());
            if (employee != null && employee.getCommissionRate() != null) {
                BigDecimal rate = employee.getCommissionRate();
                BigDecimal amount = pay.multiply(rate).setScale(2, RoundingMode.HALF_UP);
                NailCommission commission = new NailCommission();
                commission.setEmployeeId(employee.getId());
                commission.setOrderId(order.getId());
                commission.setOrderNo(order.getOrderNo());
                commission.setCommissionRate(rate);
                commission.setCommissionAmount(amount);
                commission.setStatus("0");
                commissionMapper.insert(commission);
            }
            saveCheckoutPerformance(order, item);
        }
        return orderMapper.selectVoById(order.getId());
    }

    @Override
    public Map<String, Object> options() {
        LambdaQueryWrapper<NailEmployee> employeeQuery = Wrappers.lambdaQuery();
        employeeQuery.eq(NailEmployee::getStatus, "0");
        if (!LoginHelper.isSuperAdmin()) {
            Long currentEmployeeId = NailDataScopeHelper.currentEmployeeId();
            if (currentEmployeeId != null) {
                employeeQuery.eq(NailEmployee::getId, currentEmployeeId);
            }
        }
        List<NailEmployeeVo> employees = employeeMapper.selectVoList(employeeQuery);
        LambdaQueryWrapper<NailServiceItem> itemQuery = Wrappers.lambdaQuery();
        itemQuery.eq(NailServiceItem::getStatus, "0");
        itemQuery.orderByAsc(NailServiceItem::getSort);
        List<NailServiceItemVo> items = itemMapper.selectVoList(itemQuery);
        return Map.of("employees", employees, "items", items);
    }

    @Override
    public NailPerformanceMemberCheckVo checkMember(String phone, String name) {
        NailPerformanceMemberCheckVo result = new NailPerformanceMemberCheckVo();
        result.setMember(false);
        String phoneValue = StringUtils.trim(phone);
        String nameValue = StringUtils.trim(name);
        if (StringUtils.isBlank(phoneValue) && StringUtils.isBlank(nameValue)) {
            return result;
        }
        LambdaQueryWrapper<NailMember> lqw = Wrappers.lambdaQuery();
        lqw.eq(NailMember::getStatus, "0");
        if (StringUtils.isNotBlank(phoneValue)) {
            lqw.eq(NailMember::getPhone, phoneValue);
        } else {
            lqw.eq(NailMember::getName, nameValue);
        }
        NailDataScopeHelper.applyStoreScope(lqw, "NailMember");
        lqw.orderByDesc(NailMember::getId);
        lqw.last("LIMIT 1");
        NailMember member = memberMapper.selectOne(lqw);
        if (member == null) {
            return result;
        }
        result.setMember(true);
        result.setMemberId(member.getId());
        result.setMemberNo(member.getMemberNo());
        result.setName(member.getName());
        result.setPhone(member.getPhone());
        return result;
    }

    @Override
    public List<NailEmployeePerformanceRankVo> ranking(String period) {
        LocalDate today = LocalDate.now();
        LocalDate startDate = today;
        if ("week".equals(period)) {
            startDate = today.with(DayOfWeek.MONDAY);
        } else if ("month".equals(period)) {
            startDate = today.withDayOfMonth(1);
        }
        Date start = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date end = Date.from(today.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        LambdaQueryWrapper<NailOrder> lqw = Wrappers.lambdaQuery();
        lqw.ge(NailOrder::getOrderTime, start);
        lqw.lt(NailOrder::getOrderTime, end);
        lqw.isNotNull(NailOrder::getEmployeeId);
        NailDataScopeHelper.applyStoreScope(lqw, "NailOrder");
        List<NailOrder> orders = orderMapper.selectList(lqw);
        Map<Long, NailEmployeePerformanceRankVo> ranks = new LinkedHashMap<>();
        for (NailOrder order : orders) {
            NailEmployeePerformanceRankVo row = ranks.computeIfAbsent(order.getEmployeeId(), id -> {
                NailEmployeePerformanceRankVo created = new NailEmployeePerformanceRankVo();
                created.setEmployeeId(id);
                created.setTotalAmount(BigDecimal.ZERO);
                created.setServiceCount(0L);
                created.setPraiseCount(0L);
                return created;
            });
            row.setTotalAmount(row.getTotalAmount().add(nvl(order.getPayAmount())));
            row.setServiceCount(row.getServiceCount() + 1);
            if ("1".equals(order.getPraise())) {
                row.setPraiseCount(row.getPraiseCount() + 1);
            }
        }
        List<NailEmployeePerformanceRankVo> result = new ArrayList<>(ranks.values());
        result.sort((a, b) -> b.getTotalAmount().compareTo(a.getTotalAmount()));
        for (int i = 0; i < result.size(); i++) {
            NailEmployeePerformanceRankVo row = result.get(i);
            row.setRank(i + 1);
            NailEmployee employee = employeeMapper.selectById(row.getEmployeeId());
            row.setEmployeeName(employee == null ? "未知员工" : employee.getName());
        }
        return result;
    }

    /**
     * 收银订单写入一条员工业绩。同一 orderId 已有记录时跳过，避免重复提交插两条。
     */
    private void saveCheckoutPerformance(NailOrder order, NailServiceItem item) {
        LambdaQueryWrapper<NailEmployeePerformance> exists = Wrappers.lambdaQuery();
        exists.eq(NailEmployeePerformance::getOrderId, order.getId());
        if (performanceMapper.selectCount(exists) > 0) {
            return;
        }
        NailEmployeePerformance performance = new NailEmployeePerformance();
        performance.setOrderId(order.getId());
        performance.setEmployeeId(order.getEmployeeId());
        performance.setStoreId(order.getStoreId());
        performance.setAmount(order.getPayAmount());
        performance.setPayType(order.getPayType());
        performance.setItemId(item.getId());
        performance.setItemName(item.getItemName());
        performance.setCustomerName(order.getCustomerName());
        performance.setCustomerPhone(order.getCustomerPhone());
        performance.setConsumeTime(order.getOrderTime());
        performance.setPraise(StringUtils.blankToDefault(order.getPraise(), "0"));
        if ("1".equals(performance.getPraise())) {
            performance.setPraiseImages(order.getPraiseImages());
        }
        performance.setRemark(checkoutPerformanceRemark(order));
        try {
            performanceMapper.insert(performance);
        } catch (RuntimeException ex) {
            if (!isDuplicatePerformance(ex)) {
                throw ex;
            }
        }
    }

    private String checkoutPerformanceRemark(NailOrder order) {
        String prefix = "收银订单" + StringUtils.blankToDefault(order.getOrderNo(), "");
        if (StringUtils.isBlank(order.getRemark())) {
            return prefix;
        }
        String remark = prefix + " " + order.getRemark().trim();
        return remark.length() <= 500 ? remark : remark.substring(0, 500);
    }

    private boolean isDuplicatePerformance(Throwable ex) {
        Throwable current = ex;
        while (current != null) {
            if (current instanceof DuplicateKeyException) {
                return true;
            }
            if (current instanceof java.sql.SQLException sqlException && sqlException.getErrorCode() == 1062) {
                return true;
            }
            current = current.getCause();
        }
        return false;
    }

    private NailMember resolveMember(NailCheckoutBo bo) {
        if (bo.getMemberId() != null) {
            NailMember member = memberMapper.selectById(bo.getMemberId());
            if (member == null) {
                throw new ServiceException("会员不存在");
            }
            return member;
        }
        NailPerformanceMemberCheckVo checked = checkMember(bo.getCustomerPhone(), bo.getCustomerName());
        if (!Boolean.TRUE.equals(checked.getMember())) {
            return null;
        }
        return memberMapper.selectById(checked.getMemberId());
    }

    private BigDecimal nvl(BigDecimal val) {
        return val == null ? BigDecimal.ZERO : val;
    }
}
