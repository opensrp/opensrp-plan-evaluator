package org.smartregister.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.Builder;
import org.smartregister.domain.Action.SubjectConcept;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Expression {
    private String name;
    private String language;
    private String expression;
    private SubjectConcept subjectConcept;
}