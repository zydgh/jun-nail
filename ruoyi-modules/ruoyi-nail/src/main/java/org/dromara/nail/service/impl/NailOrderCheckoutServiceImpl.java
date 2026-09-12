package org.dromara.nail.service.impl;

import lombok.RequiredArgsConstructor;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.nail.domain.NailCommission;
import org.dromara.nail.domain.NailEmployee;
import org.dromara.nail.domain.NailMember;
import org.dromara.nail.domain.NailMemberBalanceLog;
import org.dromara.nail.domain.NailMemberLevel;
import org.dromara.nail.domain.NailMemberPointLog;
import org.dromara.nail.domain.NailOrder;
import org.dromara.nail.domain.NailOrderItem;
import org.dromara.nail.domain.NailServiceItem;
import org.dromara.nail.domain.bo.NailCheckoutBo;
import org.dromara.nail.domain.vo.NailOrderVo;
import org.dromara.nail.helper.NailDataScopeHelper;
import org.dromara.nail.mapper.NailCommissionMapper;
import org.dromara.nail.mapper.NailEmployeeMapper;
import org.dromara.nail.mapper.NailMemberBalanceLogMapper;
import org.dromara.nail.mapper.NailMemberLevelMapper;
import org.dromara.nail.mapper.NailMemberMapper;
import org.dromara.nail.mapper.NailMemberPointLogMapper;
import org.dromara.nail.mapper.NailOrderItemMapper;
import org.dromara.nail.mapper.NailOrderMapper;
import org.dromara.nail.mapper.NailServiceItemMapper;
import org.dromara.nail.service.INailOrderCheckoutService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public NailOrderVo checkout(NailCheckoutBo bo) {
        NailServiceItem item = itemMapper.selectById(bo.getItemId());
        if (item == null || !"0".equals(item.getStatus())) {
            throw new ServiceException("服务项目不可用");
        }
        int qty = bo.getQuantity() == null || bo.getQuantity() < 1 ? 1 : bo.getQuantity();
        BigDecimal unit = nvl(item.getPrice());
        NailMember member = null;
        if (bo.getMemberId() != null) {
            member = memberMapper.selectById(bo.getMemberId());
            if (member == null) {
                throw new ServiceException("会员不存在");
            }
            if (item.getMemberPrice() != null) {
                unit = item.getMemberPrice();
            }
            if (member.getLevelId() != null) {
                NailMemberLevel level = levelMapper.selectById(member.getLevelId());
                if (level != null && level.getDiscount() != null) {
                    unit = unit.multiply(level.getDiscount()).setScale(2, RoundingMode.HALF_UP);
                }
            }
        }
        BigDecimal total = unit.multiply(BigDecimal.valueOf(qty)).setScale(2, RoundingMode.HALF_UP);
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
        order.setMemberId(bo.getMemberId());
        order.setEmployeeId(bo.getEmployeeId());
        order.setStoreId(bo.getStoreId());
        NailDataScopeHelper.fillStoreId(order);
        order.setOrderTime(new Date());
        order.setPayType(payType);
        order.setTotalAmount(total);
        order.setDiscountAmount(discount);
        order.setPayAmount(pay);
        int points = pay.setScale(0, RoundingMode.DOWN).intValue();
        order.setPointAmount(points);
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
        }
        return orderMapper.selectVoById(order.getId());
    }

    private BigDecimal nvl(BigDecimal val) {
        return val == null ? BigDecimal.ZERO : val;
    }
}
