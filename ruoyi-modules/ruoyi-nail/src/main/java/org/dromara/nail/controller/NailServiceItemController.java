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
import org.dromara.nail.domain.bo.NailServiceItemBo;
import org.dromara.nail.domain.vo.NailServiceItemVo;
import org.dromara.nail.service.INailServiceItemService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 美甲项目
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/nail/item")
public class NailServiceItemController extends BaseController {

    private final INailServiceItemService nailServiceItemService;

    /**
     * 查询美甲项目列表
     */
    @SaCheckPermission("nail:item:list")
    @GetMapping("/list")
    public TableDataInfo<NailServiceItemVo> list(NailServiceItemBo bo, PageQuery pageQuery) {
        return nailServiceItemService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出美甲项目列表
     */
    @SaCheckPermission("nail:item:export")
    @Log(title = "美甲项目", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(NailServiceItemBo bo, HttpServletResponse response) {
        List<NailServiceItemVo> list = nailServiceItemService.queryList(bo);
        ExcelUtil.exportExcel(list, "美甲项目", NailServiceItemVo.class, response);
    }

    /**
     * 获取美甲项目详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("nail:item:query")
    @GetMapping("/{id}")
    public R<NailServiceItemVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(nailServiceItemService.queryById(id));
    }

    /**
     * 新增美甲项目
     */
    @SaCheckPermission("nail:item:add")
    @Log(title = "美甲项目", businessType = BusinessType.INSERT)
    @RepeatSubmit
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody NailServiceItemBo bo) {
        return toAjax(nailServiceItemService.insertByBo(bo));
    }

    /**
     * 修改美甲项目
     */
    @SaCheckPermission("nail:item:edit")
    @Log(title = "美甲项目", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody NailServiceItemBo bo) {
        return toAjax(nailServiceItemService.updateByBo(bo));
    }

    /**
     * 删除美甲项目
     *
     * @param ids 主键串
     */
    @SaCheckPermission("nail:item:remove")
    @Log(title = "美甲项目", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return toAjax(nailServiceItemService.deleteWithValidByIds(Arrays.asList(ids), true));
    }

}
