package org.smartregister.converters;

import com.ibm.fhir.model.resource.Task;
import com.ibm.fhir.model.type.*;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.code.TaskPriority;
import com.ibm.fhir.model.type.code.TaskStatus;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;

public class TaskConverter {

	public Task convertTasktoFihrResource(org.smartregister.domain.Task domainTask) {
		Identifier identifier = Identifier.builder()
				.system(Uri.builder().id("identifier").value(domainTask.getIdentifier()).build())
				.build();
		Identifier planIdentifier = Identifier.builder()
				.system(Uri.builder().id("planIdentifier").value(domainTask.getPlanIdentifier()).build())
				.build();
		Identifier groupIdentifier = Identifier.builder()
				.system(Uri.builder().id("groupIdentifier").value(domainTask.getGroupIdentifier()).build())
				.build();

		TaskStatus taskStatus = TaskStatus.of(domainTask.getStatus().name());
		CodeableConcept businessStatus = CodeableConcept.builder()
				.text(String.builder().value(domainTask.getBusinessStatus()).build())
				.build();
		Reference referenceLocation = Reference.builder().reference(String.builder().value(domainTask.getLocation()).build())
				.build();
		TaskPriority priority = TaskPriority.builder().id(java.lang.String.valueOf(domainTask.getPriority())).build();

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
		Element author; //TODO
		DateTime time;
		DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
		for (int i = 0; i < domainTask.getNotes().size(); i++) {
			text = Markdown.builder().value(domainTask.getNotes().get(i).getText()).build();
			java.lang.String strDate = dateFormat.format(domainTask.getNotes().get(i).getTime());
			time = DateTime.builder().value(strDate).build();
			annotation = Annotation.builder().text(text).time(time).build();
			annotationList.add(annotation);
		}

		Reference reuqestor = Reference.builder().reference(String.builder().value(domainTask.getRequester()).build())
				.build();

		Reference owner = Reference.builder().reference(String.builder().value(domainTask.getOwner()).build())
				.build();

		CodeableConcept reasonReference = CodeableConcept.builder()
				.text(String.builder().value(domainTask.getReasonReference()).build())
				.build();

		java.lang.String authoredOnString = dateFormat.format(domainTask.getAuthoredOn());
		DateTime authoredOn = DateTime.builder().value(authoredOnString).build();

		java.lang.String lastModifiedString = dateFormat.format(domainTask.getLastModified());
		DateTime lastModified = DateTime.builder().value(lastModifiedString).build();

		java.lang.String startString = dateFormat.format(domainTask.getExecutionStartDate());
		DateTime start = DateTime.builder().value(startString).build();

		java.lang.String endStart = dateFormat.format(domainTask.getExecutionEndDate());
		DateTime end = DateTime.builder().value(endStart).build();
		Period period = Period.builder().start(start).end(end).build();

		Task fihrTask = Task.builder().identifier(identifier, planIdentifier, groupIdentifier).status(taskStatus)
				.businessStatus(businessStatus).
						location(referenceLocation).
						priority(priority).
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
				.build();
		return fihrTask;
	}
}
