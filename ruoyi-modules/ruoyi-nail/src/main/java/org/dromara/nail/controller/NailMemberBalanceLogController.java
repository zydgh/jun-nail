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
import org.dromara.nail.domain.bo.NailMemberBalanceLogBo;
import org.dromara.nail.domain.vo.NailMemberBalanceLogVo;
import org.dromara.nail.service.INailMemberBalanceLogService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 余额变动
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/member/balanceLog")
public class NailMemberBalanceLogController extends BaseController {

    private final INailMemberBalanceLogService nailMemberBalanceLogService;

    /**
     * 查询余额变动列表
     */
    @SaCheckPermission("member:balance:list")
    @GetMapping("/list")
    public TableDataInfo<NailMemberBalanceLogVo> list(NailMemberBalanceLogBo bo, PageQuery pageQuery) {
        return nailMemberBalanceLogService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出余额变动列表
     */
    @SaCheckPermission("member:balance:export")
    @Log(title = "余额变动", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(NailMemberBalanceLogBo bo, HttpServletResponse response) {
        List<NailMemberBalanceLogVo> list = nailMemberBalanceLogService.queryList(bo);
        ExcelUtil.exportExcel(list, "余额变动", NailMemberBalanceLogVo.class, response);
    }

    /**
     * 获取余额变动详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("member:balance:query")
    @GetMapping("/{id}")
    public R<NailMemberBalanceLogVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(nailMemberBalanceLogService.queryById(id));
    }

    /**
     * 新增余额变动
     */
    @SaCheckPermission("member:balance:add")
    @Log(title = "余额变动", businessType = BusinessType.INSERT)
    @RepeatSubmit
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody NailMemberBalanceLogBo bo) {
        return toAjax(nailMemberBalanceLogService.insertByBo(bo));
    }

    /**
     * 修改余额变动
     */
    @SaCheckPermission("member:balance:edit")
    @Log(title = "余额变动", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody NailMemberBalanceLogBo bo) {
        return toAjax(nailMemberBalanceLogService.updateByBo(bo));
    }

    /**
     * 删除余额变动
     *
     * @param ids 主键串
     */
    @SaCheckPermission("member:balance:remove")
    @Log(title = "余额变动", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return toAjax(nailMemberBalanceLogService.deleteWithValidByIds(Arrays.asList(ids), true));
    }

}
