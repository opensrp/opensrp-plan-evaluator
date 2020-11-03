package org.smartregister.domain;

import com.google.gson.annotations.SerializedName;

import lombok.ToString;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ToString
public class Task  implements Serializable{

	private static final long serialVersionUID = -9118755114172291102L;

	public enum TaskStatus {
		@SerializedName("Draft")
		DRAFT, @SerializedName("Ready")
		READY, @SerializedName("Cancelled")
		CANCELLED, @SerializedName("In Progress")
		IN_PROGRESS, @SerializedName("Completed")
		COMPLETED, @SerializedName("Failed")
		FAILED, @SerializedName("Archived")
        ARCHIVED;

		private static final Map<String, TaskStatus> lookup = new HashMap<String, TaskStatus>();

		static {
			lookup.put("Draft", TaskStatus.DRAFT);
			lookup.put("Ready", TaskStatus.READY);
			lookup.put("Cancelled", TaskStatus.CANCELLED);
			lookup.put("In Progress", TaskStatus.IN_PROGRESS);
			lookup.put("Completed", TaskStatus.COMPLETED);
			lookup.put("Failed", TaskStatus.FAILED);
			lookup.put("Archived", TaskStatus.ARCHIVED);
		}

		public static TaskStatus get(String algorithm) {
			return lookup.get(algorithm);
		}
	}
	
	
	public enum TaskPriority {
		
		@SerializedName("routine")
		ROUTINE,
		@SerializedName("urgent")
		URGENT,
		@SerializedName("asap")
		ASAP,
		@SerializedName("stat")
		STAT;
		
		public static TaskPriority get(String priority) {
			if (priority == null)
				throw new IllegalArgumentException("Value is required");
			switch (priority) {
				case "routine":
					return ROUTINE;
				case "urgent":
					return URGENT;
				case "asap":
					return ASAP;
				case "stat":
					return STAT;
				default:
					throw new IllegalArgumentException("Not a valid Task priority");
			}
			
		}
	}

	public static final String[] INACTIVE_TASK_STATUS = new String[]{TaskStatus.CANCELLED.name(), TaskStatus.ARCHIVED.name()};

	private String identifier;

	private String planIdentifier;

	private String groupIdentifier;

	private TaskStatus status;

	private String businessStatus;

	private TaskPriority priority;

	private String code;

	private String description;

	private String focus;

	@SerializedName("for")
	private String forEntity;

	private DateTime executionStartDate;

	private DateTime executionEndDate;

	private DateTime authoredOn;

	private DateTime lastModified;

	private String owner;

	@SerializedName("note")
	private List<Note> notes;

	private long serverVersion;

	private String reasonReference;

	private String location;

	private String requester;

	private String syncStatus;

	private String structureId;

	private Long rowid;

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getPlanIdentifier() {
		return planIdentifier;
	}

	public void setPlanIdentifier(String planIdentifier) {
		this.planIdentifier = planIdentifier;
	}

	public String getGroupIdentifier() {
		return groupIdentifier;
	}

	public void setGroupIdentifier(String groupIdentifier) {
		this.groupIdentifier = groupIdentifier;
	}

	public TaskStatus getStatus() {
		return status;
	}

	public void setStatus(TaskStatus status) {
		this.status = status;
	}

	public String getBusinessStatus() {
		return businessStatus;
	}

	public void setBusinessStatus(String businessStatus) {
		this.businessStatus = businessStatus;
	}

	public TaskPriority getPriority() {
		return priority;
	}

	public void setPriority(TaskPriority priority) {
		this.priority = priority;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFocus() {
		return focus;
	}

	public void setFocus(String focus) {
		this.focus = focus;
	}

	public String getForEntity() {
		return forEntity;
	}

	public void setForEntity(String forEntity) {
		this.forEntity = forEntity;
	}

	public DateTime getExecutionStartDate() {
		return executionStartDate;
	}

	public void setExecutionStartDate(DateTime executionStartDate) {
		this.executionStartDate = executionStartDate;
	}

	public DateTime getExecutionEndDate() {
		return executionEndDate;
	}

	public void setExecutionEndDate(DateTime executionEndDate) {
		this.executionEndDate = executionEndDate;
	}

	public DateTime getAuthoredOn() {
		return authoredOn;
	}

	public void setAuthoredOn(DateTime authoredOn) {
		this.authoredOn = authoredOn;
	}

	public DateTime getLastModified() {
		return lastModified;
	}

	public void setLastModified(DateTime lastModified) {
		this.lastModified = lastModified;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public List<Note> getNotes() {
		return notes;
	}

	public void setNotes(List<Note> notes) {
		this.notes = notes;
	}

	public Long getServerVersion() {
		return serverVersion;
	}

	public void setServerVersion(Long serverVersion) {
		this.serverVersion = serverVersion;
	}

	public String getReasonReference() {
		return reasonReference;
	}

	public void setReasonReference(String reasonReference) {
		this.reasonReference = reasonReference;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getRequester() {
		return requester;
	}

	public void setRequester(String requester) {
		this.requester = requester;
	}

	public String getSyncStatus() {
		return syncStatus;
	}

	public void setSyncStatus(String syncStatus) {
		this.syncStatus = syncStatus;
	}

	public String getStructureId() {
		return structureId;
	}

	public void setStructureId(String structureId) {
		this.structureId = structureId;
	}

	public Long getRowid() {
		return rowid;
	}

	public void setRowid(Long rowid) {
		this.rowid = rowid;
	}
}
