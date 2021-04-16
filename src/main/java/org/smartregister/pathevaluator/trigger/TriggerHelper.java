/**
 * 
 */
package org.smartregister.pathevaluator.trigger;

import static org.smartregister.pathevaluator.TriggerType.EVENT_SUBMISSION;
import static org.smartregister.pathevaluator.TriggerType.PERIODIC;
import static org.smartregister.pathevaluator.TriggerType.PLAN_ACTIVATION;
import static org.smartregister.pathevaluator.TriggerType.PLAN_JURISDICTION_MODIFICATION;

import java.sql.Time;
import java.util.List;
import java.util.Set;

import org.joda.time.DateTime;
import org.smartregister.domain.Timing;
import org.smartregister.domain.TimingRepeat;
import org.smartregister.domain.Trigger;
import org.smartregister.pathevaluator.PathEvaluatorLibrary;
import org.smartregister.pathevaluator.ResourceType;
import org.smartregister.pathevaluator.TriggerType;
import org.smartregister.pathevaluator.action.ActionHelper;

import com.ibm.fhir.model.resource.QuestionnaireResponse;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * @author Samuel Githengi created on 06/19/20
 */
@RequiredArgsConstructor
public class TriggerHelper {
	
	@NonNull
	private ActionHelper actionHelper;
	
	private PathEvaluatorLibrary pathEvaluatorLibrary = PathEvaluatorLibrary.getInstance();

	public static final int TIMING_OFFSET_SECONDS = PathEvaluatorLibrary.getInstance().getScheduledActivationErrorMarginSeconds();

	private DateTime timeNow;
	
	/**
	 * Checks if trigger conditions for an action are met
	 * 
	 * @param triggers the set of triggers being evaluated
	 * @param triggerEvent triggering event
	 * @param quest
	 * @return true if trigger conditions are met
	 */
	public boolean evaluateTrigger(Set<Trigger> triggers, TriggerType triggerEvent, String planIdentifier,
	        QuestionnaireResponse questionnaireResponse) {
		if (triggers == null)
			return false;
		QuestionnaireResponse questionnaireWithEntityIdId = null;
		if (questionnaireResponse != null) {
			questionnaireWithEntityIdId = questionnaireResponse.toBuilder()
			        .id(questionnaireResponse.getSubject().getReference().getValue()).build();
		}
		boolean valid = false;
		for (Trigger trigger : triggers) {
			if ((PLAN_ACTIVATION == triggerEvent || PLAN_JURISDICTION_MODIFICATION == triggerEvent)
			        && PLAN_ACTIVATION.getValue().equalsIgnoreCase(trigger.getName())) {
				return true;
			} else if (trigger.getExpression() != null && EVENT_SUBMISSION == triggerEvent  ) {
				if (trigger.getExpression().getSubjectCodableConcept() == null) {
					valid = pathEvaluatorLibrary.evaluateBooleanExpression(questionnaireResponse,
					    trigger.getExpression().getExpression());
				} else {
					valid = actionHelper
					        .getConditionSubjectResources(questionnaireWithEntityIdId, planIdentifier,
					            ResourceType.from(trigger.getExpression().getSubjectCodableConcept().getText()),
					            ResourceType.QUESTIONAIRRE_RESPONSE)
					        .stream().anyMatch(resource -> pathEvaluatorLibrary.evaluateBooleanExpression(resource,
					            trigger.getExpression().getExpression()));
				}
				if (valid) {
					return true;
				}
			} else if (PERIODIC.value().equals(trigger.getType()) && trigger.getTimingTiming() != null) {
				Timing timing = trigger.getTimingTiming();
				List<DateTime> eventLists = timing.getEvent();

				for (DateTime dateTime: eventLists) {
					if (dateTime.isBefore(now())) {
						TimingRepeat repeat = timing.getRepeat();
						if (repeat != null && repeat.getFrequency() == 1 && repeat.getPeriodUnit().equals(TimingRepeat.DurationCode.d)) {
							List<Time> timesOfDay = repeat.getTimeOfDay();
							DateTime today = now();
							Time timeNow = Time.valueOf(today.hourOfDay().get() + ":" + today.minuteOfHour().get() + ":" + today.secondOfMinute().get());

							// Compare the time to make sure that we are at that time or within the offset +/-
							for (Time timeOfDay: timesOfDay) {
								long millisDifference = Math.abs(timeNow.getTime() - timeOfDay.getTime());

								if (millisDifference <= (TIMING_OFFSET_SECONDS * 1000)) {
									return true;
								}
							}
						}
					}
				}
			}
		}
		return valid;
	}

	protected DateTime now() {
		return timeNow != null ? timeNow : DateTime.now();
	}

	protected void setNow(DateTime timeNow) {
		this.timeNow = timeNow;
	}
	
}
