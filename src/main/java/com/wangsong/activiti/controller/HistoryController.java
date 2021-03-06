package com.wangsong.activiti.controller;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wangsong.activiti.service.HistoryService;
import com.wangsong.activiti.service.ProcessDefinitionService;
import com.wangsong.common.controller.BaseController;
import com.wangsong.common.model.Page;
import com.wangsong.common.util.UserUtil;
import com.wangsong.system.model.User;

/**
 * 字典controller
 * @author ty
 * @date 2015年1月13日
 */
@Controller
@RequestMapping("activiti/history")
public class HistoryController extends BaseController{
	
	@Autowired
	private HistoryService historyService;
	@Autowired
	private ProcessDefinitionService processDefinitionService;
	/**
	 * 默认页面
	 */
	@RequestMapping(value="toList")
	public String list() {
		return "activiti/history/list";
	}

	/**
	 * 获取字典json
	 */
	
	@RequestMapping(value="list")
	@ResponseBody
	public Map<String, Object> dictList(HttpServletRequest request) {
		Page<Map<String, Object>> page = getPage(request);
		page= historyService.findHistoryTaskListByUserId(page,((User)UserUtil.getUser()).getId().toString());
		
		return getEasyUIData(page);
	}
	
	@RequestMapping(value="/commentList")
	@ResponseBody
	public Map<String, Object> list(HttpServletRequest request,String businessKey) {
		Page<Map<String, Object>> page = getPage(request);
		List<Map<String, Object>> commentList =historyService.findHistoryCommentByBusinessKey(businessKey);
		page.setResult(commentList); 
		page.setTotalCount(commentList.size());
		return getEasyUIData(page);
	}
	
	/**
	 * 修改字典跳转
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	
	@RequestMapping(value = "/toUpdate")
	public String updateForm(String id) {
		HistoricTaskInstance historicTaskInstance = historyService.findHistoricTaskInstanceByTaskId(id);
		String url = historicTaskInstance.getFormKey();
		String sid = historyService.findHistoryIdByTaskId(historicTaskInstance.getProcessInstanceId());
		return "redirect:"+url+"?id="+sid+"&display=no";
	}
	
	@RequestMapping(value = "toViewImage", method = RequestMethod.GET)
	public String toViewImage(String taskId,Model model) {
		ProcessDefinition pd = historyService.findHistoryProcessDefinitionByTaskId(taskId);
		model.addAttribute("deploymentId", pd.getDeploymentId());
		model.addAttribute("diagramResourceName",pd.getDiagramResourceName());
		
		ProcessInstance  processInstance =historyService.findProcessInstanceByTaskId(taskId);
		if(processInstance!=null){
			Map<String, Object> map = historyService.findHistoryCoordingByTaskId(taskId);
			model.addAttribute("acs", map);
		}
		
		
		return "activiti/history/viewImage";
	}
	
	
	@RequestMapping(value="viewImage",method = RequestMethod.GET)
	public void viewImage(String deploymentId,String diagramResourceName,HttpServletResponse response) throws Exception{
	
		//2：获取资源文件表（act_ge_bytearray）中资源图片输入流InputStream
		InputStream in = processDefinitionService.findImageInputStream(deploymentId,diagramResourceName);
		//3：从response对象获取输出流
		OutputStream out = response.getOutputStream();
		//4：将输入流中的数据读取出来，写到输出流中
		for(int b=-1;(b=in.read())!=-1;){
			out.write(b);
		}
		out.close();
		in.close();
		//将图写到页面上，用输出流写
	}
}
