package org.s.api.admin.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.s.api.admin.core.model.ApiHistory;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;


@Mapper
public interface ApiHistoryDao {

    public int add(ApiHistory apiHistory);


    public ApiHistory load(@Param("id") int id);
    public List<ApiHistory> loadByDocumentId(@Param("documentId") int documentId,@Param("size") int size);
    
    public IPage<ApiHistory> selectApiHistory(Page<ApiHistory> page, @Param("documentId") int documentId);
}
