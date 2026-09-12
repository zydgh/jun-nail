package org.dromara.nail.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.nail.domain.vo.NailDashboardVo;
import org.dromara.nail.service.INailDashboardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 营业工作台
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/nail/dashboard")
public class NailDashboardController {

    private final INailDashboardService dashboardService;

    /**
     * 工作台概览
     */
    @SaCheckPermission("nail:dashboard:list")
    @GetMapping("/overview")
    public R<NailDashboardVo> overview() {
        return R.ok(dashboardService.overview());
    }
}
