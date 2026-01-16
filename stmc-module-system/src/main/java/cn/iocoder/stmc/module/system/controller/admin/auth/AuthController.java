package cn.iocoder.stmc.module.system.controller.admin.auth;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.stmc.framework.common.enums.CommonStatusEnum;
import cn.iocoder.stmc.framework.common.enums.UserTypeEnum;
import cn.iocoder.stmc.framework.common.exception.util.ServiceExceptionUtil;
import cn.iocoder.stmc.framework.common.pojo.CommonResult;
import cn.iocoder.stmc.framework.security.config.SecurityProperties;
import cn.iocoder.stmc.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.stmc.module.system.controller.admin.auth.vo.*;
import cn.iocoder.stmc.module.system.convert.auth.AuthConvert;
import cn.iocoder.stmc.module.system.dal.dataobject.permission.MenuDO;
import cn.iocoder.stmc.module.system.dal.dataobject.permission.RoleDO;
import cn.iocoder.stmc.module.system.dal.dataobject.user.AdminUserDO;
import cn.iocoder.stmc.module.system.enums.logger.LoginLogTypeEnum;
import cn.iocoder.stmc.module.system.service.auth.AdminAuthService;
import cn.iocoder.stmc.module.system.service.permission.MenuService;
import cn.iocoder.stmc.module.system.service.permission.PermissionService;
import cn.iocoder.stmc.module.system.service.permission.RoleService;
import cn.iocoder.stmc.module.system.service.social.SocialClientService;
import cn.iocoder.stmc.module.system.service.user.AdminUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static cn.iocoder.stmc.framework.common.pojo.CommonResult.success;
import static cn.iocoder.stmc.framework.common.util.collection.CollectionUtils.convertSet;
import static cn.iocoder.stmc.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;
import static cn.iocoder.stmc.module.system.enums.ErrorCodeConstants.*;

@Tag(name = "管理后台 - 认证")
@RestController
@RequestMapping("/system/auth")
@Validated
@Slf4j
public class AuthController {

    @Resource
    private AdminAuthService authService;
    @Resource
    private AdminUserService userService;
    @Resource
    private RoleService roleService;
    @Resource
    private MenuService menuService;
    @Resource
    private PermissionService permissionService;
    @Resource
    private SocialClientService socialClientService;

    @Resource
    private SecurityProperties securityProperties;

    @PostMapping("/login")
    @PermitAll
    @Operation(summary = "使用账号密码登录")
    public CommonResult<AuthLoginRespVO> login(@RequestBody @Valid AuthLoginReqVO reqVO) {
        return success(authService.login(reqVO));
    }

    @PostMapping("/logout")
    @PermitAll
    @Operation(summary = "登出系统")
    public CommonResult<Boolean> logout(HttpServletRequest request) {
        String token = SecurityFrameworkUtils.obtainAuthorization(request,
                securityProperties.getTokenHeader(), securityProperties.getTokenParameter());
        if (StrUtil.isNotBlank(token)) {
            authService.logout(token, LoginLogTypeEnum.LOGOUT_SELF.getType());
        }
        return success(true);
    }

    @PostMapping("/refresh-token")
    @PermitAll
    @Operation(summary = "刷新令牌")
    @Parameter(name = "refreshToken", description = "刷新令牌", required = true)
    public CommonResult<AuthLoginRespVO> refreshToken(@RequestParam("refreshToken") String refreshToken) {
        return success(authService.refreshToken(refreshToken));
    }

    @GetMapping("/get-permission-info")
    @Operation(summary = "获取登录用户的权限信息")
    public CommonResult<AuthPermissionInfoRespVO> getPermissionInfo() {
        // 1.1 获得用户信息
        AdminUserDO user = userService.getUser(getLoginUserId());
        if (user == null) {
            return success(null);
        }

        // 1.2 获得角色列表
        Set<Long> roleIds = permissionService.getUserRoleIdListByUserId(getLoginUserId());
        if (CollUtil.isEmpty(roleIds)) {
            return success(AuthConvert.INSTANCE.convert(user, Collections.emptyList(), Collections.emptyList()));
        }
        List<RoleDO> roles = roleService.getRoleList(roleIds);
        roles.removeIf(role -> !CommonStatusEnum.ENABLE.getStatus().equals(role.getStatus())); // 移除禁用的角色

        // 1.3 获得菜单列表
        Set<Long> menuIds = permissionService.getRoleMenuListByRoleId(convertSet(roles, RoleDO::getId));
        List<MenuDO> menuList = menuService.getMenuList(menuIds);
        menuList = menuService.filterDisableMenus(menuList);

        // 2. 拼接结果返回
        return success(AuthConvert.INSTANCE.convert(user, roles, menuList));
    }

    @PostMapping("/register")
    @PermitAll
    @Operation(summary = "注册用户（已禁用）")
    public CommonResult<AuthLoginRespVO> register(@RequestBody @Valid AuthRegisterReqVO registerReqVO) {
        // 注册功能已禁用，只允许管理员分配账号
        throw ServiceExceptionUtil.exception(AUTH_REGISTER_DISABLED);
    }

    // ========== 短信登录相关 ==========

    @PostMapping("/sms-login")
    @PermitAll
    @Operation(summary = "使用短信验证码登录（已禁用）")
    public CommonResult<AuthLoginRespVO> smsLogin(@RequestBody @Valid AuthSmsLoginReqVO reqVO) {
        // 短信登录功能已禁用
        throw ServiceExceptionUtil.exception(AUTH_SMS_LOGIN_DISABLED);
    }

    @PostMapping("/send-sms-code")
    @PermitAll
    @Operation(summary = "发送手机验证码（已禁用）")
    public CommonResult<Boolean> sendLoginSmsCode(@RequestBody @Valid AuthSmsSendReqVO reqVO) {
        // 短信功能已禁用
        throw ServiceExceptionUtil.exception(AUTH_SMS_LOGIN_DISABLED);
    }

    @PostMapping("/reset-password")
    @PermitAll
    @Operation(summary = "重置密码（已禁用）")
    public CommonResult<Boolean> resetPassword(@RequestBody @Valid AuthResetPasswordReqVO reqVO) {
        // 密码重置功能已禁用，请联系管理员
        throw ServiceExceptionUtil.exception(AUTH_RESET_PASSWORD_DISABLED);
    }

    // ========== 社交登录相关（已禁用） ==========

    @GetMapping("/social-auth-redirect")
    @PermitAll
    @Operation(summary = "社交授权的跳转（已禁用）")
    @Parameters({
            @Parameter(name = "type", description = "社交类型", required = true),
            @Parameter(name = "redirectUri", description = "回调路径")
    })
    public CommonResult<String> socialLogin(@RequestParam("type") Integer type,
                                            @RequestParam("redirectUri") String redirectUri) {
        // 社交登录功能已禁用
        throw ServiceExceptionUtil.exception(AUTH_SOCIAL_LOGIN_DISABLED);
    }

    @PostMapping("/social-login")
    @PermitAll
    @Operation(summary = "社交快捷登录（已禁用）")
    public CommonResult<AuthLoginRespVO> socialQuickLogin(@RequestBody @Valid AuthSocialLoginReqVO reqVO) {
        // 社交登录功能已禁用
        throw ServiceExceptionUtil.exception(AUTH_SOCIAL_LOGIN_DISABLED);
    }

}
