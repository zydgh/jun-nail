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
import org.dromara.nail.domain.bo.NailStoreBo;
import org.dromara.nail.domain.vo.NailStoreVo;
import org.dromara.nail.service.INailStoreService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 门店
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/store/store")
public class NailStoreController extends BaseController {

    private final INailStoreService nailStoreService;

    /**
     * 查询门店列表
     */
    @SaCheckPermission("store:store:list")
    @GetMapping("/list")
    public TableDataInfo<NailStoreVo> list(NailStoreBo bo, PageQuery pageQuery) {
        return nailStoreService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出门店列表
     */
    @SaCheckPermission("store:store:export")
    @Log(title = "门店", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(NailStoreBo bo, HttpServletResponse response) {
        List<NailStoreVo> list = nailStoreService.queryList(bo);
        ExcelUtil.exportExcel(list, "门店", NailStoreVo.class, response);
    }

    /**
     * 获取门店详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("store:store:query")
    @GetMapping("/{id}")
    public R<NailStoreVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(nailStoreService.queryById(id));
    }

    /**
     * 新增门店
     */
    @SaCheckPermission("store:store:add")
    @Log(title = "门店", businessType = BusinessType.INSERT)
    @RepeatSubmit
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody NailStoreBo bo) {
        return toAjax(nailStoreService.insertByBo(bo));
    }

    /**
     * 修改门店
     */
    @SaCheckPermission("store:store:edit")
    @Log(title = "门店", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody NailStoreBo bo) {
        return toAjax(nailStoreService.updateByBo(bo));
    }

    /**
     * 删除门店
     *
     * @param ids 主键串
     */
    @SaCheckPermission("store:store:remove")
    @Log(title = "门店", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return toAjax(nailStoreService.deleteWithValidByIds(Arrays.asList(ids), true));
    }

}
