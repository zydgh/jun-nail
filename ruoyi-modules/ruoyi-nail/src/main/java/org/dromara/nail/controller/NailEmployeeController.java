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
import org.dromara.nail.domain.bo.NailEmployeeBo;
import org.dromara.nail.domain.vo.NailEmployeeVo;
import org.dromara.nail.service.INailEmployeeService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 员工
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/employee/employee")
public class NailEmployeeController extends BaseController {

    private final INailEmployeeService nailEmployeeService;

    /**
     * 查询员工列表
     */
    @SaCheckPermission("employee:employee:list")
    @GetMapping("/list")
    public TableDataInfo<NailEmployeeVo> list(NailEmployeeBo bo, PageQuery pageQuery) {
        return nailEmployeeService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出员工列表
     */
    @SaCheckPermission("employee:employee:export")
    @Log(title = "员工", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(NailEmployeeBo bo, HttpServletResponse response) {
        List<NailEmployeeVo> list = nailEmployeeService.queryList(bo);
        ExcelUtil.exportExcel(list, "员工", NailEmployeeVo.class, response);
    }

    /**
     * 获取员工详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("employee:employee:query")
    @GetMapping("/{id}")
    public R<NailEmployeeVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(nailEmployeeService.queryById(id));
    }

    /**
     * 新增员工
     */
    @SaCheckPermission("employee:employee:add")
    @Log(title = "员工", businessType = BusinessType.INSERT)
    @RepeatSubmit
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody NailEmployeeBo bo) {
        return toAjax(nailEmployeeService.insertByBo(bo));
    }

    /**
     * 修改员工
     */
    @SaCheckPermission("employee:employee:edit")
    @Log(title = "员工", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody NailEmployeeBo bo) {
        return toAjax(nailEmployeeService.updateByBo(bo));
    }

    /**
     * 删除员工
     *
     * @param ids 主键串
     */
    @SaCheckPermission("employee:employee:remove")
    @Log(title = "员工", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return toAjax(nailEmployeeService.deleteWithValidByIds(Arrays.asList(ids), true));
    }

}
