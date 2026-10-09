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
import org.dromara.nail.domain.bo.NailEmployeePerformanceBo;
import org.dromara.nail.domain.vo.NailEmployeePerformanceRankVo;
import org.dromara.nail.domain.vo.NailEmployeePerformanceVo;
import org.dromara.nail.domain.vo.NailPerformanceMemberCheckVo;
import org.dromara.nail.service.INailEmployeePerformanceService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 员工业绩
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/employee/performance")
public class NailEmployeePerformanceController extends BaseController {

    private final INailEmployeePerformanceService performanceService;

    @SaCheckPermission("employee:performance:list")
    @GetMapping("/list")
    public TableDataInfo<NailEmployeePerformanceVo> list(
        NailEmployeePerformanceBo bo, PageQuery pageQuery) {
        return performanceService.queryPageList(bo, pageQuery);
    }

    @SaCheckPermission("employee:performance:list")
    @GetMapping("/ranking")
    public R<List<NailEmployeePerformanceRankVo>> ranking(
        @RequestParam(defaultValue = "day") String period) {
        return R.ok(performanceService.ranking(period));
    }

    @SaCheckPermission("employee:performance:list")
    @GetMapping("/options")
    public R<Map<String, Object>> options() {
        return R.ok(performanceService.options());
    }

    @SaCheckPermission("employee:performance:list")
    @GetMapping("/member-check")
    public R<NailPerformanceMemberCheckVo> memberCheck(
        @RequestParam(required = false) String phone,
        @RequestParam(required = false) String name) {
        return R.ok(performanceService.checkMember(phone, name));
    }

    @SaCheckPermission("employee:performance:export")
    @Log(title = "员工业绩", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(
        NailEmployeePerformanceBo bo, HttpServletResponse response) {
        ExcelUtil.exportExcel(
            performanceService.queryList(bo),
            "员工业绩",
            NailEmployeePerformanceVo.class,
            response);
    }

    @SaCheckPermission("employee:performance:query")
    @GetMapping("/{id}")
    public R<NailEmployeePerformanceVo> getInfo(
        @NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(performanceService.queryById(id));
    }

    @SaCheckPermission("employee:performance:add")
    @Log(title = "员工业绩", businessType = BusinessType.INSERT)
    @RepeatSubmit
    @PostMapping
    public R<Void> add(
        @Validated(AddGroup.class)
        @RequestBody NailEmployeePerformanceBo bo) {
        return toAjax(performanceService.insertByBo(bo));
    }

    @SaCheckPermission("employee:performance:edit")
    @Log(title = "员工业绩", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PutMapping
    public R<Void> edit(
        @Validated(EditGroup.class)
        @RequestBody NailEmployeePerformanceBo bo) {
        return toAjax(performanceService.updateByBo(bo));
    }

    @SaCheckPermission("employee:performance:remove")
    @Log(title = "员工业绩", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(
        @NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return toAjax(performanceService.deleteWithValidByIds(
            Arrays.asList(ids), true));
    }
}
