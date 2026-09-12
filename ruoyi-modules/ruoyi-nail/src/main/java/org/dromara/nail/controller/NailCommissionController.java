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
import org.dromara.nail.domain.bo.NailCommissionBo;
import org.dromara.nail.domain.vo.NailCommissionVo;
import org.dromara.nail.service.INailCommissionService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 员工提成
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/nail/commission")
public class NailCommissionController extends BaseController {

    private final INailCommissionService nailCommissionService;

    /**
     * 查询员工提成列表
     */
    @SaCheckPermission("nail:commission:list")
    @GetMapping("/list")
    public TableDataInfo<NailCommissionVo> list(NailCommissionBo bo, PageQuery pageQuery) {
        return nailCommissionService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出员工提成列表
     */
    @SaCheckPermission("nail:commission:export")
    @Log(title = "员工提成", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(NailCommissionBo bo, HttpServletResponse response) {
        List<NailCommissionVo> list = nailCommissionService.queryList(bo);
        ExcelUtil.exportExcel(list, "员工提成", NailCommissionVo.class, response);
    }

    /**
     * 获取员工提成详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("nail:commission:query")
    @GetMapping("/{id}")
    public R<NailCommissionVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(nailCommissionService.queryById(id));
    }

    /**
     * 新增员工提成
     */
    @SaCheckPermission("nail:commission:add")
    @Log(title = "员工提成", businessType = BusinessType.INSERT)
    @RepeatSubmit
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody NailCommissionBo bo) {
        return toAjax(nailCommissionService.insertByBo(bo));
    }

    /**
     * 修改员工提成
     */
    @SaCheckPermission("nail:commission:edit")
    @Log(title = "员工提成", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody NailCommissionBo bo) {
        return toAjax(nailCommissionService.updateByBo(bo));
    }

    /**
     * 删除员工提成
     *
     * @param ids 主键串
     */
    @SaCheckPermission("nail:commission:remove")
    @Log(title = "员工提成", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return toAjax(nailCommissionService.deleteWithValidByIds(Arrays.asList(ids), true));
    }

}
