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
import org.dromara.nail.domain.bo.NailMemberBo;
import org.dromara.nail.domain.vo.NailMemberVo;
import org.dromara.nail.service.INailMemberService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 会员
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/member/member")
public class NailMemberController extends BaseController {

    private final INailMemberService nailMemberService;
    private final org.dromara.nail.service.INailMemberWalletService memberWalletService;

    /**
     * 查询会员列表
     */
    @SaCheckPermission("member:member:list")
    @GetMapping("/list")
    public TableDataInfo<NailMemberVo> list(NailMemberBo bo, PageQuery pageQuery) {
        return nailMemberService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出会员列表
     */
    @SaCheckPermission("member:member:export")
    @Log(title = "会员", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(NailMemberBo bo, HttpServletResponse response) {
        List<NailMemberVo> list = nailMemberService.queryList(bo);
        ExcelUtil.exportExcel(list, "会员", NailMemberVo.class, response);
    }

    /**
     * 获取会员详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("member:member:query")
    @GetMapping("/{id}")
    public R<NailMemberVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(nailMemberService.queryById(id));
    }

    /**
     * 新增会员
     */
    @SaCheckPermission("member:member:add")
    @Log(title = "会员", businessType = BusinessType.INSERT)
    @RepeatSubmit
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody NailMemberBo bo) {
        return toAjax(nailMemberService.insertByBo(bo));
    }

    /**
     * 修改会员
     */
    @SaCheckPermission("member:member:edit")
    @Log(title = "会员", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody NailMemberBo bo) {
        return toAjax(nailMemberService.updateByBo(bo));
    }

    /**
     * 删除会员
     *
     * @param ids 主键串
     */
    @SaCheckPermission("member:member:remove")
    @Log(title = "会员", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return toAjax(nailMemberService.deleteWithValidByIds(Arrays.asList(ids), true));
    }


    /**
     * 会员充值
     */
    @SaCheckPermission("member:member:recharge")
    @Log(title = "会员充值", businessType = BusinessType.UPDATE)
    @PostMapping("/recharge")
    public R<Void> recharge(@Validated @RequestBody org.dromara.nail.domain.bo.NailMemberRechargeBo bo) {
        memberWalletService.recharge(bo);
        return R.ok();
    }

    /**
     * 余额调整
     */
    @SaCheckPermission("member:member:adjust")
    @Log(title = "会员余额调整", businessType = BusinessType.UPDATE)
    @PostMapping("/adjust")
    public R<Void> adjust(@Validated @RequestBody org.dromara.nail.domain.bo.NailMemberRechargeBo bo) {
        memberWalletService.adjust(bo);
        return R.ok();
    }

    /**
     * 生日提醒
     */
    @SaCheckPermission("member:member:list")
    @GetMapping("/birthday")
    public R<java.util.List<NailMemberVo>> birthday(@RequestParam(defaultValue = "7") Integer days) {
        return R.ok(memberWalletService.birthdayRemind(days));
    }

}
