package org.smartregister.converters;

import com.ibm.fhir.model.resource.Task;
import com.ibm.fhir.model.type.*;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.code.TaskIntent;
import com.ibm.fhir.model.type.code.TaskPriority;
import com.ibm.fhir.model.type.code.TaskStatus;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.format.ISODateTimeFormat;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;

public class TaskConverter {

	public static Task convertTasktoFihrResource(org.smartregister.domain.Task domainTask) {
		Identifier identifier = Identifier.builder()
				.system(Uri.builder().value("identifier").build())
				.value(String.builder().value(domainTask.getIdentifier()).build())
				.build();
		Identifier groupIdentifier = Identifier.builder()
				.system(Uri.builder().value("groupIdentifier").build())
				.value(String.builder().value(domainTask.getGroupIdentifier()).build())
				.build();

		TaskStatus taskStatus = TaskStatus.of(StringUtils.toRootLowerCase(domainTask.getStatus().name()));
		CodeableConcept businessStatus = CodeableConcept.builder()
				.text(String.builder().value(domainTask.getBusinessStatus()).build())
				.build();
		Reference referenceLocation = Reference.builder().reference(String.builder().value(domainTask.getLocation()).build())
				.build();
		//		TaskPriority priority = TaskPriority.builder().id("priority").value(java.lang.String.valueOf(domainTask.getPriority())).build();
		//TODO : Need to set priority as its an enum in FIHR

		Reference focus = Reference.builder().reference(String.builder().value(domainTask.getFocus()).build())
				.build();

		Reference forEntity = Reference.builder().reference(String.builder().value(domainTask.getForEntity()).build())
				.build();
		String description = String.builder().value(domainTask.getDescription()).build();
		CodeableConcept code = CodeableConcept.builder().text(String.builder().value(domainTask.getCode()).build())
				.build();

		Collection<Annotation> annotationList = new ArrayList<>();
		Annotation annotation;
		Markdown text;
		Element author;
		DateTime time;

		for (int i = 0; i < domainTask.getNotes().size(); i++) {
			text = Markdown.builder().value(domainTask.getNotes().get(i).getText()).build();
			java.lang.String strDate = ISODateTimeFormat.dateTime().print(domainTask.getNotes().get(i).getTime());
			time = DateTime.builder().value(strDate).build();
			author = String.builder().value(domainTask.getNotes().get(i).getAuthorString()).build();
			annotation = Annotation.builder().text(text).time(time).author(author).build();
			annotationList.add(annotation);
		}

		Reference reuqestor = Reference.builder().reference(String.builder().value(domainTask.getRequester()).build())
				.build();

		Reference owner = Reference.builder().reference(String.builder().value(domainTask.getOwner()).build())
				.build();

		CodeableConcept reasonReference = CodeableConcept.builder()
				.text(String.builder().value(domainTask.getReasonReference()).build())
				.build();

		java.lang.String authoredOnString = ISODateTimeFormat.dateTime().print(domainTask.getAuthoredOn());
		DateTime authoredOn = DateTime.builder().value(authoredOnString).build();

		java.lang.String lastModifiedString = ISODateTimeFormat.dateTime().print(domainTask.getLastModified());
		DateTime lastModified = DateTime.builder().value(lastModifiedString).build();

		java.lang.String startString = ISODateTimeFormat.dateTime().print(domainTask.getExecutionStartDate());
		DateTime start = DateTime.builder().value(startString).build();

		java.lang.String endStart = ISODateTimeFormat.dateTime().print(domainTask.getExecutionEndDate());
		DateTime end = DateTime.builder().value(endStart).build();
		Period period = Period.builder().start(start).end(end).build();

		Reference planIdentifier = Reference.builder()
				.reference(String.builder().value(domainTask.getPlanIdentifier()).build())
				.build();

		java.lang.String version = java.lang.String.valueOf(domainTask.getServerVersion());
		Id versionId = Id.builder().value(version).build();
		Meta meta = Meta.builder().versionId(versionId).build();

		Task fihrTask = Task.builder().identifier(identifier).status(taskStatus)
				.businessStatus(businessStatus).
						location(referenceLocation).
						focus(focus).
						_for(forEntity).
						description(description).
						code(code).
						note(annotationList).
						requester(reuqestor).
						owner(owner).
						statusReason(reasonReference).
						authoredOn(authoredOn)
				.lastModified(lastModified)
				.executionPeriod(period)
				.basedOn(planIdentifier)
				.groupIdentifier(groupIdentifier)
				.meta(meta)
				.intent(TaskIntent.UNKNOWN)  //required property
				.build();
		return fihrTask;
	}
}
