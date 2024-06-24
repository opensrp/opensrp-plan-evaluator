OPENSRP EVENT TO FHIR QUESTIONNAIRESPONSE RESOURCE MAPPING

| DOMAIN CLASS  |FHIR RESOURCE  |
|--|--|
| eventType  |questionnaire  |
| entityType  |serviceType  |
| version  |meta.versionId  |
| businessStatus  |status  |
| baseEntityId  |subject.reference  |
| providerId  |author.reference  |
| locationId  |linkId:"locationId".item.answer  |
| teamId  |linkId:"teamId".item.answer  |
| team  |linkId:"team".item.answer  |

Sample Opensrp event record

```
{
  "_id": "89766405-54f8-40d7-9742-2b7b0d049604",
  "obs": [],
  "_rev": "v1",
  "type": "Event",
  "teamId": " ",
  "details": {
    "id": "2206",
    "age": "23",
    "bfid": "7107060601",
    "flag": "Site",
    "species": "V",
    "surname": "-",
    "focus_id": "b8a404d0-5e4d-455f-8bc1-90f8005c3b0a",
    "first_name": "เช่อ",
    "focus_name": "บ้านไร่ (7107060601)",
    "case_number": "141117000006590210607170031362",
    "family_name": "เช่อ",
    "focus_reason": "Investigation",
    "focus_status": "A1",
    "house_number": "m13",
    "ep1_create_date": "2021-06-07T00:00:00.000+0000",
    "ep3_create_date": "2021-06-08T00:00:00.000+0000",
    "investigtion_date": "2020-12-06T00:00:00.000+0000",
    "case_classification": "A"
  },
  "version": 1557860282617,
  "duration": 0,
  "eventDate": "2020-12-06T03:00:00.000+03:00",
  "eventType": "Case_Details",
  "entityType": "Case_Details",
  "locationId": "b8a404d0-5e4d-455f-8bc1-90f8005c3b0a",
  "providerId": "nifi-user",
  "dateCreated": "2021-10-21T21:32:51.636+03:00",
  "identifiers": {},
  "baseEntityId": "b8a404d0-5e4d-455f-8bc1-90f8005c3b0a",
  "serverVersion": 1607066371619,
  "formSubmissionId": "efd96df5-a642-452c-9cd7-58b02cdc81b6"
}

```

Fhir QuestionnaireResponse representation

```
{
    "resourceType": "QuestionnaireResponse",
    "id": "efd96df5-a642-452c-9cd7-58b02cdc81b6",
    "meta": {
        "versionId": "1607066371619"
    },
    "questionnaire": "Case_Details",
    "_questionnaire": {
        "id": "eventType"
    },
    "status": "completed",
    "subject": {
        "id": "baseEntityId",
        "reference": "b8a404d0-5e4d-455f-8bc1-90f8005c3b0a"
    },
    "author": {
        "id": "providerId",
        "reference": "nifi-user"
    },
    "item": [
        {
            "linkId": "locationId",
            "answer": [
                {
                    "valueString": "b8a404d0-5e4d-455f-8bc1-90f8005c3b0a"
                }
            ]
        },
        {
            "linkId": "id",
            "definition": "details",
            "answer": [
                {
                    "valueString": "2206"
                }
            ]
        },
        {
            "linkId": "age",
            "definition": "details",
            "answer": [
                {
                    "valueString": "23"
                }
            ]
        },
        {
            "linkId": "bfid",
            "definition": "details",
            "answer": [
                {
                    "valueString": "7107060601"
                }
            ]
        },
        {
            "linkId": "flag",
            "definition": "details",
            "answer": [
                {
                    "valueString": "Site"
                }
            ]
        },
        {
            "linkId": "species",
            "definition": "details",
            "answer": [
                {
                    "valueString": "V"
                }
            ]
        },
        {
            "linkId": "surname",
            "definition": "details",
            "answer": [
                {
                    "valueString": "-"
                }
            ]
        },
        {
            "linkId": "focus_id",
            "definition": "details",
            "answer": [
                {
                    "valueString": "b8a404d0-5e4d-455f-8bc1-90f8005c3b0a"
                }
            ]
        },
        {
            "linkId": "first_name",
            "definition": "details",
            "answer": [
                {
                    "valueString": "เช่อ"
                }
            ]
        },
        {
            "linkId": "focus_name",
            "definition": "details",
            "answer": [
                {
                    "valueString": "บ้านไร่ (7107060601)"
                }
            ]
        },
        {
            "linkId": "case_number",
            "definition": "details",
            "answer": [
                {
                    "valueString": "141117000006590210607170031362"
                }
            ]
        },
        {
            "linkId": "family_name",
            "definition": "details",
            "answer": [
                {
                    "valueString": "เช่อ"
                }
            ]
        },
        {
            "linkId": "focus_reason",
            "definition": "details",
            "answer": [
                {
                    "valueString": "Investigation"
                }
            ]
        },
        {
            "linkId": "focus_status",
            "definition": "details",
            "answer": [
                {
                    "valueString": "A1"
                }
            ]
        },
        {
            "linkId": "house_number",
            "definition": "details",
            "answer": [
                {
                    "valueString": "m13"
                }
            ]
        },
        {
            "linkId": "ep1_create_date",
            "definition": "details",
            "answer": [
                {
                    "valueString": "2021-06-07T00:00:00.000+0000"
                }
            ]
        },
        {
            "linkId": "ep3_create_date",
            "definition": "details",
            "answer": [
                {
                    "valueString": "2021-06-08T00:00:00.000+0000"
                }
            ]
        },
        {
            "linkId": "investigtion_date",
            "definition": "details",
            "answer": [
                {
                    "valueString": "2020-12-06T00:00:00.000+0000"
                }
            ]
        },
        {
            "linkId": "case_classification",
            "definition": "details",
            "answer": [
                {
                    "valueString": "A"
                }
            ]
        }
    ]
}

```
