OPENSRP LOCATION TO FHIR LOCATION RESOURCE MAPPING

| DOMAIN CLASS  |FHIR RESOURCE  |
|--|--|
| id  |id  |
| geometry  |(not represented)  |
| properties.status  |status  |
| properties.parentId  |partOf  |
| properties.name  |name  |
| properties.geographicalLevel  |  |
| properties.OpenMRS_Id  |identifier.[{"system": "OpenMRS_Id","value": ""}  |
| properties.externalId  |identifier.[{"system": "externalId","value": ""}   |
| properties.name_en  |alias  |
| properties.version  |meta.versionId  |
| isJurisdiction  |true = physicalType.jdn , false=physicalType:bu  |


Sample Opensrp location (structure) record

```
{
  "id": 8897,
  "json": {
    "type": "Feature",
    "id": "b8a404d0-5e4d-455f-8bc1-90f8005c3b0a",
    "properties": {
      "status": "Active",
      "parentId": "a5167f21-d0ab-4c38-b1b9-f0913de005d4",
      "name": "เมืองทอง(Site)-3",
      "geographicLevel": 5,
      "version": 0
    },
    "serverVersion": 1600251375766,
    "locationTags": [
      {
        "id": 2,
        "name": "Operational Area"
      }
    ]
  }
}

```

Fhir Location representation

```
{
    "resourceType": "Location",
    "id": "b8a404d0-5e4d-455f-8bc1-90f8005c3b0a",
    "meta": {
        "versionId": "0",
        "lastUpdated": "2020-09-16T13:16:15.766+03:00"
    },
    "identifier": [
        {
            "system": "OpenMRS_Id",
            "value": "1b15db0b-9229-4562-81ca-86a11b777169"
        },
        {
            "system": "externalId",
            "value": "1206030005"
        },
        {
            "system": "hasGeometry",
            "value": "false"
        }
    ],
    "status": "active",
    "name": "เมืองทอง(Site)-3",
    "mode": "instance",
    "_mode": {
        "id": "mode"
    },
    "physicalType": {
        "coding": [
            {
                "code": "jdn"
            }
        ]
    },
    "partOf": {
        "reference": "a5167f21-d0ab-4c38-b1b9-f0913de005d4"
    }
}

```
