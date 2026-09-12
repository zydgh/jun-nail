package org.dromara.nail.helper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.dromara.common.core.domain.dto.RoleDTO;
import org.dromara.common.core.domain.model.LoginUser;
import org.dromara.common.satoken.utils.LoginHelper;
import org.dromara.nail.domain.NailEmployee;
import org.dromara.nail.mapper.NailEmployeeMapper;
import org.dromara.common.core.utils.SpringUtils;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

/**
 * 美甲业务数据权限：全部 / 本门店 / 本部门 / 本人
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NailDataScopeHelper {

    private static final Set<String> STORE_ENTITIES = Set.of(
        "NailMember", "NailEmployee", "NailEmployeeShift", "NailAppointment", "NailOrder"
    );

    public static Long currentStoreId() {
        if (!LoginHelper.isLogin()) {
            return null;
        }
        LoginUser loginUser = LoginHelper.getLoginUser();
        NailEmployeeMapper mapper = SpringUtils.getBean(NailEmployeeMapper.class);
        NailEmployee employee = mapper.selectOne(new LambdaQueryWrapper<NailEmployee>()
            .eq(NailEmployee::getUserId, loginUser.getUserId())
            .last("limit 1"));
        return employee == null ? null : employee.getStoreId();
    }

    public static Long currentEmployeeId() {
        if (!LoginHelper.isLogin()) {
            return null;
        }
        NailEmployeeMapper mapper = SpringUtils.getBean(NailEmployeeMapper.class);
        NailEmployee employee = mapper.selectOne(new LambdaQueryWrapper<NailEmployee>()
            .eq(NailEmployee::getUserId, LoginHelper.getUserId())
            .last("limit 1"));
        return employee == null ? null : employee.getId();
    }

    public static void applyStoreScope(LambdaQueryWrapper<?> lqw, String entityName) {
        if (!LoginHelper.isLogin() || LoginHelper.isSuperAdmin()) {
            return;
        }
        LoginUser loginUser = LoginHelper.getLoginUser();
        String maxScope = maxDataScope(loginUser.getRoles());
        if ("1".equals(maxScope)) {
            return;
        }
        Long storeId = currentStoreId();
        if ("7".equals(maxScope)) {
            if (STORE_ENTITIES.contains(entityName) && storeId != null) {
                lqw.apply("store_id = {0}", storeId);
            } else if (storeId == null) {
                lqw.apply("create_by = {0}", loginUser.getUserId());
            }
            return;
        }
        if ("3".equals(maxScope) || "4".equals(maxScope)) {
            if (loginUser.getDeptId() != null) {
                lqw.apply("create_dept = {0}", loginUser.getDeptId());
            }
            return;
        }
        if ("5".equals(maxScope)) {
            Long employeeId = currentEmployeeId();
            if ("NailEmployee".equals(entityName) && employeeId != null) {
                lqw.apply("id = {0}", employeeId);
            } else if ("NailOrder".equals(entityName) && employeeId != null) {
                lqw.apply("employee_id = {0}", employeeId);
            } else if ("NailAppointment".equals(entityName) && employeeId != null) {
                lqw.apply("employee_id = {0}", employeeId);
            } else if ("NailCommission".equals(entityName) && employeeId != null) {
                lqw.apply("employee_id = {0}", employeeId);
            } else {
                lqw.apply("create_by = {0}", loginUser.getUserId());
            }
        }
    }

    public static void fillStoreId(Object entity) {
        try {
            Method getter = entity.getClass().getMethod("getStoreId");
            Method setter = entity.getClass().getMethod("setStoreId", Long.class);
            if (getter.invoke(entity) == null) {
                Long storeId = currentStoreId();
                if (storeId != null) {
                    setter.invoke(entity, storeId);
                }
            }
        } catch (ReflectiveOperationException ignored) {
            return;
        }
    }

    private static String maxDataScope(List<RoleDTO> roles) {
        if (roles == null || roles.isEmpty()) {
            return "5";
        }
        String max = "5";
        for (RoleDTO role : roles) {
            if (role.getDataScope() != null && role.getDataScope().compareTo(max) < 0) {
                max = role.getDataScope();
            }
        }
        return max;
    }
}
