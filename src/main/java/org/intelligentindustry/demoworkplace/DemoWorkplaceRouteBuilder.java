package org.intelligentindustry.demoworkplace;

import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Predicate;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.processor.aggregate.AggregationStrategyBeanAdapter;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;

public class DemoWorkplaceRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		from("timer://foo?repeatCount=1")
		//from("milo-client:opc.tcp://localhost:12686/milo?node=RAW(ns=2;s=HelloWorld/Dynamic/Int32)&samplingInterval=1000&allowedSecurityPolicies=None" )
		//.log("${body}")
		
//		.choice()
//			.when(new Predicate() {
//				
//				@Override
//				public boolean matches(Exchange exchange) {
//					DataValue message = exchange.getIn().getBody(DataValue.class);
//					System.out.println("type: "+message.getValue().getDataType());
//					System.out.println("value: "+message.getValue().getValue());
//					Integer integer = Integer.parseInt(message.getValue().getValue().toString());
//					
//					return integer > 0;
//				}})
//				.log("alert!")
//			.otherwise()
//				.log("sub zero")
//		.end()
		.process(new Processor() {
			
			@Override
			public void process(Exchange exchange) throws Exception {
				
				Variant[] params = new Variant[1];
				params[0] = new Variant(13);
				
				exchange.getMessage().setBody(params);
				
				
			}
		})
		.log("params: ${body}")
		.setHeader("await", constant(true))
//		.setExchangePattern(ExchangePattern.InOut)
		.to("milo-client:opc.tcp://milo.digitalpetri.com:62541/milo?node=RAW(ns=2;s=Methods)&method=RAW(ns=2;s=Methods/sqrt(x))&allowedSecurityPolicies=None")
		//.to( "milo-client:opc.tcp://localhost:12686/milo?node=RAW(ns=2;s=HelloWorld)&method=RAW(ns=2;s=HelloWorld/sqrt(x))&allowedSecurityPolicies=None")
		//.setHeader("await", constant(true)) // await: parameter "defaultAwaitWrites"
//		.enrich("milo-client:opc.tcp://localhost:12686/milo?node=RAW(ns=2;s=HelloWorld)&method=RAW(ns=2;s=HelloWorld/sqrt(x))&allowedSecurityPolicies=None" , 
//				new AggregationStrategy() {
//
//			@Override
//			public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
//				System.out.println(newExchange.getMessage().getBody().toString());
//				
//				return newExchange;
//			}} )
//		.to("mock:test")
		.log("result ${body}")
		.to("mock:test")
		;
		
	}

}
