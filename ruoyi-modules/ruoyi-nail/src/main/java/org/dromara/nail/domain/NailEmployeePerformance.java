package org.dromara.nail.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.tenant.core.TenantEntity;

import java.io.Serial;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 员工业绩对象 nail_employee_performance
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("nail_employee_performance")
public class NailEmployeePerformance extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id")
    private Long id;

    private Long employeeId;

    private Long storeId;

    private BigDecimal amount;

    private Long itemId;

    private String itemName;

    private String customerName;

    private String customerPhone;

    private Date consumeTime;

    private String praise;

    private String remark;

    @TableLogic
    private String delFlag;
}
