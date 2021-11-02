Case Confirmation Action

```
{
        "identifier": "6b2a7118-c6c9-43b7-8227-5d3275878d97",
        "prefix": 1,
        "title": "การยืนยันบ้านผู้ป่วย",
        "description": "ยืนยันบ้านผู้ป่วย",
        "code": "Case Confirmation",
        "timingPeriod": {
          "start": "2021-09-09",
          "end": "2021-09-19"
        },
        "reason": "Investigation",
        "goalId": "Case_Confirmation",
        "subjectCodableConcept": {
          "text": "QuestionnaireResponse"
        },
        "trigger": [
          {
            "type": "named-event",
            "name": "plan-activation"
          }
        ],
        "condition": [
          {
            "kind": "applicability",
            "expression": {
              "description": "Event is case details event",
              "expression": "questionnaire = 'Case_Details'"
            }
          }
        ],
        "definitionUri": "case_confirmation.json",
        "type": "create"
      }
```

BCC Action

```
 {
        "identifier": "23087863-53d9-44f1-86ac-348a7427e9a4",
        "prefix": 3,
        "title": "กิจกรรมการให้สุขศึกษา",
        "description": "ดำเนินกิจกรรมให้สุขศึกษา",
        "code": "BCC",
        "timingPeriod": {
          "start": "2021-09-09",
          "end": "2021-09-29"
        },
        "reason": "Investigation",
        "goalId": "BCC_Focus",
        "subjectCodableConcept": {
          "text": "Jurisdiction"
        },
        "trigger": [
          {
            "type": "named-event",
            "name": "plan-activation"
          }
        ],
        "condition": [
          {
            "kind": "applicability",
            "expression": {
              "description": "Jurisdiction type location",
              "expression": "Location.physicalType.coding.exists(code='jdn')"
            }
          }
        ],
        "definitionUri": "behaviour_change_communication.json",
        "type": "create"
 }
 
```

Family Registration Action

```

{
        "identifier": "a00a5201-c51c-44e3-bd28-ea94c1081135",
        "prefix": 2,
        "title": "ลงทะเบียนครัวเรือน",
        "description": "ลงทะเบียนครัวเรือนและสมาชิกในครัวเรือน (100%) ภายในพื้นที่ปฏิบัติงาน",
        "code": "RACD Register Family",
        "timingPeriod": {
          "start": "2021-09-09",
          "end": "2021-09-29"
        },
        "reason": "Investigation",
        "goalId": "RACD_register_families",
        "subjectCodableConcept": {
          "text": "Location"
        },
        "trigger": [
          {
            "type": "named-event",
            "name": "plan-activation"
          },
          {
            "type": "named-event",
            "name": "event-submission",
            "expression": {
              "description": "Trigger when a Register_Structure event is submitted",
              "expression": "questionnaire = 'Register_Structure' or questionnaire = 'Archive_Family'"
            }
          }
        ],
        "condition": [
          {
            "kind": "applicability",
            "expression": {
              "description": "Structure is residential or type does not exist",
              "expression": "$this.is(FHIR.QuestionnaireResponse) or (($this.type.where(id='locationType').exists().not() or $this.type.where(id='locationType').text = 'Residential Structure') and $this.contained.exists().not())",
              "subjectCodableConcept": {
                "text": "Family"
              }
            }
          },
          {
            "kind": "applicability",
            "expression": {
              "description": "Apply to residential structures in Register_Structure questionnaires",
              "expression": "$this.is(FHIR.Location) or (questionnaire = 'Register_Structure' and $this.item.where(linkId='structureType').answer.value ='Residential Structure')"
            }
          }
        ],
        "definitionUri": "family_register.json",
        "type": "create"
      }

```

Blood Screening Action

```

 {
      "code": "Blood Screening",
      "type": "create",
      "title": "กิจกรรมการเจาะโลหิต",
      "goalId": "RACD_Blood_Screening",
      "prefix": 2,
      "reason": "Investigation",
      "trigger": [
        {
          "name": "plan-activation",
          "type": "named-event"
        },
        {
          "name": "event-submission",
          "type": "named-event",
          "expression": {
            "expression": "questionnaire = 'Family_Member_Registration'",
            "description": "Trigger when a Family Member Registration event is submitted"
          }
        }
      ],
      "condition": [
        {
          "kind": "applicability",
          "expression": {
            "expression": "($this.is(FHIR.Patient) and $this.birthDate &amp;lt;= today() - 5 'years') or ($this.contained.where(Patient.birthDate &amp;lt;= today() - 5 'years').exists())",
            "description": "Person is older than 5 years or person associated with questionnaire response if older than 5 years"
          }
        }
      ],
      "identifier": "a116c3a0-ee39-587b-99c1-2f4eea658601",
      "description": "ลงทะเบียนครัวเรือนและสมาชิกในครัวเรือนภายในพื้นที่ปฏิบัติงาน",
      "timingPeriod": {
        "end": "2021-07-01T0000",
        "start": "2021-06-24T0000"
      },
      "definitionUri": "blood_screening.json",
      "subjectCodableConcept": {
        "text": "Person"
      }
  }

```

