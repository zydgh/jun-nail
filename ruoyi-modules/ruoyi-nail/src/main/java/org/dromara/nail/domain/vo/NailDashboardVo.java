package org.dromara.nail.domain.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 工作台统计
 */
@Data
public class NailDashboardVo {

    private Long memberCount;

    private Long employeeCount;

    private Long todayOrderCount;

    private BigDecimal todayAmount;

    private BigDecimal monthAmount;

    private Long birthdayCount;

    private List<NameValue> memberTrend = new ArrayList<>();

    private List<NameValue> revenueTrend = new ArrayList<>();

    private List<NameValue> employeeRank = new ArrayList<>();

    @Data
    public static class NameValue {
        private String name;
        private BigDecimal value;

        public NameValue() {
        }

        public NameValue(String name, BigDecimal value) {
            this.name = name;
            this.value = value;
        }
    }
}
