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
import org.dromara.nail.domain.bo.NailOrderBo;
import org.dromara.nail.domain.vo.NailOrderVo;
import org.dromara.nail.service.INailOrderService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 订单
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/nail/order")
public class NailOrderController extends BaseController {

    private final INailOrderService nailOrderService;
    private final org.dromara.nail.service.INailOrderCheckoutService orderCheckoutService;

    /**
     * 查询订单列表
     */
    @SaCheckPermission("nail:order:list")
    @GetMapping("/list")
    public TableDataInfo<NailOrderVo> list(NailOrderBo bo, PageQuery pageQuery) {
        return nailOrderService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出订单列表
     */
    @SaCheckPermission("nail:order:export")
    @Log(title = "订单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(NailOrderBo bo, HttpServletResponse response) {
        List<NailOrderVo> list = nailOrderService.queryList(bo);
        ExcelUtil.exportExcel(list, "订单", NailOrderVo.class, response);
    }

    /**
     * 获取订单详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("nail:order:query")
    @GetMapping("/{id}")
    public R<NailOrderVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(nailOrderService.queryById(id));
    }

    /**
     * 新增订单
     */
    @SaCheckPermission("nail:order:add")
    @Log(title = "订单", businessType = BusinessType.INSERT)
    @RepeatSubmit
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody NailOrderBo bo) {
        return toAjax(nailOrderService.insertByBo(bo));
    }

    /**
     * 修改订单
     */
    @SaCheckPermission("nail:order:edit")
    @Log(title = "订单", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody NailOrderBo bo) {
        return toAjax(nailOrderService.updateByBo(bo));
    }

    /**
     * 删除订单
     *
     * @param ids 主键串
     */
    @SaCheckPermission("nail:order:remove")
    @Log(title = "订单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return toAjax(nailOrderService.deleteWithValidByIds(Arrays.asList(ids), true));
    }


    /**
     * 收银下单
     */
    @SaCheckPermission("nail:order:add")
    @Log(title = "美甲收银", businessType = BusinessType.INSERT)
    @PostMapping("/checkout")
    public R<NailOrderVo> checkout(@Validated @RequestBody org.dromara.nail.domain.bo.NailCheckoutBo bo) {
        return R.ok(orderCheckoutService.checkout(bo));
    }

}
