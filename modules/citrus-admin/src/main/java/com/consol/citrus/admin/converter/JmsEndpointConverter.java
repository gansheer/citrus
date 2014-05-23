package com.consol.citrus.admin.converter;

import com.consol.citrus.admin.model.EndpointData;
import com.consol.citrus.model.config.core.JmsEndpoint;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author Christoph Deppisch
 * @since 1.3.1
 */
@Component
public class JmsEndpointConverter implements EndpointConverter<JmsEndpoint> {

    @Override
    public EndpointData convert(JmsEndpoint definition) {
        EndpointData endpointData = new EndpointData("jms-endpoint");

        endpointData.setName(definition.getId());

        if (StringUtils.hasText(definition.getDestinationName())) {
            endpointData.add("destination-name", definition.getDestinationName());
        } else {
            endpointData.add("destination", definition.getDestination());
        }

        return endpointData;
    }

    @Override
    public Class<JmsEndpoint> getModelClass() {
        return JmsEndpoint.class;
    }
}