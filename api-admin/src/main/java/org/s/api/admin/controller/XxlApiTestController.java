package org.s.api.admin.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.s.api.admin.core.consistant.RequestConfig;
import org.s.api.admin.core.model.ReturnT;
import org.s.api.admin.core.model.XxlApiDocument;
import org.s.api.admin.core.model.XxlApiProject;
import org.s.api.admin.core.model.XxlApiTestHistory;
import org.s.api.admin.core.util.JacksonUtil;
import org.s.api.admin.core.util.ThrowableUtil;
import org.s.api.admin.core.util.tool.StringTool;
import org.s.api.admin.dao.IXxlApiDocumentDao;
import org.s.api.admin.dao.IXxlApiGroupDao;
import org.s.api.admin.dao.IXxlApiProjectDao;
import org.s.api.admin.dao.IXxlApiTestHistoryDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

/**
 * @author xuxueli 2017-04-04 18:10:54
 */
@Controller
@RequestMapping("/test")
public class XxlApiTestController {
	private static Logger logger = LoggerFactory.getLogger(XxlApiTestController.class);

	@Resource
	private IXxlApiDocumentDao xxlApiDocumentDao;
	@Resource
	private IXxlApiTestHistoryDao xxlApiTestHistoryDao;
	@Resource
	private IXxlApiProjectDao xxlApiProjectDao;
	@Resource
	private IXxlApiGroupDao xxlApiGroupDao;
	
	

	
	 //是否开启生生产环境接口的run权限:1开启 / 0不开启(默认)
    @Value("${org.s.apiadmin.enableProdRun:0}")
    private String enableProdRun;
    
	
	/**
	 * 接口测试，待完善
	 * @return
	 */
	@RequestMapping
	public String index(Model model,
						int documentId,
						@RequestParam(required = false, defaultValue = "0") int testId) {


		// params
		XxlApiDocument document = document = xxlApiDocumentDao.load(documentId);
		if (document == null) {
			throw new RuntimeException("接口ID非法");
		}
		XxlApiProject project = xxlApiProjectDao.load(document.getProjectId());

		List<Map<String, String>> requestHeaders = null;
		List<Map<String, String>> queryParams = null;

		if (testId > 0) {
			XxlApiTestHistory testHistory = xxlApiTestHistoryDao.load(testId);
			if (testHistory == null) {
				throw new RuntimeException("测试用例ID非法");
			}
			model.addAttribute("testHistory", testHistory);

			requestHeaders = (StringTool.isNotBlank(testHistory.getRequestHeaders()))? JacksonUtil.readValue(testHistory.getRequestHeaders(), List.class):null;
			queryParams = (StringTool.isNotBlank(testHistory.getQueryParams()))? JacksonUtil.readValue(testHistory.getQueryParams(), List.class):null;
		} else {
			requestHeaders = (StringTool.isNotBlank(document.getRequestHeaders()))? JacksonUtil.readValue(document.getRequestHeaders(), List.class):null;
			queryParams = (StringTool.isNotBlank(document.getQueryParams()))? JacksonUtil.readValue(document.getQueryParams(), List.class):null;
		}

		model.addAttribute("document", document);
		model.addAttribute("project", project);
		model.addAttribute("requestHeaders", requestHeaders);
		model.addAttribute("queryParams", queryParams);
        model.addAttribute("documentId", documentId);
        model.addAttribute("testId", testId);

		// enum
		model.addAttribute("RequestMethodEnum", RequestConfig.RequestMethodEnum.values());
		model.addAttribute("requestHeadersEnum", RequestConfig.requestHeadersEnum);
		model.addAttribute("QueryParamTypeEnum", RequestConfig.QueryParamTypeEnum.values());
		model.addAttribute("ResponseContentType", RequestConfig.ResponseContentType.values());
		model.addAttribute("enableProdRun" ,enableProdRun);
		
		return "test/test.index";
	}

	@RequestMapping("/add")
	@ResponseBody
	public ReturnT<Integer> add(XxlApiTestHistory xxlApiTestHistory) {
		int ret = xxlApiTestHistoryDao.add(xxlApiTestHistory);
		return ret>0?new ReturnT<Integer>(xxlApiTestHistory.getId()):new ReturnT<Integer>(ReturnT.FAIL_CODE, null);
	}

