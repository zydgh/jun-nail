package org.dromara.nail.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.nail.domain.NailMember;
import org.dromara.nail.domain.NailMemberBalanceLog;
import org.dromara.nail.domain.bo.NailMemberRechargeBo;
import org.dromara.nail.domain.vo.NailMemberVo;
import org.dromara.nail.mapper.NailMemberBalanceLogMapper;
import org.dromara.nail.mapper.NailMemberMapper;
import org.dromara.nail.service.INailMemberWalletService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 会员储值
 */
@RequiredArgsConstructor
@Service
public class NailMemberWalletServiceImpl implements INailMemberWalletService {

    private final NailMemberMapper memberMapper;
    private final NailMemberBalanceLogMapper balanceLogMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void recharge(NailMemberRechargeBo bo) {
        if (bo.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ServiceException("充值金额必须大于0");
        }
        NailMember member = memberMapper.selectById(bo.getMemberId());
        if (member == null) {
            throw new ServiceException("会员不存在");
        }
        BigDecimal gift = bo.getGiftAmount() == null ? BigDecimal.ZERO : bo.getGiftAmount();
        BigDecimal before = nvl(member.getBalance());
        BigDecimal after = before.add(bo.getAmount()).add(gift);
        member.setBalance(after);
        member.setTotalRecharge(nvl(member.getTotalRecharge()).add(bo.getAmount()));
        memberMapper.updateById(member);
        insertLog(member.getId(), "recharge", bo.getAmount(), before, before.add(bo.getAmount()), "充值");
        if (gift.compareTo(BigDecimal.ZERO) > 0) {
            insertLog(member.getId(), "gift", gift, before.add(bo.getAmount()), after, StringUtils.blankToDefault(bo.getRemark(), "充值赠送"));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void adjust(NailMemberRechargeBo bo) {
        NailMember member = memberMapper.selectById(bo.getMemberId());
        if (member == null) {
            throw new ServiceException("会员不存在");
        }
        BigDecimal before = nvl(member.getBalance());
        BigDecimal after = before.add(bo.getAmount());
        if (after.compareTo(BigDecimal.ZERO) < 0) {
            throw new ServiceException("调整后余额不能为负");
        }
        member.setBalance(after);
        memberMapper.updateById(member);
        insertLog(member.getId(), "adjust", bo.getAmount(), before, after, bo.getRemark());
    }

    @Override
    public List<NailMemberVo> birthdayRemind(Integer days) {
        int window = days == null ? 7 : days;
        LocalDate today = LocalDate.now();
        List<NailMember> list = memberMapper.selectList(new LambdaQueryWrapper<NailMember>()
            .isNotNull(NailMember::getBirthday)
            .eq(NailMember::getStatus, "0"));
        return list.stream().filter(m -> inBirthdayWindow(m.getBirthday(), today, window))
            .map(m -> memberMapper.selectVoById(m.getId()))
            .collect(Collectors.toList());
    }

    private boolean inBirthdayWindow(Date birthday, LocalDate today, int days) {
        LocalDate birth = birthday.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate thisYear = birth.withYear(today.getYear());
        for (int i = 0; i <= days; i++) {
            if (today.plusDays(i).getMonthValue() == thisYear.getMonthValue()
                && today.plusDays(i).getDayOfMonth() == thisYear.getDayOfMonth()) {
                return true;
            }
            LocalDate nextYear = birth.withYear(today.getYear() + 1);
            if (today.plusDays(i).getMonthValue() == nextYear.getMonthValue()
                && today.plusDays(i).getDayOfMonth() == nextYear.getDayOfMonth()) {
                return true;
            }
        }
        return false;
    }

    private void insertLog(Long memberId, String type, BigDecimal amount, BigDecimal before, BigDecimal after, String remark) {
        NailMemberBalanceLog log = new NailMemberBalanceLog();
        log.setMemberId(memberId);
        log.setChangeType(type);
        log.setAmount(amount);
        log.setBeforeBalance(before);
        log.setAfterBalance(after);
        log.setRemark(remark);
        balanceLogMapper.insert(log);
    }

    private BigDecimal nvl(BigDecimal val) {
        return val == null ? BigDecimal.ZERO : val;
    }
}
