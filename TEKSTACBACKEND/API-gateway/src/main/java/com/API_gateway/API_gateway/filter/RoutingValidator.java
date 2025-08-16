package com.API_gateway.API_gateway.filter;
import java.util.List;
import java.util.function.Predicate;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Component
public class RoutingValidator {
	public static final List<String> openEndPoints=List.of("/auth/save","/auth/login","/auth/Validatetoken");
	
	//
	public Predicate<ServerHttpRequest> isSecured=
			i->openEndPoints.stream().noneMatch(uri->i.getURI().getPath().contains(uri));
			
			
			
}
