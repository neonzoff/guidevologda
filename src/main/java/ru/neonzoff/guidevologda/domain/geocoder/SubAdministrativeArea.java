package ru.neonzoff.guidevologda.domain.geocoder;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "SubAdministrativeAreaName",
        "Locality"
})
@Generated("jsonschema2pojo")
public class SubAdministrativeArea {

    @JsonProperty("SubAdministrativeAreaName")
    private String subAdministrativeAreaName;
    @JsonProperty("Locality")
    private Locality locality;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("SubAdministrativeAreaName")
    public String getSubAdministrativeAreaName() {
        return subAdministrativeAreaName;
    }

    @JsonProperty("SubAdministrativeAreaName")
    public void setSubAdministrativeAreaName(String subAdministrativeAreaName) {
        this.subAdministrativeAreaName = subAdministrativeAreaName;
    }

    @JsonProperty("Locality")
    public Locality getLocality() {
        return locality;
    }

    @JsonProperty("Locality")
    public void setLocality(Locality locality) {
        this.locality = locality;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}