package cn.iocoder.stmc.module.erp.service.product;

import cn.iocoder.stmc.framework.common.pojo.PageResult;
import cn.iocoder.stmc.module.erp.controller.admin.product.vo.ProductPageReqVO;
import cn.iocoder.stmc.module.erp.controller.admin.product.vo.ProductSaveReqVO;
import cn.iocoder.stmc.module.erp.dal.dataobject.product.ProductDO;
import cn.iocoder.stmc.module.erp.dal.dataobject.product.ProductSpecDO;

import javax.validation.Valid;
import java.util.List;

public interface ProductService {

    Long createProduct(@Valid ProductSaveReqVO createReqVO);

    void updateProduct(@Valid ProductSaveReqVO updateReqVO);

    void deleteProduct(Long id);

    ProductDO getProduct(Long id);

    PageResult<ProductDO> getProductPage(ProductPageReqVO pageReqVO);

    List<ProductDO> getProductSimpleList(String name);

    List<ProductSpecDO> getProductSpecList(Long productId);

    List<ProductSpecDO> getProductSpecListByProductIdAndStatus(Long productId, Integer status);
}
