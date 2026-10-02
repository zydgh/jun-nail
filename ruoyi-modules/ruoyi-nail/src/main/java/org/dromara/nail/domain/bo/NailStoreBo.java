package org.dromara.nail.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.nail.domain.NailStore;



/**
 * 门店业务对象 nail_store
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = NailStore.class, reverseConvertGenerate = false)
public class NailStoreBo extends BaseEntity {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 门店编号
     */
    private String storeCode;

    /**
     * 门店名称
     */
    @NotBlank(message = "门店名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String storeName;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 门店地址
     */
    private String address;

    /**
     * 负责人
     */
    private String manager;

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
