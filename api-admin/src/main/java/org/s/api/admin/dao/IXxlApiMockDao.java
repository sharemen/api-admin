package org.s.api.admin.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.s.api.admin.core.model.XxlApiMock;

import java.util.List;

/**
 * Created by xuxueli on 17/4/1.
 */
@Mapper
public interface IXxlApiMockDao {

    public int add(XxlApiMock xxlApiMock);
    public int update(XxlApiMock xxlApiMock);
    public int delete(@Param("id") int id);

    public List<XxlApiMock> loadAll(@Param("documentId") int documentId);
    public XxlApiMock load(@Param("id") int id);
    public XxlApiMock loadByUuid(@Param("uuid") String uuid);
    
    //通过mock对应的api URI获取默认的那1条默认的mock数据
    public XxlApiMock loadByUri(@Param("reqUri") String reqUri);
    
    //将除了当前mock之外的 对应的api的mock数据设置为非默认状态
    //仅在修改mock默认状态时调用
    public int resetDefaultByUri(XxlApiMock xxlApiMock);

}
