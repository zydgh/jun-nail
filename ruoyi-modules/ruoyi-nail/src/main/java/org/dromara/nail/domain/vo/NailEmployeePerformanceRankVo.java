package org.dromara.nail.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 员工业绩排行榜
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NailEmployeePerformanceRankVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Integer rank;

    private Long employeeId;

    private String employeeName;

    private BigDecimal totalAmount;

    private Long serviceCount;

    private Long praiseCount;
}
