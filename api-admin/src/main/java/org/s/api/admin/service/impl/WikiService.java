package org.s.api.admin.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.s.api.admin.core.model.XxlApiDocument;
import org.s.api.admin.core.util.JacksonUtil;
import org.s.api.admin.core.util.tool.StringTool;
import org.s.api.admin.core.util.wiki.WIKIClient;
import org.s.api.admin.core.util.wiki.auth.AuthMethod;
import org.s.api.admin.core.util.wiki.auth.BasicAuth;
import org.s.api.admin.core.util.wiki.ex.WikiClientException;
import org.s.api.admin.core.util.wiki.model.WikiCreatReq;
import org.s.api.admin.core.util.wiki.model.WikiPage;
import org.s.api.admin.core.util.wiki.model.WikiUpdateReq;
import org.s.api.admin.dao.IXxlApiDocumentDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.template.Template;
import freemarker.template.TemplateException;


@Service
public class WikiService {
	//访问wiki系统的用户名
	@Value("${org.s.apiadmin.wikiuser:}")
	private String username;
	//访问wiki系统的密码
	@Value("${org.s.apiadmin.wikipass:}")
	private  String password;
	//访问wiki系统host
	@Value("${org.s.apiadmin.wikihost:}")
	private  String wikihost;
	@Value("${org.s.apiadmin.wikidefaultspace:}")
	//wikipage 默认保存空间key
	private  String defaultspace;
	//是否允许自动生成API 的wiki文档 0 不允许 ，1 允许
	@Value("${org.s.apiadmin.enableSyncWiki:0}")
	private  String enableSyncWiki;
	
	//为了防止跟已有的wiki页面重名，给自动生成的wiki title加个后缀
	@Value("${org.s.apiadmin.wikisuffix:[apiadmin]}")
	private  String wikisuffix;
	
	@Autowired
	FreeMarkerConfigurer configurer;
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private boolean  enableSyncWiki() {
		return "1".equals(enableSyncWiki);
	}
	
	public class WikiResult{
		long wikiId;
		
		String returnMsg;

		
		

		public WikiResult(long wikiId, String returnMsg) {
			super();
			this.wikiId = wikiId;
			this.returnMsg = returnMsg;
		}
		
		

		public WikiResult(String errorMsg) {
			super();
			this.returnMsg = errorMsg;
		}



		public long getWikiId() {
			return wikiId;
		}

		public void setWikiId(long wikiId) {
			this.wikiId = wikiId;//this.wikiId = wikiId == null ? null : wikiId.trim();
		}

		public String getReturnMsg() {
			return returnMsg;
		}

		public void setReturnMsg(String returnMsg) {
			this.returnMsg = returnMsg == null ? null : returnMsg.trim();
		}
		
		
	}
	
	@Resource
	private IXxlApiDocumentDao xxlApiDocumentDao;
	
	public WikiResult saveOrUpdateWiki(XxlApiDocument document) {
	
		AuthMethod basicAuth = new BasicAuth(username, password);
		WikiResult wikiResult =null;
		
		if(!enableSyncWiki()) {
			return new  WikiResult("wiki自动同步功能未开启");
		}
			
		//已有对应wiki文档则更新
		if(document.getWikiId() !=0) {

			WikiUpdateReq updateReq = new WikiUpdateReq();
			updateReq.setId(document.getWikiId());
			updateReq.setTitle(document.getName()+wikisuffix);
			
			
			//获取API数据对应的 WIKI内容
			updateReq.setContent(getWikiContent(document));

			try {
				WikiPage wikipage= WIKIClient.updateObj(wikihost, basicAuth, updateReq);
				
				wikiResult = new WikiResult(wikipage.getId(), "wiki文档更新成功");
				logger.info(" API "+document.getName() +" "+wikiResult.getReturnMsg()+" "+wikihost+"/pages/viewpage.action?pageId="+wikiResult.getWikiId());
			} catch (WikiClientException e) {
				wikiResult = new  WikiResult("wiki文档更新失败");
				logger.error(" API "+document.getName() +wikiResult.getReturnMsg()+e);
				
			}
		}
		else//只有指定了上级页面id.确定了页面位置，才会创建新的wiki页面
		if(document.getWikiParentId()!=0) {
			//创建wiki
			WikiCreatReq wikiCreatReq = new WikiCreatReq();
			wikiCreatReq.setTitle(document.getName()+wikisuffix);
			//生成 api wiki page 内容
			wikiCreatReq.setContent(getWikiContent(document));
			wikiCreatReq.setSpacekey(defaultspace);
			
			WikiPage parentWikiPage =null;
			try {
				if(document.getWikiParentId()!=0) {
					parentWikiPage = WIKIClient.findByIdObj(wikihost, basicAuth, document.getWikiParentId());
				}
				if(parentWikiPage !=null) {
					wikiCreatReq.setParentPageId(document.getWikiParentId());
					wikiCreatReq.setSpacekey(parentWikiPage.getSpacekey());
				}
				 WikiPage wikinewpage = WIKIClient.creatObj(wikihost,basicAuth,wikiCreatReq);
				 wikiResult = new WikiResult(wikinewpage.getId(), "wiki文档创建成功");
				 logger.info(" API "+document.getName() +" "+wikiResult.getReturnMsg()+" "+wikihost+"/pages/viewpage.action?pageId="+wikiResult.getWikiId());
				 
			} catch (WikiClientException e) {
				wikiResult = new  WikiResult("wiki文档创建失败");
				logger.error(" API "+document.getName() +wikiResult.getReturnMsg()+e);
				
			}

		}
		
		return wikiResult ;
	}
	
	/**
	 * 通过freemarker模板生成wiki页内容
	 * @param document
	 * @return
	 */
	private String getWikiContent(XxlApiDocument document) {
		String resp = null;
		Map <String,Object> conextMap = new HashMap<String,Object>();
		
		conextMap.put("document", document);
		conextMap.put("requestHeadersList", (StringTool.isNotBlank(document.getRequestHeaders()))?JacksonUtil.readValue(document.getRequestHeaders(), List.class):null );
		conextMap.put("queryParamList", (StringTool.isNotBlank(document.getQueryParams()))?JacksonUtil.readValue(document.getQueryParams(), List.class):null );
		conextMap.put("responseParamList", (StringTool.isNotBlank(document.getResponseParams()))?JacksonUtil.readValue(document.getResponseParams(), List.class):null );
		conextMap.put("needResourceList", (StringTool.isNotBlank(document.getNeedResources()))?JacksonUtil.readValue(document.getNeedResources(), List.class):null );

		
		//生成 api wiki page 内容
		 try {
			Template template = configurer.getConfiguration().getTemplate("wiki/api.wikitemplate.ftl");
		    resp = FreeMarkerTemplateUtils.processTemplateIntoString(template,conextMap);
			
		 } catch (IOException | TemplateException e1) {

			 logger.error(" API "+document.getName() +"wiki内容生成失败"+e1);
		}
		 return resp;
	}
}
