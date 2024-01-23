<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="e3ps" uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<div class="sub_menu" id="sub_menu">
  <h2>${e3ps:getMessage('배포 관리')}</h2>

  <jsp:include page="${e3ps:getIncludeURLString('/statistics/include_statistics')}" flush="true">
    <jsp:param name="obj" value="distribute" />
    <jsp:param name="active" value="true" />
    <jsp:param name="today" value="true" />
  </jsp:include>
  
  <jsp:include page="${e3ps:getIncludeURLString('/portal/include_menuContents')}" flush="true">
		<jsp:param name="alias" value="distribute" />
	</jsp:include>

  <%-- <!-- ss_menu -->
  <div class="ss_menu pt20">
    <ul class="line">
      <li>
        <h4>
          <a class="on" onclick="javascript:subMenuSlide(this);" href="#"
            >${e3ps:getMessage('배포 관리')}</a
          >
        </h4>
        <ul class="subtt pdt7 pb15" style="display: none">
          <li>
            <a
              class="linkMenu subLinkMenu"
              href="javascript:moveLocation('/distribute/searchDistribute')"
              >${e3ps:getMessage('도면출도의뢰서 검색')}</a
            >
          </li>
          <li>
            <a
              class="linkMenu subLinkMenu"
              href="javascript:moveLocation('/distribute/createDistribute')"
              >${e3ps:getMessage('배포 등록')}</a
            >
          </li>
           <li>
            <a
              class="linkMenu subLinkMenu"
              href="javascript:moveLocation('/distribute/requestFormDistribute')"
              >${e3ps:getMessage('도면출도의뢰서')}</a
            >
          </li>
          <li>
            <a
              class="linkMenu subLinkMenu"
              href="javascript:moveLocation('/distribute/distributeRegistration')"
              >${e3ps:getMessage('배포요청 등록')}</a
            >
          </li>
          <li>
            <a
              class="linkMenu subLinkMenu"
              href="javascript:moveLocation('/distribute/distributeRereption')"
              >${e3ps:getMessage('배포요청 수신')}</a
            >
          </li>
         
        </ul>
      </li>
    </ul>
  </div>
  <!-- //ss_menu --> --%>
</div>
