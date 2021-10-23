package org.intelligentindustry.demoworkplace;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.api.identity.AnonymousProvider;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

import org.eclipse.milo.opcua.sdk.client.api.identity.IdentityProvider;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;

import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodResult;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.util.ConversionUtil.l;

public class MainApp {

	public static void main(String... args) throws Exception {
		
		System.out.println("Starting MainApp");

		OpcUaClient client = createClient();

		client.connect().get();

		NodeId objectId = NodeId.parse("ns=2;s=HelloWorld");
        NodeId methodId = NodeId.parse("ns=2;s=HelloWorld/sqrt(x)");

        CallMethodRequest request = new CallMethodRequest(
            objectId,
            methodId,
            new Variant[]{new Variant(17.9)}
        );

		CallMethodResult result = client.call(request).get();

		if ( result.getStatusCode().isGood()){

			Double value = (Double) l(result.getOutputArguments()).get(0).getValue();
			System.out.println("Received value "+value);

		}else {
			StatusCode[] inputArgumentResults = result.getInputArgumentResults();
			for (int i = 0; i < inputArgumentResults.length; i++) {
				System.out.println("inputArgumentResults[{}]={}"+i+" --> "+  inputArgumentResults[i]);
			}
		}     
     
    }
	

	private static OpcUaClient createClient() throws Exception {
 
        return OpcUaClient.create(
            getEndpointUrl(),
            endpoints ->
                endpoints.stream()
                    .filter(endpointFilter())
                    .findFirst(),
            configBuilder ->
                configBuilder
                    .setApplicationName(LocalizedText.english("eclipse milo opc-ua client"))
                    .setApplicationUri("urn:eclipse:milo:examples:client")
                    .setIdentityProvider(getIdentityProvider())
                    .setRequestTimeout(uint(5000))
                    .build()
        );
    }

	static String getEndpointUrl() {
        return "opc.tcp://localhost:12686/milo";
    }

    static Predicate<EndpointDescription> endpointFilter() {
        return e -> getSecurityPolicy().getUri().equals(e.getSecurityPolicyUri());
    }

    static SecurityPolicy getSecurityPolicy() {
        return SecurityPolicy.None;
	}

    static IdentityProvider getIdentityProvider() {
        return new AnonymousProvider();
    }


}
