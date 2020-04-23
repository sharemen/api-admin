package org.s.api.admin.core.model;

import java.sql.Timestamp;
import java.util.Date;


public class ApiHistory {

    private long id;
    private long documentId;             // 接口ID
    private Date time;               // 创建时间
    private String user;          // 操作用户
    private String operate;       // 操作类型:add,update,del

    
    
    public ApiHistory(int documentId,String user, String operate) {
		super();
		this.user = user;
		this.operate = operate;
		this.documentId = documentId;
	}

    
	public ApiHistory(long id, long documentId, Timestamp time, String user, String operate) {
		super();
		this.id = id;
		this.documentId = documentId;
		this.time = time;
		this.user = user;
		this.operate = operate;
	}


	public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(long documentId) {
        this.documentId = documentId;
    }

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;//this.time = time == null ? null : time.trim();
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user == null ? null : user.trim();
	}

	public String getOperate() {
		return operate;
	}

	public void setOperate(String operate) {
		this.operate = operate == null ? null : operate.trim();
	}
    
}
