package com.liferay.command.portlet;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.osgi.service.component.annotations.Component;	

@Component(
		property = {	
		"javax.portlet.name=com_liferay_command_SamplePortlet",
		"mvc.command.name=/samplePortlet/addEntity"
		
		}, 
		service = MVCActionCommand.class)
	
public class SampleActionCommand extends BaseMVCActionCommand {


	@Override
	protected void doProcessAction(ActionRequest actionRequest, ActionResponse actionResponse) throws Exception {
	
		for(int i=0;i<5;i++)
		{
			_log.info(" SampleActionCommand ----------doProcessAction");
			System.out.println("SampleActionCommand ----------doProcessAction");
		}
			
			
			
	}
	
	
	private static Log _log = LogFactoryUtil.getLog(SampleActionCommand.class);
}
