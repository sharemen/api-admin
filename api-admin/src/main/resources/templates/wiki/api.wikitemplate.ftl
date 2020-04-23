<ac:structured-macro ac:name="markdown"><ac:plain-text-body><![CDATA[
由 ${document.creatUser} 于  ${document.addTime ?string('yyyy-MM-dd HH:mm:ss')} 创建<br/>
由  ${document.updateUser} 于  ${document.updateTime ?string('yyyy-MM-dd HH:mm:ss')} 最后修改<br/>

## URL

> ${document.requestUrl!""}

----

## 依赖资源
<#if needResourceList?exists && needResourceList?size gt 0>
| 资源类型              | 资源名称   | 资源说明                                                       |
| ------------------- | ------ | -------- | ------------------------------------------------------------------------- |
<#list needResourceList as needResource>
    | ${needResource.type} |  ${needResource.name} | ${needResource.desc} |
</#list>
 <#else>
 ----
</#if>

## 功能说明

${document.remark!""}

----

## 请求方式

> ${document.requestMethod!""}

----
## 请求头部


<#if requestHeadersList?exists  && requestHeadersList?size gt 0>
| 头部标签             | 头部内容   | 
| ------ |  -------------------|
 <#list requestHeadersList as requestHeadersMap>
    <#assign key = requestHeadersMap['key'] />
    <#assign value = requestHeadersMap['value'] />
    | ${key} | ${value} | 
</#list>
 <#else>
 ----
</#if>


## 请求参数

<#if queryParamList?exists>
| 字段名              | 类型   | 是否必填 | 说明                                                                      |
| ------------------- | ------ | -------- | ------------------------------------------------------------------------- |
<#list queryParamList as queryParam>
    | ${queryParam.name} | ${queryParam.type} | <#if queryParam.notNull == "true" >是<#else>否</#if>      | ${queryParam.desc}       |
</#list>
 <#else>
 ----
</#if>


## 请求参数说明

${document.reqRemark!""}

----
### 请求示例

``` json
${document.requestExample!""}
```


## 返回结果


<#if responseParamList?exists && responseParamList?size gt 0>
| 字段名              | 类型   | 是否必填 | 说明                                                                      |
| ------------------- | ------ | -------- | ------------------------------------------------------------------------- |
<#list responseParamList as responseParam>
    | ${responseParam.name} | ${responseParam.type} | <#if responseParam.notNull == "true" >是<#else>否</#if>      | ${responseParam.desc}       |
</#list>
 <#else>
 ----
</#if>

## 返回结果说明

${document.respRemark!""}


### 返回成功示例

``` json
${document.successRespExample}
```


### 返回失败示例

``` json
${document.failRespExample}
```
----
]]></ac:plain-text-body></ac:structured-macro>
