package org.dromara.nail.domain.vo;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.common.excel.annotation.ExcelDictFormat;
import org.dromara.common.excel.convert.ExcelDictConvert;
import org.dromara.common.translation.annotation.Translation;
import org.dromara.common.translation.constant.TransConstant;
import org.dromara.nail.domain.NailEmployeePerformance;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 员工业绩视图对象
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = NailEmployeePerformance.class)
public class NailEmployeePerformanceVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long employeeId;

    @ExcelProperty("员工姓名")
    private String employeeName;

    private Long storeId;

    @ExcelProperty("业绩金额")
    private BigDecimal amount;

    @ExcelProperty(value = "付款方式", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "nail_performance_pay_type")
    private String payType;

    private Long itemId;

    @ExcelProperty("服务项目")
    private String itemName;

    @ExcelProperty("客户姓名")
    private String customerName;

    @ExcelProperty("客户手机号")
    private String customerPhone;

    @ExcelProperty("消费时间")
    private Date consumeTime;

    @ExcelProperty("是否好评")
    private String praise;

    private String praiseImages;

    @Translation(type = TransConstant.OSS_ID_TO_URL, mapper = "praiseImages")
    private String praiseImageUrls;

    @ExcelProperty("备注")
    private String remark;

    @ExcelProperty("订单ID")
    private Long orderId;
}
