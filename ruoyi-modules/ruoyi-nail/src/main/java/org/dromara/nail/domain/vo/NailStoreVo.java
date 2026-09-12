package org.dromara.nail.domain.vo;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.nail.domain.NailStore;

import java.io.Serial;
import java.io.Serializable;


/**
 * 门店视图对象 nail_store
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = NailStore.class)
public class NailStoreVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private Long id;

    /**
     * 门店编号
     */
    @ExcelProperty(value = "门店编号")
    private String storeCode;

    /**
     * 门店名称
     */
    @ExcelProperty(value = "门店名称")
    private String storeName;

    /**
     * 联系电话
     */
    @ExcelProperty(value = "联系电话")
    private String phone;

    /**
     * 门店地址
     */
    @ExcelProperty(value = "门店地址")
    private String address;

    /**
     * 负责人
     */
    @ExcelProperty(value = "负责人")
    private String manager;

    /**
     * 排序
     */
    @ExcelProperty(value = "排序")
    private Integer sort;

    /**
     * 状态
     */
    @ExcelProperty(value = "状态")
    private String status;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;

}
