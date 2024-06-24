Fhir plan definition documentation [here](https://www.hl7.org/fhir/plandefinition.html)

**Sample Plan**

```

{
  "identifier": "877a0d0b-5602-4223-bd43-7a2acf2638b1",
  "version": "2",
  "name": "A1-Thailand test site BVBD 1-Rose_Mary-2021-11-01-Site",
  "title": "A1 - Thailand test site BVBD 1 - Rose Mary - 2021-11-01 - Site",
  "status": "active",
  "date": "2021-11-01",
  "effectivePeriod": {
    "start": "2021-11-01",
    "end": "2021-11-21"
  },
  "useContext": [
    {
      "code": "interventionType",
      "valueCodableConcept": "Dynamic-FI"
    },
    {
      "code": "fiStatus",
      "valueCodableConcept": "A1"
    },
    {
      "code": "fiReason",
      "valueCodableConcept": "Case Triggered"
    },
    {
      "code": "caseNum",
      "valueCodableConcept": "2401224081141261897"
    },
    {
      "code": "opensrpEventId",
      "valueCodableConcept": "5834b612-d6fb-4525-9ecb-353eba8ae78e"
    },
    {
      "code": "taskGenerationStatus",
      "valueCodableConcept": "internal"
    },
    {
      "code": "teamAssignmentStatus",
      "valueCodableConcept": "True"
    }
  ],
  "jurisdiction": [
    {
      "code": "ab7e1954-0ac8-475e-b70a-e896babdb649"
    }
  ],
  "serverVersion": 1601948145105,
  "goal": [
    {
      "id": "Case_Confirmation",
      "description": "ยืนยันบ้านผู้ป่วย",
      "priority": "medium-priority",
      "target": [
        {
          "measure": "จำนวนผู้ป่วยที่ได้รับการยืนยัน",
          "detail": {
            "detailQuantity": {
              "value": 1,
              "comparator": "&amp;gt;=",
              "unit": "case(s)"
            }
          },
          "due": "2021-11-11"
        }
      ]
    },
    {
      "id": "RACD_register_families",
      "description": "ลงทะเบียนครัวเรือนและสมาชิกในครัวเรือน (100%) ภายในพื้นที่ปฏิบัติงาน",
      "priority": "medium-priority",
      "target": [
        {
          "measure": "ร้อยละของบ้าน/สิ่งปลูกสร้างที่ได้ลงทะเบียนข้อมูลครัวเรือน",
          "detail": {
            "detailQuantity": {
              "value": 100,
              "comparator": "&amp;gt;=",
              "unit": "Percent"
            }
          },
          "due": "2021-11-21"
        }
      ]
    },
    {
      "id": "RACD_Blood_Screening",
      "description": "เจาะเลือดรอบบ้านผู้ป่วยในรัศมี 1 กิโลเมตร (100%)",
      "priority": "medium-priority",
      "target": [
        {
          "measure": "จำนวนผู้ที่ได้รับการเจาะโลหิต",
          "detail": {
            "detailQuantity": {
              "value": 50,
              "comparator": "&amp;gt;=",
              "unit": "Person(s)"
            }
          },
          "due": "2021-11-21"
        }
      ]
    },
    {
      "id": "BCC_Focus",
      "description": "ให้สุขศึกษาในพื้นที่ปฏิบัติงานอย่างน้อย 1 ครั้ง",
      "priority": "medium-priority",
      "target": [
        {
          "measure": "จำนวนกิจกรรมการให้สุขศึกษา",
          "detail": {
            "detailQuantity": {
              "value": 1,
              "comparator": "&amp;gt;=",
              "unit": "activit(y|ies)"
            }
          },
          "due": "2021-11-21"
        }
      ]
    },
    {
      "id": "RACD_bednet_distribution",
      "description": "แจกมุ้งทุกหลังคาเรือนในพื้นที่ปฏิบัติงาน (100%)",
      "priority": "medium-priority",
      "target": [
        {
          "measure": "จำนวนบ้าน/สิ่งปลูกสร้างที่ได้รับมุ้ง",
          "detail": {
            "detailQuantity": {
              "value": 90,
              "comparator": "&amp;gt;=",
              "unit": "Percent"
            }
          },
          "due": "2021-11-21"
        }
      ]
    },
    {
      "id": "Larval_Dipping",
      "description": "ดำเนินกิจกรรมจับลูกน้ำอย่างน้อย 3 แห่งในพื้นที่ปฏิบัติงาน",
      "priority": "medium-priority",
      "target": [
        {
          "measure": "จำนวนกิจกรรมการตักลูกน้ำ",
          "detail": {
            "detailQuantity": {
              "value": 3,
              "comparator": "&amp;gt;=",
              "unit": "activit(y|ies)"
            }
          },
          "due": "2021-11-21"
        }
      ]
    },
    {
      "id": "Mosquito_Collection",
      "description": "กิจกรรมจับยุงกำหนดไว้อย่างน้อย 3 แห่ง",
      "priority": "medium-priority",
      "target": [
        {
          "measure": "จำนวนกิจกรรมการจับยุง",
          "detail": {
            "detailQuantity": {
              "value": 3,
              "comparator": "&amp;gt;=",
              "unit": "activit(y|ies)"
            }
          },
          "due": "2021-11-21"
        }
      ]
    }
  ],
  "action": [
    {
      "identifier": "4f4ec90e-f448-47c9-9eae-1055e78a5558",
      "prefix": 1,
      "title": "การยืนยันบ้านผู้ป่วย",
      "description": "ยืนยันบ้านผู้ป่วย",
      "code": "Case Confirmation",
      "timingPeriod": {
        "start": "2021-11-01",
        "end": "2021-11-11"
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
    },
    {
      "identifier": "3dbbcbaf-6d52-4666-a86c-30a2f102c6ac",
      "prefix": 2,
      "title": "ลงทะเบียนครัวเรือน",
      "description": "ลงทะเบียนครัวเรือนและสมาชิกในครัวเรือน (100%) ภายในพื้นที่ปฏิบัติงาน",
      "code": "RACD Register Family",
      "timingPeriod": {
        "start": "2021-11-01",
        "end": "2021-11-21"
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
    },
    {
      "identifier": "2580f450-a9d1-44a7-b9be-32788fbf7085",
      "prefix": 3,
      "title": "กิจกรรมการเจาะโลหิต",
      "description": "เจาะเลือดรอบบ้านผู้ป่วยในรัศมี 1 กิโลเมตร (100%)",
      "code": "Blood Screening",
      "timingPeriod": {
        "start": "2021-11-01",
        "end": "2021-11-21"
      },
      "reason": "Investigation",
      "goalId": "RACD_Blood_Screening",
      "subjectCodableConcept": {
        "text": "Person"
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
            "description": "Trigger when a Family Registration or Family Member Registration event is submitted",
            "expression": "questionnaire = 'Family_Registration' or questionnaire = 'Family_Member_Registration'"
          }
        }
      ],
      "condition": [
        {
          "kind": "applicability",
          "expression": {
            "description": "Person is older than 5 years or person associated with questionnaire response if older than 5 years",
            "expression": "($this.is(FHIR.Patient) and $this.birthDate &amp;amp;lt;= today() - 5 'years') or ($this.contained.where(Patient.birthDate &amp;amp;lt;= today() - 5 'years').exists())"
          }
        }
      ],
      "definitionUri": "blood_screening.json",
      "type": "create"
    },
    {
      "identifier": "07ef6c57-87e0-4819-98d2-37c3ba75c46c",
      "prefix": 4,
      "title": "กิจกรรมการให้สุขศึกษา",
      "description": "ดำเนินกิจกรรมให้สุขศึกษา",
      "code": "BCC",
      "timingPeriod": {
        "start": "2021-11-01",
        "end": "2021-11-21"
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
    },
    {
      "identifier": "9b9f1ebf-dff5-4e0a-87c9-f1622e773072",
      "prefix": 5,
      "title": "กิจกรรมสำรวจ/ชุบ/แจกมุ้ง",
      "description": "แจกมุ้งทุกหลังคาเรือนในพื้นที่ปฏิบัติงาน (100%)",
      "code": "Bednet Distribution",
      "timingPeriod": {
        "start": "2021-11-01",
        "end": "2021-11-21"
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
    },
    {
      "identifier": "c0f80539-3e48-4d69-8b74-e7779c5b2fe9",
      "prefix": 6,
      "title": "กิจกรรมการตักลูกน้ำ",
      "description": "ดำเนินกิจกรรมจับลูกน้ำอย่างน้อย 3 แห่งในพื้นที่ปฏิบัติงาน",
      "code": "Larval Dipping",
      "timingPeriod": {
        "start": "2021-11-01",
        "end": "2021-11-21"
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
    },
    {
      "identifier": "ebd04720-49f4-44e2-819d-b2f0f6a62f53",
      "prefix": 7,
      "title": "กิจกรรมการจับยุง",
      "description": "กิจกรรมจับยุงกำหนดไว้อย่างน้อย 3 แห่ง",
      "code": "Mosquito Collection",
      "timingPeriod": {
        "start": "2021-11-01",
        "end": "2021-11-21"
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
  ],
  "experimental": false
}

```
