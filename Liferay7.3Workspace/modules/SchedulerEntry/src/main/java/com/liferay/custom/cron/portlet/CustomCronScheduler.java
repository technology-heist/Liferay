package com.liferay.custom.cron.portlet;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.messaging.BaseMessageListener;
import com.liferay.portal.kernel.messaging.DestinationNames;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.module.framework.ModuleServiceLifecycle;
import com.liferay.portal.kernel.scheduler.SchedulerEngineHelper;
import com.liferay.portal.kernel.scheduler.SchedulerEntry;
import com.liferay.portal.kernel.scheduler.SchedulerEntryImpl;
import com.liferay.portal.kernel.scheduler.SchedulerException;
import com.liferay.portal.kernel.scheduler.StorageType;
import com.liferay.portal.kernel.scheduler.StorageTypeAware;
import com.liferay.portal.kernel.scheduler.Trigger;
import com.liferay.portal.kernel.scheduler.TriggerFactory;
import com.liferay.portal.kernel.util.GetterUtil;

import java.util.Date;
import java.util.Map;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;

/**
 * 
 * @author 
 * 
 */

@Component(
immediate = true,
property = {"cron.expression=0 0 0 * * ?"},
service = CustomCronScheduler.class 

		)

public class CustomCronScheduler extends BaseMessageListener {

	@Activate

	@Modified

	protected void activate(Map<String,Object> properties	) {		
		
		 // extract the cron expression from the properties
	    String cronExpression = GetterUtil.getString(properties.get("cron.expression"), _DEFAULT_CRON_EXPRESSION);

	    Class<?> clazz = getClass();

		String className = clazz.getName();

		 Trigger trigger = _triggerFactory.createTrigger(className, className, new Date(), null, cronExpression);

		SchedulerEntry schedulerEntry = new SchedulerEntryImpl(className, trigger);

		_schedulerEngineHelper.register(this, schedulerEntry, DestinationNames.SCHEDULER_DISPATCH);

	}

	@Override

	protected void doReceive(Message message) throws Exception {

		System.out.println("+++++++++++++++++++++++triggered by new cron ++++++++++++++++++++++++++++++");

		if (_log.isInfoEnabled()) {

			_log.info("Received message on schedule: " + message);

		}

	}
	
	protected StorageType getStorageType() {
	    if (_schedulerEntryImpl instanceof StorageTypeAware) {
	      return ((StorageTypeAware) _schedulerEntryImpl).getStorageType();
	    }	    
	    return StorageType.PERSISTED;
	  }
	
	

	@Deactivate
	  protected void deactivate() {
	    if (_initialized) {
	      try {
	        _schedulerEngineHelper.unschedule(_schedulerEntryImpl, getStorageType());
	      } catch (SchedulerException se) {
	        if (_log.isWarnEnabled()) {
	          _log.warn("Unable to unschedule trigger", se);
	        }	
	      }
	      _schedulerEngineHelper.unregister(this);
	    }
	    _initialized = false;
	  }
	
	
	@Reference(target = ModuleServiceLifecycle.PORTAL_INITIALIZED, unbind = "-")
	  protected void setModuleServiceLifecycle(ModuleServiceLifecycle moduleServiceLifecycle) {
	  }

	  @Reference(unbind = "-")
	  protected void setTriggerFactory(TriggerFactory triggerFactory) {
	    _triggerFactory = triggerFactory;
	  }

	  @Reference(unbind = "-")
	  protected void setSchedulerEngineHelper(SchedulerEngineHelper schedulerEngineHelper) {
	    _schedulerEngineHelper = schedulerEngineHelper;
	  }

	  // the default cron expression is to run daily at midnight
	  private static final String _DEFAULT_CRON_EXPRESSION = "0 0 0 * * ?";
	  private volatile boolean _initialized;
	  private SchedulerEntryImpl _schedulerEntryImpl = null;

	
	private static final Log _log = LogFactoryUtil.getLog(CustomCronScheduler.class);

	@Reference(target = ModuleServiceLifecycle.PORTAL_INITIALIZED, unbind = "-")

	private volatile ModuleServiceLifecycle _moduleServiceLifecycle;

	@Reference(unbind = "-")

	private volatile SchedulerEngineHelper _schedulerEngineHelper;

	@Reference(unbind = "-")

	private volatile TriggerFactory _triggerFactory;

}
