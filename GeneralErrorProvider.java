package com.rest.providers;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.rest.beans.ErrorMessage;

@Provider
public class GeneralErrorProvider implements ExceptionMapper<Exception> {

	@Override
	public Response toResponse(Exception exception) {
		ErrorMessage errorMessage = new ErrorMessage("please try again");
		if (exception instanceof IllegalArgumentException || exception instanceof NullPointerException) {
			errorMessage.setError("please check bad or missing input format");
		}
		return Response.serverError().entity(errorMessage).build();
	}

}
