package org.s.api.admin.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.s.api.admin.core.model.XxlApiDataTypeField;

import java.util.List;

/**
 * Created by xuxueli on 17/6/3.
 */
@Mapper
public interface IXxlApiDataTypeFieldDao {

    public int add(List<XxlApiDataTypeField> xxlApiDataTypeFieldList);

    public int deleteByParentDatatypeId(@Param("parentDatatypeId") int parentDatatypeId);

    public List<XxlApiDataTypeField> findByParentDatatypeId(@Param("parentDatatypeId") int parentDatatypeId);

    public List<XxlApiDataTypeField> findByFieldDatatypeId(@Param("fieldDatatypeId") int fieldDatatypeId);

}
