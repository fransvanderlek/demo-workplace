package org.intelligentindustry.demoworkplace;

import java.util.Arrays;

import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;

public class DemoWorkplaceRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		from("timer://runOnce?repeatCount=1&delay=1000").log("fooiing around!")
				.setHeader("CamelMiloNodeIds", constant(Arrays.asList("ns=2;s=HelloWorld/Dynamic/Double")))

				.setHeader("await", constant(true)) // await: parameter "defaultAwaitWrites"

				.enrich("milo-client:opc.tcp://milo.digitalpetri.com:62541/milo?samplingInterval=1000&allowedSecurityPolicies=None",
						new AggregationStrategy() {

							@Override
							public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
								return newExchange;
							}
						})
				.log("${body}").to("mock:test1");
	}

}
