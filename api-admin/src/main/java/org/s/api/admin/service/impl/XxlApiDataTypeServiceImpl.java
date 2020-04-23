package org.s.api.admin.service.impl;


import java.util.List;

import javax.annotation.Resource;

import org.s.api.admin.core.model.XxlApiDataType;
import org.s.api.admin.core.model.XxlApiDataTypeField;
import org.s.api.admin.dao.IXxlApiDataTypeDao;
import org.s.api.admin.dao.IXxlApiDataTypeFieldDao;
import org.s.api.admin.service.IXxlApiDataTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author xuxueli
 */
@Service
public class XxlApiDataTypeServiceImpl implements IXxlApiDataTypeService {
	private static Logger logger = LoggerFactory.getLogger(XxlApiDataTypeServiceImpl.class);

	@Resource
	private IXxlApiDataTypeDao xxlApiDataTypeDao;
	@Resource
	private IXxlApiDataTypeFieldDao xxlApiDataTypeFieldDao;

	@Override
	public XxlApiDataType loadDataType(int dataTypeId) {
		XxlApiDataType dataType = xxlApiDataTypeDao.load(dataTypeId);
		if (dataType == null) {
			return dataType;
		}

		// fill field datatype
		int maxRelateLevel = 5;
		dataType = fillFileDataType(dataType, maxRelateLevel);
		return dataType;
	}

	/**
	 * parse field of datatype (注意，循环引用问题；此处显示最长引用链路长度为5；)
	 *
	 * @param dataType
	 * @param maxRelateLevel
	 * @return
	 */
	private XxlApiDataType fillFileDataType(XxlApiDataType dataType, int maxRelateLevel){
		// init field list
		List<XxlApiDataTypeField> fieldList = xxlApiDataTypeFieldDao.findByParentDatatypeId(dataType.getId());
		dataType.setFieldList(fieldList);
		// parse field list
		if (dataType.getFieldList()!=null && dataType.getFieldList().size()>0 && maxRelateLevel>0) {
			for (XxlApiDataTypeField field: dataType.getFieldList()) {
				XxlApiDataType fieldDataType = xxlApiDataTypeDao.load(field.getFieldDatatypeId());
				fieldDataType = fillFileDataType(fieldDataType, --maxRelateLevel);
				field.setFieldDatatype(fieldDataType);
			}
		}
		return dataType;
	}

}
