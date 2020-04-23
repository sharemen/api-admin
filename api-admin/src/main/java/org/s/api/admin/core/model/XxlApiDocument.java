package org.s.api.admin.core.model;

import java.util.Date;

/**
 * Created by xuxueli on 17/3/31.
 */
public class XxlApiDocument {

    private int id;                     // 接口ID
    private int projectId;              // 项目ID
    private int groupId;                // 分组ID
    private String name;                // 接口名称
    private int status;                 // 状态：0-启用、1-维护、2-废弃
    private int starLevel;              // 星标等级：0-普通接口、1-一星接口
    private String requestUrl;          // Request URL：相对地址
    private String requestMethod;       // Request Method：如POST、GET
    private String requestHeaders;      // Request Headers：Map-JSON格式字符串
    private String queryParams;         // Query String Parameters：VO-JSON格式字符串
    private int responseDatatypeId;     // 响应数据类型ID
    private String responseParams;      // Response Parameters：VO-JSON格式字符串
    private String successRespType;     // Response Content-type：成功接口，如JSON、XML、HTML、TEXT
    private String successRespExample;  // Response Content：成功接口
    private String failRespType;        // Response Content-type：失败接口
    private String failRespExample;     // Response Content：失败接口
    private String remark;              // 备注
    private Date addTime;               // 创建时间
    private Date updateTime;            // 更新时间
    private String requestExample;  //请求示例
    private String needResources;      // 依赖资源：VO-JSON格式字符串
    private long wikiId;  //api对应的文档id
    private long wikiParentId; // api对应的父级页面id，只用来在创建时定位页面位置，不持久化
    private String respRemark;//返回结果备注：描述json格式的细节
    private String reqRemark;//返回结果备注：描述json格式的细节
    private String creatUser;//接口创建人
    private String updateUser;//接口最后修改

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStarLevel() {
        return starLevel;
    }

    public void setStarLevel(int starLevel) {
        this.starLevel = starLevel;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getRequestHeaders() {
        return requestHeaders;
    }

    public void setRequestHeaders(String requestHeaders) {
        this.requestHeaders = requestHeaders;
    }

    public String getQueryParams() {
        return queryParams;
    }

    public void setQueryParams(String queryParams) {
        this.queryParams = queryParams;
    }

    public String getResponseParams() {
        return responseParams;
    }

    public void setResponseParams(String responseParams) {
        this.responseParams = responseParams;
    }

    public int getResponseDatatypeId() {
        return responseDatatypeId;
    }

    public void setResponseDatatypeId(int responseDatatypeId) {
        this.responseDatatypeId = responseDatatypeId;
    }

    public String getSuccessRespType() {
        return successRespType;
    }

    public void setSuccessRespType(String successRespType) {
        this.successRespType = successRespType;
    }

    public String getSuccessRespExample() {
        return successRespExample;
    }

    public void setSuccessRespExample(String successRespExample) {
        this.successRespExample = successRespExample;
    }

    public String getFailRespType() {
        return failRespType;
    }

    public void setFailRespType(String failRespType) {
        this.failRespType = failRespType;
    }

    public String getFailRespExample() {
        return failRespExample;
    }

    public void setFailRespExample(String failRespExample) {
        this.failRespExample = failRespExample;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

	public String getRequestExample() {
		return requestExample;
	}

	public void setRequestExample(String requestExample) {
		this.requestExample = requestExample;
	}

	public String getNeedResources() {
		return needResources;
	}

	public void setNeedResources(String needResources) {
		this.needResources = needResources;
	}

	public long getWikiId() {
		return wikiId;
	}

	public void setWikiId(long wikiId) {
		this.wikiId = wikiId;//this.wikiId = wikiId == null ? null : wikiId.trim();
	}

	public long getWikiParentId() {
		return wikiParentId;
	}

	public void setWikiParentId(long wikiParentId) {
		this.wikiParentId = wikiParentId;//this.wikiParentId = wikiParentId == null ? null : wikiParentId.trim();
	}

	public String getRespRemark() {
		return respRemark;
	}

	public void setRespRemark(String respRemark) {
		this.respRemark = respRemark == null ? null : respRemark.trim();
	}

	public String getCreatUser() {
		return creatUser;
	}

	public void setCreatUser(String creatUser) {
		this.creatUser = creatUser == null ? null : creatUser.trim();
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser == null ? null : updateUser.trim();
	}

	public String getReqRemark() {
		return reqRemark;
	}

	public void setReqRemark(String reqRemark) {
		this.reqRemark = reqRemark;//this.reqRemark = reqRemark == null ? null : reqRemark.trim();
	}
	
	
    
}
