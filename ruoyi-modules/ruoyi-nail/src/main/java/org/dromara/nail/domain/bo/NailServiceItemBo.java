package org.dromara.nail.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.nail.domain.NailServiceItem;


import java.math.BigDecimal;

/**
 * 美甲项目业务对象 nail_service_item
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = NailServiceItem.class, reverseConvertGenerate = false)
public class NailServiceItemBo extends BaseEntity {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 项目编号
     */
    private String itemCode;

    /**
     * 项目名称
     */
    @NotBlank(message = "项目名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String itemName;

    /**
     * 分类
     */
    private String category;

    /**
     * 时长(分钟)
     */
    private Integer durationMin;

    /**
     * 原价
     */
    @NotNull(message = "原价不能为空", groups = { AddGroup.class, EditGroup.class })
    private BigDecimal price;

    /**
     * 会员价
     */
    private BigDecimal memberPrice;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 状态
     */
    private String status;

    /**
     * 备注
     */
    private String remark;

}