	@RequestMapping("/update")
	@ResponseBody
	public ReturnT<String> update(XxlApiTestHistory xxlApiTestHistory) {
		int ret = xxlApiTestHistoryDao.update(xxlApiTestHistory);
		return ret>0?ReturnT.SUCCESS:ReturnT.FAIL;
	}

	@RequestMapping("/delete")
	@ResponseBody
	public ReturnT<String> delete(int id) {
		int ret = xxlApiTestHistoryDao.delete(id);
		return ret>0?ReturnT.SUCCESS:ReturnT.FAIL;
	}

	/**
	 * 测试Run
	 * @return
	 */
	@RequestMapping("/run")
	@ResponseBody
	public ReturnT<String> run(XxlApiTestHistory xxlApiTestHistory, HttpServletRequest request, HttpServletResponse response) {

		// valid
		RequestConfig.ResponseContentType contentType = RequestConfig.ResponseContentType.match(xxlApiTestHistory.getRespType());
		if (contentType == null) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "响应数据类型(MIME)非法");
		}

		if (StringTool.isBlank(xxlApiTestHistory.getRequestUrl())) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "请输入接口URL");
		}
		

		// request headers
		Map<String, String> requestHeaderMap = null;
		List<Map<String, String>> requestHeaders = (StringTool.isNotBlank(xxlApiTestHistory.getRequestHeaders()))? JacksonUtil.readValue(xxlApiTestHistory.getRequestHeaders(), List.class):null;
		if (requestHeaders!=null && requestHeaders.size()>0) {
			requestHeaderMap = new HashMap<String, String>();
			for (Map<String, String> item: requestHeaders) {
				requestHeaderMap.put(item.get("key"), item.get("value"));
			}
		}

		// query param
		Map<String, Object> queryParamMap = null;
		List<Map<String, Object>> queryParams = (StringTool.isNotBlank(xxlApiTestHistory.getQueryParams()))? JacksonUtil.readValue(xxlApiTestHistory.getQueryParams(), List.class):null;
		if (queryParams!=null && queryParams.size()>0) {
			queryParamMap = new HashMap<String, Object>();
			for (Map<String, Object> item: queryParams) {
				queryParamMap.put((String)item.get("key"), item.get("value"));
			}
		}

		// invoke 1/3
		HttpRequestBase remoteRequest = null;
		if (RequestConfig.RequestMethodEnum.POST.name().equals(xxlApiTestHistory.getRequestMethod())) {
			HttpPost httpPost = new HttpPost(xxlApiTestHistory.getRequestUrl());
			
			// query params
			if (queryParamMap != null && !queryParamMap.isEmpty()) {
				List<NameValuePair> formParams = new ArrayList<NameValuePair>();
				for(Map.Entry<String,Object> entry : queryParamMap.entrySet()){
					formParams.add(new BasicNameValuePair(entry.getKey(), String.valueOf(entry.getValue())));
				}
				try {
					//是否 用requestbody传输
					if(xxlApiTestHistory.getIsRequestBody()) {
 						httpPost.setEntity(new StringEntity(JSON.toJSONString(queryParamMap),"","UTF-8"));
 					}else {
 						httpPost.setEntity(new UrlEncodedFormEntity(formParams, "UTF-8"));
 					}
					
				} catch (UnsupportedEncodingException e) {
					logger.error(e.getMessage(), e);
				} 
			}
			remoteRequest = httpPost;
		} else if (RequestConfig.RequestMethodEnum.GET.name().equals(xxlApiTestHistory.getRequestMethod())) {
			remoteRequest = new HttpGet(markGetUrl(xxlApiTestHistory.getRequestUrl(), queryParamMap));
		} else if (RequestConfig.RequestMethodEnum.PUT.name().equals(xxlApiTestHistory.getRequestMethod())) {
			remoteRequest = new HttpPut(markGetUrl(xxlApiTestHistory.getRequestUrl(), queryParamMap));
		} else if (RequestConfig.RequestMethodEnum.DELETE.name().equals(xxlApiTestHistory.getRequestMethod())) {
			remoteRequest = new HttpDelete(markGetUrl(xxlApiTestHistory.getRequestUrl(), queryParamMap));
		} else if (RequestConfig.RequestMethodEnum.HEAD.name().equals(xxlApiTestHistory.getRequestMethod())) {
			remoteRequest = new HttpHead(markGetUrl(xxlApiTestHistory.getRequestUrl(), queryParamMap));
		} else if (RequestConfig.RequestMethodEnum.OPTIONS.name().equals(xxlApiTestHistory.getRequestMethod())) {
			remoteRequest = new HttpOptions(markGetUrl(xxlApiTestHistory.getRequestUrl(), queryParamMap));
		} else if (RequestConfig.RequestMethodEnum.PATCH.name().equals(xxlApiTestHistory.getRequestMethod())) {
			remoteRequest = new HttpPatch(markGetUrl(xxlApiTestHistory.getRequestUrl(), queryParamMap));
		} else {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "请求方法非法");
		}

		// invoke 2/3
		if (requestHeaderMap!=null && !requestHeaderMap.isEmpty()) {
			for(Map.Entry<String,String> entry : requestHeaderMap.entrySet()){
				remoteRequest.setHeader(entry.getKey(), entry.getValue());
			}
		}
		
		String responseContent = null;
		
		//有接口需要实现登录才能访问
		if(xxlApiTestHistory.getWithTestToken()) {
			
			responseContent = remoteCallWtihLogin(remoteRequest,request,queryParamMap); 
			
		
		}else {
			// write response
			responseContent = remoteCall(remoteRequest);
		}
		
		
		
		return new ReturnT<String>(responseContent);
	}

	private String markGetUrl(String url, Map<String, Object> queryParamMap){
		String finalUrl = url;
		if (queryParamMap!=null && queryParamMap.size()>0) {
			finalUrl = url + "?";
			for(Map.Entry<String,Object> entry : queryParamMap.entrySet()){
				finalUrl += entry.getKey() + "=" + entry.getValue() + "&";
			}
			finalUrl = finalUrl.substring(0, finalUrl.length()-1);	// 后缀处理，去除 ？ 或 & 符号
		}
		return finalUrl;
	}

	/**
	 * 实现网关登录调用  公司自己有接口网关需要登录时使用
	 * @param remoteRequest
	 * @param request
	 * @return
	 */
	private String remoteCallWtihLogin(HttpRequestBase remoteRequest,HttpServletRequest request,Map<String, Object> queryParamMap) {
		String responseContent = null;
		
		//TODO 实现接口网关登录逻辑
			
		responseContent = remoteCall( remoteRequest );
			
			
		
		
		return responseContent;
	}
	
	
	private String remoteCall(HttpRequestBase remoteRequest){
		// remote test
		String responseContent = null;
		remoteRequest.setHeader(HttpHeaders.CONTENT_TYPE,"application/json;charset=utf-8");
		CloseableHttpClient httpClient = null;
		try{
			org.apache.http.client.config.RequestConfig requestConfig = org.apache.http.client.config.RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();
			remoteRequest.setConfig(requestConfig);
			
			httpClient = HttpClients.custom().disableAutomaticRetries().build();

			// parse response
			HttpResponse response = httpClient.execute(remoteRequest);
			HttpEntity entity = response.getEntity();
			if (null != entity) {
				int statusCode = response.getStatusLine().getStatusCode();
				if (statusCode == 200) {
					responseContent = EntityUtils.toString(entity, "UTF-8");
				} else {
					responseContent = "请求状态异常：" + response.getStatusLine().getStatusCode()+"\\n"+EntityUtils.toString(entity, "UTF-8");
					if (statusCode==302 && response.getFirstHeader("Location")!=null) {
						responseContent += "；Redirect地址：" + response.getFirstHeader("Location").getValue();
					}

				}
				EntityUtils.consume(entity);
			}
			logger.info("http statusCode error, statusCode:" + response.getStatusLine().getStatusCode());
		} catch (Exception e) {
			responseContent = "请求异常：" + ThrowableUtil.toString(e);
		} finally{
			if (remoteRequest!=null) {
				remoteRequest.releaseConnection();
			}
			if (httpClient!=null) {
				try {
					httpClient.close();
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
		return responseContent;
	}

    
}
