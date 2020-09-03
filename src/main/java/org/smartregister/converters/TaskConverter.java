package org.smartregister.converters;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.format.ISODateTimeFormat;
import org.smartregister.domain.Note;

import com.ibm.fhir.model.resource.Task;
import com.ibm.fhir.model.type.Annotation;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.Id;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Markdown;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Period;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.TaskIntent;
import com.ibm.fhir.model.type.code.TaskStatus;

public class TaskConverter {
	
	public static Task convertTasktoFihrResource(org.smartregister.domain.Task domainTask) {
		
		Task.Builder builder = Task.builder();
		Identifier identifier = Identifier.builder().system(Uri.builder().value("identifier").build())
		        .value(String.builder().value(domainTask.getIdentifier()).build()).build();
		Identifier groupIdentifier = Identifier.builder().system(Uri.builder().value("groupIdentifier").build())
		        .value(String.builder().value(domainTask.getGroupIdentifier()).build()).build();
		
		TaskStatus taskStatus = TaskStatus.of(StringUtils.toRootLowerCase(domainTask.getStatus().name()));
		CodeableConcept businessStatus = CodeableConcept.builder()
		        .text(String.builder().value(domainTask.getBusinessStatus()).build()).build();
		if (StringUtils.isNotBlank(domainTask.getLocation())) {
			builder.location(Reference.builder().reference(String.of(domainTask.getLocation())).build());
		}
		//		TaskPriority priority = TaskPriority.builder().id("priority").value(java.lang.String.valueOf(domainTask.getPriority())).build();
		//TODO : Need to set priority as its an enum in FIHR
		
		Reference focus = Reference.builder().reference(String.builder().value(domainTask.getFocus()).build()).build();
		
		Reference forEntity = Reference.builder().reference(String.builder().value(domainTask.getForEntity()).build())
		        .build();
		String description = String.builder().value(domainTask.getDescription()).build();
		CodeableConcept code = CodeableConcept.builder().text(String.builder().value(domainTask.getCode()).build()).build();
		
		Collection<Annotation> annotationList = new ArrayList<>();
		Annotation annotation;
		Markdown text;
		Element author;
		DateTime time;
		
		if (domainTask.getNotes() != null) {
			for (Note note : domainTask.getNotes()) {
				text = Markdown.builder().value(note.getText()).build();
				java.lang.String strDate = ISODateTimeFormat.dateTime().print(note.getTime());
				time = DateTime.builder().value(strDate).build();
				author = String.builder().value(note.getAuthorString()).build();
				annotation = Annotation.builder().text(text).time(time).author(author).build();
				annotationList.add(annotation);
			}
		}
		
		if (StringUtils.isNotBlank(domainTask.getRequester())) {
			builder.requester(Reference.builder().reference(String.of(domainTask.getRequester())).build());
		}
		
		Reference owner = Reference.builder().reference(String.builder().value(domainTask.getOwner()).build()).build();
		
		if (StringUtils.isNotBlank(domainTask.getReasonReference())) {
			builder.statusReason(CodeableConcept.builder().text(String.of(domainTask.getReasonReference())).build());
		}
		
		java.lang.String authoredOnString = ISODateTimeFormat.dateTime().print(domainTask.getAuthoredOn());
		DateTime authoredOn = DateTime.builder().value(authoredOnString).build();
		
		java.lang.String lastModifiedString = ISODateTimeFormat.dateTime().print(domainTask.getLastModified());
		DateTime lastModified = DateTime.builder().value(lastModifiedString).build();
		
		java.lang.String startString = ISODateTimeFormat.dateTime().print(domainTask.getExecutionStartDate());
		DateTime start = DateTime.builder().value(startString).build();
		Period.Builder period = Period.builder().start(start);
		
		if (domainTask.getExecutionEndDate() != null) {
			java.lang.String end = ISODateTimeFormat.dateTime().print(domainTask.getExecutionEndDate());
			period.end(DateTime.builder().value(end).build());
		}
		
		Reference planIdentifier = Reference.builder()
		        .reference(String.builder().value(domainTask.getPlanIdentifier()).build()).build();
		
		if (domainTask.getServerVersion() != null) {
			java.lang.String version = java.lang.String.valueOf(domainTask.getServerVersion());
			Id versionId = Id.builder().value(version).build();
			builder.meta(Meta.builder().versionId(versionId).build());
		}
		
		/** @formatter:off **/
		Task fihrTask = builder.identifier(identifier).status(taskStatus)
				.businessStatus(businessStatus)
				.focus(focus)
				._for(forEntity)
				.description(description)
				.code(code)
				.note(annotationList)
				.owner(owner)
				.authoredOn(authoredOn)
				.lastModified(lastModified)
				.executionPeriod(period.build())
				.basedOn(planIdentifier)
				.groupIdentifier(groupIdentifier)
				.intent(TaskIntent.PLAN)  //required property
				.build();
		/** @formatter:on **/
		return fihrTask;
	}
}
