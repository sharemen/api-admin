package org.s.api.admin.controller;

import org.s.api.admin.core.consistant.OperateEnum;
import org.s.api.admin.core.consistant.RequestConfig;
import org.s.api.admin.core.consistant.ResourceTypeEnum;
import org.s.api.admin.core.model.*;
import org.s.api.admin.core.util.JacksonUtil;
import org.s.api.admin.core.util.tool.ArrayTool;
import org.s.api.admin.core.util.tool.StringTool;
import org.s.api.admin.core.util.wiki.WIKIClient;
import org.s.api.admin.core.util.wiki.model.WikiPage;
import org.s.api.admin.dao.*;
import org.s.api.admin.service.IXxlApiDataTypeService;
import org.s.api.admin.service.impl.LoginService;
import org.s.api.admin.service.impl.WikiService;
import org.s.api.admin.service.impl.WikiService.WikiResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xuxueli 2017-03-31 18:10:37
 */
@Controller
@RequestMapping("/document")
public class XxlApiDocumentController {

	@Resource
	private IXxlApiBizDao xxlApiBizDao;
	@Resource
	private IXxlApiDocumentDao xxlApiDocumentDao;
	@Resource
	private IXxlApiProjectDao xxlApiProjectDao;
	@Resource
	private IXxlApiGroupDao xxlApiGroupDao;
	@Resource
	private IXxlApiMockDao xxlApiMockDao;
	@Resource
	private IXxlApiTestHistoryDao xxlApiTestHistoryDao;
	@Resource
	private ApiHistoryDao apiHistoryDao;
	@Resource
	private IXxlApiDataTypeService xxlApiDataTypeService;
	@Resource
	private  WikiService wikiService;
	@Resource
	private LoginService loginService;
	
	//访问wiki系统host
	@Value("${org.s.apiadmin.wikihost:https://de-wiki.dataenlighten.com}")
	private  String wikihost;
	
	//访问wiki系统的用户名
	@Value("${org.s.apiadmin.wikiuser:mjopenapi}")
	private String username;
	
	//是否允许自动生成API 的wiki文档 0 不允许 ，1 允许
	@Value("${org.s.apiadmin.enableSyncWiki:0}")
	private  String enableSyncWiki;

	private boolean hasBizPermission(HttpServletRequest request, int bizId){
		XxlApiUser loginUser = (XxlApiUser) request.getAttribute(LoginService.LOGIN_IDENTITY);
		if((Boolean)request.getAttribute(LoginService.LOGIN_GUEST) == true) {
			return false;
		}
		else
		if ( loginUser.getType()==1 ||
				ArrayTool.contains(StringTool.split(loginUser.getPermissionBiz(), ","), String.valueOf(bizId))
				) {
			return true;
		} else {
			return false;
		}
	}


