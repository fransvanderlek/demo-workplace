package org.intelligentindustry.demoworkplace;

import org.apache.camel.Exchange;
import org.apache.camel.Predicate;
import org.apache.camel.builder.RouteBuilder;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;

public class DemoWorkplaceRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		from("milo-client:opc.tcp://milo.digitalpetri.com:62541/milo?node=RAW(ns=2;s=Dynamic/RandomInt32)&samplingInterval=1000&allowedSecurityPolicies=None" )
		.log("${body}")
		.choice()
			.when(new Predicate() {
				
				@Override
				public boolean matches(Exchange exchange) {
					DataValue message = exchange.getIn().getBody(DataValue.class);
					System.out.println("type: "+message.getValue().getDataType());
					System.out.println("value: "+message.getValue().getValue());
					Integer integer = Integer.parseInt(message.getValue().getValue().toString());
					
					return integer > 0;
				}})
				.log("alert!")
			.otherwise()
				.log("sub zero")
		.end()
		.to("mock:test1");
	}

}
