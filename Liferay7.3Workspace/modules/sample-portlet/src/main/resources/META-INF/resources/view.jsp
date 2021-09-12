<%@ include file="/init.jsp" %>

<p>
	<b><liferay-ui:message key="sample.caption"/></b>
	
	
</p>

<portlet:actionURL  name="/samplePortlet/addEntity" var="addEntityURL" >
                <portlet:param name="Entity" value="1234" />
</portlet:actionURL>

<a href="${addEntityURL}">Click here Add Entity</a>

        
          
            
          