	@RequestMapping("/markStar")
	@ResponseBody
	public ReturnT<String> markStar(HttpServletRequest request, int id, int starLevel) {

		XxlApiDocument document = xxlApiDocumentDao.load(id);
		if (document == null) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "操作失败，接口ID非法");
		}

		// 权限
		XxlApiProject apiProject = xxlApiProjectDao.load(document.getProjectId());
		if (!hasBizPermission(request, apiProject.getBizId())) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "您没有相关业务线的权限,请联系管理员开通");
		}

		document.setStarLevel(starLevel);

		int ret = xxlApiDocumentDao.update(document);
		return (ret>0)?ReturnT.SUCCESS:ReturnT.FAIL;
	}

	@RequestMapping("/delete")
	@ResponseBody
	public ReturnT<String> delete(HttpServletRequest request, int id) {

		XxlApiDocument document = xxlApiDocumentDao.load(id);
		if (document == null) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "操作失败，接口ID非法");
		}

		// 权限
		XxlApiProject apiProject = xxlApiProjectDao.load(document.getProjectId());
		if (!hasBizPermission(request, apiProject.getBizId())) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "您没有相关业务线的权限,请联系管理员开通");
		}

		// 存在Test记录，拒绝删除
		/*
		 * List<XxlApiTestHistory> historyList =
		 * xxlApiTestHistoryDao.loadByDocumentId(id); if (historyList!=null &&
		 * historyList.size()>0) { return new ReturnT<String>(ReturnT.FAIL_CODE,
		 * "拒绝删除，该接口下存在Test记录，不允许删除"); }
		 */

		// 存在Mock记录，拒绝删除
		/*
		 * List<XxlApiMock> mockList = xxlApiMockDao.loadAll(id); if (mockList!=null &&
		 * mockList.size()>0) { return new ReturnT<String>(ReturnT.FAIL_CODE,
		 * "拒绝删除，该接口下存在Mock记录，不允许删除"); }
		 */

		int ret = xxlApiDocumentDao.delete(id);
		
		//记录操作日志
		XxlApiUser loginUser = loginService.ifLogin(request);
		apiHistoryDao.add(new ApiHistory(id,loginUser.getUserName(),OperateEnum.DEL.getValue()));
		
		
		return (ret>0)?ReturnT.SUCCESS:ReturnT.FAIL;
	}

	/**
	 * 新增，API
	 *
	 * @param projectId
	 * @return
	 */
	@RequestMapping("/addPage")
	public String addPage(HttpServletRequest request, Model model, int projectId, @RequestParam(required = false, defaultValue = "0") int groupId) {

		// project
		XxlApiProject project = xxlApiProjectDao.load(projectId);
		if (project == null) {
			throw new RuntimeException("操作失败，项目ID非法");
		}
		model.addAttribute("projectId", projectId);
		model.addAttribute("groupId", groupId);


		// 权限
		if (!hasBizPermission(request, project.getBizId())) {
			throw new RuntimeException("您没有相关业务线的权限,请联系管理员开通");
		}

		// groupList
		List<XxlApiGroup> groupList = xxlApiGroupDao.loadAll(projectId);
		model.addAttribute("groupList", groupList);

		// enum
		model.addAttribute("RequestMethodEnum", RequestConfig.RequestMethodEnum.values());
		model.addAttribute("requestHeadersEnum", RequestConfig.requestHeadersEnum);
		model.addAttribute("QueryParamTypeEnum", RequestConfig.QueryParamTypeEnum.values());
		model.addAttribute("ResponseContentType", RequestConfig.ResponseContentType.values());
		model.addAttribute("ResourceTypeEnum", ResourceTypeEnum.values());
		model.addAttribute("wikihost",wikihost);
		model.addAttribute("wikiusername",username);
		model.addAttribute("enableSyncWiki",enableSyncWiki);
		
		
		return "document/document.add";
	}
	@RequestMapping("/add")
	@ResponseBody
	public ReturnT<Integer> add(HttpServletRequest request, XxlApiDocument xxlApiDocument) {

		XxlApiProject project = xxlApiProjectDao.load(xxlApiDocument.getProjectId());
		if (project == null) {
			return new ReturnT<Integer>(ReturnT.FAIL_CODE, "操作失败，项目ID非法");
		}

		// 权限
		if (!hasBizPermission(request, project.getBizId())) {
			return new ReturnT<Integer>(ReturnT.FAIL_CODE, "您没有相关业务线的权限,请联系管理员开通");
		}
		//获取当前用户
		XxlApiUser loginUser = loginService.ifLogin(request);
		//设置接口操作人
		xxlApiDocument.setCreatUser(loginUser.getUserName());
		xxlApiDocument.setUpdateUser(loginUser.getUserName());
		
		int ret = xxlApiDocumentDao.add(xxlApiDocument);
		
		//记录操作日志
		apiHistoryDao.add(new ApiHistory(xxlApiDocument.getId(),loginUser.getUserName(),OperateEnum.ADD.getValue()));
		
		
		//触发同步wiki生成api文档
		if(ret > 0) {
			WikiResult wikiResult = wikiService.saveOrUpdateWiki(xxlApiDocument);
			if(wikiResult!=null && wikiResult.getWikiId() != 0) {
				xxlApiDocument.setWikiId(wikiResult.getWikiId());
				xxlApiDocumentDao.update(xxlApiDocument);
			
			}
		}
		
	
		return (ret>0)?new ReturnT<Integer>(xxlApiDocument.getId()):new ReturnT<Integer>(ReturnT.FAIL_CODE, null);
	}

	/**
	 * 更新，API
	 * @return
	 */
	@RequestMapping("/updatePage")
	public String updatePage(HttpServletRequest request, Model model, int id) {

		// document
		XxlApiDocument xxlApiDocument = xxlApiDocumentDao.load(id);
		if (xxlApiDocument == null) {
			throw new RuntimeException("操作失败，接口ID非法");
		}
		model.addAttribute("document", xxlApiDocument);
		model.addAttribute("requestHeadersList", (StringTool.isNotBlank(xxlApiDocument.getRequestHeaders()))?JacksonUtil.readValue(xxlApiDocument.getRequestHeaders(), List.class):null );
		model.addAttribute("queryParamList", (StringTool.isNotBlank(xxlApiDocument.getQueryParams()))?JacksonUtil.readValue(xxlApiDocument.getQueryParams(), List.class):null );
		model.addAttribute("responseParamList", (StringTool.isNotBlank(xxlApiDocument.getResponseParams()))?JacksonUtil.readValue(xxlApiDocument.getResponseParams(), List.class):null );
		model.addAttribute("needResourceList", (StringTool.isNotBlank(xxlApiDocument.getNeedResources()))?JacksonUtil.readValue(xxlApiDocument.getNeedResources(), List.class):null );
		
		// project
		int projectId = xxlApiDocument.getProjectId();
		model.addAttribute("projectId", projectId);


		// 权限
		XxlApiProject project = xxlApiProjectDao.load(xxlApiDocument.getProjectId());
		if (!hasBizPermission(request, project.getBizId())) {
			throw new RuntimeException("您没有相关业务线的权限,请联系管理员开通");
		}

		// groupList
		List<XxlApiGroup> groupList = xxlApiGroupDao.loadAll(projectId);
		model.addAttribute("groupList", groupList);

		// enum
		model.addAttribute("RequestMethodEnum", RequestConfig.RequestMethodEnum.values());
		model.addAttribute("requestHeadersEnum", RequestConfig.requestHeadersEnum);
		model.addAttribute("QueryParamTypeEnum", RequestConfig.QueryParamTypeEnum.values());
		model.addAttribute("ResponseContentType", RequestConfig.ResponseContentType.values());
		model.addAttribute("ResourceTypeEnum", ResourceTypeEnum.values());
		model.addAttribute("wikihost",wikihost);
		model.addAttribute("wikiusername",username);
		model.addAttribute("enableSyncWiki",enableSyncWiki);
		
		// 响应数据类型
		XxlApiDataType responseDatatypeRet = xxlApiDataTypeService.loadDataType(xxlApiDocument.getResponseDatatypeId());
		model.addAttribute("responseDatatype", responseDatatypeRet);
		
		//查询最近操作记录
		List<ApiHistory>  apiHistorys = apiHistoryDao.loadByDocumentId(id,10);
		model.addAttribute("apiHistorys", apiHistorys);
		
		return "document/document.update";
	}
	@RequestMapping("/update")
	@ResponseBody
	public ReturnT<String> update(HttpServletRequest request, XxlApiDocument xxlApiDocument) {

		XxlApiDocument oldVo = xxlApiDocumentDao.load(xxlApiDocument.getId());
		if (oldVo == null) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "操作失败，接口ID非法");
		}

		// 权限
		XxlApiProject project = xxlApiProjectDao.load(oldVo.getProjectId());
		if (!hasBizPermission(request, project.getBizId())) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "您没有相关业务线的权限,请联系管理员开通");
		}

		// fill not-change val
		xxlApiDocument.setProjectId(oldVo.getProjectId());
		xxlApiDocument.setStarLevel(oldVo.getStarLevel());
		xxlApiDocument.setAddTime(oldVo.getAddTime());
		xxlApiDocument.setCreatUser(oldVo.getCreatUser());
		//获取当前用户
		XxlApiUser loginUser = loginService.ifLogin(request);
		//设置接口操作人
		xxlApiDocument.setUpdateUser(loginUser.getUserName());
				
		int ret = xxlApiDocumentDao.update(xxlApiDocument);

		apiHistoryDao.add(new ApiHistory(xxlApiDocument.getId(),loginUser.getUserName(),OperateEnum.UPDATE.getValue()));
		
		//触发同步wiki生成api文档
		if(ret > 0) {
			//获取最新数据
			xxlApiDocument = xxlApiDocumentDao.load(xxlApiDocument.getId());
			WikiResult wikiResult = wikiService.saveOrUpdateWiki(xxlApiDocument);
			if(wikiResult !=null && wikiResult.getWikiId() != 0) {
				xxlApiDocument.setWikiId(wikiResult.getWikiId());
				xxlApiDocumentDao.update(xxlApiDocument);
			}
		}
		
		return (ret>0)?ReturnT.SUCCESS:ReturnT.FAIL;
	}

	/**
	 * 详情页，API
	 * @return
	 */
	@RequestMapping("/detailPage")
	public String detailPage(HttpServletRequest request, Model model, int id) {

		// document
		XxlApiDocument xxlApiDocument = xxlApiDocumentDao.load(id);
		if (xxlApiDocument == null) {
			throw new RuntimeException("操作失败，接口ID非法");
		}
		model.addAttribute("document", xxlApiDocument);
		model.addAttribute("requestHeadersList", (StringTool.isNotBlank(xxlApiDocument.getRequestHeaders()))?JacksonUtil.readValue(xxlApiDocument.getRequestHeaders(), List.class):null );
		model.addAttribute("queryParamList", (StringTool.isNotBlank(xxlApiDocument.getQueryParams()))?JacksonUtil.readValue(xxlApiDocument.getQueryParams(), List.class):null );
		model.addAttribute("responseParamList", (StringTool.isNotBlank(xxlApiDocument.getResponseParams()))?JacksonUtil.readValue(xxlApiDocument.getResponseParams(), List.class):null );
		model.addAttribute("needResourceList", (StringTool.isNotBlank(xxlApiDocument.getNeedResources()))?JacksonUtil.readValue(xxlApiDocument.getNeedResources(), List.class):null );
		
		// project
		int projectId = xxlApiDocument.getProjectId();
		XxlApiProject project = xxlApiProjectDao.load(projectId);
		model.addAttribute("projectId", projectId);
		model.addAttribute("project", project);

		// groupList
		List<XxlApiGroup> groupList = xxlApiGroupDao.loadAll(projectId);
		model.addAttribute("groupList", groupList);

		// mock list
		List<XxlApiMock> mockList = xxlApiMockDao.loadAll(id);
		model.addAttribute("mockList", mockList);

		// test list
		List<XxlApiTestHistory> testHistoryList = xxlApiTestHistoryDao.loadByDocumentId(id);
		model.addAttribute("testHistoryList", testHistoryList);

		// enum
		model.addAttribute("RequestMethodEnum", RequestConfig.RequestMethodEnum.values());
		model.addAttribute("requestHeadersEnum", RequestConfig.requestHeadersEnum);
		model.addAttribute("QueryParamTypeEnum", RequestConfig.QueryParamTypeEnum.values());
		model.addAttribute("ResponseContentType", RequestConfig.ResponseContentType.values());
		model.addAttribute("ResourceTypeEnum", ResourceTypeEnum.values());
		model.addAttribute("wikihost",wikihost);
		model.addAttribute("wikiusername",username);
		model.addAttribute("enableSyncWiki",enableSyncWiki);
		
		// 响应数据类型
		XxlApiDataType responseDatatypeRet = xxlApiDataTypeService.loadDataType(xxlApiDocument.getResponseDatatypeId());
		model.addAttribute("responseDatatype", responseDatatypeRet);
		
		Page<ApiHistory> page = new Page<ApiHistory>();
		page.setSize(10);
		page.setCurrent(1);
		IPage<ApiHistory> ipage = apiHistoryDao.selectApiHistory(page, id);
		ipage.getRecords();
		// 权限
		model.addAttribute("hasBizPermission", hasBizPermission(request, project.getBizId()));

		return "document/document.detail";
	}
	
	/**
	 * 详情页，API
	 * @return
	 */
	@RequestMapping("/apiIndex")
	public String apiIndex(HttpServletRequest request, Model model,ApiIndexParam param) {
		int apiId = param.getApiId();
		int projectId = param.getProjectId();
		//查询业务线列表
		List<XxlApiBiz> bizList = xxlApiBizDao.pageList(0, 100, null);
		model.addAttribute("bizList", bizList);
		
		//项目列表
		List<XxlApiProject> projectList = xxlApiProjectDao.pageList(0, 100, null, 0);
		Map<Integer,List<XxlApiProject>> projectMap = new HashMap<Integer,List<XxlApiProject>>();
		
		//按业务线索引项目信息
		for(XxlApiProject project : projectList) {
			if(!projectMap.containsKey(project.getBizId())){
				projectMap.put(project.getBizId(), new ArrayList<XxlApiProject>());
			}
			projectMap.get(project.getBizId()).add(project);
			
		}
		for(Integer bizkey : projectMap.keySet()) {
			model.addAttribute("project-"+String.valueOf(bizkey),projectMap.get(bizkey));
		}
		model.addAttribute("projectMap", projectMap);
		model.addAttribute("projectlist", projectList);
		
		if(apiId != 0) {
			// document
			XxlApiDocument xxlApiDocument = xxlApiDocumentDao.load(apiId);
			model.addAttribute("document", xxlApiDocument);
			if(xxlApiDocument!=null) {
				
			}
			model.addAttribute("requestHeadersList", (StringTool.isNotBlank(xxlApiDocument.getRequestHeaders()))?JacksonUtil.readValue(xxlApiDocument.getRequestHeaders(), List.class):null );
			model.addAttribute("queryParamList", (StringTool.isNotBlank(xxlApiDocument.getQueryParams()))?JacksonUtil.readValue(xxlApiDocument.getQueryParams(), List.class):null );
			model.addAttribute("responseParamList", (StringTool.isNotBlank(xxlApiDocument.getResponseParams()))?JacksonUtil.readValue(xxlApiDocument.getResponseParams(), List.class):null );
			// project
			projectId = xxlApiDocument.getProjectId();
			
			// mock list
			List<XxlApiMock> mockList = xxlApiMockDao.loadAll(apiId);
			model.addAttribute("mockList", mockList);

			// test list
			List<XxlApiTestHistory> testHistoryList = xxlApiTestHistoryDao.loadByDocumentId(apiId);
			model.addAttribute("testHistoryList", testHistoryList);
			
			// 响应数据类型
			XxlApiDataType responseDatatypeRet = xxlApiDataTypeService.loadDataType(xxlApiDocument.getResponseDatatypeId());
			model.addAttribute("responseDatatype", responseDatatypeRet);
		}
		
		model.addAttribute("projectId", projectId);
		if(projectId != 0) {
			XxlApiProject project = xxlApiProjectDao.load(projectId);
			
			model.addAttribute("project", project);
	
			// groupList 
			List<XxlApiGroup> groupList = xxlApiGroupDao.loadAll(projectId);
			model.addAttribute("groupList", groupList);
			// 权限
			model.addAttribute("hasBizPermission", hasBizPermission(request, project.getBizId()));
		}

	

		// enum
		model.addAttribute("RequestMethodEnum", RequestConfig.RequestMethodEnum.values());
		model.addAttribute("requestHeadersEnum", RequestConfig.requestHeadersEnum);
		model.addAttribute("QueryParamTypeEnum", RequestConfig.QueryParamTypeEnum.values());
		model.addAttribute("ResourceTypeEnum", ResourceTypeEnum.values());
		model.addAttribute("ResponseContentType", RequestConfig.ResponseContentType.values());

		

		

		return "document/api.index";
	}
	
	

}
