package org.s.api.admin.core.model;

import java.util.Date;

/**
 * Created by xuxueli on 17/4/1.
 */
public class XxlApiMock {

    private int id;
    private int documentId;         // 接口ID
    private String uuid;
    private String respType;        // Response Content-type：如JSON、XML、HTML、TEXT、JSONP
    private String respExample;     // Response Content
    private String reqUri; //mock数据对应的api URI 冗余存储以便于 检索，展示
    private int isdefault; //是否为默认mock数据，0否，1是，当通过uri查询时，否认给出1的数据
    private Date creatTime;
    private Date updateTime;
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDocumentId() {
        return documentId;
    }

    public void setDocumentId(int documentId) {
        this.documentId = documentId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getRespType() {
        return respType;
    }

    public void setRespType(String respType) {
        this.respType = respType;
    }

    public String getRespExample() {
        return respExample;
    }

    public void setRespExample(String respExample) {
        this.respExample = respExample;
    }

	public String getReqUri() {
		return reqUri;
	}

	public void setReqUri(String reqUri) {
		this.reqUri = reqUri;
	}

	public int getIsdefault() {
		return isdefault;
	}

	public void setIsdefault(int isdefault) {
		this.isdefault = isdefault;
	}

	public Date getCreatTime() {
		return creatTime;
	}

	public void setCreatTime(Date creatTime) {
		this.creatTime = creatTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
    
    
}
