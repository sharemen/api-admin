package org.s.api.admin.controller;

import org.s.api.admin.controller.annotation.PermessionLimit;
import org.s.api.admin.core.consistant.RequestConfig;
import org.s.api.admin.core.model.ReturnT;
import org.s.api.admin.core.model.XxlApiDocument;
import org.s.api.admin.core.model.XxlApiMock;
import org.s.api.admin.dao.IXxlApiDocumentDao;
import org.s.api.admin.dao.IXxlApiMockDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author xuxueli 2017-03-31 18:10:37
 */
@Controller
@RequestMapping("/mock")
public class XxlApiMockController {
	private static Logger logger = LoggerFactory.getLogger(XxlApiMockController.class);

	@Resource
	private IXxlApiMockDao xxlApiMockDao;
	@Resource
	private IXxlApiDocumentDao xxlApiDocumentDao;
	
	static final int DEFAULT_STATE = 1;

	/**
	 * 保存Mock数据
	 * @param xxlApiMock
	 * @return
	 */
	@RequestMapping("/add")
	@ResponseBody
	public ReturnT<String> add(XxlApiMock xxlApiMock) {

		XxlApiDocument document = xxlApiDocumentDao.load(xxlApiMock.getDocumentId());
		if (document == null) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "保存Mock数据失败，接口ID非法");
		}
		String uuid = UUID.randomUUID().toString();

		xxlApiMock.setUuid(uuid);
		List<XxlApiMock> mocklist = xxlApiMockDao.loadAll(xxlApiMock.getDocumentId());
		if(mocklist == null || mocklist.isEmpty()) {
			//添加的第一条mock数据自动为默认数据
			xxlApiMock.setIsdefault(DEFAULT_STATE);
		}
		xxlApiMock.setReqUri(document.getRequestUrl());
		
		int ret = xxlApiMockDao.add(xxlApiMock);
		return (ret>0)?ReturnT.SUCCESS:ReturnT.FAIL;
	}

	@RequestMapping("/delete")
	@ResponseBody
	public ReturnT<String> delete(int id) {
		int ret = xxlApiMockDao.delete(id);
		if(ret>0) {
			return ReturnT.SUCCESS;
		}else {
			
			return ReturnT.FAIL;
		}
	}

	@RequestMapping("/update")
	@ResponseBody
	public ReturnT<String> update(XxlApiMock xxlApiMock) {
		
		
		XxlApiDocument document = xxlApiDocumentDao.load(xxlApiMock.getDocumentId());
		if (document == null) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "保存Mock数据失败，接口ID非法");
		}
		xxlApiMock.setReqUri(document.getRequestUrl());
		
		int ret = xxlApiMockDao.update(xxlApiMock);
		return (ret>0)?ReturnT.SUCCESS:ReturnT.FAIL;
	}
	
	@RequestMapping("/setdefault")
	@ResponseBody
	public ReturnT<String> setDefault(int id) {
		XxlApiMock xxlApiMock = xxlApiMockDao.load(id);
		xxlApiMock.setIsdefault(DEFAULT_STATE);
		
		//重置该api的默认状态
		xxlApiMockDao.resetDefaultByUri(xxlApiMock);
		//修改当前mock为默认
		int ret = xxlApiMockDao.update(xxlApiMock);
		return (ret>0)?ReturnT.SUCCESS:ReturnT.FAIL;
	}

	@RequestMapping("/run/{uuid}")
	@PermessionLimit(limit=false)
	public void run(@PathVariable("uuid") String uuid, HttpServletRequest request, HttpServletResponse response) {
		XxlApiMock xxlApiMock = xxlApiMockDao.loadByUuid(uuid);
		if (xxlApiMock == null) {
			throw new RuntimeException("Mock数据ID非法");
		}

		RequestConfig.ResponseContentType contentType = RequestConfig.ResponseContentType.match(xxlApiMock.getRespType());
		if (contentType == null) {
			throw new RuntimeException("Mock数据响应数据类型(MIME)非法");
		}

		// write response
		try {
			response.setCharacterEncoding("UTF-8");
			response.setContentType(contentType.type);

			PrintWriter writer = response.getWriter();
			writer.write(xxlApiMock.getRespExample());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	@RequestMapping("/run/**")
	@PermessionLimit(limit=false)
	public void runbyURI(HttpServletRequest request, HttpServletResponse response) {
		String uri =  request.getRequestURI();
		String uuid =null;
		XxlApiMock xxlApiMock = null;
		uri = uri.substring(uri.indexOf("/mock/run/")+"/mock/run".length(),uri.length());
		
		 //检查URI中是否包含uuid
		 String uuidRegex = "(\\p{XDigit}{8}-\\p{XDigit}{4}-\\p{XDigit}{4}-\\p{XDigit}{4}-\\p{XDigit}{12})(.*)?";
		 Pattern pattern = Pattern.compile(uuidRegex);
		 Matcher m = pattern.matcher(uri);
		 //
		 if(m.find()) {
			 uuid = m.group(0);
			 uri = m.replaceAll("");
			 uri = uri.substring(0,uri.lastIndexOf("/"));
		 }
		 
		//如果请求中带了uuid优先匹配
	    if(uuid !=null) {
	    	xxlApiMock = xxlApiMockDao.loadByUuid(uuid);
	    }
	    //其次匹配uri
	    if(xxlApiMock == null) {
	        xxlApiMock = xxlApiMockDao.loadByUri(uri);
	    }
		if (xxlApiMock == null) {
			throw new RuntimeException("未找到对应的Mock数据");
		}

		RequestConfig.ResponseContentType contentType = RequestConfig.ResponseContentType.match(xxlApiMock.getRespType());
		if (contentType == null) {
			throw new RuntimeException("Mock数据响应数据类型(MIME)非法");
		}

		// write response
		try {
			response.setCharacterEncoding("UTF-8");
			response.setContentType(contentType.type);

			PrintWriter writer = response.getWriter();
			writer.write(xxlApiMock.getRespExample());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

	}

}
