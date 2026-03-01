package cn.iocoder.stmc.module.erp.service.product;

import cn.iocoder.stmc.framework.common.pojo.PageResult;
import cn.iocoder.stmc.framework.common.util.object.BeanUtils;
import cn.iocoder.stmc.module.erp.controller.admin.product.vo.ProductPageReqVO;
import cn.iocoder.stmc.module.erp.controller.admin.product.vo.ProductSaveReqVO;
import cn.iocoder.stmc.module.erp.dal.dataobject.product.ProductDO;
import cn.iocoder.stmc.module.erp.dal.dataobject.product.ProductSpecDO;
import cn.iocoder.stmc.module.erp.dal.mysql.product.ProductMapper;
import cn.iocoder.stmc.module.erp.dal.mysql.product.ProductSpecMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.List;

import static cn.iocoder.stmc.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.stmc.module.erp.enums.ErrorCodeConstants.PRODUCT_NAME_EXISTS;
import static cn.iocoder.stmc.module.erp.enums.ErrorCodeConstants.PRODUCT_NOT_EXISTS;

@Service
@Validated
public class ProductServiceImpl implements ProductService {

    @Resource
    private ProductMapper productMapper;

    @Resource
    private ProductSpecMapper productSpecMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createProduct(ProductSaveReqVO createReqVO) {
        // 校验名称唯一
        validateProductNameUnique(null, createReqVO.getName());
        // 插入产品
        ProductDO product = BeanUtils.toBean(createReqVO, ProductDO.class);
        productMapper.insert(product);
        // 插入规格
        for (ProductSaveReqVO.ProductSpecItem specItem : createReqVO.getSpecs()) {
            ProductSpecDO spec = BeanUtils.toBean(specItem, ProductSpecDO.class);
            spec.setProductId(product.getId());
            if (spec.getStatus() == null) {
                spec.setStatus(0);
            }
            productSpecMapper.insert(spec);
        }
        return product.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateProduct(ProductSaveReqVO updateReqVO) {
        // 校验存在
        validateProductExists(updateReqVO.getId());
        // 校验名称唯一
        validateProductNameUnique(updateReqVO.getId(), updateReqVO.getName());
        // 更新产品
        ProductDO updateObj = BeanUtils.toBean(updateReqVO, ProductDO.class);
        productMapper.updateById(updateObj);
        // 删除旧规格，插入新规格
        productSpecMapper.deleteByProductId(updateReqVO.getId());
        for (ProductSaveReqVO.ProductSpecItem specItem : updateReqVO.getSpecs()) {
            ProductSpecDO spec = BeanUtils.toBean(specItem, ProductSpecDO.class);
            spec.setId(null); // 重新生成ID
            spec.setProductId(updateReqVO.getId());
            if (spec.getStatus() == null) {
                spec.setStatus(0);
            }
            productSpecMapper.insert(spec);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteProduct(Long id) {
        validateProductExists(id);
        productMapper.deleteById(id);
        productSpecMapper.deleteByProductId(id);
    }

    @Override
    public ProductDO getProduct(Long id) {
        return productMapper.selectById(id);
    }

    @Override
    public PageResult<ProductDO> getProductPage(ProductPageReqVO pageReqVO) {
        return productMapper.selectPage(pageReqVO);
    }

    @Override
    public List<ProductDO> getProductSimpleList(String name) {
        return productMapper.selectListByNameLike(name);
    }

    @Override
    public List<ProductSpecDO> getProductSpecList(Long productId) {
        return productSpecMapper.selectListByProductId(productId);
    }

    @Override
    public List<ProductSpecDO> getProductSpecListByProductIdAndStatus(Long productId, Integer status) {
        return productSpecMapper.selectListByProductIdAndStatus(productId, status);
    }

    private void validateProductExists(Long id) {
        if (productMapper.selectById(id) == null) {
            throw exception(PRODUCT_NOT_EXISTS);
        }
    }

    private void validateProductNameUnique(Long id, String name) {
        ProductDO product = productMapper.selectByName(name);
        if (product == null) {
            return;
        }
        if (id == null) {
            throw exception(PRODUCT_NAME_EXISTS);
        }
        if (!product.getId().equals(id)) {
            throw exception(PRODUCT_NAME_EXISTS);
        }
    }
}
