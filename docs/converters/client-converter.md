CLIENT TO PATIENT RESOURCE MAPPING

| DOMAIN CLASS  |FHIR RESOURCE  |
|--|--|
| birthdate  |birthDate  |
| gender  |gender  |
| firstName  |name.given[0]  |
| middleName  |name.given[1]  |
| lastName  |name.family  |
| fullName  |name.text  |
| deathdate  |deceased.deceasedDateTime  |
| clientType |identifier.clientType  |
| relationships  |Identifiers with codeable concept code relationship |
| identifiers  |identifiers  |
| attributes  |Identifiers with codeable concept code relationship |
| baseEntityId  |id  |

Opensrp client record
```
{
  "_id": "81a40d3d-3cb0-46fa-a62b-1480511d1955",
  "_rev": "v1",
  "type": "Client",
  "gender": "Male",
  "teamId": "a3bf8a41-3753-4b1d-816b-3999a30250c9",
  "lastName": "Shsha",
  "addresses": [],
  "birthdate": "1955-01-01T07:00:00.000+07:00",
  "firstName": "Jsvsg",
  "attributes": {
    "residence": "a0bc28da-d9ef-4313-83f1-6c7e00df1526",
    "age_entered": "65"
  },
  "locationId": "b8a404d0-5e4d-455f-8bc1-90f8005c3b0a",
  "dateCreated": "2020-11-18T16:07:13.007+07:00",
  "identifiers": {
    "opensrp_id": "11564507"
  },
  "baseEntityId": "5e157eec-b1ce-4993-b5e2-c4406b5e1633",
  "relationships": {
    "family": [
      "4330230d-b89b-41f7-b384-24334c2112dd"
    ]
  },
  "serverVersion": 1600349606186,
  "birthdateApprox": true,
  "deathdateApprox": false,
  "clientDatabaseVersion": 11,
  "clientApplicationVersion": 25
}
```

Fhir Patient representation

```
{
    "resourceType": "Patient",
    "id": "5e157eec-b1ce-4993-b5e2-c4406b5e1633",
    "identifier": [
        {
            "system": "opensrp_id",
            "value": "11564507"
        },
        {
            "type": {
                "coding": [
                    {
                        "extension": [
                            {
                                "url": "relationships",
                                "valueString": "4330230d-b89b-41f7-b384-24334c2112dd"
                            }
                        ],
                        "code": "relationship"
                    }
                ],
                "text": "family"
            }
        },
        {
            "id": "residence",
            "type": {
                "coding": [
                    {
                        "code": "attribute"
                    }
                ]
            },
            "value": "a0bc28da-d9ef-4313-83f1-6c7e00df1526"
        },
        {
            "id": "age_entered",
            "type": {
                "coding": [
                    {
                        "code": "attribute"
                    }
                           ]
            },
            "value": "65"
        }
    ],
    "name": [
        {
            "text": "Jsvsg Shsha",
            "family": "Shsha",
            "given": [
                "Jsvsg"
            ]
        }
    ],
    "gender": "male",
    "birthDate": "1955-01-01"
}


```

