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
import org.dromara.nail.domain.bo.NailAppointmentBo;
import org.dromara.nail.domain.vo.NailAppointmentVo;
import org.dromara.nail.service.INailAppointmentService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 预约
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/nail/appointment")
public class NailAppointmentController extends BaseController {

    private final INailAppointmentService nailAppointmentService;

    /**
     * 查询预约列表
     */
    @SaCheckPermission("nail:appointment:list")
    @GetMapping("/list")
    public TableDataInfo<NailAppointmentVo> list(NailAppointmentBo bo, PageQuery pageQuery) {
        return nailAppointmentService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出预约列表
     */
    @SaCheckPermission("nail:appointment:export")
    @Log(title = "预约", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(NailAppointmentBo bo, HttpServletResponse response) {
        List<NailAppointmentVo> list = nailAppointmentService.queryList(bo);
        ExcelUtil.exportExcel(list, "预约", NailAppointmentVo.class, response);
    }

    /**
     * 获取预约详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("nail:appointment:query")
    @GetMapping("/{id}")
    public R<NailAppointmentVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(nailAppointmentService.queryById(id));
    }

    /**
     * 新增预约
     */
    @SaCheckPermission("nail:appointment:add")
    @Log(title = "预约", businessType = BusinessType.INSERT)
    @RepeatSubmit
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody NailAppointmentBo bo) {
        return toAjax(nailAppointmentService.insertByBo(bo));
    }

    /**
     * 修改预约
     */
    @SaCheckPermission("nail:appointment:edit")
    @Log(title = "预约", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody NailAppointmentBo bo) {
        return toAjax(nailAppointmentService.updateByBo(bo));
    }

    /**
     * 删除预约
     *
     * @param ids 主键串
     */
    @SaCheckPermission("nail:appointment:remove")
    @Log(title = "预约", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return toAjax(nailAppointmentService.deleteWithValidByIds(Arrays.asList(ids), true));
    }

}
