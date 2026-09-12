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
import org.dromara.nail.domain.bo.NailMemberPointLogBo;
import org.dromara.nail.domain.vo.NailMemberPointLogVo;
import org.dromara.nail.service.INailMemberPointLogService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 积分变动
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/member/pointLog")
public class NailMemberPointLogController extends BaseController {

    private final INailMemberPointLogService nailMemberPointLogService;

    /**
     * 查询积分变动列表
     */
    @SaCheckPermission("member:point:list")
    @GetMapping("/list")
    public TableDataInfo<NailMemberPointLogVo> list(NailMemberPointLogBo bo, PageQuery pageQuery) {
        return nailMemberPointLogService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出积分变动列表
     */
    @SaCheckPermission("member:point:export")
    @Log(title = "积分变动", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(NailMemberPointLogBo bo, HttpServletResponse response) {
        List<NailMemberPointLogVo> list = nailMemberPointLogService.queryList(bo);
        ExcelUtil.exportExcel(list, "积分变动", NailMemberPointLogVo.class, response);
    }

    /**
     * 获取积分变动详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("member:point:query")
    @GetMapping("/{id}")
    public R<NailMemberPointLogVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(nailMemberPointLogService.queryById(id));
    }

    /**
     * 新增积分变动
     */
    @SaCheckPermission("member:point:add")
    @Log(title = "积分变动", businessType = BusinessType.INSERT)
    @RepeatSubmit
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody NailMemberPointLogBo bo) {
        return toAjax(nailMemberPointLogService.insertByBo(bo));
    }

    /**
     * 修改积分变动
     */
    @SaCheckPermission("member:point:edit")
    @Log(title = "积分变动", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody NailMemberPointLogBo bo) {
        return toAjax(nailMemberPointLogService.updateByBo(bo));
    }

    /**
     * 删除积分变动
     *
     * @param ids 主键串
     */
    @SaCheckPermission("member:point:remove")
    @Log(title = "积分变动", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return toAjax(nailMemberPointLogService.deleteWithValidByIds(Arrays.asList(ids), true));
    }

}
