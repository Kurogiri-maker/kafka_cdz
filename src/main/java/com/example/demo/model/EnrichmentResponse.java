package com.example.demo.model;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EnrichmentResponse {
    private String type;
    private String numero;
    private String attributeName;
    private String attributeValue;



    @Override
    public String toString() {
        return String.format("{\"type\": \"%s\"," +
                        "\n\"numero\": \"%s\"," +
                        "\n\"attributeName\": \"%s\"," +
                        "\n\"attributeValue\": \"%s\"}",
                type, numero, attributeName, attributeValue);
    }
}
