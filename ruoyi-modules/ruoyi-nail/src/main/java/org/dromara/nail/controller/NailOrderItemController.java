package org.dromara.nail.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.excel.utils.ExcelUtil;
import org.dromara.common.idempotent.annotation.RepeatSubmit;
import org.dromara.common.log.annotation.Log;
import org.dromara.common.log.enums.BusinessType;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.web.core.BaseController;
import org.dromara.nail.domain.bo.NailOrderItemBo;
import org.dromara.nail.domain.vo.NailOrderItemVo;
import org.dromara.nail.service.INailOrderItemService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 订单明细
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/nail/orderItem")
public class NailOrderItemController extends BaseController {

    private final INailOrderItemService nailOrderItemService;

    /**
     * 查询订单明细列表
     */
    @SaCheckPermission("nail:orderItem:list")
    @GetMapping("/list")
    public TableDataInfo<NailOrderItemVo> list(NailOrderItemBo bo, PageQuery pageQuery) {
        return nailOrderItemService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出订单明细列表
     */
    @SaCheckPermission("nail:orderItem:export")
    @Log(title = "订单明细", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(NailOrderItemBo bo, HttpServletResponse response) {
        List<NailOrderItemVo> list = nailOrderItemService.queryList(bo);
        ExcelUtil.exportExcel(list, "订单明细", NailOrderItemVo.class, response);
    }

    /**
     * 获取订单明细详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("nail:orderItem:query")
    @GetMapping("/{id}")
    public R<NailOrderItemVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(nailOrderItemService.queryById(id));
    }

    /**
     * 新增订单明细
     */
    @SaCheckPermission("nail:orderItem:add")
    @Log(title = "订单明细", businessType = BusinessType.INSERT)
    @RepeatSubmit
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody NailOrderItemBo bo) {
        return toAjax(nailOrderItemService.insertByBo(bo));
    }

    /**
     * 修改订单明细
     */
    @SaCheckPermission("nail:orderItem:edit")
    @Log(title = "订单明细", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody NailOrderItemBo bo) {
        return toAjax(nailOrderItemService.updateByBo(bo));
    }

    /**
     * 删除订单明细
     *
     * @param ids 主键串
     */
    @SaCheckPermission("nail:orderItem:remove")
    @Log(title = "订单明细", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return toAjax(nailOrderItemService.deleteWithValidByIds(Arrays.asList(ids), true));
    }

}
