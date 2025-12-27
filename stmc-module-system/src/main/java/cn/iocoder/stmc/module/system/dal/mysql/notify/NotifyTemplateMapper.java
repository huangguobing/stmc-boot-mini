package cn.iocoder.stmc.module.system.dal.mysql.notify;

import cn.iocoder.stmc.framework.common.pojo.PageResult;
import cn.iocoder.stmc.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.stmc.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.stmc.module.system.controller.admin.notify.vo.template.NotifyTemplatePageReqVO;
import cn.iocoder.stmc.module.system.dal.dataobject.notify.NotifyTemplateDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NotifyTemplateMapper extends BaseMapperX<NotifyTemplateDO> {

    default NotifyTemplateDO selectByCode(String code) {
        return selectOne(NotifyTemplateDO::getCode, code);
    }

    default PageResult<NotifyTemplateDO> selectPage(NotifyTemplatePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<NotifyTemplateDO>()
                .likeIfPresent(NotifyTemplateDO::getCode, reqVO.getCode())
                .likeIfPresent(NotifyTemplateDO::getName, reqVO.getName())
                .eqIfPresent(NotifyTemplateDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(NotifyTemplateDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(NotifyTemplateDO::getId));
    }

}
