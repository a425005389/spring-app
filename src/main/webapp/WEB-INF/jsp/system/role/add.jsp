<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/include.jsp"%>

<html>
	<head>
		<title>My JSP 'addUser.jsp' starting page</title>
		<script type="text/javascript">
			function submitForm(){
				if($("#ff").form('validate')==true){
					$.ajax({   
					     url:$("#ff").attr("action"),   
					     type:$("#ff").attr("method"),   
					     data:$("#ff").serializeArray(),
					     success:function(data){   
					    	 if(data.result=="success"){
					        	window.location="${pageContext.request.contextPath}/system/role/toList.do";
					        }else{
					        	$.messager.alert('提示',data.msg);
					        }
					     }
					});
				}
			}
			function toList(){
				window.location="${pageContext.request.contextPath}/system/role/toList.do";
			}
		</script>
	</head>

	<body>
		<form id="ff" action="${pageContext.request.contextPath}/system/role/add.do" method="POST">
			<input type="hidden" name="id"/>
			<table>
				<tr>
					<td>名称:</td>
					<td><input type="text" name="name" class="easyui-textbox" required="true" validType="length[1,25]"></td>
				</tr>
				<tr>
					<td>权限:</td>
					<td>
						<input name="resourcesId" required="true" class="easyui-combotree" cascadeCheck ="false" panelHeight="auto" data-options="url:'${pageContext.request.contextPath}/system/resources/list.do'" multiple >

					</td>
				</tr>
				
			
			</table>
		</form>
		<div id="dlg-buttons">
			<a href="javascript:void(0)" class="easyui-linkbutton" onclick="submitForm()">保存</a>
			<a href="javascript:void(0)" class="easyui-linkbutton"  onclick="toList()">返回</a>
		</div>
	</body>
</html>
