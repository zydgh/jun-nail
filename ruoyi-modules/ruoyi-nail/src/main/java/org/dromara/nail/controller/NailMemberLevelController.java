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
import org.dromara.nail.domain.bo.NailMemberLevelBo;
import org.dromara.nail.domain.vo.NailMemberLevelVo;
import org.dromara.nail.service.INailMemberLevelService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 会员等级
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/member/level")
public class NailMemberLevelController extends BaseController {

    private final INailMemberLevelService nailMemberLevelService;

    /**
     * 查询会员等级列表
     */
    @SaCheckPermission("member:level:list")
    @GetMapping("/list")
    public TableDataInfo<NailMemberLevelVo> list(NailMemberLevelBo bo, PageQuery pageQuery) {
        return nailMemberLevelService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出会员等级列表
     */
    @SaCheckPermission("member:level:export")
    @Log(title = "会员等级", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(NailMemberLevelBo bo, HttpServletResponse response) {
        List<NailMemberLevelVo> list = nailMemberLevelService.queryList(bo);
        ExcelUtil.exportExcel(list, "会员等级", NailMemberLevelVo.class, response);
    }

    /**
     * 获取会员等级详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("member:level:query")
    @GetMapping("/{id}")
    public R<NailMemberLevelVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(nailMemberLevelService.queryById(id));
    }

    /**
     * 新增会员等级
     */
    @SaCheckPermission("member:level:add")
    @Log(title = "会员等级", businessType = BusinessType.INSERT)
    @RepeatSubmit
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody NailMemberLevelBo bo) {
        return toAjax(nailMemberLevelService.insertByBo(bo));
    }

    /**
     * 修改会员等级
     */
    @SaCheckPermission("member:level:edit")
    @Log(title = "会员等级", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody NailMemberLevelBo bo) {
        return toAjax(nailMemberLevelService.updateByBo(bo));
    }

    /**
     * 删除会员等级
     *
     * @param ids 主键串
     */
    @SaCheckPermission("member:level:remove")
    @Log(title = "会员等级", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return toAjax(nailMemberLevelService.deleteWithValidByIds(Arrays.asList(ids), true));
    }

}
