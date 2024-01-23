<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> <%@ taglib
prefix="e3ps" uri="/WEB-INF/tlds/e3ps-functions.tld"%>

<div class="sub_menu" id="sub_menu">
  <h2>${e3ps:getMessage('대시 보드')}</h2>


	<jsp:include page="${e3ps:getIncludeURLString('/portal/include_menuContents')}" flush="true">
		<jsp:param name="alias" value="dashboard" />
	</jsp:include>
  <%-- <!-- ss_menu -->
  <div class="ss_menu pt20">
    <ul class="line">
      <li>
        <h4>
          <a class="on" onclick="javascript:subMenuSlide(this);" href="#"
            >${e3ps:getMessage('프로젝트')} ${e3ps:getMessage('현황판')}</a
          >
        </h4>
        <ul class="subtt pdt7 pb15" style="display: none">
       	 <!-- 
          <li>
            <a
              class="linkMenu subLinkMenu"
              href="javascript:moveLocation('/dashboard/projectLevelDashboard')"
              >${e3ps:getMessage('제품군별')} ${e3ps:getMessage('현황판')}</a
            >
          </li>
          
          <li>
            <a
              class="linkMenu subLinkMenu"
              href="javascript:moveLocation('/dashboard/project/subsidiary')"
              >${e3ps:getMessage('계열사별')} ${e3ps:getMessage('현황판')}</a
            >
          </li>
          -->
          <li>
            <a
              class="linkMenu subLinkMenu"
              href="javascript:moveLocation('/dashboard/progress')"
              >${e3ps:getMessage('프로젝트')} ${e3ps:getMessage('진척현황')}</a
            >
          </li>
           <!-- 
          <li>
            <a
              class="linkMenu subLinkMenu"
              href="javascript:moveLocation('/dashboard/project/leadTime')"
              >${e3ps:getMessage('프로젝트')} ${e3ps:getMessage('리드타임')}</a
            >
          </li>
          
          <c:if test="${e3ps:isAdmin()}">
          <li>
            <a class="linkMenu subLinkMenu"  href="javascript:moveLocation('/dashboard/setting')" >${e3ps:getMessage('현황판 관리')}</a>
          </li>
          -->
          </c:if> 
          
        </ul>
      </li>
    </ul>
  </div>
  <!-- //ss_menu --> --%>
</div>