Bednet Distribution Action

```

 {
        "identifier": "e05e4ba5-adc2-4201-b27e-806b6d22df3c",
        "prefix": 4,
        "title": "กิจกรรมสำรวจ/ชุบ/แจกมุ้ง",
        "description": "แจกมุ้งทุกหลังคาเรือนในพื้นที่ปฏิบัติงาน (100%)",
        "code": "Bednet Distribution",
        "timingPeriod": {
          "start": "2021-09-09",
          "end": "2021-09-29"
        },
        "reason": "Investigation",
        "goalId": "RACD_bednet_distribution",
        "subjectCodableConcept": {
          "text": "Location"
        },
        "trigger": [
          {
            "type": "named-event",
            "name": "plan-activation"
          },
          {
            "type": "named-event",
            "name": "event-submission",
            "expression": {
              "description": "Trigger when a Family Registration event is submitted",
              "expression": "questionnaire = 'Family_Registration'"
            }
          }
        ],
        "condition": [
          {
            "kind": "applicability",
            "expression": {
              "description": "Structure is residential or type does not exist",
              "expression": "$this.is(FHIR.QuestionnaireResponse) or (($this.type.where(id='locationType').exists().not() or $this.type.where(id='locationType').text = 'Residential Structure') and $this.contained.exists())",
              "subjectCodableConcept": {
                "text": "Family"
              }
            }
          }
        ],
        "definitionUri": "bednet_distribution.json",
        "type": "create"
      }

```

Larval Dipping Action

```

 {
        "identifier": "c61f7559-e72d-4179-a295-86d86b1fa29b",
        "prefix": 5,
        "title": "กิจกรรมการตักลูกน้ำ",
        "description": "ดำเนินกิจกรรมจับลูกน้ำอย่างน้อย 3 แห่งในพื้นที่ปฏิบัติงาน",
        "code": "Larval Dipping",
        "timingPeriod": {
          "start": "2021-09-09",
          "end": "2021-09-29"
        },
        "reason": "Investigation",
        "goalId": "Larval_Dipping",
        "subjectCodableConcept": {
          "text": "Location"
        },
        "trigger": [
          {
            "type": "named-event",
            "name": "plan-activation"
          },
          {
            "type": "named-event",
            "name": "event-submission",
            "expression": {
              "description": "Trigger when a Register_Structure event is submitted",
              "expression": "questionnaire = 'Register_Structure'"
            }
          }
        ],
        "condition": [
          {
            "kind": "applicability",
            "expression": {
              "description": "Structure is a larval breeding site",
              "expression": "$this.is(FHIR.QuestionnaireResponse) or $this.type.where(id='locationType').text = 'Larval Breeding Site'"
            }
          },
          {
            "kind": "applicability",
            "expression": {
              "description": "Apply to larval breeding sites in Register_Structure questionnaires",
              "expression": "$this.is(FHIR.Location) or (questionnaire = 'Register_Structure' and $this.item.where(linkId='structureType').answer.value ='Larval Breeding Site')"
            }
          }
        ],
        "definitionUri": "larval_dipping_form.json",
        "type": "create"
 }

```

Mosquito Collection Action

```

{
        "identifier": "481854d1-2c6b-4ab7-bfd7-aa6cf25dd7d0",
        "prefix": 6,
        "title": "กิจกรรมการจับยุง",
        "description": "กิจกรรมจับยุงกำหนดไว้อย่างน้อย 3 แห่ง",
        "code": "Mosquito Collection",
        "timingPeriod": {
          "start": "2021-09-09",
          "end": "2021-09-29"
        },
        "reason": "Investigation",
        "goalId": "Mosquito_Collection",
        "subjectCodableConcept": {
          "text": "Location"
        },
        "trigger": [
          {
            "type": "named-event",
            "name": "plan-activation"
          },
          {
            "type": "named-event",
            "name": "event-submission",
            "expression": {
              "description": "Trigger when a Register_Structure event is submitted",
              "expression": "questionnaire = 'Register_Structure'"
            }
          }
        ],
        "condition": [
          {
            "kind": "applicability",
            "expression": {
              "description": "Structure is a mosquito collection point",
              "expression": "$this.is(FHIR.QuestionnaireResponse) or $this.type.where(id='locationType').text = 'Mosquito Collection Point'"
            }
          },
          {
            "kind": "applicability",
            "expression": {
              "description": "Apply to mosquito collection point in Register_Structure questionnaires",
              "expression": "$this.is(FHIR.Location) or (questionnaire = 'Register_Structure' and $this.item.where(linkId='structureType').answer.value ='Mosquito Collection Point')"
            }
          }
        ],
        "definitionUri": "mosquito_collection_form.json",
        "type": "create"
      }

```