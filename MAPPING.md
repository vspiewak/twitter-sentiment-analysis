curl -XPUT 'localhost:9200/_template/twitter_template' -d '
{
    "template": "twitter*",
    "settings": {
        "index.refresh_interval": "1s",
        "analysis": {
            "analyzer": {
                "default": {
                    "type": "standard",
                    "stopwords": "_none_"
                }
            }
        }
    },
    "mappings": {
        "_default_": {
            "_all": {
                "enabled": true
            },
            "dynamic_templates": [
                {
                    "string_fields": {
                        "match": "*",
                        "match_mapping_type": "string",
                        "mapping": {
                            "type": "string",
                            "index": "analyzed",
                            "omit_norms": true,
                            "fields": {
                                "raw": {
                                    "type": "string",
                                    "index": "not_analyzed",
                                    "ignore_above": 256
                                }
                            }
                        }
                    }
                }
            ],
            "properties": {
                "location": {
                    "type": "geo_point"
                }
            }
        }
    }
}'
